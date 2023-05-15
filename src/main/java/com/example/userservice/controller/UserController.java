package com.example.userservice.controller;

import com.example.userservice.dto.RequestUserDto;
import com.example.userservice.dto.ResponseUserDto;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/health_check")
    public String status() {
        return "It's Working in User Service";
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUserDto> createUser(@RequestBody RequestUserDto requestUserDto) {
        userService.createUser(requestUserDto);

        ResponseUserDto responseUserDto = ResponseUserDto.builder()
                .email(requestUserDto.getEmail())
                .name(requestUserDto.getName())
                .userId(requestUserDto.getUserId())
                .build();

        return new ResponseEntity<>(responseUserDto, HttpStatus.CREATED);
    }

}
