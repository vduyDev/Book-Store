package com.example.identityservice.customer;

import com.example.common.request.CustomerRequest;
import com.example.common.request.CustomerUpdateRequest;

import java.util.List;

public interface CustomerService {
   List<Customer> listCustomer();
   Customer getCustomerById(String id);
   void deleteCustomerById(String id);
   Customer createCustomer(CustomerRequest customer);
   Customer updateCustomer(String id, CustomerUpdateRequest customer);
}
