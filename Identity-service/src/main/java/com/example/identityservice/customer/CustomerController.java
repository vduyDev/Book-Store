package com.example.identityservice.customer;
import com.example.common.DTO.CustomerDTO;
import com.example.common.request.AuthRequest;
import com.example.common.request.CustomerRequest;
import com.example.common.request.CustomerUpdateRequest;
import com.example.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    @PostMapping("/login")
    public AccessTokenResponse login(@RequestBody AuthRequest authRequest){
        return  customerService.login(authRequest);
    }
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

    @GetMapping("/get-customer-in-borrwing/{id}")
    public CustomerDTO getCustomerInBorrowing(@PathVariable String id){
        return  customerService.getCustomerInBorrowing(id);
    }

    @GetMapping("/get-list-customer-in-borrowing")
    List<CustomerDTO> getListCustomerInBorrowing(){
        return  customerService.getListCustomerInBorrowing();
    }
}
