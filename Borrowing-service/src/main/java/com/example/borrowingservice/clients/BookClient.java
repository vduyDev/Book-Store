package com.example.borrowingservice.clients;
import com.example.borrowingservice.config.FeignClientConfig;
import com.example.common.DTO.BookPurchaseDTO;
import com.example.common.request.BookPurchaseRequest;
import com.example.common.response.BookResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@FeignClient(name = "book-service",configuration = FeignClientConfig.class)
public interface BookClient {
    @PostMapping("/books/update-stock")
    List<BookPurchaseDTO> updateBookStock(List<BookPurchaseRequest> request);

    @PostMapping("/books/get-list-book-by-list-id")
    List<BookResponse> addBookStock(List<Integer> booksid);

}
