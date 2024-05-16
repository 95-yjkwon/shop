package com.keduit.shop.config;


import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //security가 post를 처리한다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
        System.out.println("============================SecurityFilterChain============================");

        http.formLogin()
                //로그인 처리 화면
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email") //loaduserbyusername(string email)로 실행
                .failureUrl("/members/login/error")
                .and()
                .logout()
                //로그아웃 처리 url
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/");

        //permitAll(): 모든 사용자가 인증(로그인)없이 해당 경로에 접근 가능
        //has("ADMIN"):관리자인 경우
        //위의 경우 이외의 페이지는 인증절차가 필요하다.
        http.authorizeRequests()
                .mvcMatchers("/","/members/**",
                        "/item/**", "/images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated();



        //인증되지 않은 사용자가 리소스 접근하여 실패했을 때 처리하는 핸들러 등록!!!!!와우 대단하다~!
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    //리소스의 스태틱 안에 있는 요소들은 인증없이 접근 가능하다.
    //리소스 /스태틱 폴더의 하위 파일은 인증을 제외한다.
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web)->web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
