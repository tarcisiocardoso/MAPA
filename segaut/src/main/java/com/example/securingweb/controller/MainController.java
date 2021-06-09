package com.example.securingweb.controller;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.Fuseable.SynchronousSubscription;

@Controller
public class MainController {
    
	@Value( "${spring.resource-uri}" )
	private String resourceUri;

	@Autowired
    private WebClient webClient;
	
	// @GetMapping(value = "/greeting")
    // public Object getArticles(
    //   @RegisteredOAuth2AuthorizedClient("articles-client-authorization-code") OAuth2AuthorizedClient authorizedClient
    // ) {
    //     return this.webClient
    //       .get()
    //       .uri(resourceUri+"/me")
    //       .attributes(oauth2AuthorizedClient(authorizedClient))
    //       .retrieve()
    //       .bodyToMono(HashMap.class)
    //       .block();
    // }


    @GetMapping("/greeting")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model,
			@RegisteredOAuth2AuthorizedClient("articles-client-authorization-code") OAuth2AuthorizedClient authorizedClient,
			Authentication authentication
			) {
		model.addAttribute("name", name);
		
		System.out.println(">>>>-"+resourceUri);
		System.out.println("----->"+ authorizedClient.getPrincipalName() );
		
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)
		  SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		  boolean hasRole = false;
		  for (GrantedAuthority authority : authorities) {
		     System.out.println("authority: "+ authority);
		  }

		Object o = this.webClient
		          .get()
		          .uri(resourceUri+"/me")
		          .attributes(oauth2AuthorizedClient(authorizedClient))
		          .retrieve()
		          .bodyToMono(HashMap.class)
				  .block();
				  
		model.addAttribute("user", o);
		// Collection<?extends Object> granted = authentication.getCredentials();
	    // Collection<?extends GrantedAuthority> granted = authentication.getAuthorities();

	    // for( Object ga: granted) {
	    // 	System.out.println("Authority: "+ga.toString() );
	    // }
		
		// model.addAttribute("lst", granted );
		
//		System.out.println("======>"+o.getClass().getName());
		
		return "greeting";
	}

	@GetMapping("/perform_logout")
	public String logout(Model model) {
		return "sair";
	}

	@GetMapping("/lista")
	public String lista(Model model) {
		return "lista";
	}

	@GetMapping("/alterarSenha")
	public String alterarSenha(Model model) {
		return "alterarSenha";
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}
}