package br.gov.mapa.seguranca.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import br.gov.mapa.seguranca.jaas.JAASRole;
import br.gov.mapa.seguranca.model.AppClient;
import br.gov.mapa.seguranca.repository.AppClientRepository;

public class CustomClientRegistrationRepository implements RegisteredClientRepository{

	@Autowired
	AppClientRepository repository;
	
	@Override
	public RegisteredClient findByClientId(String clientId) {
		System.out.println("--CustomClientRegistrationRepository findByClientId--->"+ clientId);
		
		
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//        System.out.println("findByClientId -->"+ authentication );
//        if( authentication != null ) {
//        	System.out.println("=========================================");
////        	Collection<? extends GrantedAuthority> lst = new ArrayList<SimpleGrantedAuthority>();
////        	lst.add(new SimpleGrantedAuthority("AAAAAA") );
////          	aut.add( new SimpleGrantedAuthority(jaasRole.toString()) );
//          
//        	Collection<SimpleGrantedAuthority> oldAuthorities = (Collection<SimpleGrantedAuthority>)authentication.getAuthorities();
//        	SimpleGrantedAuthority authority = new SimpleGrantedAuthority("NOVA_AUTORITY");
//        	List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
//        	updatedAuthorities.add(authority);
//        	updatedAuthorities.addAll(oldAuthorities);
//        	SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
        
        
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("articles-client")
                .clientSecret("secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://localhost:8080/login/oauth2/code/articles-client-oidc")
                .redirectUri("http://localhost:8080/authorized")
                .scope(OidcScopes.OPENID)
                .scope("articles.read")
                .build();
         
        return registeredClient;
              
//		Optional<AppClient> opt = repository.findByClientId(clientId);
//		
//		System.out.println( opt.isPresent() );
//		if( !opt.isPresent() ) {
//			//throw new RuntimeException("Cliente "+ clientId+" não registrado");
//			return null;
//		}
//		
//		RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
//		          .clientId("articles-client")
//		          .clientSecret("secret")
//		          .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
//		          .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//		          .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//		          .redirectUri("http://localhost:8080/login/oauth2/code/articles-client-oidc")
//		          .redirectUri("http://localhost:8080/authorized")
//		          .scope(OidcScopes.OPENID)
//		          .scope("articles.read")
//		          .build();
//		
//		return registeredClient;
	}

	@Override
	public RegisteredClient findById(String id) {
		System.out.println("--CustomClientRegistrationRepository findById--->"+ id);
		RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
		          .clientId("articles-client")
		          .clientSecret("secret")
		          .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
		          .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
		          .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
		          .redirectUri("http://localhost:8080/login/oauth2/code/articles-client-oidc")
		          .redirectUri("http://localhost:8080/authorized")
		          .scope(OidcScopes.OPENID)
		          .scope("articles.read")
		          .build();
		
		return registeredClient;
	}

}
