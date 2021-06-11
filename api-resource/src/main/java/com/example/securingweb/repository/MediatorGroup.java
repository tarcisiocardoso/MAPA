package com.example.securingweb.repository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.securingweb.model.JAASGroup;
import com.example.securingweb.util.AbstractMediator;

@SuppressWarnings("deprecation")
@Repository
public class MediatorGroup extends AbstractMediator {

	@Autowired
	JdbcTemplate jdbcTemplate;
	private final Logger LOGGER = LoggerFactory.getLogger(MediatorRole.class);

	/**
     * Obtém um {@link Set} de {@link JAASGroup} do usuário informado
     *
     * @param idUsuario  Código identificador do usuário
     * @param principals
     * @return
     * @throws Exception
     */
    public List<JAASGroup> getGruposUsuario(String idUsuario, List<Principal> principals) throws Exception {
    	List<JAASGroup> groups = new ArrayList<JAASGroup>();
//        Connection currentConnection = null;
        try {
            String sql = getSQLGrupos();
//            currentConnection = Conexao.getCurrentConnection(MediatorGroup.class);
//            PreparedStatement ps = currentConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            ps.setLong(1, idUsuario);

            // Parametros da cláusula IN
//            setaValoresClausulaINSQL(ps, idsGrupos);
            groups = jdbcTemplate.query(sql, new Object[]{idUsuario}, (rs, rowNum) ->{
            	return new JAASGroup(rs.getLong("id_grupo"), rs.getString("nm_grupo"), rs.getString("nm_contexto"));              
            });
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                do {
//                    JAASGroup jAASGroup = new JAASGroup(rs.getLong("id_grupo"), rs.getString("nm_grupo"), rs.getString("nm_contexto"));
//                    if (!principals.contains(jAASGroup)) {
//                        groups.add(jAASGroup);
//                    }
//                } while (rs.next());
//                rs.close();
//            }
//            ps.close();
        } catch (Exception e) {
            LOGGER.error("Erro ao tentar carregar grupos do usuário", e);
            throw e;
        }
        return groups;
    }


    private static String getSQLGrupos() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT gp.id_grupo, gp.nm_grupo, apl.nm_contexto ");
        sql.append("FROM autenticacao.s_usuario_grupo ug ");
        sql.append("INNER JOIN autenticacao.s_grupo gp ON ug.id_grupo = gp.id_grupo ");
        sql.append("INNER JOIN autenticacao.s_aplicacao apl ON apl.id_aplicacao = gp.id_aplicacao ");
        sql.append("WHERE ug.id_usuario = ? ");
        sql.append("AND (ug.st_ativo is null or ug.st_ativo = 'S') ");

        // Cláusula IN (?, ?, ?) condicional
//        criarClausulaINSQL(sql, "ug.id_grupo", idsGrupos);

        return sql.toString();
    }
}
