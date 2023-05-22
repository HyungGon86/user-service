package com.example.userservice.service;

import com.example.userservice.domain.Users;
import com.example.userservice.dto.RequestUserDto;
import com.example.userservice.dto.ResponseUserDto;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void createUser(RequestUserDto requestUserDto) {
        Users users = Users.builder()
                .name(requestUserDto.getName())
                .email(requestUserDto.getEmail())
                .encryptedPwd(passwordEncoder.encode(requestUserDto.getPwd()))
                .build();

        userRepository.save(users);
    }

    public ResponseUserDto getUserByEmail(String email) {
        Users users = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저정보가 존재하지 않습니다."));

        return ResponseUserDto.builder()
                .email(users.getEmail())
                .name(users.getName())
                .responseOrders(users.getOrders())
                .build();
    }


}
