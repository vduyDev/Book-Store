package com.example.identityservice.customer;


import com.example.common.DTO.CustomerDTO;
import com.example.common.request.CustomerRequest;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Collections;

public class  CustomerMapper {

    public static Customer requestToCustomer(CustomerRequest request){
        return Customer.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();
    }

    public static Customer userToCustomer(UserRepresentation user) {
        String phone = user.getAttributes().getOrDefault("phone", Collections.singletonList(null)).get(0);
        String address = user.getAttributes().getOrDefault("address", Collections.singletonList(null)).get(0);
        return Customer.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(phone)
                .address(address)
                .build();
    }

    public static CustomerDTO toCustomerDTO(UserRepresentation userRepresentation) {
        return CustomerDTO.builder()
                .name(userRepresentation.getLastName())
                .id(userRepresentation.getId())
                .build();
    }
}
