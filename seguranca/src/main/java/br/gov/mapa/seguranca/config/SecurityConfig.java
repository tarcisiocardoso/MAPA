package br.gov.mapa.seguranca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import br.gov.mapa.seguranca.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
      System.out.println(">>>>defaultSecurityFilterChain<<<<");
        http
//        .oauth2Login()
//        	.userInfoEndpoint()
//        		.oidcUserService(oidcUserService)
        
//        .mvcMatcher("/me/**")
//	        .authorizeRequests()
//	        .mvcMatchers("/articles/**")
//	        .access("hasAuthority('SCOPE_articles.read')")
//	        .and()
//	        .oauth2ResourceServer()
//	        .jwt()
//	        .and()
        
        .authorizeRequests()
          .antMatchers("/", "/api/**", "/home", "/erro**", "/styles_java/**", "/segaut/**").permitAll()
          .anyRequest().authenticated()
          .and()
        .formLogin()
          .loginPage("/login")
          .permitAll()
          .and()
        .logout()
          .permitAll()
          .logoutUrl("/perform_logout")
          .invalidateHttpSession(true)
          .deleteCookies("JSESSIONID");
    
        return http.build();
    }
    
    @Bean
    UserDetailsService users() {
      System.out.println(">>>>SecurityConfig 222<<<<");
        return new CustomUserDetailsService();
    }
}
