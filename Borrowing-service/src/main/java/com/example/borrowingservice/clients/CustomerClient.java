package com.example.borrowingservice.clients;
import com.example.common.DTO.CustomerDTO;
import com.example.common.configure.FeignClientConfig;
import com.example.common.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "identity-service", configuration = FeignClientConfig.class)
public interface CustomerClient {

    @GetMapping("/customers/get-customer-in-borrwing/{id}")
    CustomerDTO getCustomerInBorrowing(@PathVariable String id);


    @GetMapping("/customers/get-list-customer-in-borrowing")
    List<CustomerDTO> getListCustomerInBorrowing();

    @GetMapping("/customers/{id}")
    ApiResponse<CustomerDTO> getCustomerById(@PathVariable String id);

}
