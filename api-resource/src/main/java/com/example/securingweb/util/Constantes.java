package com.example.securingweb.util;

/**
 * Constantes para o componente segurança
 */
public final class Constantes {

    /* Mensagens de erro*/
    public static final String MSG_ERRO_FECHAR_CONEXAO = "Erro ao tentar fechar conexao";
    public static final String MSG_ERRO_CALLBACKS = "Erro ao tentar receber informacao requisitada no provider Callbacks";
    public static final String MSG_ERRO_SENHA_INVALIDA = "Senha invalida.";
    public static final String MSG_ERRO_USUARIO_INVALIDO = "Usuario invalido.";
    public static final String MSG_ERRO_USUARIO_INEXISTENTE = "Usuario nao encontrado no banco de dados.";
    public static final String MSG_ERRO_CALLBACK_NAO_ESPECIFICADO = "Nenhum CallbackHandler especificado";
    public static final String MSG_ERRO_ENCRIPTAR_SENHA = "Erro ao tentar encriptar senha";
    public static final String MSG_ERRO_CONECTAR_AO_BANCO_DE_DADOS = "Erro ao tentar conectar ao banco de dados: ";
    public static final String MSG_ERRO_CARREGAR_DADOS = "Erro ao tentar carregar os dados de aplicacoes,"
            + " modulos e grupos do usuario: ";
    public static final String MSG_ERRO_CALLBACK_NAO_SUPORTADO = "Callback class not supported";
    public static final String MSG_ERRO_USUARIO_NAO_AUTENTICADO = "Usuario nao esta autenticado.";
    public static final String MSG_ERRO_AUTENTICACAO = "Erro ao tentar autenticar o usuario: ";
    public static final String MSG_ERRO_LOGOUT = "Erro ao efetuar logout: ";
    public static final String MSG_ERRO_OBTER_FOTO_AD = "Erro ao carregar foto do AD: ";

    /*Propriedades*/
    public static final String DS_SENHA = "ds_senha";
    public static final String ID_PESSOA_ESTRANGEIRA = "id_pessoa_estrangeira";
    public static final String ID_PESSOA_FISICA = "id_pessoa_fisica";
    public static final String ID_PESSOA_JURIDICA = "id_pessoa_juridica";
    public static final String STRING_S = "S";
    public static final String USUARIO_INTERNET = "usuariointernet";

    public static final String ST_ATIVO         = STRING_S;
    public static final String ST_REDE_INTERNO  = "A";
    public static final String ST_REDE_EXTERNO  = "E";


    /*Datasource*/
    public static final String DATASOURCE = "jdbc/SegurancaDS";
    
    private Constantes() {
        //Construtor privado
    }
}
