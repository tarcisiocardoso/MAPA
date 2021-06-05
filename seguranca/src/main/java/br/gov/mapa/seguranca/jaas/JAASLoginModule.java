package br.gov.mapa.seguranca.jaas;

import static br.gov.mapa.seguranca.jaas.JAASLoginModule.LoginMethod.LOGIN_UNICO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.gov.mapa.seguranca.util.Constantes;

@Service
public class JAASLoginModule {
	private static final Logger LOGGER = LoggerFactory.getLogger(JAASLoginModule.class);

	@Autowired
	MediatorUser mediatorUser;
	@Autowired
	AutenticadorAD autenticadorAD;
	@Autowired
	MediatorLoginUnico mediatorLoginUnico;
	
	@Autowired
	Autenticador autenticador;
	@Autowired
	MediatorRole mediatorRole;
	
    public enum LoginMethod {PASSWD, JWT_MAPA, LOGIN_UNICO}

    private String username;
    private String password;
    private long idPessoa;
    private long idUsuario;
    private String documento;
    private String name;
    private String email;
    private boolean ativo;
    private String stAtivo;

    private JWTPayload jwt;
    private String contextoAplicacao;
    private LoginMethod loginMethod;

    private boolean commitSucceeded;
    private boolean succeeded;
    private boolean autenticadoComCertificado;
    private boolean autenticadoComLoginUnico;

    
    /**
     * {@inheritDoc}
     */
    public UserVO login(String username) throws LoginException {
        LOGGER.debug("Início do método de login");
        this.username = username;
        try {
//            limparPrincipals();
//            handleCallbacks();

            // 1 - Tentativa de login usando JWT
//            final String mapaJwtAssertion = getJWT();
//            if (mapaJwtAssertion != null) {
//                return autenticarComJWT(mapaJwtAssertion);
//            }

            UserVO user = mediatorUser.getUserByCPF(username);
            return user;
            // Tenta autenticar com o certificado
//            if (user != null && user.getDsAssinatura() != null && !"".equals(user.getDsAssinatura())) {
//                return autenticarComCertificado(user);
//            }
//            //Tenta autenticar com usuário e senha
//            return autenticarComUsuarioESenha(user);
        } catch (SQLException ex) {
            LOGGER.error(Constantes.MSG_ERRO_AUTENTICACAO + username, ex);
            throw new LoginException(ex.getMessage()); //NOPMD
        } catch (Exception ex) {
            LOGGER.error(Constantes.MSG_ERRO_AUTENTICACAO + username, ex);
            throw new LoginException(ex.getMessage()); //NOPMD
        }
    }
    public String getUserRedePass( ) {
    	List<UserVO> lst = autenticadorAD.findByUID(username);
    	if( lst != null && lst.size() > 0 ) {
    		return lst.get(0).getDbSenha();
    	}
    	return null;
    }
    public boolean logingRedeOK( ) {
    	return this.autenticadorAD.autenticar(username, password);
    }
    

    /**
     * @param token
     * @return
     * @throws Exception
     */
    private boolean autenticarComJWT(String token) throws Exception {
        jwt = new JWTPayload(token);

        // @TODO: FAZER CHAMADA AO WSO2 AFIM DE VALIDAR O JWT

        idUsuario = Long.valueOf(jwt.getClaim(MapaJWTClaims.ID_USUARIO.name));
        String username = jwt.getClaim(MapaJWTClaims.NOME_USUARIO.name);

        String idPessoaFisica = jwt.getClaim(MapaJWTClaims.ID_PESSOA.name);
        String idPessoaJuridica = jwt.getClaim(MapaJWTClaims.ID_PESSOA_JURIDICA.name);
        String idPessoaEstrangeira = jwt.getClaim(MapaJWTClaims.ID_PESSO_AESTRANGEIRA.name);

        final String tipoPessoa;
        if (idPessoaJuridica != null) {

            String nomePessoaJuridica = jwt.getClaim(MapaJWTClaims.RAZAO_SOCIAL.name);
            String cnpj = jwt.getClaim(MapaJWTClaims.CNPJ.name);

            documento = cnpj;

            name = nomePessoaJuridica;
            tipoPessoa = "J";
            idPessoa = Long.valueOf(idPessoaJuridica);

        } else if (idPessoaEstrangeira != null) {

            String nomePessoaEstrangeira = jwt.getClaim(MapaJWTClaims.NOME_PESSOA_ESTRANGEIRA.name);
            String identificacao = jwt.getClaim(MapaJWTClaims.IDENTIFICACAO.name);

            documento = identificacao;
            name = nomePessoaEstrangeira;
            tipoPessoa = "E";
            idPessoa = Long.valueOf(idPessoaEstrangeira);
        } else {

            String nomePessoaFisica = jwt.getClaim(MapaJWTClaims.NOME_PESSOA.name);
            String cpf = jwt.getClaim(MapaJWTClaims.DOCUMENTO.name);

            documento = cpf;
            name = nomePessoaFisica;
            tipoPessoa = "F";
            idPessoa = Long.valueOf(idPessoaFisica);
        }

        email = jwt.getClaim(MapaJWTClaims.EMAIL.name);
        stAtivo = jwt.getClaim(MapaJWTClaims.ATIVO.name);

        ativo = stAtivo.equalsIgnoreCase("A");

        return true;
    }

    private boolean autenticarComCertificado(UserVO userVO) throws Exception {
        boolean autenticado = true;
        LOGGER.info("Autenticando usuário por Certificado Digital...");
        username = userVO.getDsLogin();
        idUsuario = userVO.getIdUsuario();
        email = userVO.getEmail();
        stAtivo = userVO.getStAtivo();
        idPessoa = userVO.getIdPessoa();
        name = userVO.getNome();
        documento = userVO.getDocumento();

        String[] dados = userVO.getDsAssinatura().split(",");
        String assinatura = dados[0].split(":")[1].replaceAll("\"", "");
        String dataGerada = dados[1].split(":")[1].replaceAll("\"", "").replace("}", "");
        if (!assinatura.equals(new String(password))) {
            autenticado = false;
            LOGGER.warn("Usuario nao autenticado.");
            LOGGER.warn("Houve tentativa de autenticacao com certificado digital mas a assinatura gerada em banco nao confere com a informada em pagina");
            limparAssinaturaDoUsuario(userVO.getIdUsuario());
            throw new LoginException("Autenticação com certificado digital inválida");
        } else {
            int segundos = 30;
            Calendar dataHoraAtual = Calendar.getInstance();
            Calendar dataAutenticacao = Calendar.getInstance();
            dataAutenticacao.setTimeInMillis(Long.valueOf(dataGerada));
            Calendar dataAutenticacaoMaisDelay = dataAutenticacao;
            dataAutenticacaoMaisDelay.add(Calendar.SECOND, segundos); //Adiciona 30 segundos à data de autenticacao
            if (dataHoraAtual.after(dataAutenticacaoMaisDelay)) {
                LOGGER.warn("Tentativa de autenticar com certificado digital ultrapassou o tempo limite de " + segundos + " segundos");
                LOGGER.warn("Hora atual: " + dataHoraAtual.get(Calendar.MINUTE) + ":" + dataHoraAtual.get(Calendar.SECOND));
                LOGGER.warn("Hora da autenticacao com delay de " + segundos + " segundos: " + dataAutenticacaoMaisDelay.get(Calendar.MINUTE) + ":"
                        + dataAutenticacaoMaisDelay.get(Calendar.SECOND));
                limparAssinaturaDoUsuario(userVO.getIdUsuario());
                throw new LoginException("Autenticação com certificado digital inválida");
            }
        }
        if (Autenticador.isUsuarioDeRede(stAtivo)) {
            ativo = autenticadorAD.isUsuarioAtivo(username);
            if (!ativo) {
                LOGGER.warn("Usuário autenticado com certificado mas não está ativo no AD [" + username + "]");
            }
        }
        limparAssinaturaDoUsuario(userVO.getIdUsuario());
        autenticadoComCertificado = autenticado;
        LOGGER.info("Término do método de login por certificado: " + new Date());
        return autenticado;
    }

    private void limparAssinaturaDoUsuario(Long idUsuarioCert) throws Exception {
        mediatorUser.cleanSign(idUsuarioCert);
    }

    private boolean autenticarComUsuarioESenha(UserVO u) throws Exception {
        UserVO user = mediatorUser.getUser(u.getDsLogin());
        idUsuario = user.getIdUsuario();
        email = user.getEmail();
        stAtivo = user.getStAtivo();
        idPessoa = user.getIdPessoa();
        name = user.getNome();
        documento = user.getDocumento();
        password = user.getDbSenha();

        if (LOGIN_UNICO.equals(loginMethod)) {
            ativo = true;
            autenticadoComLoginUnico = true;

            mediatorLoginUnico.vincularAplicacaoAoUsuarioLoginUnico(idUsuario, stAtivo, contextoAplicacao);
            return true;
        }

        //Se usuário de rede, tenta autenticar no AD
        String passwordClean = password;
        boolean autenticado = autenticador.autenticar(u.getDsLogin(), passwordClean, documento);
        if (!autenticado) {
            LOGGER.debug("Usuario nao autenticado");
            throw new LoginException(Constantes.MSG_ERRO_SENHA_INVALIDA);
        }

        ativo = autenticador.isUsuarioAtivo(u.getDsLogin());
        return true;
    }
/*
    private void handleCallbacks() throws LoginException {
        if (callbackHandler == null) {
            LOGGER.error(Constantes.MSG_ERRO_CALLBACK_NAO_ESPECIFICADO);
            throw new LoginException(Constantes.MSG_ERRO_CALLBACK_NAO_ESPECIFICADO);
        }

        Callback[] callbacks;
        try {
            callbacks = new Callback[5];
            callbacks[0] = new NameCallback("Username: ");
            callbacks[1] = new PasswordCallback("Password: ", false);
            callbacks[2] = new NameCallback("MAPA-JWT-Assertion");
            callbacks[3] = new TextInputCallback("Contexto-Aplicacao");
            callbacks[4] = new ChoiceCallback("LoginMethod", new String[]{PASSWD.name(), JWT_MAPA.name(), LOGIN_UNICO.name()}, 0, false);
            callbackHandler.handle(callbacks);

            // Seta os valores nas variáveis
            contextoAplicacao = ((TextInputCallback) callbacks[3]).getText();
            contextoAplicacao = MediatorLoginUnico.getContextoAplicacaoPorUrl(contextoAplicacao);

            ChoiceCallback choiceCallback = (ChoiceCallback) callbacks[4];
            int selectedIndex = choiceCallback.getSelectedIndexes()[0];
            loginMethod = LoginMethod.valueOf(choiceCallback.getChoices()[selectedIndex]);
        } catch (Exception e) {
            try {
                callbacks = new Callback[2];
                callbacks[0] = new NameCallback("Username: ");
                callbacks[1] = new PasswordCallback("Password: ", false);
                callbackHandler.handle(callbacks);
                loginMethod = PASSWD;
            } catch (Exception ioException) {
                LOGGER.error(Constantes.MSG_ERRO_CALLBACKS + e);
                throw new LoginException(e.getMessage()); //NOPMD
            }
        }

        // Seta os valores comuns (Usuário e Senha) nas variáveis
        username = ((NameCallback) callbacks[0]).getName();
        if (username == null || "".equals(username)) {
            username = Constantes.USUARIO_INTERNET;
        }

        password = ((PasswordCallback) callbacks[1]).getPassword();
        ((PasswordCallback) callbacks[1]).clearPassword();
    }
    */

    public void carregarDados() throws Exception {
        // Referente às informações do usuario autenticado.
        carregarInformacoesUsuario();

        // Quando o Login é com JWT, filtra apenas pelos GRUPOS definidos neste JWT, evita consulta desnecessária
        String[] idsGrupos = null;
        String[] idsAplicacoes = null;
        if (jwt != null) {
            idsGrupos = jwt.getClaim(MapaJWTClaims.GRUPOS.name).split(";");
            idsAplicacoes = jwt.getClaim(MapaJWTClaims.APLICACOES.name).split(";");
        }

        // Referente às aplicacoes que o usuario autenticado tem permissão
        carregarAplicacoesUsuario(idsAplicacoes);

        // Referente aos grupos que o usuário autenticado está vinculado
        carregarGruposUsuario(idsGrupos);

        // Referente aos módulos de acesso que o usuario autenticado tem permissão
        carregarModulosUsuario(idsGrupos);

        mediatorUser.registraUltimoLogin(idUsuario);
    }

    private void carregarModulosUsuario(String[] idsGrupos) throws Exception {
    	//TODO o que fazer com o modulo do usuario
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	Collection<SimpleGrantedAuthority> aut = new ArrayList<SimpleGrantedAuthority>();
        List<JAASRole> modulosUsuario = mediatorRole.getModulosUsuario(idUsuario, idsGrupos, authentication.getAuthorities() );
//        new ArrayList<SimpleGrantedAuthority>()
        for (JAASRole jaasRole : modulosUsuario) {
//            subject.getPrincipals().add(jaasRole);
        	aut.add( new SimpleGrantedAuthority(jaasRole.toString()) );
        }
    }

    private void carregarGruposUsuario(String[] idsGrupos) throws Exception {
    	//TODO O que fazer com o grupo do ususario
//        Set<JAASGroup> gruposUsuario = MediatorGroup.getGruposUsuario(idUsuario, idsGrupos, subject.getPrincipals());
//        for (JAASGroup jaasGroup : gruposUsuario) {
//            subject.getPrincipals().add(jaasGroup);
//        }
    }

    private void carregarAplicacoesUsuario(String[] idsAplicacoes) throws Exception {
    	//TODO o que fazer com as aplicações do usuario
//        Set<JAASApplication> apps = MediatorApplication.getAplicacoesUsuario(idUsuario, idsAplicacoes, autenticadoComLoginUnico, subject.getPrincipals());
//        for (JAASApplication jAASApplication : apps) {
//            subject.getPrincipals().add(jAASApplication);
//        }
    }

    private JAASPrincipal carregarInformacoesUsuario() {
//    	UserVO user = mediatorUser.getUserByCPF(login);
    			
        JAASPrincipal principal = new JAASPrincipal(username, idUsuario, documento, name, idPessoa, email);
        principal.setAtivo(ativo);
        principal.setUsuarioDeRede(Autenticador.isUsuarioDeRede(stAtivo));
        principal.setAutenticadoComCertificado(autenticadoComCertificado);
        principal.setAutenticadoComLoginUnico(autenticadoComLoginUnico);
        principal.setContextoAplicacaoAutenticacao(contextoAplicacao);
        principal.setFoto(MediatorFotoAD.FOTO_DEFAULT);

        if (jwt != null) {
            String idPessoaEstrangeira = jwt.getClaim(MapaJWTClaims.ID_PESSO_AESTRANGEIRA.name);
            principal.setUsuarioEstrangeiro(idPessoaEstrangeira != null);
        } else {
            try {
                principal.setUsuarioEstrangeiro(mediatorUser.isUsuarioEstrangeiro(idUsuario));
            } catch (Exception e) {
                LOGGER.error("Não foi possível veriricar se é um usuário estrangeiro. Por default, considera usuário externo nacional", e);
            }
        }

        // Removi a lógica de obter a foto no Login e transportei para o método JAASPrincipal.getFoto
        // Deste modo, o carregamento só será chamado quando for usado
        // obterFoto(principal);

//        subject.getPrincipals().add(principal);
        return principal;
    }

//    private void obterFoto(JAASPrincipal principal) {
//        new Thread(new FotoADThread(principal), "ObterFoto").start();
//    }

}
