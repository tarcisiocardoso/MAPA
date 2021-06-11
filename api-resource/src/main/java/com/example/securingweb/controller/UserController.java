package com.example.securingweb.controller;

import java.security.Principal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securingweb.UserService;
import com.example.securingweb.model.UserVO;
import com.example.securingweb.repository.UserRepository;

@RestController
public class UserController {

	@Autowired
	UserService service;
	
	@GetMapping("/me")
    public UserVO getArticles(Principal principal) throws Exception {
		if( principal == null) {
			throw new RuntimeException("Usuario não encontrado");
		}
		
		
//		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)
//		  SecurityContextHolder.getContext().getAuthentication().getAuthorities();
//		  boolean hasRole = false;
//		  for (GrantedAuthority authority : authorities) {
//		     System.out.println("authority: "+ authority);
//		  }
//		for( Object o: principal.)
		UserVO vo = service.getUser(principal);
//		  UserVO vo = new UserVO();
//		  vo.nome = "AAAAAAAAAAAAAAAAA";
		return vo;

    }
}
