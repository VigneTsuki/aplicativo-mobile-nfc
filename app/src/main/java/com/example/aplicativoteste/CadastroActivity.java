package com.example.aplicativoteste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aplicativoteste.models.CadastroUsuarioRequest;
import com.example.aplicativoteste.models.CadastroUsuarioResponse;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CadastroActivity extends AppCompatActivity {

    static final String Url = "44.202.42.205:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Button btnVoltar = findViewById(R.id.btnVoltar);
        Button btnCadastrar = findViewById(R.id.btnCadastro);
        btnVoltar.setOnClickListener(v -> Voltar());
        btnCadastrar.setOnClickListener(v -> Cadastrar());
    }

    public void Voltar(){
        Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void Cadastrar(){
        String codigo = ((EditText) findViewById(R.id.editTextCodigo)).getText().toString();
        String email = ((EditText) findViewById(R.id.editTextEmail)).getText().toString();
        String telefone = ((EditText) findViewById(R.id.editTextTelefone)).getText().toString();
        String nome = ((EditText) findViewById(R.id.editTextNome)).getText().toString();
        String senha = ((EditText) findViewById(R.id.editTextSenha)).getText().toString();
        String reSenha = ((EditText) findViewById(R.id.editTextReSenha)).getText().toString();

        if(codigo.equals("")){
            Toast.makeText(CadastroActivity.this,
                    "Preencha o campo Codigo",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(email.equals("")){
            Toast.makeText(CadastroActivity.this,
                    "Preencha o campo Email",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(telefone.equals("")){
            Toast.makeText(CadastroActivity.this,
                    "Preencha o campo Telefone",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(nome.equals("")){
            Toast.makeText(CadastroActivity.this,
                    "Preencha o campo Nome",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(senha.equals("")){
            Toast.makeText(CadastroActivity.this,
                    "Preencha o campo Senha",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if(reSenha.equals("")){
            Toast.makeText(CadastroActivity.this,
                    "Preencha o campo Senha novamente",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        CadastroUsuarioRequest cadastroRequest = new CadastroUsuarioRequest(codigo, email, telefone,
                nome, senha, reSenha);

        OkHttpClient client = new OkHttpClient();
        String url = "http://" + Url + "/aluno";
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String requestBody = new Gson().toJson(cadastroRequest);
        RequestBody body = RequestBody.create(mediaType, requestBody);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(CadastroActivity.this,
                        "Ocorreu um erro no cadastro, por favor tente novamente mais tarde",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    CadastroUsuarioResponse cadastroResponse = new Gson().fromJson(responseBody, CadastroUsuarioResponse.class);
                    CadastroActivity.this.runOnUiThread(() -> {
                        if(cadastroResponse.getSucesso()){
                            Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(CadastroActivity.this, cadastroResponse.getMensagem(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else{
                    CadastroActivity.this.runOnUiThread(() -> {
                        Toast.makeText(CadastroActivity.this, "Erro no cadastro.", Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }
}