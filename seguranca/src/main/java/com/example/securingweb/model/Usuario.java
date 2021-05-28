package com.example.securingweb.model;

public class Usuario {

	public Long id_usuario;
	public String ds_login;
	public String ds_senha;
	public String nm_pessoa_fisica;
	
	public Usuario() {}
	public Usuario(Long id, String ds, String pass, String nm) {
		id_usuario = id;
		ds_login = ds;
		ds_senha = pass;
		nm_pessoa_fisica= nm;
	}
	
	public String toString() {
		return "Login:"+ds_login+", Nome: "+nm_pessoa_fisica;
	}
}
