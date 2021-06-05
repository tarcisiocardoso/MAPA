package br.gov.mapa.seguranca.jaas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.mapa.seguranca.util.Constantes;

@Service
public class AutenticadorBD {
	private static final Logger LOG = LoggerFactory.getLogger(AutenticadorBD.class);
	
	@Autowired
	MediatorUser mediatorUser;
	
	@Autowired
	MediatorPassword mediatorPassword;
	
	
    public boolean autenticar(final String username, final String password) {
        return this.autenticar(username, password, null);
    }

    /** {@inheritDoc} */
    
    public boolean autenticar(final String username, final String password, final String documento) {
        try {
            UserVO user = mediatorUser.getUser(username);
            String encriptedPassword = mediatorPassword.encriptarSenha(password);
            if (user == null) {
                LOG.warn("Usuario nao autenticado pois nao existe: " + username);
                return false;
            }
            return encriptedPassword.equals(user.getDbSenha()) && Constantes.ST_ATIVO.equals(user.getStAtivo());
        } catch (Exception e) {
            LOG.warn("Falha na autenticação com o banco de dados", e);
        }
        return false;
    }

    /** {@inheritDoc} */
    public boolean isUsuarioAtivo(String username) throws Exception {
        UserVO user = mediatorUser.getUser(username);
        return user != null && "S".equals(user.getStAtivo());
    }
}
