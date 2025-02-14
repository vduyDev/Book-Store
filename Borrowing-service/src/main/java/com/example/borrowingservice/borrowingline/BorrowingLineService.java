package com.example.borrowingservice.borrowingline;


import com.example.borrowingservice.clients.BookClient;
import com.example.common.DTO.BookPurchaseDTO;
import com.example.common.DTO.BorrowingLineDTO;
import com.example.common.response.BookResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BorrowingLineService {

    private final BorrowingLineRepository borrowingLineRepository;

    private final BookClient bookClient;


    public List<BorrowingLineDTO> getBorrowingLines(String borrowingId) {

        List<BorrowingLine> borrowingLines = borrowingLineRepository.findByBorrowing(borrowingId);

        List<Integer> listIds = borrowingLines.stream().map(BorrowingLine::getBookId).toList();

        Map<Integer, BookResponse> bookMap = bookClient.getListBook(listIds).stream()
                .collect(Collectors.toMap(BookResponse::getId, book -> book));


        return borrowingLines.stream().map(borrowingLine -> mapToBorrowingLineDTO(borrowingLine, bookMap)).toList();
    }

    private BorrowingLineDTO mapToBorrowingLineDTO(
            BorrowingLine borrowingLine,
            Map<Integer, BookResponse> bookMap
    ) {
        // Lấy book tương ứng từ Map
        BookResponse book = bookMap.get(borrowingLine.getBookId());
        if (book == null) {
            return null; // Nếu không tìm thấy book, bỏ qua BorrowingLine này
        }

        // Tạo BookPurchaseDTO
        BookPurchaseDTO bookPurchaseDTO = BookPurchaseDTO.builder()
                .bookId(book.getId())
                .name(book.getName())
                .price(book.getPrice())
                .image(book.getUrlImage())
                .build();

        // Tạo BorrowingLineDTO và gán BookPurchaseDTO
        BorrowingLineDTO borrowingLineDTO = BorrowingLineMapper.toBorrowingLineDTO(borrowingLine);
        borrowingLineDTO.setBook(bookPurchaseDTO);
        return borrowingLineDTO;
    }
}
