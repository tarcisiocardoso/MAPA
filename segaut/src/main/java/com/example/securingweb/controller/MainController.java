package com.example.securingweb.controller;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.Fuseable.SynchronousSubscription;

@Controller
public class MainController {
    
	@Value( "${spring.security.oauth2.client.provider.spring.issuer-uri}" )
	private String providerUrl;
	
	@Autowired
    private WebClient webClient;
	
    @GetMapping("/greeting")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model,
			@RegisteredOAuth2AuthorizedClient("articles-client-authorization-code") OAuth2AuthorizedClient authorizedClient
			) {
		model.addAttribute("name", name);
		
		System.out.println(">>>>-"+providerUrl);
		System.out.println("----->"+ authorizedClient.getPrincipalName() );
		
		Object o = this.webClient
		          .get()
		          .uri(providerUrl+"/api/me")
		          .attributes(oauth2AuthorizedClient(authorizedClient))
		          .retrieve()
		          .bodyToMono(String[].class)
		          .block();
		
		System.out.println("======>"+o);
		
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