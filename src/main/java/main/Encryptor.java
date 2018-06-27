package main;
import util.Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import java.security.MessageDigest;
import java.util.Base64;

import static util.Utils.EVP_BytesToKey;

public class Encryptor {
    public static byte[] encrypt(String pass, byte[] message, String algorithm,String mode, Integer blockSize) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm + "/" + mode + "/PKCS5PADDING");
            byte[][] keyAndIV = EVP_BytesToKey(blockSize, cipher.getBlockSize(), MessageDigest.getInstance("SHA-256"), pass.getBytes(), Utils.ITERATIONS);
            IvParameterSpec iv = new IvParameterSpec(keyAndIV[Utils.INDEX_IV]);
            SecretKeySpec skeySpec = new SecretKeySpec(keyAndIV[Utils.INDEX_KEY], algorithm);
            if (mode.toUpperCase().equals("CBC") || mode.toUpperCase().equals("CFB") || mode.toUpperCase().equals("OFB"))
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            else if (mode.toUpperCase().equals("ECB"))
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            else throw new IllegalStateException("Wrong encryption mode");

            byte[] encrypted = cipher.doFinal(message);

            return encrypted;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(String pass, byte[] encrypted, String algorithm, String mode, Integer blockSize) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm + "/" + mode + "/NoPadding");
            byte[][] keyAndIV = EVP_BytesToKey(blockSize, cipher.getBlockSize(), MessageDigest.getInstance("SHA-256"), pass.getBytes(), Utils.ITERATIONS);
            IvParameterSpec iv = new IvParameterSpec(keyAndIV[Utils.INDEX_IV]);
            SecretKeySpec skeySpec = new SecretKeySpec(keyAndIV[Utils.INDEX_KEY], algorithm);
            if (mode.toUpperCase().equals("CBC") || mode.toUpperCase().equals("CFB") || mode.toUpperCase().equals("OFB"))
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            else if (mode.toUpperCase().equals("ECB"))
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            else throw new IllegalStateException("Wrong encryption mode");

            byte[] original = cipher.doFinal(encrypted);

            return original;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
