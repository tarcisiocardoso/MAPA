package com.example.securingweb.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.ldap.LdapName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.securingweb.model.Usuario;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

public class CustomUserDetailsService implements UserDetailsService{

	public final String QUERY = "SELECT id_usuario, ds_login, ds_senha, nm_pessoa_fisica FROM usuario WHERE ds_login = ?";
	
	@Autowired
	JdbcTemplate jdbcTemplate;
  
	@Autowired
	LdapTemplate ldapTemplate;
  
	@SuppressWarnings("deprecation")
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		System.out.println(">>>>>>bla lalalalalalal<<<<<<<<<<<<<<<");
		Usuario usuario = null;
		try {
			usuario = jdbcTemplate.queryForObject(QUERY, new Object[] { login }, new RowMapper<Usuario>() {
				@Override
				public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
					return new Usuario(rs.getLong("id_usuario"), rs.getString("ds_login"), rs.getString("ds_senha"), rs.getString("nm_pessoa_fisica") );
				}
			});

		}catch(DataAccessException e) {
			System.err.println(login+" não encontrado no banco: "+e.getMessage());
		}
		
		List<Usuario> lst = findByUID(login);
		usuario = lst.size()> 0? lst.get(0):null;
		System.out.println(usuario);
//		UserDetails user = User.withDefaultPasswordEncoder()
//		          .username("adm")
//		          .password("123")
//		          .roles("USER")
//		          .build();
		
		if( usuario == null) throw new RuntimeException("Usuário não encontrado ");
		
		UserDetails user = User.withDefaultPasswordEncoder()
	        .username(usuario.nm_pessoa_fisica)
	        .password(usuario.ds_senha)
	        .roles("USER")
	        .build();				
		return user;
	}
	
	private List<Usuario> findByUID(String uid) {
		return ldapTemplate.search(query().where("uid").is(uid), PERSON_CONTEXT_MAPPER);
	}
	
	private final static ContextMapper<Usuario> PERSON_CONTEXT_MAPPER = new AbstractContextMapper<Usuario>() {
		@Override
		public Usuario doMapFromContext(DirContextOperations context) {
			System.out.println(">>>>PERSON_CONTEXT_MAPPER<<<<<<");
			Usuario person = new Usuario();

			LdapName dn = LdapUtils.newLdapName(context.getDn());

//			person.setCountry(LdapUtils.getStringValue(dn, 0));
//			person.setCompany(LdapUtils.getStringValue(dn, 1));
//			person.nm_pessoa_fisica = (context.getStringAttribute("name"));
			person.nm_pessoa_fisica = (context.getStringAttribute("sn"));
			person.ds_login = (context.getStringAttribute("uid"));
//			person.ds_senha = (LdapUtils.getStringValue(dn, 1)); //(context.getStringAttribute("userPassword"));

			Object o = context.getObjectAttribute("userPassword");
			byte[] bytes = (byte[]) o;
			String hash = new String(bytes);
			person.ds_senha = hash;
			
			System.out.println( person );
//			person.setDescription(context.getStringAttribute("description"));
//			person.setPhone(context.getStringAttribute("telephoneNumber"));

			return person;
		}
	};
}
