package com.example.securingweb.controller;

import java.security.Principal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securingweb.model.UserVO;
import com.example.securingweb.repository.UserRepository;

@RestController
public class UserController {

	@Autowired
	UserRepository repository;
	
	@GetMapping("/me")
    public UserVO getArticles(Principal principal) throws Exception {
		if( principal == null) {
			throw new RuntimeException("Usuario não encontrado");
		}
		System.out.println("Principal no me: "+principal.getName());
		String split[] =  principal.getName().split(":");
		String id = split[0];
		
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)
		  SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		  boolean hasRole = false;
		  for (GrantedAuthority authority : authorities) {
		     System.out.println("authority: "+ authority);
		  }
//		for( Object o: principal.)
//		UserVO vo = repository.getUser(id);
		  UserVO vo = new UserVO();
		  vo.nome = "AAAAAAAAAAAAAAAAA";
		return vo;

    }
}
