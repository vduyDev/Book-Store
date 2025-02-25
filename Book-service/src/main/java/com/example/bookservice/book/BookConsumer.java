package com.example.bookservice.book;

import com.example.bookservice.clients.BorrowingClient;
import com.example.common.DTO.BorrowingLineDTO;
import com.example.common.event.BookReturnEvent;
import com.example.common.request.BookPurchaseRequest;
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
            update(line.getBook().getBookId(),line.getQuantity());
        });
    }

    @KafkaListener(topics = "inventory-updated",groupId = "inventory")
    public void updateInventory(BookReturnEvent bookReturnEvent){
            log.info("book-update");
            update(bookReturnEvent.getBookId(),bookReturnEvent.getQuantity());
    }


    private void update(Integer id,int quantity){
        Book book = bookService.getBookById(id);
        int stock = book.getStock() + quantity;
        book.setStock(stock);
        bookService.updateStock(book.getId(), stock);
    }
}
