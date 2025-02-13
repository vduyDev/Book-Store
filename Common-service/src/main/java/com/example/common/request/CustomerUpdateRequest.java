package com.example.common.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerUpdateRequest {
    @NotBlank(message = "Họ không được bỏ trống.")
    private String firstName;

    @NotBlank(message = "Tên không được bỏ trống.")
    private String lastName;


    @NotBlank(message = "Số điện thoại không được bỏ trống.")
    private String phone;

    @NotBlank(message = "Địa chỉ không được bỏ trống.")
    private String address;

}
