package org.example.exception;

import org.example.utils.Errors;

public class CarsRentalException extends RuntimeException {
    private static String messagePattern = "[%s]: %s";

    public CarsRentalException(Errors error) {
        super(String.format(messagePattern, error.getCode(), error.getMessage()));
    }

    public CarsRentalException(Errors error, String... args) {
        super(String.format(messagePattern, error.getCode(), String.format(error.getMessage(), args)));
    }
}
