package com.example.aplicativoteste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obter os valores de login e senha
                String login = ((EditText) findViewById(R.id.editTextLogin)).getText().toString();
                String senha = ((EditText) findViewById(R.id.editTextSenha)).getText().toString();

                // Verificar o login e senha (exemplo simples, você deve implementar a lógica de autenticação)
                if (login.equals("usuario") && senha.equals("senha")) {
                    // Criar uma Intent para navegar para a segunda tela
                    Intent intent = new Intent(MainActivity.this, SegundaTelaActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}