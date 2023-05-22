package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class RequestUserDto {

    @NotNull(message = "Email cannot be null")
    @Size(min = 2, message = "Email not be less than two characters")
    @Email
    private String email;

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, message = "Name not be less than two characters")
    private String name;

    @NotNull(message = "Password cannot be null")
    @Size(min = 2, message = "Password not be less than two characters")
    private String pwd;

    private Date createdAt;

    private String encryptedPwd;

    public RequestUserDto() {
    }

    @Builder
    public RequestUserDto(String email, String name, String pwd, Date createdAt, String encryptedPwd) {
        this.email = email;
        this.name = name;
        this.pwd = pwd;
        this.createdAt = createdAt;
        this.encryptedPwd = encryptedPwd;
    }
}
