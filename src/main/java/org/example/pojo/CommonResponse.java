package org.example.pojo;

import org.springframework.util.StringUtils;

public class CommonResponse {
    private static final String SUCCESS_CODE = "S";
    private static final String SUCCESS_MSG = "Success";
    private static final String ERROR_CODE = "E";
    private static final String ERROR_MSG = "Unknown exception";

    private String code;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static CommonResponse success() {
        CommonResponse response = new CommonResponse();
        response.setCode(SUCCESS_CODE);
        response.setMessage(SUCCESS_MSG);
        return response;
    }

    public static CommonResponse fail() {
        return fail(null);
    }

    public static CommonResponse fail(String message) {
        CommonResponse response = new CommonResponse();
        response.setCode(ERROR_CODE);
        if (StringUtils.hasText(message)) {
            response.setMessage(message);
        } else {
            response.setMessage(ERROR_MSG);
        }
        return response;
    }
}
