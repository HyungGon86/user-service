package com.example.userservice.security;

public final class JwtProperties {
    private JwtProperties() {}

    public static final String SECRET_KEY = "user_token";
    public static final Long EXPIRATION_TIME = 3600000L;
}
