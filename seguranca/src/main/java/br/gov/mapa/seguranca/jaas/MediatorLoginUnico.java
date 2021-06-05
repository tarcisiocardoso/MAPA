package br.gov.mapa.seguranca.jaas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@SuppressWarnings("deprecation")
@Repository
public class MediatorLoginUnico {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public void vincularAplicacaoAoUsuarioLoginUnico(Long idUsuario, String stUsuarioAtivo, String nmContextoAplicacao) throws Exception {
        nmContextoAplicacao = getContextoAplicacaoPorUrl(nmContextoAplicacao);

        try {
            AplicacaoLoginUnicoDTO aplicacaoLoginUnico = consultarAplicacaoLoginUnicoPorContextoAplicacao(nmContextoAplicacao);
            if (aplicacaoLoginUnico == null) {
                throw new Exception("Aplicação não encontrada ou não está integrada com o login único");
            }

            // Se o usuário NÃO estiver ativo (e for o primeiro acesso pelo login único) todos os vinculos com os grupos atuais devem ser desativados
            if ("N".equals(stUsuarioAtivo) && isPrimeiroAcessoLoginUnico(idUsuario)) {
                desativarGruposDoUsuario(idUsuario);
            }

            // Se essa pessoa possuir um usuário externo (ex) as permissões devem ser copiadas para o usuário CPF.
            // Essa operação de cópia dos perfis é realizada uma única vez, no primeiro acesso do usuário a aplicação.
            if (isUsuarioNaoVinculadoAplicacaoLoginUnico(idUsuario, nmContextoAplicacao)) {
                copiarPermissoesDoUsuarioExternoParaUsuarioCpf(idUsuario, nmContextoAplicacao);
                desabilitarPermissoesDoUsuarioExterno(idUsuario, nmContextoAplicacao);
                vincularUsuarioAplicacaoLoginUnico(aplicacaoLoginUnico.getId(), idUsuario);
            }

            atualizarPermissoesDoLoginUnicoPorIdUsuario(idUsuario);
        } finally {
        }
    }

    private AplicacaoLoginUnicoDTO consultarAplicacaoLoginUnicoPorContextoAplicacao(String nmContextoAplicacao) throws Exception {
        try {
            String sql = "SELECT salu.ID_APLICACAO_LOGIN_UNICO, salu.ID_APLICACAO " +
                    "FROM AUTENTICACAO.S_APLICACAO_LOGIN_UNICO salu " +
                    "INNER JOIN AUTENTICACAO.S_APLICACAO sa ON sa.ID_APLICACAO = salu.ID_APLICACAO " +
                    "WHERE sa.NM_CONTEXTO = ? AND salu.ST_ATIVO = 1";

            AplicacaoLoginUnicoDTO dto = jdbcTemplate.queryForObject(sql, new Object[]{nmContextoAplicacao.replace("/", "")}, (rs, rowNum) -> 
            	new AplicacaoLoginUnicoDTO(getAsLong(rs.getObject(1)), getAsLong(rs.getObject(2)))
            		);
            
            return dto;
        } catch (DataAccessException e) {
            throw new Exception(e);
        }
    }

    private void desativarGruposDoUsuario(Long idUsuario) throws Exception {
        try {
            // Só inativa os vínculos com os grupos que NÃO sejam relacionados aos parametrizados para o "Login Único"
            String sql = "UPDATE AUTENTICACAO.S_USUARIO_GRUPO SET ST_ATIVO = 'N' " +
                    "WHERE ID_USUARIO = ? AND ID_APLICACAO_SERVICO IS NULL " +
                    "AND ID_GRUPO NOT IN (" +
                    "  SELECT sg.ID_GRUPO FROM AUTENTICACAO.S_APLICACAO sa " +
                    "  INNER JOIN AUTENTICACAO.S_GRUPO sg ON sa.ID_APLICACAO = sg.ID_APLICACAO " +
                    "  WHERE sa.SG_APLICACAO = 'SOLICITA' AND lower(sg.NM_GRUPO) in ('usuário', 'usuario') " +
                    ")";
            jdbcTemplate.update(sql, idUsuario);
        } catch (DataAccessException e) {
            throw new Exception(e);
        }
    }

    public boolean isAplicacaoIntegradaComLoginUnico(String urlAplicacao) throws Exception {
        try {
            String sql = "select 1 from AUTENTICACAO.S_APLICACAO_LOGIN_UNICO SALU " +
                    "inner join AUTENTICACAO.S_APLICACAO SA on SALU.ID_APLICACAO = SA.ID_APLICACAO " +
                    "where SALU.ST_ATIVO = 1 and lower(SA.NM_CONTEXTO) = lower(?)";
            int qtd = jdbcTemplate.queryForObject(sql, new Object[]{urlAplicacao}, (rs, rowNum) -> rs.getInt(1));
            return qtd > 0;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public String getContextoAplicacaoPorUrl(String urlAplicacao) {
        String contexto = urlAplicacao;
        if (urlAplicacao.charAt(0) == '/') {
            contexto = contexto.substring(1);
        }
        if (contexto.indexOf('/') != -1) {
            contexto = contexto.substring(0, contexto.indexOf('/'));
        }
        return contexto;
    }

    public boolean isUsuarioNaoVinculadoAplicacaoLoginUnico(Long idUsuario, String nmContextoAplicacao) throws Exception {
        try {
            String sql = "SELECT 1 FROM AUTENTICACAO.S_USUARIO_APL_LOGIN_UNICO SUALU " +
                    "INNER JOIN AUTENTICACAO.S_APLICACAO_LOGIN_UNICO SALU ON SALU.ID_APLICACAO_LOGIN_UNICO = SUALU.ID_APLICACAO_LOGIN_UNICO " +
                    "INNER JOIN AUTENTICACAO.S_APLICACAO SA ON SA.ID_APLICACAO = SALU.ID_APLICACAO " +
                    "WHERE SUALU.ID_USUARIO = ? AND lower(SA.NM_CONTEXTO) = lower(?)";
            int qtd = jdbcTemplate.queryForObject(sql, new Object[]{idUsuario}, (rs, rowNum) -> rs.getInt(1));
            return qtd < 1;
        } catch (DataAccessException e) {
            throw new Exception(e);
        }
    }

    private boolean isPrimeiroAcessoLoginUnico(Long idUsuario) throws Exception {
        try {
            String sql = "SELECT 1 FROM AUTENTICACAO.S_USUARIO_APL_LOGIN_UNICO WHERE ID_USUARIO = ?";
            int qtd = jdbcTemplate.queryForObject(sql, new Object[]{idUsuario}, (rs, rowNum) -> rs.getInt(1));
            return qtd > 0 ;
        } catch (DataAccessException e) {
            throw new Exception(e);
        }
    }

    private void atualizarPermissoesDoLoginUnicoPorIdUsuario(Long idUsuario) throws Exception {
        try {
            String sql = "merge into AUTENTICACAO.S_USUARIO_GRUPO ug " +
                    "using (select * from AUTENTICACAO.VW_USUARIO_GRUPO_LOGIN_UNICO where ID_USUARIO = ?) v " +
                    "on (ug.ID_USUARIO = v.ID_USUARIO and ug.ID_GRUPO = v.ID_GRUPO and ug.ID_APLICACAO_SERVICO = v.ID_APLICACAO_SERVICO) " +
                    "when matched then " +
                    "update set ug.ST_ATIVO = case when v.ST_APLICACAO_ATIVO = 1 and v.ST_SERVICO_ATIVO = 1 and v.ST_SERVICO_GRUPO_ATIVO = 1 then 'S' else 'N' end " +
                    "where ug.ST_ATIVO <> (case when v.ST_APLICACAO_ATIVO = 1 and v.ST_SERVICO_ATIVO = 1 and v.ST_SERVICO_GRUPO_ATIVO = 1 then 'S' else 'N' end) " +
                    "when not matched then " +
                    "insert (ug.ID_USUARIO_GRUPO, ug.ID_USUARIO, ug.ID_GRUPO, ug.ID_APLICACAO_SERVICO, ug.ST_ATIVO) " +
                    "values (AUTENTICACAO.SQ_USUARIO_GRUPO.nextval, v.ID_USUARIO, v.ID_GRUPO, v.ID_APLICACAO_SERVICO, 'S') " +
                    "where (v.ST_APLICACAO_ATIVO = 1 and v.ST_SERVICO_ATIVO = 1 and v.ST_SERVICO_GRUPO_ATIVO = 1)";
            jdbcTemplate.update(sql, idUsuario);
        } catch (DataAccessException e) {
            throw new Exception(e);
        }
    }

    private void copiarPermissoesDoUsuarioExternoParaUsuarioCpf(Long idUsuario, String nmContextoAplicacao) throws Exception {
        try {
            String sql = "merge into AUTENTICACAO.S_USUARIO_GRUPO ug " +
                    "using ( " +
                    "  select suCpf.ID_USUARIO, sugExt.ID_GRUPO, sugExt.ST_ATIVO ST_ATIVO_USU_EXT, sugCpf.ST_ATIVO ST_ATIVO_USU_CPF " +
                    "  from AUTENTICACAO.S_USUARIO suExt " +
                    "  inner join AUTENTICACAO.S_USUARIO_GRUPO sugExt ON sugExt.ID_USUARIO = suExt.ID_USUARIO " +
                    "  inner join AUTENTICACAO.S_GRUPO sg ON sg.ID_GRUPO = sugExt.ID_GRUPO " +
                    "  inner join AUTENTICACAO.S_APLICACAO sa ON sa.ID_APLICACAO = sg.ID_APLICACAO " +
                    "  left join AUTENTICACAO.S_USUARIO suCpf ON suCpf.ID_PESSOA_CORPORATIVO_MAPA = suExt.ID_PESSOA_CORPORATIVO_MAPA AND suCpf.ID_USUARIO = ? " +
                    "  left join AUTENTICACAO.S_USUARIO_GRUPO sugCpf ON sugCpf.ID_USUARIO = suCpf.ID_USUARIO AND sugExt.ID_GRUPO = sugCpf.ID_GRUPO " +
                    "  where suExt.ID_PESSOA_CORPORATIVO_MAPA = (select ID_PESSOA_CORPORATIVO_MAPA from AUTENTICACAO.S_USUARIO WHERE ID_USUARIO = ?) " +
                    "  and suExt.ST_ATIVO = 'E' " +
                    "  and sugExt.ID_APLICACAO_SERVICO IS NULL " +
                    "  and lower(sa.NM_CONTEXTO) = lower(?) " +
                    ") v  " +
                    "on (ug.ID_USUARIO = v.ID_USUARIO and ug.ID_GRUPO = v.ID_GRUPO)  " +
                    "when matched then  " +
                    "update set ug.ST_ATIVO = case when v.ST_ATIVO_USU_EXT = 'S' then v.ST_ATIVO_USU_EXT else v.ST_ATIVO_USU_CPF end  " +
                    "where ug.ST_ATIVO <> (case when v.ST_ATIVO_USU_EXT = 'S' then v.ST_ATIVO_USU_EXT else v.ST_ATIVO_USU_CPF end)  " +
                    "when not matched then  " +
                    "insert (ug.ID_USUARIO_GRUPO, ug.ID_USUARIO, ug.ID_GRUPO, ug.ST_ATIVO)  " +
                    "values (AUTENTICACAO.SQ_USUARIO_GRUPO.nextval, v.ID_USUARIO, v.ID_GRUPO, v.ST_ATIVO_USU_EXT)";
            
            jdbcTemplate.update(sql, idUsuario, idUsuario, nmContextoAplicacao);
        } catch (DataAccessException e) {
            throw new Exception(e);
        }
    }

    private void desabilitarPermissoesDoUsuarioExterno(Long idUsuario, String nmContextoAplicacao) throws Exception {
        try {
            String sql = "UPDATE AUTENTICACAO.S_USUARIO_GRUPO SET ST_ATIVO = 'N' WHERE ID_USUARIO_GRUPO IN ( " +
                    "  select sugExt.ID_USUARIO_GRUPO " +
                    "  from AUTENTICACAO.S_USUARIO suExt " +
                    "  inner join AUTENTICACAO.S_USUARIO_GRUPO sugExt ON sugExt.ID_USUARIO = suExt.ID_USUARIO " +
                    "  inner join AUTENTICACAO.S_GRUPO sg ON sg.ID_GRUPO = sugExt.ID_GRUPO " +
                    "  inner join AUTENTICACAO.S_APLICACAO sa ON sa.ID_APLICACAO = sg.ID_APLICACAO " +
                    "  where suExt.ID_PESSOA_CORPORATIVO_MAPA = (select ID_PESSOA_CORPORATIVO_MAPA from AUTENTICACAO.S_USUARIO WHERE ID_USUARIO = ?) " +
                    "  and suExt.ST_ATIVO = 'E' " +
                    "  and sugExt.ID_APLICACAO_SERVICO IS NULL " +
                    "  and lower(sa.NM_CONTEXTO) = lower(?) " +
                    ")";
            jdbcTemplate.update(sql, idUsuario, nmContextoAplicacao);
        } catch (DataAccessException e) {
            throw new Exception(e);
        }
    }

    private void vincularUsuarioAplicacaoLoginUnico(long idAplicacaoLoginUnico, Long idUsuario) throws Exception {
        try {
            String sql = "INSERT INTO AUTENTICACAO.S_USUARIO_APL_LOGIN_UNICO(ID_APLICACAO_LOGIN_UNICO, ID_USUARIO) VALUES (?,?)";
            jdbcTemplate.update(sql, idAplicacaoLoginUnico, idUsuario);
        } catch (DataAccessException e) {
            throw new Exception(e);
        }
    }

    private long getAsLong(Object obj) {
        return ((Number) obj).longValue();
    }

    // Classes auxiliares
    private class AplicacaoLoginUnicoDTO {
        private final long id;
        private final long idAplicacao;

        public AplicacaoLoginUnicoDTO(long id, long idAplicacao) {
            this.id = id;
            this.idAplicacao = idAplicacao;
        }

        public long getId() {
            return id;
        }

        public long getIdAplicacao() {
            return idAplicacao;
        }
    }

    private class ServicoGrupoDTO {
        private final long idServico;
        private final long idGrupo;

        public ServicoGrupoDTO(long idServico, long idGrupo) {
            this.idServico = idServico;
            this.idGrupo = idGrupo;
        }

        public long getIdServico() {
            return idServico;
        }

        public long getIdGrupo() {
            return idGrupo;
        }
    }

}

