package com.example.securingweb.config;

import java.util.Collections;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.web.SecurityFilterChain;

import com.example.securingweb.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
      System.out.println(">>>>defaultSecurityFilterChain<<<<");
        http
        .authorizeRequests()
          .antMatchers("/", "/home", "/erro**", "/styles_java/**", "/segaut/**").permitAll()
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
//	Object configure(AuthenticationManagerBuilder auth) throws Exception {
//		System.out.println(">>>>>WebSecurityConfig<<<<");
//		auth
//			.ldapAuthentication()
//				.userDnPatterns("uid={0},ou=people")
//				.groupSearchBase("ou=groups")
//				.contextSource()
//					.url("ldap://localhost:8389/dc=springframework,dc=org")
//					.and()
//				.passwordCompare()
//					.passwordEncoder(new BCryptPasswordEncoder())
//					.passwordAttribute("userPassword");
//	}
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
      System.out.println(">>>>SecurityConfig 222<<<<");
        return new CustomUserDetailsService();
    }
}
