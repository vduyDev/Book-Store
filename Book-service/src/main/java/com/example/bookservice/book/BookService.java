package com.example.bookservice.book;
import com.example.common.DTO.BookPurchaseDTO;
import com.example.common.request.BookPurchaseRequest;
import com.example.common.request.BookRequest;
import com.example.common.response.BookResponse;
import com.example.common.response.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    PageResponse<BookResponse> lisBook(int page, int size);
    Book getBookById(Integer id);
    void deleteBookById(Integer id);
    BookResponse createBook(BookRequest book,MultipartFile file);
    PageResponse<BookResponse> searchBookByName(String name,int page, int size);
    void updateStock(Integer id,int stock);
    void uploadImage(Integer id, MultipartFile file);
    List<BookPurchaseDTO>  updateStockBookPurchase(List<BookPurchaseRequest> request);
    List<BookResponse> getBookByListId(List<Integer> ids);
}
