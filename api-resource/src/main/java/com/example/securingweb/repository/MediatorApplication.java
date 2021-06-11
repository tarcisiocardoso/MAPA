package com.example.securingweb.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.securingweb.model.JAASApplication;
import com.example.securingweb.util.AbstractMediator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
@Repository
public class MediatorApplication extends AbstractMediator {

	@Autowired
	JdbcTemplate jdbcTemplate;
	private final Logger LOGGER = LoggerFactory.getLogger(MediatorRole.class);


    /**
     * Obtém um {@link List} de {@link JAASApplication} públicas
     *
     * @param principals
     * @return
     * @throws Exception
     */
    protected List<JAASApplication> getAplicacoesPublicas(List<Principal> principals, boolean usuarioAutenticado) throws Exception {
        List<JAASApplication> apps = new ArrayList<JAASApplication>();
//        Connection currentConnection = null;
        try {
//            currentConnection = Conexao.getCurrentConnection(MediatorApplication.class);
            String sql = getSQLAplicacoesComModulosPublicos(usuarioAutenticado);
//            PreparedStatement ps = currentConnection.prepareStatement(sql,
//                    ResultList.TYPE_SCROLL_INSENSITIVE,
//                    ResultList.CONCUR_READ_ONLY);
            apps = obterApps(principals, new Object[] {}, sql);
//            ps.close();
            
            
        } catch (Exception e) {
            LOGGER.error("Erro ao obter aplicacoes que contem modulos publicos", e);
            throw e;
        }
        return apps;
    }

    /**
     * Obtém um {@link List} de {@link JAASApplication} do usuário informado
     *
     * @param idUsuario  Código identificador do usuário
     * @param autenticadoComLoginUnico
     * @param principals
     * @return
     * @throws Exception
     */
    public List<JAASApplication> getAplicacoesUsuario(String idUsuario, String[] idsAplicacoes, boolean autenticadoComLoginUnico, List<Principal> principals) throws Exception {
        List<JAASApplication> apps;
        String sql = getSQLAutorizacao(idsAplicacoes, autenticadoComLoginUnico);
//        Connection conn = null;

        try {
//            conn = Conexao.getCurrentConnection(MediatorApplication.class);
//            PreparedStatement ps = conn.prepareStatement(sql, ResultList.TYPE_SCROLL_INSENSITIVE, ResultList.CONCUR_READ_ONLY);
//            ps.ListLong(1, idUsuario);

            // Parametros da cláusula IN
            List<String>lst = setaValoresClausulaINSQL(idsAplicacoes);
            Object arr[] = new Object[lst.size()+1];
            int i=0;
            arr[i++] = idUsuario;
            for(String s: lst) {
            	arr[i++] = s;
            }
            apps = obterApps(principals, arr, sql);
//            ps.close();
        } catch (Exception e) {
            LOGGER.error("Erro ao obter aplicacoes do usuario de id=" + idUsuario, e);
            throw e;
        }

        boolean usuarioAutenticado = idUsuario != null;
        apps.addAll(getAplicacoesPublicas(principals, usuarioAutenticado));
        return apps;
    }

    private List<JAASApplication> obterApps(List<Principal> principals, Object arr[], String sql) throws Exception {
        List<JAASApplication> apps = new ArrayList<JAASApplication>();
//        ResultList rs = ps.executeQuery();
//        if (rs.next()) {
//            do {
//                JAASApplication jAASApplication = new JAASApplication(
//                        rs.getString("sg_aplicacao"),
//                        rs.getLong("id_aplicacao"),
//                        rs.getString("nm_aplicacao"),
//                        rs.getString("ds_versao"),
//                        rs.getString("nm_contexto")
//                );
//                if (!principals.contains(jAASApplication)) {
//                    apps.add(jAASApplication);
//                }
//            } while (rs.next());
//            rs.close();
//        }
        
		List<JAASApplication> lstTemp = jdbcTemplate.query(sql, arr, (rs, rowNum) ->{
	          JAASApplication jAASApplication = new JAASApplication(
		          rs.getString("sg_aplicacao"),
		          rs.getLong("id_aplicacao"),
		          rs.getString("nm_aplicacao"),
		          rs.getString("ds_versao"),
		          rs.getString("nm_contexto")
			  );
			  
            return jAASApplication;
        });
    	
    	for( JAASApplication app: lstTemp) {
    		if (!principals.contains(app)) {
			      apps.add(app);
			}
    	}
        
        return apps;
    }

    private String getSQLAutorizacao(String[] idsAplicacoes, boolean autenticadoComLoginUnico) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ( ");
        sql.append("  SELECT DISTINCT(sa.id_aplicacao), sa.sg_aplicacao, sa.nm_aplicacao, sa.ds_versao, sa.nm_contexto ");
        sql.append("  FROM autenticacao.s_aplicacao sa, ");
        sql.append("       autenticacao.s_modulo sm, ");
        sql.append("       autenticacao.s_grupo sg, ");
        sql.append("       autenticacao.s_usuario_grupo sug ");
        sql.append("  WHERE sa.id_aplicacao = sm.id_aplicacao ");
        sql.append("  AND sm.id_modulo_subordinado is null ");
        sql.append("  AND sm.cs_modulo = 'DO' ");
        sql.append("  AND sa.id_aplicacao = sg.id_aplicacao ");
        sql.append("  AND sg.id_grupo = sug.id_grupo ");
        sql.append("  AND sug.id_usuario = ? ");
        sql.append("  AND (sug.st_ativo is null or sug.st_ativo = 'S')");

        // Cláusula IN (?, ?, ?) condicional
        criarClausulaINSQL(sql, "sa.SG_APLICACAO", idsAplicacoes);

        if(autenticadoComLoginUnico) {
            sql.append("  UNION ");
            sql.append("  SELECT DISTINCT(sa.id_aplicacao), sa.sg_aplicacao, sa.nm_aplicacao, sa.ds_versao, sa.nm_contexto ");
            sql.append("  FROM autenticacao.s_aplicacao sa ");
            sql.append("  INNER JOIN autenticacao.s_aplicacao_login_unico salu on salu.id_aplicacao = sa.id_aplicacao ");
            sql.append("  WHERE salu.ST_ATIVO = '1' ");
        }

        sql.append(") sa ");
        sql.append("ORDER BY sa.sg_aplicacao asc ");
        return sql.toString();
    }


    private String getSQLAplicacoesComModulosPublicos(boolean usuarioAutenticado) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT a.id_aplicacao, a.sg_aplicacao, a.nm_aplicacao, a.ds_versao,a.nm_contexto ");
        sql.append("  FROM autenticacao.s_aplicacao a ");
        sql.append(" WHERE exists ");
        sql.append("       ( select 1 ");
        sql.append("           from autenticacao.s_modulo m ");
        sql.append("          where m.id_modulo_subordinado is not null ");
        sql.append("            and m.id_aplicacao = a.id_aplicacao ");
        sql.append("            and m.cs_modulo = 'DO' ");
        sql.append("            and m.st_modulo_ativo = 'S' ");
        if (usuarioAutenticado) {
            sql.append("            and m.st_publico = 'S' ");
        } else {
            sql.append("            and m.st_publico <> 'N' "); //S ou E
        }
        sql.append("       ) ");
        sql.append(" ORDER BY a.sg_aplicacao asc ");
        return sql.toString();
    }
}
