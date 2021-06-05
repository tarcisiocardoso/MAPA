package br.gov.mapa.seguranca.controller;

import java.security.Principal;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;  
//import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;  
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;  


@RestController
public class UserController {

	
	@GetMapping("/api/me")
    public String[] getArticles(Principal principal) {
		System.out.println("Principal: "+principal);
		
        return new String[]{"Article 1", "Article 2", "Article 3"};
    }
	
    @RequestMapping("/api/oauthinfo")  
    @ResponseBody  
    public String oauthUserInfo(@AuthenticationPrincipal OAuth2User oauth2User) {
    	System.out.println("--->oauth2User -->"+ oauth2User);
        return  
            "User Name: " + oauth2User.getName() + "<br/>" +  
            "User Authorities: " + oauth2User.getAuthorities() + "<br/>";  
              
              
    }  
}
