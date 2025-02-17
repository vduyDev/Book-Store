package com.example.identityservice.customer;

import com.example.common.DTO.CustomerDTO;
import com.example.common.request.AuthRequest;
import com.example.common.request.CustomerRequest;
import com.example.common.request.CustomerUpdateRequest;
import org.keycloak.representations.AccessTokenResponse;

import java.util.List;
import java.util.Set;

public interface CustomerService {
   List<Customer> listCustomer();
   Customer getCustomerById(String id);
   void deleteCustomerById(String id);
   Customer createCustomer(CustomerRequest customer);
   Customer updateCustomer(String id, CustomerUpdateRequest customer);
   AccessTokenResponse login(AuthRequest authRequest);
   CustomerDTO getCustomerInBorrowing(String id);
   List<CustomerDTO> getListCustomerInBorrowing();
}
