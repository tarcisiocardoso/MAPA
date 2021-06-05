package br.gov.mapa.seguranca.jaas;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.gov.mapa.seguranca.util.Constantes;

@Repository
public class MediatorPassword {
	private static final Logger LOGGER = LoggerFactory.getLogger(MediatorPassword.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	/**
     * Encripta a senha informada
     *
     * @param senha
     * @return
     * @throws LoginException
     */
    public String encriptarSenha(String senha) throws LoginException {
        if (senha == null || "".equals(senha)) {
            return senha;
        }
        String encryptPassword = "";
        String sql = " SELECT utl_raw.cast_to_raw(AUTENTICACAO.ENCRYPT (?)) as senha FROM dual ";
//        Connection currentConnection = null;
        try {
//            currentConnection = Conexao.getCurrentConnection(MediatorPassword.class);
//            PreparedStatement ps = currentConnection.prepareStatement(sql);
//            ps.setString(1, senha);
//            ResultSet rs = ps.executeQuery();
        	
        	encryptPassword = jdbcTemplate.queryForObject(sql, new Object[]{senha}, (rs, rowNum) -> rs.getString(1));        	
        	
//            if (rs != null && rs.next()) {
//                encryptPassword = rs.getString("senha");
//                rs.close();
//            }
//            ps.close();
        } catch (DataAccessException e) {
            LOGGER.error(Constantes.MSG_ERRO_ENCRIPTAR_SENHA, e);
            LOGGER.error("SQL: " + sql);
            throw new LoginException(e.getMessage());
        } catch (Exception e) {
            LOGGER.error(Constantes.MSG_ERRO_ENCRIPTAR_SENHA, e);
            throw new LoginException(e.getMessage());
        }
        return encryptPassword;
    }
}
