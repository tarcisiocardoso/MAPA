package com.example.securingweb.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
		System.out.println(">>>>>>bla lalalalalalal<<<<<<<<<<<<<<<");
		UserDetails user = User.withDefaultPasswordEncoder()
		          .username("adm")
		          .password("123")
		          .roles("USER")
		          .build();
		return user;
	}
}
