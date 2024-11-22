package com.ktb.eatbookappbackend.global.authentication;

import com.ktb.eatbookappbackend.global.message.GlobalErrorMessage;
import com.ktb.eatbookappbackend.global.reponse.FailureResponseDTO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 특정 메서드에 접근하기 전에 사용자가 인증되었는지 확인하는 Aspect입니다. 또한 인증된 멤버의 ID를 가져오는 방법을 제공합니다.
 */
@Aspect
@Component
public class AuthenticationAspect {

    private static final ThreadLocal<String> memberIdThreadLocal = new ThreadLocal<>();

    private final String fixedMemberId = "00bc7946-f8ad-4076-805f-8c8346b339a8";

    /**
     * 사용자가 인증되었는지 확인하고 인증된 멤버의 ID를 스레드 로컬 변수에 저장합니다.
     */
    @Around("@annotation(authenticated)")
    public Object checkAuthentication(ProceedingJoinPoint joinPoint, Authenticated authenticated) throws Throwable {
        boolean isAuthenticated = isUserAuthenticated();
        if (!isAuthenticated) {
            return ResponseEntity.status(401).body(FailureResponseDTO.of(GlobalErrorMessage.UNAUTHORIZED_ACCESS));
        }

        memberIdThreadLocal.set(fixedMemberId);

        try {
            return joinPoint.proceed();
        } finally {
            memberIdThreadLocal.remove();
        }
    }

    /**
     * 사용자가 인증되었는지 확인합니다.
     */
    private boolean isUserAuthenticated() {
        // TO DO - 실제 인증 로직 구현
        return true;
    }

    /**
     * 스레드 로컬 변수에서 인증된 멤버의 ID를 가져옵니다.
     */
    public static String getAuthenticatedMemberId() {
        return memberIdThreadLocal.get();
    }
}

