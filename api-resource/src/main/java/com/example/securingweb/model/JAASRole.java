package com.example.securingweb.model;


/**
 * Classe representa um principal do tipo Role, seu uso ocorre no momento
 * que o usuário é autenticado, onde são armazenadas todas as aplicações que o
 * usuário tem acesso, criando assim uma role para cada aplicação, com isso é
 * possivel confrontar se o usuário tem permissão para acessar uma determinada
 * aplicação.
 *
 * @author francisco.menezes
 */
public class JAASRole {

	private static final long serialVersionUID = 1L;

    private final Long id;
    private final String csModulo;
    private final boolean publico;
    private final boolean gravaSaida;
    private final String contextoAplicacao;

    private String name;
    
    
    /**
     * Cria um objeto {@link JAASRole}.
     *
     * @param name
     */
    public JAASRole(String name) {
        this(name, null, null, false, false, null);
    }

    /**
     * Cria um objeto {@link JAASRole}.
     *
     * @param name
     * @param idModulo
     * @param csModulo
     * @param publico
     * @param gravaSaida
     */
    public JAASRole(String name, Long idModulo, String csModulo, boolean publico, boolean gravaSaida, String contextoAplicacao) {
        setName(name);
        if (csModulo != null && csModulo.contains("WS")) {
            setName(csModulo + "[@]" + name);
        }
        this.id = idModulo;
        this.csModulo = csModulo;
        this.publico = publico;
        this.gravaSaida = gravaSaida;
        this.contextoAplicacao = contextoAplicacao;
    }

    /**
     * {@inheritDoc}
     */
    public byte[] getSignedData() {
        return getName().getBytes();
    }

    public Long getId() {
        return id;
    }

    public String getCsModulo() {
        return csModulo;
    }

    public boolean isPublico() {
        return publico;
    }

    public boolean isGravaSaida() {
        return gravaSaida;
    }

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
        JAASRole other = (JAASRole) obj;
        if (this.getName() == null) {
            if (other.getName() != null) {
                return false;
            }
        } else if (!this.getName().equalsIgnoreCase(other.getName())) {
            return false;
        }
        return true;
    }
    private void setName(String n) { this.name = n; }
    private String getName() { return name; }
}

