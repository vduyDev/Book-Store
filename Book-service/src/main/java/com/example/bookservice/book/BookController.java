package com.example.bookservice.book;

import com.example.bookservice.clients.BorrowingClient;
import com.example.common.DTO.BookPurchaseDTO;
import com.example.common.DTO.BorrowingLineDTO;
import com.example.common.request.BookPurchaseRequest;
import com.example.common.request.BookRequest;
import com.example.common.request.StockUpdateRequest;
import com.example.common.response.ApiResponse;
import com.example.common.response.BookResponse;
import com.example.common.response.PageResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
    private final BorrowingClient borrowingClient;


    @Operation(
            summary = "Lấy danh sách sách", // Tiêu đề API trong Swagger UI
            description = "API trả về danh sách có hỗ trợ phân trang." // Mô tả chi tiết API

    )
    @GetMapping
    public ApiResponse<PageResponse<BookResponse>> getListBook(
            @Parameter(description = "Số trang cần lấy (mặc định: 1)", example = "1")
            @RequestParam(value = "page", defaultValue = "1", required = false) int page,

            @Parameter(description = "Số lượng sách trên mỗi trang (mặc định: 3)", example = "3")
            @RequestParam(value = "size", defaultValue = "3", required = false) int size
    ) {
        return ApiResponse.<PageResponse<BookResponse>>builder()
                .data(bookService.lisBook(page, size))
                .message("Success")
                .status(200)
                .build();
    }

    @Operation(
            summary = "Upload hình ảnh của sách",
            description = "Thêm hình ảnh của sách đã tồn tại."
    )
    @PostMapping("/upload-image/{id}")
    public ApiResponse<String> uploadImage(
            @Parameter(description = "id của sách", name = "id")
            @PathVariable int id,
            @RequestParam("file") MultipartFile file
    ) {
        bookService.uploadImage(id, file);
        return ApiResponse.<String>builder()
                .data("upload hình ảnh  thành công.")
                .message("Success")
                .status(200)
                .build();
    }

    @Operation(
            summary = "Tìm kiếm sách",
            description = "Tìm kiếm sách theo tên."
    )
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

    @Operation(
            summary = "Cập nhật số lượng sách",
            description = "Cập nhật số lượng sách trong kho dựa trên ID sách."
    )
    @PutMapping("/{id}/stock")
    public ApiResponse<String> updateStock(@PathVariable Integer id, @RequestBody StockUpdateRequest request) {
        bookService.updateStock(id, request.getStock());
        return ApiResponse.<String>builder()
                .data("Cập nhật số lượng thành công.")
                .message("Success")
                .status(200)
                .build();

    }

    @Hidden
    @PostMapping("/update-stock")
    public List<BookPurchaseDTO> updateStock(@Valid @RequestBody List<BookPurchaseRequest> request) {
        return bookService.updateStockBookPurchase(request);
    }

    @Operation(summary = "Tạo sách mới")
    @PostMapping
    public ApiResponse<BookResponse> createBook(
            @Valid @RequestBody BookRequest request,
            @RequestParam("file") MultipartFile file
    ) {
        return ApiResponse.<BookResponse>builder()
                .status(201)
                .message("Success")
                .data(bookService.createBook(request, file))
                .build();
    }

    @Operation(summary = "Xóa sách theo ID")
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteBookById(@PathVariable Integer id) {
        bookService.deleteBookById(id);
        return ApiResponse.<String>builder()
                .status(200)
                .data("Xóa thành công.")
                .message("Success")
                .build();
    }

    @Operation(summary = "Lấy thông tin sách theo ID")
    @GetMapping("/{id}")
    public ApiResponse<BookResponse> getBookById(@PathVariable Integer id) {
        BookResponse bookResponse = BookMapper.toBookResponse(bookService.getBookById(id));
        return ApiResponse.<BookResponse>builder()
                .status(200)
                .data(bookResponse)
                .message("Success")
                .build();
    }

    @Hidden
    @PostMapping("/get-list-book-by-list-id")
    public List<BookResponse> getBookByListId(@RequestBody List<Integer> ids) {
        return bookService.getBookByListId(ids);
    }

}
