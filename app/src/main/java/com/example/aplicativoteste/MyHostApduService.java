package com.example.aplicativoteste;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import java.nio.charset.StandardCharsets;

public class MyHostApduService extends HostApduService {
    public MyHostApduService() {
    }

    private static final String TAG = "MyHostApduService";

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        if (commandApdu != null && commandApdu.length > 2) {
            if (commandApdu[0] == (byte) 0x00 && commandApdu[1] == (byte) 0xA4 &&
                    commandApdu[2] == (byte) 0x04 && commandApdu[3] == (byte) 0x00) {
                return buildNdefResponse();
            }
        }
        return new byte[]{};
    }

    private byte[] buildNdefResponse() {

        // Máximo de 26 bytes
        SharedPreferences sharedPreferences = getSharedPreferences("PresencaEscolar", Context.MODE_PRIVATE);
        String CodigoAluno = sharedPreferences.getString("CodigoAluno", "");

        if(CodigoAluno.equals(""))
            return null;

        byte[] ndefMessage = createNdefMessage(CodigoAluno);
        byte[] ndefResponse = new byte[ndefMessage.length + 2];
        System.arraycopy(ndefMessage, 0, ndefResponse, 0, ndefMessage.length);
        ndefResponse[ndefMessage.length] = (byte) 0x90; // SW1
        ndefResponse[ndefMessage.length + 1] = (byte) 0x00; // SW2
        return ndefResponse;
    }

    private byte[] createNdefMessage(String text) {
        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
        int ndefMessageLength = textBytes.length + 3; // Total length of ndefMessage
        if (ndefMessageLength > 255) {
            // NDEF message too long (max length is 255 bytes)
            // Handle this case if needed
            return null;
        }

        byte[] ndefMessage = new byte[ndefMessageLength];
        ndefMessage[0] = (byte) 0xD1; // NDEF Header
        ndefMessage[1] = (byte) 0x01; // Type Length
        ndefMessage[2] = (byte) (textBytes.length); // Payload Length
        System.arraycopy(textBytes, 0, ndefMessage, 3, textBytes.length);
        return ndefMessage;
    }

    @Override
    public void onDeactivated(int reason) {
        Log.d(TAG, "onDeactivated: " + reason);
    }
}