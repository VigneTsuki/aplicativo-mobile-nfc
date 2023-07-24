package com.example.aplicativoteste;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SegundaTelaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda_tela);

        // Exibir a mensagem "Olá Mundo!" na segunda tela
        TextView textViewMensagem = findViewById(R.id.textViewMensagem);
        textViewMensagem.setText("Olá Mundo!");

    }
}