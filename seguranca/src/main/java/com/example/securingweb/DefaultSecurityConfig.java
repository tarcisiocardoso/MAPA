package com.example.securingweb;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import com.example.securingweb.service.CustomUserDetailsService;

@EnableWebSecurity
public class DefaultSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
      System.out.println(">>>>defaultSecurityFilterChain<<<<");
        http
        .authorizeRequests()
          .antMatchers("/", "/home", "/styles_java/**", "/segaut/**").permitAll()
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
    

//    @Bean
//    UserDetailsService users() {
//      System.out.println(">>>>users<<<<");
//        UserDetails user = User.withDefaultPasswordEncoder()
//          .username("adm")
//          .password("123")
//          .roles("USER")
//          .build();
//        return new InMemoryUserDetailsManager(user);
//    }

//    @Bean
//    UserDetailsService users() {
//      System.out.println(">>>>users<<<<");
//        UserDetails user = User.withDefaultPasswordEncoder()
//          .username("adm")
//          .password("123")
//          .roles("USER")
//          .build();
//        return new UserDetailsService() {
//			
//			@Override
//			public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
//				System.out.println(">>>>>>bla<<<<<<<<<<<<<<<");
//				return user;
//			}
//		};
//    }
    
    @Bean
    UserDetailsService users() {
      System.out.println(">>>>users 222<<<<");
        return new CustomUserDetailsService();
    }
}
