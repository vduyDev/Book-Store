package com.example.borrowingservice.borrowing;
import com.example.common.event.BookReturnEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class BorrowingProducer {

    private KafkaTemplate<String, Object> kafkaTemplate;

    public void kafkaUpdateBookStock(List<BookReturnEvent> bookReturnEvents) {
        log.info("asdasdasasdasd");
        bookReturnEvents.forEach(bookReturnEvent -> {

            kafkaTemplate.send("inventory-updated", bookReturnEvent);
        });
    }

}
