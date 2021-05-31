package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;

@Configuration
public class AuthConfig extends WebSecurityConfigurerAdapter{
    // @Autowired
    // private UserDetailsService userDetailsService;

    // @Override
    // protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    // }

    @Override
  public final void configure(final WebSecurity web) throws Exception {
      System.out.println(">>>>configure<<<<");
    super.configure(web);
    web.httpFirewall(new DefaultHttpFirewall());
  }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super.configure(http);
        System.out.println(">>>>configureII<<<<");
        http.csrf().disable().authorizeRequests()
        
        .antMatchers("/employee/me").authenticated()
        .antMatchers("/**").permitAll();;

    }
}