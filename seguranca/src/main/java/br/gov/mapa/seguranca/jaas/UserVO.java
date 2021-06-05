package br.gov.mapa.seguranca.jaas;


public final class UserVO {

    private Long idUsuario;
    private Long idPessoa;
    private String email;
    private String stAtivo;
    private String nome;
    private String documento;
    private String tipoPessoa;
    private String dbSenha;
    private String dsLogin;
    private String dsAssinatura;

    /**
     * Cria um objeto {@link UserVO}.
     *
     */
    public UserVO() {
        this.dbSenha = "";
    }

    /**
     * Recupera o valor de idUsuario.
     *
     * @return idUsuario
     */
    public Long getIdUsuario() {
        return idUsuario;
    }

    /**
     * Atribui idUsuario.
     *
     * @param idUsuario o valor a ajustar em idUsuario
     */
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Recupera o valor de idPessoa.
     *
     * @return idPessoa
     */
    public Long getIdPessoa() {
        return idPessoa;
    }

    /**
     * Atribui idPessoa.
     *
     * @param idPessoa o valor a ajustar em idPessoa
     */
    public void setIdPessoa(Long idPessoa) {
        this.idPessoa = idPessoa;
    }

    /**
     * Recupera o valor de email.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Atribui email.
     *
     * @param email o valor a ajustar em email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Recupera o valor de stAtivo.
     *
     * @return stAtivo
     */
    public String getStAtivo() {
        return stAtivo;
    }

    /**
     * Atribui stAtivo.
     *
     * @param stAtivo o valor a ajustar em stAtivo
     */
    public void setStAtivo(String stAtivo) {
        this.stAtivo = stAtivo;
    }

    /**
     * Recupera o valor de nome.
     *
     * @return nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Atribui nome.
     *
     * @param nome o valor a ajustar em nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Recupera o valor de documento.
     *
     * @return documento
     */
    public String getDocumento() {
        return documento;
    }

    /**
     * Atribui documento.
     *
     * @param documento o valor a ajustar em documento
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /**
     * Recupera o valor de tipoPessoa.
     *
     * @return tipoPessoa
     */
    public String getTipoPessoa() {
        return tipoPessoa;
    }

    /**
     * Atribui tipoPessoa.
     *
     * @param tipoPessoa o valor a ajustar em tipoPessoa
     */
    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    /**
     * Recupera o valor de dbSenha.
     *
     * @return dbSenha
     */
    public String getDbSenha() {
        return dbSenha;
    }

    /**
     * Atribui dbSenha.
     *
     * @param dbSenha o valor a ajustar em dbSenha
     */
    public void setDbSenha(String dbSenha) {
        this.dbSenha = dbSenha;
    }

    /**
     * Recupera o valor de dsLogin.
     *
     * @return dsLogin
     */
    public String getDsLogin() {
        return dsLogin;
    }

    /**
     * Atribui dsLogin.
     *
     * @param dsLogin o valor a ajustar em dsLogin
     */
    public void setDsLogin(String dsLogin) {
        this.dsLogin = dsLogin;
    }

    /**
     * Recupera o valor de dsAssinatura.
     *
     * @return dsAssinatura
     */
    public String getDsAssinatura() {
        return dsAssinatura;
    }

    /**
     * Atribui dsAssinatura.
     *
     * @param dsAssinatura o valor a ajustar em dsAssinatura
     */
    public void setDsAssinatura(String dsAssinatura) {
        this.dsAssinatura = dsAssinatura;
    }
}
