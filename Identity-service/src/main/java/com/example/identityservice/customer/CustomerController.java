package com.example.identityservice.customer;
import com.example.common.request.CustomerRequest;
import com.example.common.request.CustomerUpdateRequest;
import com.example.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ApiResponse<List<Customer>> listCustomer (){
        return  ApiResponse.<List<Customer>>builder()
                .data(customerService.listCustomer())
                .status(200)
                .message("Success")
                .build();
    }

    @GetMapping ("/{id}")
    public ApiResponse<Customer> getCustomerById(@PathVariable String id){
        return  ApiResponse.<Customer>builder()
                .data( customerService.getCustomerById(id))
                .status(200)
                .message("Success")
                .build();
    }

    @PostMapping
    public ApiResponse<Customer> createCustomer(@Valid @RequestBody CustomerRequest request){
        return  ApiResponse.<Customer>builder()
                .data(customerService.createCustomer(request))
                .status(201)
                .message("Success")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCustomerByid(@PathVariable String id){
        customerService.deleteCustomerById(id);
        return  ApiResponse.<String>builder()
                .data("Xóa thành công customer có id :" + id)
                .status(200)
                .message("Success")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<Customer> updateCustomer(@PathVariable String id, @Valid @RequestBody CustomerUpdateRequest request){
        return  ApiResponse.<Customer>builder()
                .data(customerService.updateCustomer(id, request))
                .status(200)
                .message("Success")
                .build();
    }

}
