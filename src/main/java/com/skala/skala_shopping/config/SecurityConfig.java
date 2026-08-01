package com.skala.skala_shopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.skala.skala_shopping.security.JwtAuthenticationFilter;
import com.skala.skala_shopping.security.RestAccessDeniedHandler;
import com.skala.skala_shopping.security.RestAuthenticationEntryPoint;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final RestAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            RestAuthenticationEntryPoint authenticationEntryPoint,
            RestAccessDeniedHandler accessDeniedHandler
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/api/health").permitAll()
                    // 데모 웹 페이지(정적 리소스)와 오류 디스패치(/error)를 공개합니다.
                    .requestMatchers("/", "/index.html", "/favicon.ico", "/error").permitAll()
                    // 상품 조회는 공개, 등록/수정/삭제는 관리자 전용입니다.
                    .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                    .requestMatchers("/api/products/**").hasRole("ADMIN")
                    // 회원가입과 로그인은 POST 요청만 공개합니다.
                    .requestMatchers(HttpMethod.POST, "/api/customers").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/customers/login").permitAll()
                    // 로그인한 고객이라면 본인 주문/조회/비밀번호 변경이 가능합니다.
                    // (비밀번호 변경의 본인 확인은 서비스 계층에서 수행합니다)
                    .requestMatchers(HttpMethod.GET, "/api/customers/me").authenticated()
                    .requestMatchers(HttpMethod.POST, "/api/customers/order", "/api/customers/cancel").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/api/customers").authenticated()
                    // 그 외 고객 리소스(목록/검색/삭제/고객별 주문 조회)는 관리자 전용입니다.
                    .requestMatchers("/api/customers/**").hasRole("ADMIN")
                    .requestMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/v3/api-docs.yaml"
                    ).permitAll()
                    .anyRequest().authenticated())
            .exceptionHandling(exception -> exception
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler))
            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );
        return http.build();
    }
}