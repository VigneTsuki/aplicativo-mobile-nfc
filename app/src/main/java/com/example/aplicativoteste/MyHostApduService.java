package com.example.aplicativoteste;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import java.nio.charset.StandardCharsets;

public class MyHostApduService extends HostApduService {
    public MyHostApduService() {
    }

    private static final String TAG = "MyHostApduService";
    // Máximo de 26 bytes
    private static final String MENSAGEM = "00000000001111111111111111";

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        if (commandApdu != null && commandApdu.length > 2) {
            // Check for APDU SELECT command (CLA=0x00, INS=0xA4, P1=0x04, P2=0x00)
            if (commandApdu[0] == (byte) 0x00 && commandApdu[1] == (byte) 0xA4 &&
                    commandApdu[2] == (byte) 0x04 && commandApdu[3] == (byte) 0x00) {

                // Prepare and send NDEF response with the URL
                return buildNdefResponse();
            }
        }
        return new byte[]{};
    }

    private byte[] buildNdefResponse() {
        byte[] ndefMessage = createNdefMessage(MENSAGEM);
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