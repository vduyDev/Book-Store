package com.example.bookservice.category;
import com.example.common.request.CategoryRequest;
import com.example.common.response.ApiResponse;
import com.example.common.response.CategoryResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {

    private final  CategoryService categoryService;

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getListCategory(){
        return   ApiResponse.<List<CategoryResponse>>builder()
                .data(categoryService.listCategory())
                .message("Success")
                .status(200)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategoryById(@PathVariable Integer id){
        return   ApiResponse.<CategoryResponse>builder()
                .data(categoryService.getCategoryById(id))
                .message("Success")
                .status(200)
                .build();
    }

    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request){
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.createCategory(request))
                .message("Success")
                .status(201)
                .build() ;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCategory(@PathVariable Integer id){
        categoryService.deleteCategory(id);
        return ApiResponse.<String>builder()
                .data("Xóa thành công category có id là " + id)
                .message("Success")
                .status(201)
                .build() ;
    }

}
