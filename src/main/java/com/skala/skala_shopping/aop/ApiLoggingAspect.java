package com.skala.skala_shopping.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect      // 공통 관심사(로깅)를 모아둔 클래스임을 선언
@Component   // 빈으로 등록해야 AOP 가 동작합니다.
public class ApiLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(ApiLoggingAspect.class);

    // controller 패키지의 모든 public 메서드에 적용하되,
    // AuthController 는 제외합니다. 요청 파라미터(args)에 비밀번호가 담긴
    // DTO 가 포함되어 있어 로그로 유출될 수 있기 때문입니다. (3.7.1 경고 참조)
    @Around("execution(public * com.skala.shop.controller..*Controller.*(..))"
            + " && !within(com.skala.shop.controller.AuthController)")
    public Object logApi(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String method = joinPoint.getSignature().toShortString();

        log.info("[API REQUEST ] {} | args={}", method, joinPoint.getArgs());

        Object result = joinPoint.proceed();   // 실제 컨트롤러 메서드 실행

        long duration = System.currentTimeMillis() - start;
        log.info("[API RESPONSE] {} | {}ms", method, duration);
        return result;
    }
}