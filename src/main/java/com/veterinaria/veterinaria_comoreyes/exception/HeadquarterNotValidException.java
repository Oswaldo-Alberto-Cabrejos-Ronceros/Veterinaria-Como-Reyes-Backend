package com.veterinaria.veterinaria_comoreyes.exception;

public class HeadquarterNotValidException extends RuntimeException {
    public HeadquarterNotValidException(String message) {
        super(message);
    }
    public HeadquarterNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

}
