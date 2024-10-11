package dev.cloud.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {
    private SecurityUtil() {}

    // JwtFilter에서 SecurityContext에 세팅한 Member 정보를 꺼낸다.
    public static Long getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security Context에 인증정보가 없습니다.");
        }

        // 저장된 memberId를 파싱하여 반환한다.
        return Long.parseLong(authentication.getName());
    }
}
