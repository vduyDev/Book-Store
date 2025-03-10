package com.example.notificationservice.email;

import lombok.Getter;

@Getter
public enum EmailTemplates {
    PAYMENT_CONFIRMATION("payment-confirmation.html", "Payment successfully processed"),
    BORROWING_CONFIRMATION("borrowing-confirmation.html", "Borrowing confirmation")
    ;

    private final String template;
    private final String subject;


    EmailTemplates(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}
