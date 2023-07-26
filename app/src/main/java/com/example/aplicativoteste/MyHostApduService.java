package com.example.aplicativoteste;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import java.nio.charset.Charset;

public class MyHostApduService extends HostApduService {
    public MyHostApduService() {
    }

    private static final String TAG = "MyHostApduService";
    private static final String SAMPLE_URL = "112608";

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        if (commandApdu != null && commandApdu.length > 2) {
            // Check for APDU SELECT command (CLA=0x00, INS=0xA4, P1=0x04, P2=0x00)
            if (commandApdu[0] == (byte) 0x00 && commandApdu[1] == (byte) 0xA4 &&
                    commandApdu[2] == (byte) 0x04 && commandApdu[3] == (byte) 0x00) {

                // Prepare and send NDEF response with the URL
                byte[] ndefResponse = buildNdefResponse();
                return ndefResponse;
            }
        }
        return new byte[]{};
    }

    private byte[] buildNdefResponse() {
        byte[] ndefMessage = createNdefMessage(SAMPLE_URL);
        byte[] ndefResponse = new byte[ndefMessage.length + 2];
        System.arraycopy(ndefMessage, 0, ndefResponse, 0, ndefMessage.length);
        ndefResponse[ndefMessage.length] = (byte) 0x90; // SW1
        ndefResponse[ndefMessage.length + 1] = (byte) 0x00; // SW2
        return ndefResponse;
    }

    private byte[] createNdefMessage(String text) {
        byte[] textBytes = text.getBytes(Charset.forName("UTF-8"));
        int ndefMessageLength = textBytes.length + 7; // Total length of ndefMessage
        if (ndefMessageLength > 255) {
            // NDEF message too long (max length is 255 bytes)
            // Handle this case if needed
            return null;
        }

        byte[] ndefMessage = new byte[ndefMessageLength];
        ndefMessage[0] = (byte) 0xD1; // NDEF Header
        ndefMessage[1] = (byte) 0x01; // Type Length
        ndefMessage[2] = (byte) (textBytes.length + 3); // Payload Length
        ndefMessage[3] = (byte) 0x54; // Record Type: Text (T)
        ndefMessage[4] = (byte) 0x02; // Language Code Length (en)
        ndefMessage[5] = (byte) 'e'; // Language Code: 'e' (en)
        ndefMessage[6] = (byte) 'n'; // Language Code: 'n' (en)
        System.arraycopy(textBytes, 0, ndefMessage, 7, textBytes.length);
        return ndefMessage;
    }

    @Override
    public void onDeactivated(int reason) {
        Log.d(TAG, "onDeactivated: " + reason);
    }
}