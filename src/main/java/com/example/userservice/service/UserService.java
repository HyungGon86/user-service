package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.domain.Users;
import com.example.userservice.dto.RequestUserDto;
import com.example.userservice.dto.ResponseOrderDto;
import com.example.userservice.dto.ResponseUserDto;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final OrderServiceClient orderServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

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

        /*String orderUrl = String.format("http://order-service/%s/orders", users.getId());

        ResponseEntity<List<ResponseOrderDto>> orderListResponse = restTemplate.exchange(orderUrl, HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ResponseOrderDto>>() {
                });*/

        /*List<ResponseOrderDto> orderDtoList = orderServiceClient.getOrders(users.getId());*/

        log.info("Before call orders microservice");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuit breaker");
        List<ResponseOrderDto> orderDtoList = circuitBreaker.run(() -> orderServiceClient.getOrders(users.getId()), throwable -> new ArrayList<>());
        log.info("After called orders microservice");

        return ResponseUserDto.builder()
                .email(users.getEmail())
                .name(users.getName())
                .responseOrders(orderDtoList)
                .build();
    }


}
