package com.example.bookservice.category;


import com.example.common.request.CategoryRequest;
import com.example.common.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> listCategory();
    CategoryResponse createCategory(CategoryRequest request);
    void deleteCategory(Integer id);
    CategoryResponse getCategoryById(Integer id);
}
