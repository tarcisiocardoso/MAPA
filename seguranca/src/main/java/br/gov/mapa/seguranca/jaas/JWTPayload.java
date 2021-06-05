package br.gov.mapa.seguranca.jaas;

import org.json.JSONObject;
import sun.misc.BASE64Decoder;

/**
 * {
 * "idUsuario": "913539",
 * "Nome": "alex.rodin",
 * "email": "alex.rodin@engesoftware.com.br",
 * "idPessoa": "65515342",
 * "nome": "Alex Rodin de Sousa e Silva",
 * "documento": "01064208100",
 * "IdJuridica": "null",
 * "RazaoSocial": "null",
 * "CNPJ": "null",
 * "IDPessoaEstrangeira": "null"
 * "NomePessoaEstrangeira": "null",
 * "Identificacao": "null",
 * "ativo": "A",
 * "Aplicacoes": "10554;1",
 * "Grupos": "15812;20",
 * }
 */
enum MapaJWTClaims {

    RAZAO_SOCIAL("RazaoSocial"),
    ATIVO("ativo"),
    IDENTIFICACAO("Identificacao"),
    ID_USUARIO("idUsuario"),
    DOCUMENTO("documento"),
    NOME_PESSOA("nome"),
    NOME_USUARIO("Nome"),
    ID_PESSOA("idPessoa"),
    ID_PESSOA_JURIDICA("IdJuridica"),
    CNPJ("CNPJ"),
    EMAIL("email"),
    ID_PESSO_AESTRANGEIRA("IDPessoaEstrangeira"),
    NOME_PESSOA_ESTRANGEIRA("NomePessoaEstrangeira"),
    APLICACOES("Aplicacoes"),
    GRUPOS("Grupos");

    public final String name;

    MapaJWTClaims(String name) {
        this.name = name;
    }
}

/**
 * Inteface para facilitar o acesso aos dados do Payload
 */
public class JWTPayload {

    private final JSONObject json;

    JWTPayload(String token) throws Exception {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new Exception("Token JWT inválido");
        }
        String content = parts[1].trim();
        if (content.isEmpty()) {
            throw new Exception("Token JWT inválido");
        }
        String value = new String(new BASE64Decoder().decodeBuffer(content));
        byte[] bytes = new BASE64Decoder().decodeBuffer(content);

        System.out.println(value);
        json = new JSONObject(value);
    }

    public String getClaim(String key) {
        if (!json.has(key)) {
            return null;
        }
        return this.ignoreNullString(json.getString(key));
    }

    /**
     * O WSO2 entrega a string "null" quando o campo é vazio
     *
     * @param value
     * @return
     */
    private String ignoreNullString(String value) {
        if ("null".equals(value)) {
            value = null;
        }
        return value;
    }
}
