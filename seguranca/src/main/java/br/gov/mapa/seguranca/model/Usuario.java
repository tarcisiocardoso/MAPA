package br.gov.mapa.seguranca.model;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public class Usuario{

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
		return "idUsuario: "+id_usuario+" Login:"+ds_login+", Nome: "+nm_pessoa_fisica;
	}
	
	/*public Long getId() {
		return id_usuario;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//new GrantedAuthorityImpl("ROLE_USER")
		return new ArrayList<SimpleGrantedAuthority>() {{ 
			add(new SimpleGrantedAuthority("ROLE_USER"));
		}}; 	
//		return null;
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return ds_senha;
	}
	@Override
	public String getUsername() {
		return id_usuario+":"+ds_login+":"+	nm_pessoa_fisica;
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
*/	
}
