package main.encryptors;

import main.Encryptor;

public class NoChangeEncryptor implements Encryptor{

    @Override
    public byte[] encrypt(byte[] bytesToEncript) {
        return bytesToEncript;
    }

    @Override
    public byte[] decrypt(byte[] bytesToEncript) {
        return bytesToEncript;
    }
}
