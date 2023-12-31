package com.example.aplicativoteste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aplicativoteste.models.LoginRequest;
import com.example.aplicativoteste.models.LoginResponse;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    static final String Url = "vigne.moe:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnCadastro = findViewById(R.id.btnCadastro);
        btnLogin.setOnClickListener(v -> Login());
        btnCadastro.setOnClickListener(v -> Cadastro());
    }

    public void Login(){

        String login = ((EditText) findViewById(R.id.editTextLogin)).getText().toString();

        if(login.equals("")){
            Toast.makeText(MainActivity.this,
                    "Preencha o campo Login",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String senha = ((EditText) findViewById(R.id.editTextSenha)).getText().toString();

        if(senha.equals("")){
            Toast.makeText(MainActivity.this,
                    "Preencha o campo Senha",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest = new LoginRequest(login, senha);

        OkHttpClient client = new OkHttpClient();
        String url = "http://" + Url + "/login";
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String requestBody = new Gson().toJson(loginRequest);
        RequestBody body = RequestBody.create(mediaType, requestBody);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                MainActivity.this.runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Ocorreu um erro de comunicação, por favor tente novamente mais tarde", Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    LoginResponse loginResponse = new Gson().fromJson(responseBody, LoginResponse.class);
                    MainActivity.this.runOnUiThread(() -> {
                        if(loginResponse.getSucesso()){

                            SharedPreferences sharedPreferences = getSharedPreferences("PresencaEscolar", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("CodigoAluno", login.trim());
                            editor.apply();

                            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                            intent.putExtra("codigoAluno", login.trim());
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, loginResponse.getMensagem(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    MainActivity.this.runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Dados de acesso incorretos.", Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }

    public void Cadastro(){
        Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
        startActivity(intent);
    }
}