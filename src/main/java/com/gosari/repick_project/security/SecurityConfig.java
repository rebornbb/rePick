package com.gosari.repick_project.security;

import com.gosari.repick_project.user.UserSecurityService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@RequiredArgsConstructor
@Configuration //스프링환경설정파일의미-스프링시큐리티의설정
@EnableWebSecurity //모든요청URL이 스프링시큐리티의 제어받음
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 애너테이션을 사용하기 위해
public class SecurityConfig {

    private final UserSecurityService userSecurityService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /*스프링 시큐리티의 세부 설정은 SecurityFilterChain 빈을 생성하여 설정 가능*/

        /* 모든 인증되지 않은 요청을 허락한다는 의미 - 로그인하지않더라도 모든페이지에 접근가능*/
        http.authorizeRequests()
                .antMatchers("/**").permitAll()

                /*스프링 시큐리티의 로그인 설정을 담당하는 부분*/
                .and()
                .formLogin()
                .loginPage("/user/login")
                .defaultSuccessUrl("/")

                /*로그아웃을 위한 설정 추가*/
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)

        ;
        return http.build();
    }

    //PasswordEncoder 빈(bean)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*AuthenticationManager 스프링 시큐리티의 인증을 담당*/
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    }