package com.example.securingweb.model;

//id SERIAL, clientId VARCHAR(255), clientSecret VARCHAR(12), redirectUri1 VARCHAR(255), redirectUri2 VARCHAR(255), scope VARCHAR(255), nome VARCHAR(255))");
public class AppClient {

	public long id;
	public String clientId;
	public String clientSecret;
	public String redirectUri1;
	public String redirectUri2;
	public String scope;
	public String nome;
	
	
	public String toString() { return nome+": "+clientId;}
}
