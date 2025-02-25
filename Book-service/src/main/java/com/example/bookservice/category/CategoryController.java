package com.example.bookservice.category;

import com.example.common.request.CategoryRequest;
import com.example.common.response.ApiResponse;
import com.example.common.response.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Lấy danh sách danh mục",
            description = "Trả về danh sách tất cả danh mục có trong hệ thống."
    )
    @GetMapping
    public ApiResponse<List<CategoryResponse>> getListCategory() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(categoryService.listCategory())
                .message("Success")
                .status(200)
                .build();
    }
    @Operation(summary = "Lấy danh mục theo id")

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategoryById(@PathVariable Integer id) {
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.getCategoryById(id))
                .message("Success")
                .status(200)
                .build();
    }

    @Operation(summary = "Tạo danh mục")
    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .data(categoryService.createCategory(request))
                .message("Success")
                .status(201)
                .build();
    }
    @Operation(summary = "Xóa danh mục theo id")
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ApiResponse.<String>builder()
                .data("Xóa thành công category có id là " + id)
                .message("Success")
                .status(201)
                .build();
    }

}
