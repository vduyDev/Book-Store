package com.example.borrowingservice.borrowing;
import com.example.borrowingservice.borrowingline.BorrowingLine;
import com.example.borrowingservice.borrowingline.BorrowingLineMapper;
import com.example.borrowingservice.borrowingline.BorrowingLineRepository;
import com.example.borrowingservice.clients.BookClient;
import com.example.borrowingservice.clients.CustomerClient;
import com.example.borrowingservice.clients.PaymentClient;
import com.example.common.DTO.BookPurchaseDTO;
import com.example.common.DTO.CustomerDTO;
import com.example.common.DTO.PaymentDTO;
import com.example.common.enums.ErrorCode;
import com.example.common.event.BookReturnEvent;
import com.example.common.exception.AppException;
import com.example.common.request.PaymentRequest;
import com.example.common.response.BorrowingResponse;
import com.example.common.response.PaymentResponse;
import com.example.common.enums.StatusBorrowing;
import com.example.common.request.BorrowingRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BorrowingServiceImpl implements BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BorrowingLineRepository borrowingLineRepository;
    private final BookClient bookClient;
    private final BorrowingProducer borrowingProducer;
    private final PaymentClient paymentClient;
    private final CustomerClient customerClient;

    @Override
    public PaymentResponse createBorrowing(BorrowingRequest request, Principal principal) {

        // get customer
        String customerId = principal.getName();
        CustomerDTO customerDTO = customerClient.getCustomerById(customerId).getData();
        // update stock
        List<BookPurchaseDTO> purchaseDTOList;
        try {
            purchaseDTOList = bookClient.updateBookStock(request.getBookPurchase());
            purchaseDTOList.forEach(purchaseDTO -> {
                        request.getBookPurchase().forEach(bookPurchaseRequest -> {
                            if (purchaseDTO.getBookId().equals(bookPurchaseRequest.getBookId())) {
                                purchaseDTO.setQuantity(bookPurchaseRequest.getQuantity());
                            }
                        });
                    }
            );
        } catch (RuntimeException e) {
            throw new AppException(ErrorCode.NotEnoughStock);
        }

        List<BorrowingLine> borrowingLines = request.getBookPurchase().stream()
                .map(BorrowingLineMapper::mapToBorrowingLine)
                .toList();

        Borrowing borrowing = Borrowing.builder()
                .id(getRandomNumber())
                .customerId(customerId)
                .status(StatusBorrowing.BORROWED)
                .paymentMethod(request.getPaymentMethod())
                .totalBook(request.getBookPurchase().size())
                .totalAmount(100000L)
                .borrowingLines(borrowingLines)
                .build();

        borrowingLines.forEach(borrowingLine -> borrowingLine.setBorrowing(borrowing));
        borrowingRepository.save(borrowing);
        borrowingLineRepository.saveAll(borrowingLines);
        // process payment and return payment url


        PaymentRequest paymentRequest = PaymentRequest.builder()
                .paymentMethod(request.getPaymentMethod())
                .amount(borrowing.getTotalAmount())
                .borrowingId(borrowing.getId())
                .bookPurchaseDTOS(purchaseDTOList)
                .customerDTO(customerDTO)
                .build();
        return paymentClient.process(paymentRequest);

    }

    @Override
    public BorrowingResponse getBorrowingById(String id) {
        Borrowing borrowing = getById(id);
        CustomerDTO customerDTO = customerClient.getCustomerInBorrowing(borrowing.getCustomerId());
        PaymentDTO paymentDTO = paymentClient.getPaymentByBorrowingId(borrowing.getId());
        return BorrowingMapper.toBorrowingResponse(borrowing, customerDTO, paymentDTO);
    }

    @Override
    public void updateStatusBorrowingById(String id) {
        Borrowing borrowing = getById(id);
        borrowing.setStatus(StatusBorrowing.FAILED_PAYMENT);
        borrowingRepository.save(borrowing);
    }


    public List<BorrowingResponse> getListBorrowing() {
        List<Borrowing> borrowings = borrowingRepository.findAll();
        List<CustomerDTO> listCustomerDTO = customerClient.getListCustomerInBorrowing();
        List<PaymentDTO> listPaymentDTO = paymentClient.getListPayment();

        // Chuyển danh sách khách hàng thành Map<CustomerId, CustomerDTO> để truy xuất nhanh
        Map<String, CustomerDTO> customerMap = listCustomerDTO.stream()
                .collect(Collectors.toMap(CustomerDTO::getId, customer -> customer));

        // Chuyển danh sách thanh toán thành Map<BorrowingId, PaymentDTO>
        Map<String, PaymentDTO> paymentMap = listPaymentDTO.stream()
                .collect(Collectors.toMap(PaymentDTO::getBorrowingId, payment -> payment));

        return borrowings.stream().map(borrowing -> {
            CustomerDTO customerDTO = customerMap.get(borrowing.getCustomerId());
            PaymentDTO paymentDTO = paymentMap.get(borrowing.getId());
            return BorrowingMapper.toBorrowingResponse(borrowing, customerDTO, paymentDTO);
        }).toList();
    }

    @Override
    public List<BorrowingResponse> getListBorrowingByCustomer(String id) {
        CustomerDTO customerDTO = customerClient.getCustomerInBorrowing(id);
        List<Borrowing> borrowings = borrowingRepository.findBorrowingByCustomerId(id);
        List<String> ids = borrowingRepository.findBorrowingByCustomerId(id).stream().map(Borrowing::getId).toList();
        Map<String, PaymentDTO> paymentMap = paymentClient.getPaymentByListBorrowing(ids).stream()
                .collect(Collectors.toMap(PaymentDTO::getBorrowingId, payment -> payment));
        return borrowings.stream().map(borrowing -> {
            PaymentDTO paymentDTO = paymentMap.get(borrowing.getId());
            return BorrowingMapper.toBorrowingResponse(borrowing, customerDTO, paymentDTO);
        }).toList();
    }

    @Override
    public BorrowingResponse bookRecord(String id, List<Integer> request) {
        Borrowing borrowing = getById(id);

        if (borrowing.getStatus() == StatusBorrowing.COMPLETED) {
            throw new AppException(ErrorCode.BorrowingCompleted);
        }

        List<BorrowingLine> updatedBorrowingLines = new ArrayList<>();

        borrowing.getBorrowingLines().forEach(borrowingLine -> {
            if (request.contains(borrowingLine.getBookId())) {
                borrowingLine.setReturnDate(Instant.now());
                borrowingLine.setStatus(StatusBorrowing.RETURNED);
                updatedBorrowingLines.add(borrowingLine);
            } else {
                throw new AppException(ErrorCode.BookNotFound);
            }
        });
        List<BookReturnEvent> bookPurchaseRequests = updatedBorrowingLines
                .stream()
                .map(BorrowingLineMapper::toBookPurchaseRequest)
                .toList();

        borrowingProducer.kafkaUpdateBookStock(bookPurchaseRequests);
        // Lưu danh sách BorrowingLine đã cập nhật vào DB một lần duy nhất
        if (!updatedBorrowingLines.isEmpty()) {
            borrowingLineRepository.saveAll(updatedBorrowingLines);
        }

        // Kiểm tra nếu tất cả BorrowingLine đều đã RETURNED
        boolean allReturned = borrowing.getBorrowingLines().stream()
                .allMatch(borrowingLine -> borrowingLine.getStatus() == StatusBorrowing.RETURNED);

        if (allReturned) {
            borrowing.setStatus(StatusBorrowing.COMPLETED);
            borrowingRepository.save(borrowing);
        }
        return getBorrowingById(id);
    }

    @Override
    public void updateStatusBorrowing() {
        List<Borrowing> borrowings = borrowingRepository.findAll();
        borrowings.forEach(borrowing -> {
            borrowing.getBorrowingLines().forEach(borrowingLine -> {
                if (borrowingLine.getReturnDate() == null) {
                    LocalDate dueDate = borrowingLine.getDueDate().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate currentDate = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate();
                    if (currentDate.isAfter(dueDate)) {
                        borrowingLine.setStatus(StatusBorrowing.OVERDUE);
                        borrowing.setStatus(StatusBorrowing.OVERDUE);
                        borrowingLineRepository.save(borrowingLine);
                        borrowingRepository.save(borrowing);
                    }
                }
            });
        });
    }


    private String getRandomNumber() {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private Borrowing getById(String id) {
        return borrowingRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.BorrowingNotFound));
    }
}
