package ru.onlineservice.nfctest;

import androidx.annotation.NonNull;


public class MifareBlock implements Cloneable {

    private byte[] data;
    private int sector;
    private int block;
    private byte[] keyA;
    private byte[] keyB;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getSector() {
        return sector;
    }

    public void setSector(int sector) {
        this.sector = sector;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public byte[] getKeyA() {
        return keyA;
    }

    public void setKeyA(byte[] keyA) {
        this.keyA = keyA;
    }

    public byte[] getKeyB() {
        return keyB;
    }

    public void setKeyB(byte[] keyB) {
        this.keyB = keyB;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
