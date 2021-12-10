package ru.onlineservice.nfctest;

import android.app.PendingIntent;
import android.content.Intent;

import android.nfc.NfcAdapter;
import android.nfc.Tag;

import android.nfc.tech.MifareClassic;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.util.Log;
import android.widget.Toast;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    final static String TAG = "nfc_test";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

//                MifareBlock mifareBlock = new MifareBlock();
//                int sector = 1;
//                int block = 3;
//                mifareBlock.setSector(1);
//                mifareBlock.setBlock(6);
//                mifareBlock.setKeyA(MifareClassic.KEY_DEFAULT);
//                mifareBlock.setKeyB(MifareClassic.KEY_DEFAULT);
//
//                MifareBlock result = nfcMifareClassicIO.readBlock(mifareBlock);
//                Log.d("info", Arrays.toString(result.getData()));
////                Charset fromCharset = StandardCharsets.UTF_8;
////                Charset toCharset = Charset.forName("cp866");
//                byte[] bytes = result.getData();
//
//
//
//                String resulttxt = nfcMifareClassicIO.toReversedHex(bytes).replace(" ", "");
//
////
////                byte[] result1 = Arrays.copyOfRange(bytes, 5, 10);
////
////                BigInteger bigInteger = new BigInteger(result1);
////
//                Log.d("info", resulttxt);
//                Log.d("info", String.format("%s-%s-%s-%s-%s", resulttxt.substring(8,16), resulttxt.substring(4,8), resulttxt.substring(0,4), resulttxt.substring(16,20), resulttxt.substring(20)));
//
                List<MifareBlock> blockList = getMifareBlocks(nfcMifareClassicIO);
                nfcMifareClassicIO.writeBlocks(blockList);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            byte[] payload = nfcMifareClassicIO.detectTagData(tag).getBytes();



        }
    }

    private List<MifareBlock> getMifareBlocks(NfcMifareClassicIO nfcMifareClassicIO) throws CloneNotSupportedException {
        List<MifareBlock> blockList = new ArrayList<>();


        MifareBlock mifareBlock = new MifareBlock();

//        //sector 1
        mifareBlock.setSector(1);
        mifareBlock.setBlock(4);
        mifareBlock.setKeyA(KEYS.KEY_A);
        mifareBlock.setKeyB(KEYS.KEY_B);

        mifareBlock.setData(nfcMifareClassicIO.hexStringToByteArray("000000000206627c96930dfbc0c4166d"));
        blockList.add((MifareBlock)mifareBlock.clone());


        mifareBlock.setSector(1);
        mifareBlock.setBlock(5);
        mifareBlock.setKeyA(KEYS.KEY_A);
        mifareBlock.setKeyB(KEYS.KEY_B);

        mifareBlock.setData(nfcMifareClassicIO.hexStringToByteArray("012dad53013461b50000000001525500"));
        blockList.add((MifareBlock) mifareBlock.clone());

        mifareBlock.setSector(1);
        mifareBlock.setBlock(6);
        mifareBlock.setKeyA(KEYS.KEY_A);
        mifareBlock.setKeyB(KEYS.KEY_B);

        mifareBlock.setData(nfcMifareClassicIO.hexStringToByteArray("4b31489e804c5710902eeaeafffd593d"));
        blockList.add((MifareBlock)mifareBlock.clone());

        //trail
//        mifareBlock.setSector(1);
//        mifareBlock.setBlock(7);
//        mifareBlock.setKeyA(KEYS.KEY_A);
//        mifareBlock.setKeyB(KEYS.KEY_B);
//
//        mifareBlock.setData(nfcMifareClassicIO.hexStringToByteArray("ffffffffffff70f0f869fffffffffff0"));
//        blockList.add((MifareBlock)mifareBlock.clone());

//        mifareBlock.setSector(15);
//        mifareBlock.setBlock(62);
//        mifareBlock.setKeyA(KEYS.KEY_A);
//        mifareBlock.setKeyB(KEYS.KEY_B);
//
//        mifareBlock.setData(nfcMifareClassicIO.hexStringToByteArray("ffffffffffffffffffffffffffffffff"));
//        blockList.add((MifareBlock)mifareBlock.clone());
//
//        mifareBlock.setSector(15);
//        mifareBlock.setBlock(63);
//        mifareBlock.setKeyA(KEYS.KEY_A);
//        mifareBlock.setKeyB(KEYS.KEY_B);
//
//        mifareBlock.setData(nfcMifareClassicIO.hexStringToByteArray("0fffffffffff00f87f690fffffffffff"));
//        blockList.add((MifareBlock)mifareBlock.clone());

//        //sector 2
        mifareBlock.setSector(2);
        mifareBlock.setBlock(8);
        mifareBlock.setKeyA(KEYS.KEY_A);
        mifareBlock.setKeyB(KEYS.KEY_B);

        mifareBlock.setData(nfcMifareClassicIO.hexStringToByteArray("00003a65000000020000000100000209"));
        blockList.add((MifareBlock)mifareBlock.clone());

        mifareBlock.setSector(2);
        mifareBlock.setBlock(9);
        mifareBlock.setKeyA(KEYS.KEY_A);
        mifareBlock.setKeyB(KEYS.KEY_B);

        mifareBlock.setData(nfcMifareClassicIO.hexStringToByteArray("b655d8db86bc3996a3caea4f0b1ff567"));
        blockList.add((MifareBlock)mifareBlock.clone());

        mifareBlock.setSector(2);
        mifareBlock.setBlock(10);
        mifareBlock.setKeyA(KEYS.KEY_A);
        mifareBlock.setKeyB(KEYS.KEY_B);

        mifareBlock.setData(nfcMifareClassicIO.hexStringToByteArray("01000000000000000000000000000000"));
        blockList.add((MifareBlock)mifareBlock.clone());

        //trail
//        mifareBlock.setSector(2);
//        mifareBlock.setBlock(11);
//        mifareBlock.setKeyA(KEYS.KEY_A);
//        mifareBlock.setKeyB(KEYS.KEY_B);
//
//        mifareBlock.setData(nfcMifareClassicIO.hexStringToByteArray("ffffffffffff70f0f869fffffffffff0"));
//        blockList.add((MifareBlock)mifareBlock.clone());

        //sector 3
        mifareBlock.setSector(3);
        mifareBlock.setBlock(12);
        mifareBlock.setKeyA(KEYS.KEY_A);
        mifareBlock.setKeyB(KEYS.KEY_B);

        mifareBlock.setData(nfcMifareClassicIO.hexStringToByteArray("90ebaca0e0a5adaaae00000000000000"));
        blockList.add((MifareBlock)mifareBlock.clone());

        mifareBlock.setSector(3);
        mifareBlock.setBlock(13);
        mifareBlock.setKeyA(KEYS.KEY_A);
        mifareBlock.setKeyB(KEYS.KEY_B);

        mifareBlock.setData(nfcMifareClassicIO.hexStringToByteArray("00000000000000000000000000000000"));
        blockList.add((MifareBlock)mifareBlock.clone());

        mifareBlock.setSector(3);
        mifareBlock.setBlock(14);
        mifareBlock.setKeyA(KEYS.KEY_A);
        mifareBlock.setKeyB(KEYS.KEY_B);

        mifareBlock.setData(nfcMifareClassicIO.hexStringToByteArray("00000000000000000000000000000000"));
        blockList.add((MifareBlock)mifareBlock.clone());

        //trail
//        mifareBlock.setSector(3);
//        mifareBlock.setBlock(15);
//        mifareBlock.setKeyA(KEYS.KEY_A);
//        mifareBlock.setKeyB(KEYS.KEY_B);
//
//        mifareBlock.setData(nfcMifareClassicIO.hexStringToByteArray("ffffffffffff70f0f869fffffffffff0"));
//        blockList.add((MifareBlock)mifareBlock.clone());

        //sector 5
        mifareBlock.setSector(5);
        mifareBlock.setBlock(20);
        mifareBlock.setKeyA(KEYS.KEY_A);
        mifareBlock.setKeyB(KEYS.KEY_B);

        mifareBlock.setData(nfcMifareClassicIO.hexStringToByteArray("88a3aee0ec0000000000000000000000"));
        blockList.add((MifareBlock)mifareBlock.clone());

        //trail
//        mifareBlock.setSector(5);
//        mifareBlock.setBlock(23);
//        mifareBlock.setKeyA(KEYS.KEY_A);
//        mifareBlock.setKeyB(KEYS.KEY_B);
//
//        mifareBlock.setData(nfcMifareClassicIO.hexStringToByteArray("ffffffffffff70f0f869fffffffffff0"));
//        blockList.add((MifareBlock)mifareBlock.clone());

        //sector 7
        mifareBlock.setSector(7);
        mifareBlock.setBlock(28);
        mifareBlock.setKeyA(KEYS.KEY_A);
        mifareBlock.setKeyB(KEYS.KEY_B);

        mifareBlock.setData(nfcMifareClassicIO.hexStringToByteArray("80aba5aae1a0ada4e0aea2a8e7000000"));
        blockList.add((MifareBlock)mifareBlock.clone());
        //trail
//        mifareBlock.setSector(7);
//        mifareBlock.setBlock(31);
//        mifareBlock.setKeyA(KEYS.KEY_A);
//        mifareBlock.setKeyB(KEYS.KEY_B);
//
//        mifareBlock.setData(nfcMifareClassicIO.hexStringToByteArray("ffffffffffff70f0f869fffffffffff0"));
//        blockList.add((MifareBlock)mifareBlock.clone());
        return blockList;
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