package com.example.bookservice.book;

import com.example.bookservice.clients.BorrowingClient;
import com.example.common.DTO.BorrowingLineDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class BookConsumer {

    private final BookService bookService;

    private final BorrowingClient borrowingClient;

    @KafkaListener(topics = "payment-failed", groupId = "book")
    public void updateStock(String borrowingId) {
        log.info("update stock {}", borrowingId);

        List<BorrowingLineDTO> lines = borrowingClient.getBorrowingLinesByBorrowingId(borrowingId).getData();
        lines.forEach(line -> {
            Book book = bookService.getBookById(line.getBook().getBookId());
            int stock = book.getStock() + line.getQuantity();
            book.setStock(stock);
            bookService.updateStock(book.getId(), stock);
        });


    }
}
