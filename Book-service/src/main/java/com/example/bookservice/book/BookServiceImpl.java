package com.example.bookservice.book;

import com.example.bookservice.category.Category;
import com.example.common.response.CategoryResponse;
import com.example.bookservice.category.CategoryService;
import com.example.common.DTO.BookPurchaseDTO;
import com.example.common.enums.ErrorCode;
import com.example.common.exception.AppException;
import com.example.common.request.BookPurchaseRequest;
import com.example.common.request.BookRequest;
import com.example.common.response.BookResponse;
import com.example.common.response.PageResponse;
import com.example.common.utils.CloudDinaryUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepo bookRepo;
    private final CloudDinaryUtils cloudDinaryUtils;
    private final CategoryService categoryService;

    @Override
    public PageResponse<BookResponse> lisBook(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Book> bookPage = bookRepo.findAll(pageable);
        List<BookResponse> bookResponses = bookPage.getContent().stream()
                .map(BookMapper::toBookResponse)
                .toList();

        return PageResponse.<BookResponse>builder()
                .currenPage(page)
                .totalPage(bookPage.getTotalPages())
                .pageSize(bookPage.getSize())
                .totalElement((int) bookPage.getTotalElements())
                .data(bookResponses)
                .build();
    }

    @Override
    public PageResponse<BookResponse> searchBookByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Book> bookPage = bookRepo.searchByName(name, pageable);
        List<BookResponse> bookResponses = bookPage.getContent().stream()
                .map(BookMapper::toBookResponse)
                .toList();
        return PageResponse.<BookResponse>builder()
                .currenPage(page)
                .totalPage(bookPage.getTotalPages())
                .pageSize(bookPage.getSize())
                .totalElement((int) bookPage.getTotalElements())
                .data(bookResponses)
                .build();
    }

    @Override
    public void updateStock(Integer id, int stock) {
        Book book = findBookById(id);
        book.setStock(stock);
        bookRepo.save(book);
    }

    @Override
    public void uploadImage(Integer id, MultipartFile file) {
        Book book = findBookById(id);
        String imageUrl = book.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            cloudDinaryUtils.deleteImage(imageUrl);
        }
        String urlImage = cloudDinaryUtils.uploadImage(file);
        book.setImageUrl(urlImage);
        bookRepo.save(book);
    }

    @Override
    public List<BookPurchaseDTO> updateStockBookPurchase(List<BookPurchaseRequest> request) {
        List<Book> books = new ArrayList<>();
        request.forEach(bookPurchaseRequest -> {
            Book book = findBookById(bookPurchaseRequest.getBookId());
            int stock = book.getStock() - bookPurchaseRequest.getQuantity();
            if (stock < 0) {
                throw new AppException(ErrorCode.NotEnoughStock);
            }
            book.setStock(stock);
            books.add(bookRepo.save(book));
        });

      return books.stream().map(BookMapper::toBookPurchaseDTO).toList();
    }

    @Override
    public List<BookResponse> getBookByListId(List<Integer> ids) {
        return List.of();
    }

    @Override
    public BookResponse getBookById(Integer id) {
        Book book = findBookById(id);
        return BookMapper.toBookResponse(book);
    }

    @Override
    public void deleteBookById(Integer id) {
        Book book = findBookById(id);
        String imageUrl = book.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            cloudDinaryUtils.deleteImage(imageUrl);
        }
        bookRepo.delete(book);
    }

    @Override
    public BookResponse createBook(BookRequest request, MultipartFile file) {
        CategoryResponse categoryResponse = categoryService.getCategoryById(request.getCategoryId());
        Category category = new Category();
        category.setId(categoryResponse.getId());
        category.setName(categoryResponse.getName());
        String imageUrl = cloudDinaryUtils.uploadImage(file);
        Book book = BookMapper.toBook(request, category);
        book.setImageUrl(imageUrl);
        return BookMapper.toBookResponse(bookRepo.save(book));
    }

    public Book findBookById(Integer id) {
        return bookRepo
                .findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BookNotFound));
    }
}
