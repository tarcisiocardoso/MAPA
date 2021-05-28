package com.example.securingweb.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.securingweb.model.Usuario;

public class CustomUserDetailsService implements UserDetailsService{

	public final String QUERY = "SELECT id_usuario, ds_login, ds_senha, nm_pessoa_fisica FROM usuario WHERE ds_login = ?";
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("deprecation")
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		System.out.println(">>>>>>bla lalalalalalal<<<<<<<<<<<<<<<");

		Usuario usuario = jdbcTemplate.queryForObject(QUERY, new Object[] { login }, new RowMapper<Usuario>() {
			@Override
			public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Usuario(rs.getLong("id_usuario"), rs.getString("ds_login"), rs.getString("ds_senha"), rs.getString("nm_pessoa_fisica") );
			}
		});

		System.out.println(usuario);
//		UserDetails user = User.withDefaultPasswordEncoder()
//		          .username("adm")
//		          .password("123")
//		          .roles("USER")
//		          .build();
		
		
		UserDetails user = User.withDefaultPasswordEncoder()
	        .username(usuario.nm_pessoa_fisica)
	        .password(usuario.ds_senha)
	        .roles("USER")
	        .build();				
		return user;
	}
}
