package com.example.securingweb.model;

import java.util.List;

public class UserVO {

    public Long idUsuario;
    public Long idPessoa;
    public String email;
    public String stAtivo;
    public String nome;
    public String documento;
    public String tipoPessoa;

    public String dsLogin;
    public String dsAssinatura;
    
    public List<JAASApplication> apps;

}
