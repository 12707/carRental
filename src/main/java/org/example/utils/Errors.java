package org.example.utils;

public enum Errors {
    INVALID_INPUT("E0001", "The input [%s] is illegal!"),
    ILLEGAL_CUSTOMER("E0002", "The customer doesn't exist!"),
    LACK_OF_STORAGE("E0003", "The storage of cars in the garage is inadequate!"),
    POSITIVE_INTEGER_REQUIRED("E0004", "The input number [%s] should be a positive integer!"),
    LOCK_RECORD_FAIL("E0005", "Lock record failed, maybe no data found!"),
    TIME_UNIT_EXCEPTION("E0006", "The time unit [%s] input is not valid!"),
    SAVE_RENTAL_EXCEPTION("E0007", "Save rental info exception!"),
    UNKNOWN_EXCEPTION("E9999", "Unknown exception!");
    private String code;

    private String message;

    Errors(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
