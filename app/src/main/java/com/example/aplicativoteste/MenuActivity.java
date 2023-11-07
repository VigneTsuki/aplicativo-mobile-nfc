package com.example.aplicativoteste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicativoteste.models.UsuarioResponse;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MenuActivity extends AppCompatActivity {

    static final String Url = "vigne.moe:3000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        BuscarCadastro();
        TextView textViewMensagemNfc = findViewById(R.id.textViewMensagemNfc);

        PackageManager pm = getPackageManager();
        boolean hceSupported = pm.hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION);
        if (hceSupported) {
            textViewMensagemNfc.setText("O seu dispositivo está pronto para registrar presença!");
        } else {
            textViewMensagemNfc.setText("O seu dispositivo não possui NFC, " +
                    "então não poderá registrar presença automaticamente, por favor comunique seu professor.");
        }
    }

    public void BuscarCadastro() {
        TextView textViewCodigo = findViewById(R.id.textViewCodigo);
        TextView textViewNome = findViewById(R.id.textViewNome);
        TextView textViewEmail = findViewById(R.id.textViewEmail);
        TextView textViewTelefone = findViewById(R.id.textViewTelefone);

        OkHttpClient client = new OkHttpClient();

        String codigoDoAluno = getIntent().getStringExtra("codigoAluno");

        String url = "http://" + Url + "/aluno?codigo=" + codigoDoAluno;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                MenuActivity.this.runOnUiThread(() -> {
                    Toast.makeText(MenuActivity.this, "Ocorreu um erro de comunicação, por favor tente novamente mais tarde", Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    UsuarioResponse usuarioResponse = new Gson().fromJson(responseBody, UsuarioResponse.class);
                    MenuActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(usuarioResponse.getSucesso()){
                                textViewCodigo.setText(usuarioResponse.getCodigo());
                                textViewNome.setText(usuarioResponse.getNome());
                                textViewEmail.setText(usuarioResponse.getEmail());
                                textViewTelefone.setText(usuarioResponse.getTelefone());
                            } else {
                                Toast.makeText(MenuActivity.this, usuarioResponse.getMensagem(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else{
                    MenuActivity.this.runOnUiThread(() -> {
                        Toast.makeText(MenuActivity.this, "Tente novamente mais tarde.", Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }
}