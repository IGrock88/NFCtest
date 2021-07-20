package ru.onlineservice.nfctest;

import android.app.PendingIntent;
import android.content.Intent;

import android.nfc.NfcAdapter;
import android.nfc.Tag;

import android.nfc.tech.MifareClassic;
import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;


import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    final static String TAG = "nfc_test";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//            MifareClassic mifareTag = MifareClassic.get(tag);
//
//            readByteFromCard(mifareTag);

            assert tag != null;

            NfcMifareClassicIO nfcMifareClassicIO = null;
            try {
                nfcMifareClassicIO = new NfcMifareClassicIO(tag);
                MifareBlock mifareBlock = new MifareBlock();
                mifareBlock.setSector(1);
                mifareBlock.setBlock(4);
                mifareBlock.setKeyA(MifareClassic.KEY_DEFAULT);
                mifareBlock.setKeyB(MifareClassic.KEY_DEFAULT);
                byte[] data = {16, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                mifareBlock.setData(nfcMifareClassicIO.hexStringToByteArray("20FBFFFFFFFFFFFFFFFFFFFFFFFFFFFF"));
                nfcMifareClassicIO.writeBlock(mifareBlock);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            byte[] payload = nfcMifareClassicIO.detectTagData(tag).getBytes();



        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        assert nfcAdapter != null;
        //nfcAdapter.enableForegroundDispatch(context,pendingIntent,
        //                                    intentFilterArray,
        //                                    techListsArray)
        nfcAdapter.enableForegroundDispatch(this,pendingIntent,null,null);
    }

    protected void onPause() {
        super.onPause();
        //Onpause stop listening
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialise NfcAdapter
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //If no NfcAdapter, display that the device has no NFC
        if (nfcAdapter == null){
            Toast.makeText(this,"NO NFC Capabilities",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        //Create a PendingIntent object so the Android system can
        //populate it with the details of the tag when it is scanned.
        //PendingIntent.getActivity(Context,requestcode(identifier for
        //                           intent),intent,int)
        pendingIntent = PendingIntent.getActivity(this,0,new Intent(this,this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
    }


}