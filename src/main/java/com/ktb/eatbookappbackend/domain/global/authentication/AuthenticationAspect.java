package com.ktb.eatbookappbackend.domain.global.authentication;

import com.ktb.eatbookappbackend.domain.global.message.MessageCode;
import com.ktb.eatbookappbackend.domain.global.reponse.FailureResponseDTO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthenticationAspect {
    private static final ThreadLocal<String> memberIdThreadLocal = new ThreadLocal<>();

    private final String fixedMemberId = "00bc7946-f8ad-4076-805f-8c8346b339a8";

    @Around("@annotation(authenticated)")
    public Object checkAuthentication(ProceedingJoinPoint joinPoint, Authenticated authenticated) throws Throwable {
        boolean isAuthenticated = isUserAuthenticated();
        if (!isAuthenticated) {
            return ResponseEntity.status(401).body(FailureResponseDTO.of(MessageCode.GlobalErrorMessage.UNAUTHORIZED_ACCESS));
        }

        memberIdThreadLocal.set(fixedMemberId);

        try {
            return joinPoint.proceed();
        } finally {
            memberIdThreadLocal.remove();
        }
    }

    private boolean isUserAuthenticated() {
        // TO DO - 실제 인증 로직 구현
        return true;
    }

    public static String getAuthenticatedMemberId() {
        return memberIdThreadLocal.get();
    }
}

