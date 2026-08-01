package com.skala.skala_shopping.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        // GlobalExceptionHandler 의 ErrorResponse 와 같은 형식을 유지합니다.
        response.getWriter().write(
                "{\"timestamp\":\"" + LocalDateTime.now() + "\","
                        + "\"status\":403,\"code\":\"FORBIDDEN\","
                        + "\"message\":\"해당 작업을 수행할 권한이 없습니다.\","
                        + "\"path\":\"" + request.getRequestURI() + "\"}"
        );
    }
}
