package br.gov.mapa.segaut.service;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.mapa.segaut.model.CustemUserDetail;
import br.gov.mapa.segaut.model.CustemUserDetail.App;


@SuppressWarnings("unchecked")
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
	@SuppressWarnings("rawtypes")
    public CustemUserDetail buscaUserDetail( Authentication aut	) {
    	
    	Credenciais credenciais = getCredenciaisServer(aut);
    	if( credenciais == null ) return null;
    	
    	
    	OAuth2AuthorizedClient client = auth2AuthorizedClientRepository.loadAuthorizedClient("SEGAUT-client-authorization-code", aut, request );

    	if( client == null) return null;
    	
		HashMap map = this.webClient
        .get()
        .uri(resourceUri+"/me")
        .attributes(oauth2AuthorizedClient(client))
        .retrieve()
        .bodyToMono(HashMap.class)
		  .block();
		
//		System.out.println( map );
		
		List<LinkedHashMap<String, String>>lst = (List<LinkedHashMap<String, String>>)map.get("apps");
		
		ObjectMapper mapper =new ObjectMapper();
		List<App> apps = mapper.convertValue(lst, new TypeReference<List<App>>() { });
		
		List<String> roles = new ArrayList<String>();
		for( App app: apps) {
			roles.add(app.sigla );
		}
    	
		CustemUserDetail ud =  new CustemUserDetail(credenciais.nome, roles );
    	ud.apps = apps;
    	
		ud.id = map.get("idUsuario").toString();
//		ud.login = map.get("dsLogin").toString();
		ud.foto = map.get("foto").toString();
		
		
		
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
