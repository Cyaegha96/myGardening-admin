package com.ggirick.gardening_admin_backend.exceptions;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }

    // (선택 사항) 근본 원인(Throwable cause)을 포함하는 생성자
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
