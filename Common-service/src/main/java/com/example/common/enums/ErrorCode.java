package com.example.common.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {

    CustomerNotFound(404, "Customer not found"),
    CustomerAlreadyExists(400, "Customer already exists"),
    CustomerCreationFailed(500, "Customer creation failed"),
    BookNotFound(404, "Book not found"),
    NotEnoughStock(400, "Not enough stock"),
    CategoryNotFound(404, "Category not found"),
    CategoryAlreadyExists(400, "Category already exists"),
    InvalidUrlImage(400, "Invalid url"),
    DeleteImageFailed(500, "Delete image failed"),
    UploadImageFailed(500, "Upload image failed"),
    CREATE_PAYMENT_SESSION_FAILED(500, "Create payment session failed"),
    BorrowingNotFound(404, "Borrowing not found"),;


    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    private final int status;
    private final String message;



}
