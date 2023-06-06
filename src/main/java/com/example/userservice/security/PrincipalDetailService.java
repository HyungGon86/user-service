package com.example.userservice.security;

import com.example.userservice.domain.Users;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("PrincipalDetailService.loadUserByUserName");
        log.info("Login");

        Users users = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("일치하는 유저정보가 없습니다."));

        return new PrincipalDetails(users);
    }
}
