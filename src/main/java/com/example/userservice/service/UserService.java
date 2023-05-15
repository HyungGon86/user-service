package com.example.userservice.service;

import com.example.userservice.domain.Users;
import com.example.userservice.dto.RequestUserDto;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public void createUser(RequestUserDto requestUserDto) {
        requestUserDto.setUserId(UUID.randomUUID().toString());

        Users users = Users.builder()
                .userId(requestUserDto.getUserId())
                .name(requestUserDto.getName())
                .email(requestUserDto.getEmail())
                .encryptedPwd(requestUserDto.getEncryptedPwd())
                .build();

        userRepository.save(users);
    }
}
