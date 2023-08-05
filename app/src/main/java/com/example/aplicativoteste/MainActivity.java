package com.example.aplicativoteste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;
    static final String Url = "18.206.155.29";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }

    public void Login(){

        String login = ((EditText) findViewById(R.id.editTextLogin)).getText().toString();
        String senha = ((EditText) findViewById(R.id.editTextSenha)).getText().toString();

        OkHttpClient client = new OkHttpClient();
        String url = "http://" + Url + "/usuario";
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String requestBody = "{\"login\":\"" +login+ "\", \"senha\":\""+senha+"\"}";
        RequestBody body = RequestBody.create(mediaType, requestBody);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(responseBody.contains("true")){
                                Intent intent = new Intent(MainActivity.this, SegundaTelaActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "Login ou senha incorretos.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void FuncaoTesteGet() {
        TextView mTextViewResult = ((EditText) findViewById(R.id.editTextLogin));

        OkHttpClient client = new OkHttpClient();

        String url = "http://" + Url + "/weatherforecast";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextViewResult.setText(myResponse);
                        }
                    });
                }
            }
        });
    }
}