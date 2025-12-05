package com.ggirick.gardening_admin_backend.exceptions;

public class TokenRefreshException extends RuntimeException {
    public TokenRefreshException(String message) {
        super(message);
    }
}