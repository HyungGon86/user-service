package com.example.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ResponseUserDto {

    private String email;
    private String name;
    private String userId;

    @Builder
    public ResponseUserDto(String email, String name, String userId) {
        this.email = email;
        this.name = name;
        this.userId = userId;
    }
}
