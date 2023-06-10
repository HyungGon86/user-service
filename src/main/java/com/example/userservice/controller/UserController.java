package com.example.userservice.controller;

import com.example.userservice.dto.RequestUserDto;
import com.example.userservice.dto.ResponseUserDto;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final Environment env;

    @GetMapping("/health_check")
    public String status() {
        return "It's Working in User Service"
                + ". port(local.server.port)=" + env.getProperty("local.server.port")
                + ". port(server.port)=" + env.getProperty("server.port")
                + ". token secret =" + env.getProperty("token.secret")
                + ". token expiration time=" + env.getProperty("token.expiration_time");
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUserDto> createUser(@RequestBody RequestUserDto requestUserDto) {
        userService.createUser(requestUserDto);

        ResponseUserDto responseUserDto = ResponseUserDto.builder()
                .email(requestUserDto.getEmail())
                .name(requestUserDto.getName())
                .build();

        return new ResponseEntity<>(responseUserDto, HttpStatus.CREATED);
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<ResponseUserDto> getUserByUserId(@PathVariable String email) {

        ResponseUserDto user = userService.getUserByEmail(email);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUserDto>> getUsers() {
        List<ResponseUserDto> responseUserDtoList = userRepository.findAll()
                .stream()
                .map(user -> ResponseUserDto.builder()
                        .name(user.getName())
                        .email(user.getEmail())
                        .responseOrders(user.getOrders())
                        .build())
                .toList();

        return new ResponseEntity<>(responseUserDtoList, HttpStatus.OK);

    }

}
