package dev.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
   @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       http
               // CSRF 비활성화 (API 서버의 경우)
               .csrf(csrf -> csrf.disable())

               // 세션을 상태 없음으로 설정 (Stateless)
               .sessionManagement(session -> session
                       .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               )

               // 요청에 대한 권한 부여 설정
               .authorizeHttpRequests(auth -> auth
                       .requestMatchers("/api/users/**").permitAll() // 인증 관련 API는 모두 허용
                       .anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요
               )

               // 폼 로그인 비활성화
               .formLogin(form -> form.disable())

               // HTTP 기본 인증 비활성화
               .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();

   }

    // AuthenticationManager 빈 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
