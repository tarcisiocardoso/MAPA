package com.example.securingweb.repository;

import java.util.List;
import javax.security.auth.login.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.securingweb.model.UserVO;
import com.example.securingweb.util.Constantes;


@SuppressWarnings("deprecation")
@Repository
public class UserRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

	@Autowired
	JdbcTemplate jdbcTemplate;
	
    private static final String E = "E";
    private static final String F = "F";
    private static final String NR_CPF = "nr_cpf";
    private static final String NM_PESSOA_FISICA = "nm_pessoa_fisica";
    private static final String ST_ATIVO = "st_ativo";
    private static final String DS_EMAIL = "ds_email";
    private static final String ID_USUARIO = "id_usuario";
    private static final Long ID_USUARIO_INTERNET = 0L;
    
	/**
     * Obtém um usuário a partir do login informado
     *
     * @param login
     * @return
     * @throws Exception
     */
    public UserVO getUser(String login) throws Exception {
        String sql = getSQLAutenticacao();
        List<UserVO>lst = null;
        try {
        	lst = jdbcTemplate.query(sql, new Object[]{login}, (rs, rowNum) ->{
        		UserVO user = new UserVO();
                
                user.idUsuario = (rs.getLong(ID_USUARIO));
                user.email = (rs.getString(DS_EMAIL));
                user.stAtivo = (rs.getString(ST_ATIVO));
                
                if (rs.getLong(Constantes.ID_PESSOA_FISICA) != 0) {
                    user.idPessoa = (rs.getLong(Constantes.ID_PESSOA_FISICA));
                    user.nome = (rs.getString(NM_PESSOA_FISICA));
                    user.documento = (rs.getString(NR_CPF));
                    user.tipoPessoa = (F);
                } else if (rs.getLong(Constantes.ID_PESSOA_JURIDICA) != 0) {
                    user.idPessoa = (rs.getLong(Constantes.ID_PESSOA_JURIDICA));
                    user.nome = (rs.getString("nm_razao_social"));
                    user.documento = (rs.getString("nr_cnpj"));
                    user.tipoPessoa = ("J");
                } else if (rs.getLong(Constantes.ID_PESSOA_ESTRANGEIRA) != 0) {
                    user.idPessoa = (rs.getLong(Constantes.ID_PESSOA_ESTRANGEIRA));
                    user.nome = (rs.getString("nm_pessoa_estrangeira"));
                    user.documento = (rs.getString("nr_identificacao"));
                    user.tipoPessoa = (E);
                }
                return user;
            });
        	
            if(lst == null || lst.size() ==0 ) {
                throw new LoginException(Constantes.MSG_ERRO_USUARIO_INEXISTENTE);
            }
        } catch (LoginException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Erro ao tentar obter usuario para autenticacao", e);
            throw e;
        }
        return lst != null?lst.get(0):null;
    }
    
    private static String getSQLAutenticacao() {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" SELECT usu.id_usuario, usu.ds_login, usu.ds_senha, usu.ds_email, usu.st_ativo, pf.id_pessoa_fisica, ");
        sqlBuffer.append("        pf.nm_pessoa_fisica,  pf.nr_cpf, pj.id_pessoa_juridica, pj.nm_razao_social, ");
        sqlBuffer.append("        pj.nr_cnpj, pe.id_pessoa_estrangeira, pe.nm_pessoa_estrangeira, pe.nr_identificacao, usu.ds_senha ");
        sqlBuffer.append("   FROM autenticacao.s_usuario usu, ");
        sqlBuffer.append("        corporativo_mapa.s_pessoa_fisica pf, ");
        sqlBuffer.append("        corporativo_mapa.s_pessoa_juridica pj, ");
        sqlBuffer.append("        corporativo_mapa.s_pessoa_estrangeira pe  ");
        sqlBuffer.append("  WHERE usu.id_pessoa_corporativo_mapa = pf.id_pessoa_fisica(+) ");
        sqlBuffer.append("    AND usu.id_pessoa_corporativo_mapa = pj.id_pessoa_juridica(+) ");
        sqlBuffer.append("    AND usu.id_pessoa_corporativo_mapa = pe.id_pessoa_estrangeira(+) ");
        sqlBuffer.append("    AND LOWER(usu.id_usuario)=?");
        return sqlBuffer.toString();
    }
}
