package br.gov.mapa.seguranca.jaas;
import java.util.List;
import javax.security.auth.login.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import br.gov.mapa.seguranca.util.Constantes;

@SuppressWarnings("deprecation")
@Repository
public class MediatorUser extends AbstractMediator {

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
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MediatorUser.class);

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
                if (rs.getString(Constantes.DS_SENHA) != null) {
                    user.setDbSenha(rs.getString(Constantes.DS_SENHA));
                }
                user.setIdUsuario(rs.getLong(ID_USUARIO));
                user.setEmail(rs.getString(DS_EMAIL));
                user.setStAtivo(rs.getString(ST_ATIVO));
                user.setDbSenha(rs.getString("ds_senha"));
                if (rs.getLong(Constantes.ID_PESSOA_FISICA) != 0) {
                    user.setIdPessoa(rs.getLong(Constantes.ID_PESSOA_FISICA));
                    user.setNome(rs.getString(NM_PESSOA_FISICA));
                    user.setDocumento(rs.getString(NR_CPF));
                    user.setTipoPessoa(F);
                } else if (rs.getLong(Constantes.ID_PESSOA_JURIDICA) != 0) {
                    user.setIdPessoa(rs.getLong(Constantes.ID_PESSOA_JURIDICA));
                    user.setNome(rs.getString("nm_razao_social"));
                    user.setDocumento(rs.getString("nr_cnpj"));
                    user.setTipoPessoa("J");
                } else if (rs.getLong(Constantes.ID_PESSOA_ESTRANGEIRA) != 0) {
                    user.setIdPessoa(rs.getLong(Constantes.ID_PESSOA_ESTRANGEIRA));
                    user.setNome(rs.getString("nm_pessoa_estrangeira"));
                    user.setDocumento(rs.getString("nr_identificacao"));
                    user.setTipoPessoa(E);
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
        sqlBuffer.append("    AND LOWER(usu.ds_login)=?");
        return sqlBuffer.toString();
    }

    /**
     * Recupera um usuário pelo cpf, de preferência usuário de REDE
     *
     * @param cpf
     * @return
     * @throws Exception
     */
    protected UserVO getUserByCPF(String cpf) throws Exception {
        String sql = getSQLAutenticacaoCertificado();
        List<UserVO>lst = null;
        try {
//            currentConnection = Conexao.getCurrentConnection(MediatorUser.class);
//            PreparedStatement ps = currentConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            ps.setString(1, cpf);
//            ResultSet rs = ps.executeQuery();
//            String statusAtivo;
            
            lst = jdbcTemplate.query(sql, new Object[]{cpf}, (rs, rowNum) ->{
            	UserVO user = new UserVO();
            	String statusAtivo = rs.getString(ST_ATIVO); // E, A, S, N

                user.setIdUsuario(rs.getLong(ID_USUARIO));
                user.setEmail(rs.getString(DS_EMAIL));
                user.setDsLogin(rs.getString("ds_login"));
                user.setStAtivo(statusAtivo);
                user.setDsAssinatura(rs.getString("ds_assinatura_digital"));
                if (rs.getLong(Constantes.ID_PESSOA_FISICA) != 0) {
                    user.setIdPessoa(rs.getLong(Constantes.ID_PESSOA_FISICA));
                    user.setNome(rs.getString(NM_PESSOA_FISICA));
                    user.setDocumento(rs.getString(NR_CPF));
                    user.setTipoPessoa(F);
                }
//                if ("A".equals(statusAtivo) || E.equals(statusAtivo)) {
//                    //Encontrou usuario de rede
//                    break;
//                }
                return user;
            });
            
            if(lst == null || lst.size() == 0){
                throw new LoginException(Constantes.MSG_ERRO_USUARIO_INEXISTENTE);
            }
        } catch (LoginException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Erro ao tentar obter usuario a partir do CPF[" + cpf + "]", e);
            throw e;
        }
        if( lst != null ) {
        	for(UserVO vo : lst) {
        		String statusAtivo = vo.getStAtivo();
        		if ("A".equals(statusAtivo) || E.equals(statusAtivo)) {
                    //Encontrou usuario de rede
                    return vo;
                }
        	}
        	return lst.get(lst.size()-1);
        }
        return null;
    }

    private static String getSQLAutenticacaoCertificado() {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" SELECT usu.id_usuario, usu.ds_login, usu.ds_email, usu.st_ativo, pf.id_pessoa_fisica, ");
        sqlBuffer.append("        pf.nm_pessoa_fisica,  pf.nr_cpf, usu.ds_assinatura_digital");
        sqlBuffer.append("   FROM autenticacao.s_usuario usu, ");
        sqlBuffer.append("        corporativo_mapa.s_pessoa_fisica pf ");
        sqlBuffer.append("  WHERE usu.id_pessoa_corporativo_mapa = pf.id_pessoa_fisica(+) ");
//        sqlBuffer.append("    AND pf.nr_cpf=?");
        sqlBuffer.append("    AND usu.ds_login=?");
        return sqlBuffer.toString();
    }

    /**
     * Limpa o campo destinado para autenticacao com certificado digital
     *
     * @param idUsuario
     * @throws Exception
     */
    protected void cleanSign(Long idUsuario) throws Exception {
//        Connection currentConnection = null;
        try {
            String sql = "UPDATE autenticacao.s_usuario SET ds_assinatura_digital = null WHERE id_usuario = ?";
            
            jdbcTemplate.update(sql, idUsuario);
//            currentConnection = Conexao.getCurrentConnection(MediatorUser.class);
//            PreparedStatement ps = currentConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//            ps.setLong(1, idUsuario);
//            ps.executeUpdate();
//            currentConnection.commit();
//            ps.close();
        } catch (DataAccessException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Verifica se o usuário é estrangeiro
     *
     * @param idUsuario Código identificador do usuário
     * @return <code>true</code>, se usuário estrangeiro; <code>false</code> caso contrário
     * @throws Exception
     */
    protected boolean isUsuarioEstrangeiro(Long idUsuario) throws Exception {
        boolean isUsuarioEstrangeiro = false;
        String sql = getSQLVerificarSeUsuarioEstrangeiro();
        try {
        	String estrangeiro = jdbcTemplate.queryForObject(sql, new Object[]{idUsuario}, (rs, rowNum) -> rs.getString(1));        	
        	//TODO testar para saber o que vem
        	if( estrangeiro != null ) {
        		isUsuarioEstrangeiro = true;
        	}
        } catch (Exception e) {
            LOGGER.error("Erro ao tentar verificar se é usuario extrangeiro a partir do idPessoa[" + idUsuario + "]", e);
            throw e;
        }
        return isUsuarioEstrangeiro;
    }

    private static String getSQLVerificarSeUsuarioEstrangeiro() {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(" SELECT pe.id_pessoa_estrangeira ");
        sqlBuffer.append("   FROM corporativo_mapa.s_pessoa_estrangeira pe ");
        sqlBuffer.append("  INNER JOIN autenticacao.s_usuario u on u.id_pessoa_corporativo_mapa = pe.id_pessoa_estrangeira ");
        sqlBuffer.append("  WHERE u.id_usuario = ? ");
        return sqlBuffer.toString();
    }

    /**
     * Registra a data e hora do login do usuário
     *
     * @param idUsuario
     * @throws Exception
     */
    protected void registraUltimoLogin(Long idUsuario) throws Exception {
        if (!ID_USUARIO_INTERNET.equals(idUsuario)) {
            try {
                LOGGER.debug("iniciando registro de ultimo login para idUsuario=" + idUsuario);
                String sql = "call autenticacao.p_atualiza_dh_ultimo_login(?)";
                jdbcTemplate.update(sql, idUsuario);
                LOGGER.debug("atualizacao de data/hora do ultimo login delegada com sucesso ao banco de dados");
            } catch (DataAccessException e) {
                LOGGER.warn("Erro ao executar procedure para atualizar data/hora de ultimo login", e);
            } finally {
                LOGGER.debug("fim do registro de login. Iniciando liberação da conexão com banco de dados...");
                LOGGER.debug("conexao liberada!");
            }
        }
    }
}
