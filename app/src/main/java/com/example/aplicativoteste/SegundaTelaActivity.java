package com.example.aplicativoteste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SegundaTelaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segunda_tela);

        // Exibir a mensagem "Olá Mundo!" na segunda tela
        TextView textViewMensagem = findViewById(R.id.textViewMensagem);
        textViewMensagem.setText("Olá Mundo!");

        PackageManager pm = getPackageManager();
        boolean hceSupported = pm.hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION);
        if (hceSupported) {
            // O dispositivo suporta HCE
            Toast.makeText(this, "HCE é suportado no dispositivo.", Toast.LENGTH_SHORT).show();
        } else {
            // O dispositivo não suporta HCE
            Toast.makeText(this, "HCE não é suportado no dispositivo.", Toast.LENGTH_SHORT).show();
        }

    }
}