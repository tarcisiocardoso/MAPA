package com.example.securingweb.model;

/**
 * Classe representa um principal do tipo Grupo, seu uso ocorre no momento que o
 * usuário é autenticado, onde são armazenadas todos os grupo que o usuário tem
 * para uma determinada aplicacao , criando assim uma role para cada grupo, com
 * isso é possivel confrontar se o usuário tem o grupo de acesso para acessar
 * uma determinada permissão.
 * <p>
 * Atributos:
 * <p>
 * - id: Código identificador do grupo
 * - name: armazena o valor do identificador do grupo
 * - nomeGrupo: armazena o valor do nome do grupo
 *
 * @author francisco.menezes
 */
public class JAASGroup {
    private static final long serialVersionUID = 1L;

    private final Long id;
    private String nomeGrupo;
    private final String contextoAplicacao;

    /**
     * Cria um objeto {@link JAASGroup}.
     *
     * @param idGrupo
     * @param nomeGrupo
     */
    public JAASGroup(Long idGrupo, String nomeGrupo, String contextoAplicacao) {
        setName(String.valueOf(idGrupo));
        this.id = idGrupo;
        this.nomeGrupo = nomeGrupo;
        this.contextoAplicacao = contextoAplicacao;
    }

    public void setName(String nm) {
    	nomeGrupo = nm;
	}
    public String getName() { return nomeGrupo; }

	public byte[] getSignedData() {
        return getName().getBytes();
    }

    /**
     * Recupera o código identificador do grupo
     *
     * @return código identificador do grupo
     */
    public Long getIdGrupo() {
        return this.id;
    }

    /**
     * Recupera o nome do grupo
     *
     * @return nome do grupo
     */
    public String getNomeGrupo() {
        return this.nomeGrupo;
    }

    /**
     * Retorna o contexto da aplicação relacionado a esse grupo
     *
     * @return contexto da aplicação
     */
    public String getContextoAplicacao() {
        return contextoAplicacao;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.getName() == null) ? 0 : this.getName().hashCode());
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
        JAASGroup other = (JAASGroup) obj;
        if (this.getName() == null) {
            if (other.getName() != null) {
                return false;
            }
        } else if (!this.getName().equals(other.getName())) {
            return false;
        }
        return super.equals(obj);
    }
}
