package br.gov.mapa.segaut.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustemUserDetail  implements UserDetails {
	public String id;
	public String login;
	public String nome;
	public List<String>roles;
	public String foto;
	public List<App> apps;
	
	
	
	public static class App{
		public String id;
		public String nome;
		public String name;
		public String versao;
		public String sigla;
		public String contexto;
		public String signedData;
	}
	
	public CustemUserDetail(String nome, List<String>roles) {
		this.nome = nome;
		this.roles = roles;
	}
	
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String getUsername() {
		return nome;
	}
	
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<SimpleGrantedAuthority> col = new ArrayList<SimpleGrantedAuthority>();
    	for(String s: roles) {
    		col.add( new SimpleGrantedAuthority("ROLE_"+s) );	
    	}
//    	col.add( new SimpleGrantedAuthority("ROLE_USER01") );
		return col;
	}
};