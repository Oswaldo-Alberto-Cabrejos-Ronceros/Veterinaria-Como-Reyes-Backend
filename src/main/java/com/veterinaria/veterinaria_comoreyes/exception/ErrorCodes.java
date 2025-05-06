package com.veterinaria.veterinaria_comoreyes.exception;

public enum ErrorCodes {
    INVALID_CREDENTIALS("AUTH-001"),
    ACCOUNT_DISABLED("AUTH-002"),
    USER_NOT_FOUND("AUTH-003"),
    INVALID_TOKEN("AUTH-004"),
    TOKEN_EXPIRED("AUTH-005");

    private final String code;

    ErrorCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
