package com.skala.skala_shopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 비밀번호 원문을 저장하지 않고 BCrypt 해시값으로 변환합니다.
        return new BCryptPasswordEncoder();
    }
}