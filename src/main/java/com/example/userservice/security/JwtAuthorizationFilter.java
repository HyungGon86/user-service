package com.example.userservice.security;

import com.example.userservice.domain.Users;
import com.example.userservice.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;


@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;
    private JwtProperties jwtProperties;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtProperties jwtProperties) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("1. 권한이나 인증이 필요한 요청이 전달됨");
        log.info("CHECK JWT : JwtAuthorizationFilter.doFilterInternal");
        String jwtHeader = request.getHeader("Authorization"); // Header에 들어있는 Authorization을 꺼낸다.
        log.info("jwt Header : " + jwtHeader);
        log.info("============================================================================\n");

        log.info("2. Header 확인");
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }
        log.info("============================================================================\n");

        // JWT 토큰을 검증해서 정상적인 사용자인지, 권한이 맞는지 확인
        log.info("3. JWT 토큰을 검증해서 정상적인 사용자인지, 권한이 맞는지 확인");
        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
        String email = null;
        try {
            email = Jwts
                    .parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(jwtToken)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new RuntimeException();
        }
        log.info("============================================================================\n");

        if (email != null) {
            // 서명이 정상적으로 됨
            log.info("서명이 정상적으로 됨");

            // 4. 정상적인 서명이 검증되었으므로 username으로 회원을 조회한다.
            log.info("4. 서명이 검증되었다.");
            log.info("Athentication 생성을 위해 username으로 회원 조회 후 PricipalDetails 객체로 감싼다.");
            Users users = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저가 없음"));
            PrincipalDetails principalDetails = new PrincipalDetails(users);
            log.info("============================================================================\n");

            // 5. jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
            log.info("5. Authentication 객체 생성");
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            principalDetails, null, principalDetails.getAuthorities()
                    );
            log.info("============================================================================\n");

            // 6. 강제로 시큐리티_세션에 접근하여 Authentication 객체를 저장해준다.
            log.info("6. 시큐리티_세션에 접근하여 Authentication 객체 저장");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("============================================================================\n");

            chain.doFilter(request, response);
        }
    }

}
