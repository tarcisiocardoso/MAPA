package com.example.securingweb.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import com.example.securingweb.model.AppClient;
import com.example.securingweb.repository.AppClientRepository;

public class CustomClientRegistrationRepository implements RegisteredClientRepository{

	@Autowired
	AppClientRepository repository;
	
	@Override
	public RegisteredClient findByClientId(String clientId) {
		System.out.println("--CustomClientRegistrationRepository findByClientId--->"+ clientId);
		
		Optional<AppClient> opt = repository.findByClientId(clientId);
		
		System.out.println( opt.isPresent() );
		if( !opt.isPresent() ) {
			//throw new RuntimeException("Cliente "+ clientId+" não registrado");
			return null;
		}
		
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
