package com.example.securingweb;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
          .authorizeRequests(authorizeRequests -> authorizeRequests
            .antMatchers("/", "/error", "/webjars/**", "/segaut/**", "/styles_java/**", "/js/**", "/perform_logout").permitAll()
            .anyRequest().authenticated()
          )
          .exceptionHandling(e -> e
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
          )
          .csrf(c -> c
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
          )
          // .logout(l -> l
          //   .logoutSuccessUrl("/logoutTela").permitAll()
          // )
          .oauth2Login(oauth2Login ->
            oauth2Login.loginPage("/oauth2/authorization/articles-client-oidc"))
          .oauth2Client(withDefaults());
        return http.build();
    }
}
