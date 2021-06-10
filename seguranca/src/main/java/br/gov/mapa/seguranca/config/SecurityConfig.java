package br.gov.mapa.seguranca.config;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import br.gov.mapa.seguranca.jaas.MediatorPassword;
import br.gov.mapa.seguranca.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//	@Autowired
//    private CustomUserDetailsService userDetailsService;
	
	@Autowired
	MediatorPassword mediatorPassword;
	
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
      
//      UserDetails user =
// 			 User.withDefaultPasswordEncoder()
// 				.username("user")
// 				.password("password")
// 				.roles("USER", "USER02")
// 				.build();
//
// 		return new InMemoryUserDetailsManager(user);
    }
    
    
    @Bean
  	public PasswordEncoder passwordEncoder(){
    	System.out.println(">>>>>passwordEncoder<<<<");
    	PasswordEncoder encoder = new PasswordEncoder() {			
			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				try {
					String pass = mediatorPassword.encriptarSenha(rawPassword.toString());
					return pass.equals(encodedPassword);
				} catch (LoginException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
			
			@Override
			public String encode(CharSequence rawPassword) {				
				return rawPassword.toString();
			}
		};
  		return encoder;
  	}
}


