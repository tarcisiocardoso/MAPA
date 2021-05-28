package com.example.securingweb;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.example.securingweb.config.CustomAuthenticationProvider;
import com.example.securingweb.service.CustomUserDetailsService;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;

@EnableWebSecurity
public class DefaultSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
      System.out.println(">>>>defaultSecurityFilterChain<<<<");
        // http.authorizeRequests(authorizeRequests ->
        //   authorizeRequests.anyRequest().authenticated()
        // ).formLogin(withDefaults());

        // http.authorizeRequests(authorizeRequests ->
        //     authorizeRequests.anyRequest().authenticated()
        // ).formLogin(withDefaults()
        // .and()
        // .formLogin()
        // .loginPage("/login.html")
        // .loginProcessingUrl("/perform_login")
        // .defaultSuccessUrl("/homepage.html", true)
        // .failureUrl("/login.html?error=true")
        // .failureHandler(authenticationFailureHandler())
        // .and()
        // .logout()
        // .logoutUrl("/perform_logout")
        // .deleteCookies("JSESSIONID")
        // .logoutSuccessHandler(logoutSuccessHandler());

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
    
//    @Override
//    @Autowired
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(new CustomAuthenticationProvider());
//    }

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
