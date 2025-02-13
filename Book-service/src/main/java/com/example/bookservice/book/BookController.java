package com.example.bookservice.book;

import com.example.common.DTO.BookPurchaseDTO;
import com.example.common.request.BookPurchaseRequest;
import com.example.common.request.BookRequest;
import com.example.common.request.StockUpdateRequest;
import com.example.common.response.ApiResponse;
import com.example.common.response.BookResponse;
import com.example.common.response.PageResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;


    @GetMapping
    public ApiResponse<PageResponse<BookResponse>> getListBook(
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "3", required = false) int size
    ) {
        return ApiResponse.<PageResponse<BookResponse>>builder()
                .data(bookService.lisBook(page, size))
                .message("Success")
                .status(200)
                .build();
    }


    @PostMapping("/upload-image/{id}")
    public ApiResponse<String> uploadImage(
            @PathVariable int id,
            @RequestParam("file") MultipartFile file
    ) {
        bookService.uploadImage(id, file);
        return  ApiResponse.<String>builder()
                .data("upload hình ảnh  thành công.")
                .message("Success")
                .status(200)
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<BookResponse>> getListBookByName(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size
    ) {
        return ApiResponse.<PageResponse<BookResponse>>builder()
                .data(bookService.searchBookByName(name, page, size))
                .message("Success")
                .status(200)
                .build();
    }

    @PutMapping("/{id}/stock")
    public ApiResponse<String> updateStock(@PathVariable Integer id, @RequestBody StockUpdateRequest request) {
        bookService.updateStock(id, request.getStock());
        return ApiResponse.<String>builder()
                .data("Cập nhật số lượng thành công.")
                .message("Success")
                .status(200)
                .build();

    }

    @PostMapping("/update-stock")
    public List<BookPurchaseDTO> updateStock(@Valid @RequestBody List<BookPurchaseRequest> request) {
      return  bookService.updateStockBookPurchase(request);
    }

    @PostMapping
    public ApiResponse<BookResponse> createBook(
            @Valid @RequestBody BookRequest request,
            @RequestParam("file") MultipartFile file
    ) {
        return ApiResponse.<BookResponse>builder()
                .status(201)
                .message("Success")
                .data(bookService.createBook(request,file))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteBookById(@PathVariable Integer id) {
        bookService.deleteBookById(id);
        return ApiResponse.<String>builder()
                .status(200)
                .data("Xóa thành công.")
                .message("Success")
                .build();
    }


    @GetMapping("/{id}")
    public ApiResponse<BookResponse> getBookById(@PathVariable Integer id) {
        return ApiResponse.<BookResponse>builder()
                .status(200)
                .data(bookService.getBookById(id))
                .message("Success")
                .build();
    }

    @PostMapping("/get-list-book-by-list-id")
    public List<BookResponse> getBookByListId(@RequestBody List<Integer> ids) {
        return bookService.getBookByListId(ids);
    }
}
