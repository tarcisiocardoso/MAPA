package com.example.securingweb.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticlesController {

    @GetMapping("/articles")
    public String[] getArticles(Principal principal) {
		
    	System.out.println("Principal: "+principal.getName());
    	
        return new String[]{"Article 1", "Article 2", "Article 3"};
    }
}
