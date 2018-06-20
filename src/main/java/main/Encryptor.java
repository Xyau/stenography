package main;

import java.io.File;

public interface Encryptor {
    byte[] encrypt(byte[] bytesToEncript);


    byte[] decrypt(byte[] bytesToEncript);
}
