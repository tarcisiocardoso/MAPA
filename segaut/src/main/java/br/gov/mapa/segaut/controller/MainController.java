package br.gov.mapa.segaut.controller;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
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
	

	@GetMapping("/")
	public String home(Model model,
			@RegisteredOAuth2AuthorizedClient("SEGAUT-client-authorization-code") OAuth2AuthorizedClient authorizedClient) {
		
		System.out.println(">>>>resourceUri+\"/me\": "+ resourceUri+"/me");
		Object o = this.webClient
		          .get()
		          .uri(resourceUri+"/me")
		          .attributes(oauth2AuthorizedClient(authorizedClient))
		          .retrieve()
		          .bodyToMono(HashMap.class)
				  .block();
		
		model.addAttribute("user", o);
		model.addAttribute("data", getData());
		
		
		return "index.html";
	}
	@GetMapping("/authorized")
	public void authorized(Model model, @RegisteredOAuth2AuthorizedClient("SEGAUT-client-authorization-code") OAuth2AuthorizedClient authorizedClient) {
		
		System.out.println(">>>>authorized<<<<");

	}
    @GetMapping("/userInfo")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model,
			@RegisteredOAuth2AuthorizedClient("SEGAUT-client-authorization-code") OAuth2AuthorizedClient authorizedClient,
			Authentication authentication
			) {
		model.addAttribute("name", name);
		
		Object o = this.webClient
		          .get()
		          .uri(resourceUri+"/me")
		          .attributes(oauth2AuthorizedClient(authorizedClient))
		          .retrieve()
		          .bodyToMono(HashMap.class)
				  .block();
		System.out.println("----->"+ o );
		model.addAttribute("user", o);
		
		return "greeting";
	}

	@GetMapping("/logout")
	public String logout(Model model) {
		return "sair";
	}

	@GetMapping("/lista")
	public String lista(Model model,
			@RegisteredOAuth2AuthorizedClient("SEGAUT-client-authorization-code") OAuth2AuthorizedClient authorizedClient) {
		Object o = this.webClient
		          .get()
		          .uri(resourceUri+"/me")
		          .attributes(oauth2AuthorizedClient(authorizedClient))
		          .retrieve()
		          .bodyToMono(HashMap.class)
				  .block();
		
		model.addAttribute("user", o);
		model.addAttribute("data", getData());
		
		return "index.html";
	}

	@GetMapping("/alterarSenha")
	public String alterarSenha(Model model) {
		return "alterarSenha";
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}
	private String getData() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		return sdf.format(new Date() );
	}
}