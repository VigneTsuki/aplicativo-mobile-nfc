package com.example.aplicativoteste.models;

public class CadastroUsuarioRequest {
    private String Codigo;
    private String Email;
    private String Telefone;
    private String Nome;
    private String Senha;
    private String ReSenha;

    public CadastroUsuarioRequest(String codigo, String email, String telefone, String nome,
                                  String senha, String reSenha) {
        Codigo = codigo;
        Email = email;
        Telefone = telefone;
        Nome = nome;
        Senha = senha;
        ReSenha = reSenha;
    }
}
