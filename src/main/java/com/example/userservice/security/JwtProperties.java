package com.example.userservice.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "token")
public class JwtProperties {

    private final String secret;
    private final String expirationTime;

}
