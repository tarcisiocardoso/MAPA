package br.gov.mapa.seguranca.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import javax.naming.ldap.LdapName;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import br.gov.mapa.seguranca.jaas.JAASLoginModule;
import br.gov.mapa.seguranca.jaas.UserVO;
import br.gov.mapa.seguranca.model.Usuario;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

//@Service
public class CustomUserDetailsService implements UserDetailsService{

	public final String QUERY = "SELECT id_usuario, ds_login, ds_senha, nm_pessoa_fisica FROM usuario WHERE ds_login = ?";
	
	@Autowired
	JdbcTemplate jdbcTemplate;
  
	@Autowired
	LdapTemplate ldapTemplate;
	
	@Autowired
	JAASLoginModule jAASLoginModule;
	
	@Autowired
    private HttpServletRequest request;
	
	
	@SuppressWarnings("deprecation")
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		System.out.println(">>>>>>bla lalalalalalal<<<<<<<<<<<<<<<");

//	    if(StringUtils.isNotEmpty(base64AuthorizationHeader)){
//	        String decodedAuthorizationHeader = new String(Base64.getDecoder().decode(base64AuthorizationHeader), Charset.forName("UTF-8"));
//	        return decodedAuthorizationHeader.split(":")[0];
//	    }
	    
//		Enumeration<String> headerNames = request.getHeaderNames();
//	    while(headerNames.hasMoreElements()) {
//	        String headerName = headerNames.nextElement();
//	        System.out.println("Header Name - " + headerName + ", Value - " + request.getHeader(headerName));
//	   }
//	    System.out.println("\n\n");
//	    
//	    Enumeration<String> parm = request.getAttributeNames();
//		while(parm.hasMoreElements()) {
//	        String headerName = parm.nextElement();
//	        System.out.println( headerName );
//	   }
//		
//		System.out.println("\n\n");
//	    
//	    parm = request.getParameterNames();
//		while(parm.hasMoreElements()) {
//	        String headerName = parm.nextElement();
//	        System.out.println( headerName );
//	   }
	    
//	    
//	    if (authorization != null && authorization.startsWith("Basic")) {
//	        // Authorization: Basic base64credentials
//	        String base64Credentials = authorization.substring("Basic".length()).trim();
//	        String credentials = new String(Base64.getDecoder().decode(base64Credentials),
//	                Charset.forName("UTF-8"));
//	        // client/secret = clientId:secret
//	        final String[] values = credentials.split(":",2);
	        
		/*
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
		if( usuario == null ) {
			List<Usuario> lst = findByUID(login);
			usuario = lst.size()> 0? lst.get(0):null;
			System.out.println(usuario);
		}
//		UserDetails user = User.withDefaultPasswordEncoder()
//		          .username("adm")
//		          .password("123")
//		          .roles("USER")
//		          .build();
		
		
		if( usuario == null) throw new UsernameNotFoundException("Usuário não encontrado"); //throw new RuntimeException("Usuário não encontrado ");
		
		UserDetails user = User.withDefaultPasswordEncoder()
	        .username( usuario.id_usuario+":"+usuario.ds_login+":"+	usuario.nm_pessoa_fisica )
	        .password(usuario.ds_senha)
	        .roles("USER")
	        .roles("OUTRA")
	        .build();				
//		return user;
//		usuario.ds_senha = user.getPassword();
//		System.out.println("--->"+usuario);
		return user;
		*/
		UserVO user = null;
		try {
			user = jAASLoginModule.login(login);
			if( user.getDbSenha() != null && !user.getDbSenha().isEmpty() ) {
//				try {
//					jAASLoginModule.carregarDados();
//				} catch (Exception e) {
//					throw new LoginException(e.getMessage() );
//				}
			}else {
				String pass= jAASLoginModule.getUserRedePass(user.getDsLogin());
				user.setDbSenha(pass);
//				jAASLoginModule.logingRedeOK();
			}
//			jAASLoginModule.carregarDados(user);
		} catch (LoginException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		
//		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), usuario.getAuthorities() );

		String id = user.getIdUsuario()+"";
//		String login = user.getDsLogin();
		String nome = user.getNome();
		System.out.println(nome);
//		UserDetails u = user.getUserDetails();
//		System.out.println("u.getPassword() : "+u.getPassword() );
//		UserDetails u = User.withDefaultPasswordEncoder()
//		        .username( id+":"+login+":"+nome )
//		        .password(user.getDbSenha())
//		        .roles("USER")
//		        .roles("OUTRA")
//		        .build();
		
//		UserDetails u = User.withUserDetails(user.getUserDetails()).build();

		
		List<GrantedAuthority> listGrantAuthority = new ArrayList<GrantedAuthority>();
		
//		for(Role roleUser : u.getRoles()){
//			final String PREFIX = "ROLE_";
//			String role = PREFIX + roleUser.getRoleDescription();
			listGrantAuthority.add(new SimpleGrantedAuthority("ROLE_001"));
			listGrantAuthority.add(new SimpleGrantedAuthority("ROLE_002"));
			listGrantAuthority.add(new SimpleGrantedAuthority("ROLE_003"));	
//		}
	
		User u = new User(id+":"+login+":"+nome, user.getDbSenha(), listGrantAuthority);
	
//		Collection<SimpleGrantedAuthority> aut = new ArrayList<SimpleGrantedAuthority>();
//		aut.add( new SimpleGrantedAuthority("TESTE001") );
//		aut.add( new SimpleGrantedAuthority("TESTE002") );
//		u.
//		u.getAuthorities().add( new SimpleGrantedAuthority("TESTE002") );
//		return u;
		
		
//		return new SecurityUser(id+":"+login+":"+nome, user.getDbSenha(), "TESTE001");
		
		String s [] = new String[] {"ROLE_001", "ROLE_002"};
		for (String role : s) {
			listGrantAuthority.add(new SimpleGrantedAuthority(role ));
		}
		return new org.springframework.security.core.userdetails.User(
				id+":"+login+":"+nome,
				user.getDbSenha(),
		        listGrantAuthority
		);
		
		
		
		
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
			return person;
		}
	};

	public class SecurityUser implements UserDetails{
	    String ROLE_PREFIX = "ROLE_";

	    String userName;
	    String password;
	    String role;

	    public SecurityUser(String username, String password, String role){
	        this.userName = username;
	        this.password = password;
	        this.role = role;
	    }

	    @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

	        list.add(new SimpleGrantedAuthority(ROLE_PREFIX + role));

	        return list;
	    }

		@Override
		public String getPassword() {
			// TODO Auto-generated method stub
			return password;
		}

		@Override
		public String getUsername() {
			// TODO Auto-generated method stub
			return userName;
		}

		@Override
		public boolean isAccountNonExpired() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isAccountNonLocked() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEnabled() {
			// TODO Auto-generated method stub
			return false;
		}
	}
}
