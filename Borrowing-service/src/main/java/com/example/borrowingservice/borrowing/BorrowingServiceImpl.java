package com.example.borrowingservice.borrowing;

import com.example.borrowingservice.borrowingline.BorrowingLine;
import com.example.borrowingservice.borrowingline.BorrowingLineMapper;
import com.example.borrowingservice.borrowingline.BorrowingLineRepository;
import com.example.borrowingservice.clients.BookClient;
import com.example.borrowingservice.clients.PaymentClient;
import com.example.common.DTO.BookPurchaseDTO;
import com.example.common.enums.ErrorCode;
import com.example.common.exception.AppException;
import com.example.common.request.PaymentRequest;
import com.example.common.response.BorrowingResponse;
import com.example.common.response.PaymentResponse;
import com.example.common.enums.StatusBorrowing;
import com.example.common.request.BorrowingRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class BorrowingServiceImpl implements BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BorrowingLineRepository borrowingLineRepository;
    private final BookClient bookClient;
    private final PaymentClient paymentClient;

    @Override
    public PaymentResponse createBorrowing(BorrowingRequest request, Principal principal) {

        // get customer id
        String customerId = principal.getName();
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

        // create borrowing
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
                .totalFine(0L)
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
                .build();
        return paymentClient.process(paymentRequest);

    }

    @Override
    public BorrowingResponse getBorrowingById(String id) {
        Borrowing borrowing = getById(id);
        return BorrowingMapper.toBorrowingResponse(borrowing);
    }

    @Override
    public void updateStatusBorrowingById(String id) {
        Borrowing borrowing = getById(id);
        borrowing.setStatus(StatusBorrowing.CANCELLED);
        borrowingRepository.save(borrowing);
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
    private Borrowing getById(String id){
        return borrowingRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.BorrowingNotFound));
    }
}
