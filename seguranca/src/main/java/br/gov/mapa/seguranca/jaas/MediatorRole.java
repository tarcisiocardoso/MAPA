package br.gov.mapa.seguranca.jaas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import br.gov.mapa.seguranca.util.Constantes;

@SuppressWarnings("deprecation")
@Repository
public class MediatorRole extends AbstractMediator {
	@Autowired
	JdbcTemplate jdbcTemplate;
	private final Logger LOGGER = LoggerFactory.getLogger(MediatorRole.class);

	/**
     * Obtém um {@link Set} de {@link JAASRole} públicas
     *
     * @param principals
     * @return
     */
    protected List<JAASRole> getModulosPublicos(Collection<? extends GrantedAuthority> principals) {
        List<JAASRole> roles = new ArrayList<JAASRole>();
//        Connection currentConnection = null;
        try {
//            String sql = getSQLModulosPublicos();
//            currentConnection = Conexao.getCurrentConnection(MediatorRole.class);
//            PreparedStatement ps = currentConnection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            roles = obterRoles(principals);
//            ps.close();
        } catch (Exception e) {
            LOGGER.error("Erro ao obter modulos publicos", e);
        }
        return roles;
    }

    /**
     * Obtém um {@link Set} de {@link JAASRole} do usuário informado
     *
     * @param idUsuario  Código identificador do usuário
     * @param principals
     * @return
     * @throws Exception
     */
    public List<JAASRole> getModulosUsuario(Long idUsuario, String[] idsGrupos, Collection<? extends GrantedAuthority> principals) throws Exception {
        List<JAASRole> roles = null; //new HashSet<JAASRole>();
        String sql = getSQLModulos(idsGrupos);
//        Connection conn = null;
        try {
//            conn = Conexao.getCurrentConnection(MediatorRole.class);
//            PreparedStatement ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

//            ps.setLong(1, idUsuario);

            // Parametros da cláusula IN
//            setaValoresClausulaINSQL(ps, idsGrupos);

            roles = obterRoles(principals);
//            ps.close();
        } catch (Exception e) {
            LOGGER.error("Erro ao obter modulos do usuário de id=" + idUsuario, e);
            throw e;
        }
        roles.addAll(getModulosPublicos(principals));
        return roles;
    }

    private List<JAASRole> obterRoles(Collection<? extends GrantedAuthority> principals) throws DataAccessException {
        boolean contemModuloWS = false;
        boolean contemModuloWeb = false;

        String sql = getSQLModulosPublicos();
//        Set<JAASRole> listaRoles = new HashSet<JAASRole>();
//        ResultSet rs = preparedStatement.executeQuery();
        List<JAASRole> listaRoles = jdbcTemplate.query(sql, (rs, rowNum) ->{
        	String acao = rs.getString("nm_acao");
            String modulo = rs.getString("nm_modulo");
            Long idModulo = rs.getLong("id_modulo");
            String csModulo = rs.getString("cs_modulo");
            String stPublico = rs.getString("st_publico");
            String stSaida = rs.getString("st_saida_ws");
            String nmContexto = rs.getString("nm_contexto");

            boolean publico = isSim(stPublico);
            boolean gravaSaida = isSim(stSaida);

            String role = modulo;
            if (acao != null && !"".equals(acao)) {
                role = acao;
            }

            JAASRole jAASRole = new JAASRole(role, idModulo, csModulo, publico, gravaSaida, nmContexto);
            return jAASRole;
        });
        
        for(JAASRole jAASRole: listaRoles) {
        	if (!principals.contains(jAASRole)) {
                if (jAASRole.getCsModulo() != null && jAASRole.getCsModulo().contains("WS")) {
                    contemModuloWS = true;
                } else {
                    contemModuloWeb = true;
                }
//                listaRoles.add(jAASRole);
            }
        }
        
        
//        if (rs.next()) {
//            do {
//                String acao = rs.getString("nm_acao");
//                String modulo = rs.getString("nm_modulo");
//                Long idModulo = rs.getLong("id_modulo");
//                String csModulo = rs.getString("cs_modulo");
//                String stPublico = rs.getString("st_publico");
//                String stSaida = rs.getString("st_saida_ws");
//                String nmContexto = rs.getString("nm_contexto");
//
//                boolean publico = isSim(stPublico);
//                boolean gravaSaida = isSim(stSaida);
//
//                String role = modulo;
//                if (acao != null && !"".equals(acao)) {
//                    role = acao;
//                }
//
//                JAASRole jAASRole = new JAASRole(role, idModulo, csModulo, publico, gravaSaida, nmContexto);
//                if (!principals.contains(jAASRole)) {
//                    if (jAASRole.getCsModulo() != null && jAASRole.getCsModulo().contains("WS")) {
//                        contemModuloWS = true;
//                    } else {
//                        contemModuloWeb = true;
//                    }
//                    listaRoles.add(jAASRole);
//                }
//            } while (rs.next());
//            rs.close();
//        }

        if (contemModuloWeb) {
            listaRoles.add(new JAASRole("segaut"));
        }
        if (contemModuloWS) {
            listaRoles.add(new JAASRole("rest"));
        }
        return listaRoles;
    }

    private String getSQLModulos(String[] idsGrupos) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT distinct(m.nm_acao), m.id_modulo, m.nm_modulo, m.cs_modulo, m.st_publico, m.st_saida_ws, apl.nm_contexto ");
        sqlBuilder.append("FROM autenticacao.s_modulo m ");
        sqlBuilder.append("INNER JOIN autenticacao.s_acesso ac on ac.id_modulo = m.id_modulo ");
        sqlBuilder.append("INNER JOIN autenticacao.s_grupo gp on gp.id_grupo = ac.id_grupo ");
        sqlBuilder.append("INNER JOIN autenticacao.s_usuario_grupo ug on ug.id_grupo = gp.id_grupo and ug.id_usuario = ? ");
        sqlBuilder.append("INNER JOIN autenticacao.s_aplicacao apl ON apl.id_aplicacao = m.id_aplicacao ");
        sqlBuilder.append("WHERE m.st_modulo_ativo = 'S'");

        // Cláusula IN (?, ?, ?) condicional
        criarClausulaINSQL(sqlBuilder, "ug.id_grupo", idsGrupos);

        sqlBuilder.append(" AND (ug.st_ativo is null or ug.st_ativo = 'S') ");
        sqlBuilder.append(" UNION ");
        sqlBuilder.append(getSQLModulosPublicos());
        return sqlBuilder.toString();
    }

    private String getSQLModulosPublicos() {
        return "SELECT DISTINCT(m.nm_acao), m.id_modulo, m.nm_modulo, m.cs_modulo, m.st_publico, m.st_saida_ws, apl.nm_contexto " +
                "FROM autenticacao.s_modulo m INNER JOIN autenticacao.s_aplicacao apl ON apl.id_aplicacao = m.id_aplicacao " +
                "WHERE m.st_publico = 'S' AND m.st_modulo_ativo='S' ";
    }


    private boolean isSim(String string) {
        return string != null && !"".equals(string.trim()) && string.trim().equals(Constantes.STRING_S);
    }
}

