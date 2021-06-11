package com.example.securingweb.service;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.securingweb.model.CustemUserDetail;

@Service
public class SecurityService {

	@Value( "${spring.resource-uri}" )
	private String resourceUri;
	
	@Autowired
    private WebClient webClient;
	
	@Autowired
	private OAuth2AuthorizedClientRepository auth2AuthorizedClientRepository;
	

	@Autowired
	HttpServletRequest request;
	
    public CustemUserDetail buscaUserDetail( Authentication aut	) {
    	
    	Credenciais credenciais = getCredenciaisServer(aut);
    	if( credenciais == null ) return null;
    	
    	CustemUserDetail ud =  new CustemUserDetail(credenciais.nome, new ArrayList<String>() {{add("aaaa"); add("bbbb"); add("ccccc");}} );
    	
    	OAuth2AuthorizedClient client = auth2AuthorizedClientRepository.loadAuthorizedClient("SEGAUT-client-authorization-code", aut, request );


    	if( client == null) return null;
    	
    	System.out.println("AAAAAAAAAAAAAAAAAAAAAAA");
    	
    	HashMap map = this.webClient
        .get()
        .uri(resourceUri+"/me")
        .attributes(oauth2AuthorizedClient(client))
        .retrieve()
        .bodyToMono(HashMap.class)
		  .block();
		
		System.out.println( map );
		
    	return ud;
    }
    
    private Credenciais getCredenciaisServer(Authentication aut) {
    	Credenciais c = null;
		if( aut != null ) {
			String nome = aut.getName();
			String [] arr = nome.split(":");
			if( arr.length > 1) {
				c = new Credenciais();
				c.id = arr[0];
				c.login = arr[1];
				c.nome = arr[2];
				if( aut instanceof OAuth2AuthenticationToken) {
					OAuth2AuthenticationToken oaut = (OAuth2AuthenticationToken)aut;
					c.clientId = oaut.getAuthorizedClientRegistrationId();
//					authorizedClientRegistrationId
				}
			}
		}
		return c;
	}

	public class Credenciais{
		public String id;
		public String nome;
		public String login;
		public String clientId;
	}
}
