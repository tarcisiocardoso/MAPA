package br.gov.mapa.seguranca.jaas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.mapa.seguranca.repository.PropriedadesAmbienteUtil;
import br.gov.mapa.seguranca.util.Constantes;

@Service
public class Autenticador {
	private static final Logger LOG = LoggerFactory.getLogger(AutenticadorBD.class);
	
	@Autowired
	MediatorUser mediatorUser;
	@Autowired
	AutenticadorAD autenticadorAD;
	@Autowired
	AutenticadorBD autenticadorBD;

	/**
     * {@inheritDoc}
     */
    public boolean autenticar(final String username, final String password) {
        return this.autenticar(username, password, null);
    }

    /**
     * {@inheritDoc}
     */
    public boolean autenticar(final String username, final String password, final String documento) {
        if (autenticarSemValidarSenha()) {
            return true;
        }

        try {
            UserVO user = mediatorUser.getUser(username);
            if (isUsuarioDeRede(user.getStAtivo())) {
                return autenticadorAD.autenticar(username, password, documento);
            } else {
                return autenticadorBD.autenticar(username, password);
            }
        } catch (Exception e) {
            LOG.error("Falha ao recuperar o usuário do banco para autenticacao", e);
        }
        return false;
    }

    /**
     * Verifica se o status é de um usuário de rede
     *
     * @param statusAtivo Status de acesso
     * @return <code>true</code>, caso seja usuário de rede; <code>false</code> caso contrário
     */
    public static boolean isUsuarioDeRede(String statusAtivo) {
        return isUsuarioDeRedeInterno(statusAtivo) || isUsuarioDeRedeExterno(statusAtivo);
    }

    /**
     * Verifica se é um usuário de rede externo
     *
     * @param statusAtivo Status de acesso
     * @return <code>true</code>, caso seja usuário de rede externo; <code>false</code> caso contrário
     */
    public static boolean isUsuarioDeRedeExterno(String statusAtivo) {
        return Constantes.ST_REDE_EXTERNO.equals(statusAtivo.trim());
    }

    private static boolean isUsuarioDeRedeInterno(String statusAtivo) {
        return Constantes.ST_REDE_INTERNO.equals(statusAtivo.trim());
    }

    
    public void verificarConexao() {
        // método sem implementação
    }

    
    public boolean isUsuarioAtivo(String username) throws Exception {
        if (autenticarSemValidarSenha()) {
            return true;
        }

        boolean ativo = autenticadorBD.isUsuarioAtivo(username);

        if (!ativo) {
            ativo = autenticadorAD.isUsuarioAtivo(username);
        }
        return ativo;
    }

    private boolean autenticarSemValidarSenha() {
        return PropriedadesAmbienteUtil.isFuncaoHabilitada("autenticar.usuario.sem.validar.senha");
    }
}
