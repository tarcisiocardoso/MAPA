package br.gov.mapa.seguranca.jaas;

/**
 * Classe representa um principal do tipo Principal, seu uso ocorre no momento que o usuário é
 * autenticado, onde são armazenadas as principais informações do usuário, com isso é possivel
 * compartilhar essas informações entre os contexto, caso haja em algum momento a necessidade de
 * validação de informações sobre o usuário autenticado.
 *
 * @author francisco.menezes
 */
public class JAASPrincipal {

	private static final long serialVersionUID = 1L;

    private Long id;
    private String cpf;
    private String nome;
    private Long idPessoaFisica;
    private String email;
    private String ip;
    private String userAgent;
    private String foto;
    private String contextoAplicacaoAutenticacao;
    private boolean ativo;
    private boolean usuarioDeRede;
    private boolean usuarioEstrangeiro;
    private boolean autenticadoComCertificado;
    private boolean autenticadoComLoginUnico;

    /**
     * Cria um objeto {@link JAASPrincipal}.
     *
     * @param name
     * @param id
     * @param cpf
     * @param nome
     * @param idPessoaFisica
     */
    public JAASPrincipal(String name, Long id, String cpf, String nome, Long idPessoaFisica) {
        setName(name);
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.idPessoaFisica = idPessoaFisica;
    }

    /**
     * Cria um objeto {@link JAASPrincipal}.
     *
     * @param name
     * @param id             Código identificador do usuário (ID_USUARIO)
     * @param cpf
     * @param nome
     * @param idPessoaFisica Código identificador da pessoa (ID_PESSOA)
     * @param email
     */
    public JAASPrincipal(String name, Long id, String cpf, String nome, Long idPessoaFisica, String email) {
        setName(name);
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.idPessoaFisica = idPessoaFisica;
        this.email = email;
    }

    /**
     * {@inheritDoc}
     */
    public byte[] getSignedData() {
        return getName().getBytes();
    }

    public Long getIdPessoaFisica() {
        return idPessoaFisica;
    }

    public void setIdPessoaFisica(Long idPessoaFisica) {
        this.idPessoaFisica = idPessoaFisica;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isUsuarioDeRede() {
        return usuarioDeRede;
    }

    public void setUsuarioDeRede(boolean usuarioDeRede) {
        this.usuarioDeRede = usuarioDeRede;
    }

    public boolean isAutenticadoComCertificado() {
        return autenticadoComCertificado;
    }

    public void setAutenticadoComCertificado(boolean autenticadoComCertificado) {
        this.autenticadoComCertificado = autenticadoComCertificado;
    }

    public boolean isUsuarioEstrangeiro() {
        return usuarioEstrangeiro;
    }

    public boolean isAutenticadoComLoginUnico() {
        return autenticadoComLoginUnico;
    }

    public void setAutenticadoComLoginUnico(boolean autenticadoComLoginUnico) {
        this.autenticadoComLoginUnico = autenticadoComLoginUnico;
    }

    public void setUsuarioEstrangeiro(boolean usuarioEstrangeiro) {
        this.usuarioEstrangeiro = usuarioEstrangeiro;
    }

    public String getFoto() {
        if (MediatorFotoAD.FOTO_DEFAULT.equals(foto) && this.isUsuarioDeRede() && !this.getName().endsWith(".ex")) {
            this.setFoto(MediatorFotoAD.obterFotoUsuario(this.getCpf(), this.getName()));
        }
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getContextoAplicacaoAutenticacao() {
        return contextoAplicacaoAutenticacao;
    }

    public void setContextoAplicacaoAutenticacao(String contextoAplicacaoAutenticacao) {
        this.contextoAplicacaoAutenticacao = contextoAplicacaoAutenticacao;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (getName() == null ? 0 : getName().hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        JAASPrincipal other = (JAASPrincipal) obj;
        if (getName() == null) {
            if (other.getName() != null) {
                return false;
            }
        } else if (!getName().equals(other.getName())) {
            return false;
        }
        return super.equals(obj);
    }
    
    public void setName(String n) { nome = n; }
    public String getName() { return nome; }
}
