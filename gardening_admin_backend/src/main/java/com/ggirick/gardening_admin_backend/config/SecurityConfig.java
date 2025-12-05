package com.ggirick.gardening_admin_backend.config;

import com.ggirick.gardening_admin_backend.fillters.JWTFilter;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${cors.allowed-origins}")
    private String allowedOrigins; // 쉼표로 구분된 문자열

    private final JWTFilter jwtFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http
                .formLogin(form->form.disable()) //Spring security가 제공하는 Form Login 기능 off시키기
                .httpBasic(basic->basic.disable()) //Authrization header 기반의 로그인 처리 방식 차단하여 자원 낭비 방지
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //JWT 토큰 기반 인증 서버에서 불필요한 세션 생성 및 검증 로딕 차단하여 빼버리기
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(requests ->
                        requests
                                .requestMatchers("/auth/login").permitAll()
                                .requestMatchers("/auth/logout").permitAll()
                                .requestMatchers("/auth/requestOtp").permitAll()
                                .requestMatchers("/auth/checkOtp").permitAll()
                                .requestMatchers("/auth/existIdCheck").permitAll()
                                .requestMatchers("/auth/existPhoneCheck").permitAll()
                                .requestMatchers("/auth/refresh").permitAll()

                                // 나머지는 관리자 인증이 필요한 경로
                                .anyRequest().hasRole("ADMIN") // 요청을 허용할 url
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                ;

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        List<String> origins = Arrays.asList(allowedOrigins.split(","));

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(origins);
        configuration.setAllowedMethods(List.of("*")); // 요청을 허용할 method(get/post/put/delete 등)
        configuration.setAllowedHeaders(List.of("*")); // 요청을 허용할 header
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt방식으로 암호화.
    }

}