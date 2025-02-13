package com.example.bookservice.category;


import com.example.bookservice.book.Book;
import com.example.bookservice.book.BookRepo;
import com.example.common.enums.ErrorCode;
import com.example.common.exception.AppException;
import com.example.common.request.CategoryRequest;
import com.example.common.response.CategoryResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    private final BookRepo bookRepo;
    @Override
    public List<CategoryResponse> listCategory() {
        return categoryRepo.findAll()
                .stream()
                .map(category ->
                        CategoryResponse
                                .builder()
                                .name(category.getName())
                                .id(category.getId())
                                .build()
                ).toList();
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
       Category categoryExist = categoryRepo.getCategoryByName(request.getName());
        if(categoryExist != null ){
                throw  new AppException(ErrorCode.CategoryNotFound);
        }
        Category category = new Category();
        category.setName(request.getName());
        Category categorySave = categoryRepo.save(category);
        return CategoryResponse.builder()
                .name(categorySave.getName())
                .id(categorySave.getId())
                .build();
    }

    @Override
    public  CategoryResponse getCategoryById(Integer id){
        Category category = categoryRepo
                .findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.CategoryNotFound));
        return  CategoryResponse.builder()
                .name(category.getName())
                .id(category.getId())
                .build();
    }

    @Override
    public void deleteCategory(Integer id) {
        Category category = categoryRepo
                .findById(id)
                .orElseThrow(()->  new AppException(ErrorCode.CategoryNotFound));

        List<Book> books = category.getBooks()
                .stream()
                .peek(book -> book.setCategory(null))
                .toList();
        bookRepo.saveAll(books);
        categoryRepo.delete(category);
    }

}
