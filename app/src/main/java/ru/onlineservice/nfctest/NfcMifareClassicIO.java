package ru.onlineservice.nfctest;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class NfcMifareClassicIO {


    Tag tag;
    MifareClassic mifareTag;

    public NfcMifareClassicIO(Tag tag) throws IOException {
        this.tag = tag;
        connect();
    }


    public void connect() throws IOException {
        mifareTag = MifareClassic.get(tag);
        mifareTag.connect();
    }

    public void disconnect() throws IOException {
        mifareTag.close();
    }


    public String detectTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("ID (hex): ").append(toHex(id)).append('\n');
        sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n');
        sb.append("ID (dec): ").append(toDec(id)).append('\n');
        sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n');

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());

        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                String type = "Unknown";

                try {
//                    mifareTag.authenticateSectorWithKeyA(4, MifareClassic.KEY_DEFAULT);
//                    byte[] data = mifareTag.readBlock(mifareTag.sectorToBlock(4) + 3);
//                    Log.d("test", "data: " + Arrays.toString(data));
                    readAllByteFromCard(mifareTag);

//                    mifareTag.
//                    byte[] test = mifareTag.readBlock(1);
//                    Log.d("test", Arrays.toString(test));
                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                    }
                    sb.append("Mifare Classic type: ");
                    sb.append(type);
                    sb.append('\n');

                    sb.append("Mifare size: ");
                    sb.append(mifareTag.getSize() + " bytes");
                    sb.append('\n');

                    sb.append("Mifare sectors: ");
                    sb.append(mifareTag.getSectorCount());
                    sb.append('\n');

                    sb.append("Mifare blocks: ");
                    sb.append(mifareTag.getBlockCount());
                } catch (Exception e) {
                    sb.append("Mifare classic error: " + e.getMessage());
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }

            if (tech.equals(IsoDep.class.getName())){
                IsoDep isoDep = IsoDep.get(tag);

                try {
                    isoDep.connect();
                    Log.d("test", toHex(isoDep.getHiLayerResponse()));
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
        Log.v("test",sb.toString());
        return sb.toString();
    }



    public void writeBlocks(List<MifareBlock> blocks) throws Exception {
            int lastSector = 0;
            for (MifareBlock mifareBlock: blocks){
                if (mifareBlock.getSector() != lastSector){
                    if (!authSector(mifareBlock.getSector(), mifareBlock.getKeyA(), mifareBlock.getKeyB())){
                        throw new Exception("Авторизация не прошла");
                    }
                    lastSector = mifareBlock.getSector();
                }
                mifareTag.writeBlock(mifareBlock.getBlock(), mifareBlock.getData());
            }
    }

    public void writeBlock(MifareBlock block) throws Exception {
        if (isConnected()){
            if (!authSector(block.getSector(), block.getKeyA(), block.getKeyB())){
                throw new Exception("Авторизация не прошла");
            }
            mifareTag.writeBlock(block.getBlock(), block.getData());
        }
        else {
            throw new Exception("Не подключено");
        }

    }

    private boolean authSector(int sector, byte[] keyA, byte[] keyB) throws IOException {
        return mifareTag.authenticateSectorWithKeyA(sector, keyA) && mifareTag.authenticateSectorWithKeyB(sector, keyB);
    }

    private boolean isConnected() {
        return mifareTag.isConnected();
    }


    private void readAllByteFromCard(MifareClassic mifareTag) {
        try {
            while (!mifareTag.isConnected()){
                Log.d("test", "wait");
                mifareTag.connect();
            }

            Log.d("test", "go");
            mifareTag.authenticateSectorWithKeyA(15, MifareClassic.KEY_DEFAULT);
            byte[] data = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            mifareTag.writeBlock(60, data);

            int countSectors = mifareTag.getSectorCount();
            Log.d("test", String.valueOf(countSectors));
            for (int i = 0; i <= countSectors; i++){
                Log.d("test", "sector: " + i);
                boolean auth = mifareTag.authenticateSectorWithKeyA(i, MifareClassic.KEY_DEFAULT);

                Log.d("test", "auth: " + String.valueOf(auth));
                if (!auth) continue;

                int blockCountInSector = mifareTag.getBlockCountInSector(i);
                int firstBlock = mifareTag.sectorToBlock(i);

                for (int j = firstBlock; j <= firstBlock + blockCountInSector - 1; j++){
                    byte[] dataL = mifareTag.readBlock(j);

                    Log.d("test", "block - " + Integer.toHexString(j) + " data: " + toHex(dataL));
                }

            }


        }catch (Exception e){
            e.getStackTrace();

            if (e.getMessage() != null)
                Log.d("test", Objects.requireNonNull(e.getMessage()));
        }

    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            if (i > 0) {
                sb.append(" ");
            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];

        for(int i = 0; i < len; i+=2){
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }

        return data;
    }
}
