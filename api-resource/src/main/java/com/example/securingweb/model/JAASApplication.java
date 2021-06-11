package com.example.securingweb.model;

/**
 * Classe representa um principal do tipo Aplicação, seu uso ocorre no momento
 * que o usuário é autenticado, onde são armazenadas todas as aplicações que o
 * usuário tem acesso, criando assim uma role para cada aplicação, com isso é
 * possivel confrontar se o usuário tem permissão para acessar uma determinada
 * aplicação.
 * 
 * @author francisco.menezes
 * 
 */
public class JAASApplication implements Comparable<JAASApplication> {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String versao;
    private String sigla;

    /**
     * Cria um objeto {@link JAASApplication}.
     *
     * @param sigla Sigla da aplicação
     * @param id Código identificador da aplicação
     * @param nome Nome da aplicação
     * @param versao Versão da aplicação
     * @param contexto Contexto da aplicação
     */
    public JAASApplication(String sigla, Long id, String nome, String versao, String contexto) {
        setName(contexto);
        this.id = id;
        this.nome = nome;
        this.sigla = sigla;
        if (versao == null || "".equals(versao)) {
            this.versao = "1.0";
        } else {
            this.versao = versao;
        }
    }

    /** {@inheritDoc} */
    public byte[] getSignedData() {
        return getName().getBytes();
    }

    /**
     * Recupera a versão da aplicação
     *
     * @return versão versão da aplicação
     */
    public String getVersao() {
        return this.versao;
    }

    /**
     * Atribui a versão da aplicação
     *
     * @param versao versão da aplicação
     */
    public void setVersao(String versao) {
        this.versao = versao;
    }

    /**
     * Recupera o código identificador da aplicação
     *
     * @return id Código identificador da aplicação
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Atribui o código identificador da aplicação
     *
     * @param id Código identificador da aplicação
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Recupera a descrição da aplicação
     *
     * @return descrição da aplicação
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Atribui a descrição da aplicação
     *
     * @param nome descrição da aplicação
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * Recupera a sigla da aplicação
     *
     * @return sigla da aplicação
     */
    public String getName() { //NOPMD - Overriding para editar o comentário do método
        return nome;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.getName() == null) ? 0 : this.getName().hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
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
        JAASApplication other = (JAASApplication) obj;
        if (this.getName() == null) {
            if (other.getName() != null) {
                return false;
            }
        } else if (!this.getName().equalsIgnoreCase(other.getName())) {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    public int compareTo(JAASApplication anotherJAASApplication) {
        return getName().compareTo(anotherJAASApplication.getName());
    }
    public void setName(String n) { this.nome = n;}

    /**
     * Recupera a sigla da aplicação.
     *
     * @return sigla da aplicação
     */
    public String getSigla() {
        return sigla;
    }
    
    /**
     * Atribui sigla.
     *
     * @param sigla o valor a ajustar em sigla
     */
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
    
    /**
     * Recupera o valor de contexto.
     *
     * @return contexto
     */
    public String getContexto() {
        return getName();
    }
}
