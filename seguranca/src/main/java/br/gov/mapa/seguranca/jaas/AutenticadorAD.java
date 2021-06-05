package br.gov.mapa.seguranca.jaas;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;

import javax.naming.ldap.LdapName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import br.gov.mapa.seguranca.model.Usuario;

@Service
public class AutenticadorAD {
	@Autowired
	LdapTemplate ldapTemplate;
	
	/** {@inheritDoc} */
    public boolean autenticar(final String username, final String password) {
        return this.autenticar(username, password, null);
    }
    /** {@inheritDoc} */
    public boolean autenticar(final String username, final String password, final String documento) {
        return ldapTemplate.authenticate("", "(uid="+username+")", password);
    }

    
    /** {@inheritDoc} */
    public boolean isUsuarioAtivo(String username) throws Exception {
//        return getADContextBO().isUsuarioAtivo(username);
    	//TODO verificar como pegar o LDAP usuario ativo
    	return true;
    }
    
	public List<UserVO> findByUID(String uid) {
		return ldapTemplate.search(query().where("uid").is(uid), PERSON_CONTEXT_MAPPER);
	}
	
	private final static ContextMapper<UserVO> PERSON_CONTEXT_MAPPER = new AbstractContextMapper<UserVO>() {
		@Override
		public UserVO doMapFromContext(DirContextOperations context) {
			System.out.println(">>>>PERSON_CONTEXT_MAPPER<<<<<<");
			UserVO person = new UserVO();

			LdapName dn = LdapUtils.newLdapName(context.getDn());

//			person.setCountry(LdapUtils.getStringValue(dn, 0));
//			person.setCompany(LdapUtils.getStringValue(dn, 1));
//			person.nm_pessoa_fisica = (context.getStringAttribute("name"));
//			person.setNome(context.getStringAttribute("sn"));
//			String ds_login = (context.getStringAttribute("uid"));
//			person.ds_senha = (LdapUtils.getStringValue(dn, 1)); //(context.getStringAttribute("userPassword"));

			Object o = context.getObjectAttribute("userPassword");
			byte[] bytes = (byte[]) o;
			String hash = new String(bytes);
			person.setDbSenha(hash);
			
			System.out.println( person );
			return person;
		}
	};

}
