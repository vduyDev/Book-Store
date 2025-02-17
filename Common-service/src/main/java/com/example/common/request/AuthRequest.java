package com.example.common.request;


import lombok.Data;
import lombok.Getter;

@Data
public class AuthRequest {
  private String username;
  private String password;
}
