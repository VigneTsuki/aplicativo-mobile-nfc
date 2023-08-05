package com.example.aplicativoteste.models;

public class LoginRequest {
    private String Login;
    private String Senha;

    public LoginRequest(String login, String senha) {
        Login = login;
        Senha = senha;
    }
}
