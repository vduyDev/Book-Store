package com.example.identityservice.customer;

import com.example.common.DTO.CustomerDTO;
import com.example.common.request.AuthRequest;
import com.example.common.request.CustomerRequest;
import com.example.common.request.CustomerUpdateRequest;
import com.example.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    @Operation(summary = "Đăng nhập")
    @PostMapping("/login")
    public AccessTokenResponse login(@RequestBody AuthRequest authRequest) {
        return customerService.login(authRequest);
    }

    @GetMapping
    public ApiResponse<List<CustomerDTO>> listCustomer() {
        return ApiResponse.<List<CustomerDTO>>builder()
                .data(customerService.listCustomer())
                .status(200)
                .message("Success")
                .build();
    }

    @Operation(summary = "Láy danh sách customer theo id")
    @GetMapping("/{id}")
    public ApiResponse<CustomerDTO> getCustomerById(@PathVariable String id) {
        return ApiResponse.<CustomerDTO>builder()
                .data(customerService.getCustomerById(id))
                .status(200)
                .message("Success")
                .build();
    }

    @Operation(summary = "Đăng ký tài khoản")
    @PostMapping
    public ApiResponse<CustomerDTO> createCustomer(@Valid @RequestBody CustomerRequest request) {
        return ApiResponse.<CustomerDTO>builder()
                .data(customerService.createCustomer(request))
                .status(201)
                .message("Success")
                .build();
    }

    @Operation(summary = "Xóa customer theo id")
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCustomerByid(@PathVariable String id) {
        customerService.deleteCustomerById(id);
        return ApiResponse.<String>builder()
                .data("Xóa thành công customer có id :" + id)
                .status(200)
                .message("Success")
                .build();
    }

    @Operation(summary = "Cật nhật thông tin customer")
    @PutMapping("/{id}")
    public ApiResponse<CustomerDTO> updateCustomer(@PathVariable String id, @Valid @RequestBody CustomerUpdateRequest request) {
        return ApiResponse.<CustomerDTO>builder()
                .data(customerService.updateCustomer(id, request))
                .status(200)
                .message("Success")
                .build();
    }

    @Operation(summary = "Lấy thông tin customer theo id đặt sách")
    @GetMapping("/get-customer-in-borrwing/{id}")
    public CustomerDTO getCustomerInBorrowing(@PathVariable String id) {
        return customerService.getCustomerInBorrowing(id);
    }

    @Operation(summary = "Lấy danh sách thông tin customer theo id đặt sách")
    @GetMapping("/get-list-customer-in-borrowing")
    List<CustomerDTO> getListCustomerInBorrowing() {
        return customerService.getListCustomerInBorrowing();
    }



}
