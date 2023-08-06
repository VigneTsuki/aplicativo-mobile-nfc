package com.example.aplicativoteste.models;

public class UsuarioResponse {
    private Boolean sucesso;
    private String mensagem;
    private String codigo;
    private String email;
    private String telefone;
    private String nome;

    public Boolean getSucesso() {
        return sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getNome() {
        return nome;
    }
}
