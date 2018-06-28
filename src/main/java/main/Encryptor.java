package main;
import util.Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static util.Utils.EVP_BytesToKey;

public class Encryptor {
    public static byte[] encrypt(String pass, byte[] message, String algorithm,String mode, Integer keySize) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
            Cipher cipher = Cipher.getInstance(algorithm + "/" + mode + "/PKCS5PADDING");
            byte[][] keyAndIV = EVP_BytesToKey(keySize, cipher.getBlockSize(), MessageDigest.getInstance("SHA-256"), pass.getBytes(), Utils.ITERATIONS);
            IvParameterSpec iv = new IvParameterSpec(keyAndIV[Utils.INDEX_IV]);
            SecretKeySpec skeySpec = new SecretKeySpec(keyAndIV[Utils.INDEX_KEY], algorithm);
            if (mode.toUpperCase().equals("CBC") || mode.toUpperCase().equals("CFB") || mode.toUpperCase().equals("OFB"))
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            else if (mode.toUpperCase().equals("ECB"))
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            else throw new IllegalStateException("Wrong encryption mode");

            byte[] encrypted = cipher.doFinal(message);

            return encrypted;
    }

    public static byte[] decrypt(String pass, byte[] encrypted, String algorithm, String mode, Integer keySize) throws InvalidAlgorithmParameterException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algorithm + "/" + mode + "/NoPadding");
        byte[][] keyAndIV = EVP_BytesToKey(keySize, cipher.getBlockSize(), MessageDigest.getInstance("SHA-256"), pass.getBytes(), Utils.ITERATIONS);
        IvParameterSpec iv = new IvParameterSpec(keyAndIV[Utils.INDEX_IV]);
        SecretKeySpec skeySpec = new SecretKeySpec(keyAndIV[Utils.INDEX_KEY], algorithm);
        if (mode.toUpperCase().equals("CBC") || mode.toUpperCase().equals("CFB") || mode.toUpperCase().equals("OFB"))
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        else if (mode.toUpperCase().equals("ECB"))
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        else throw new IllegalStateException("Wrong encryption mode");

        byte[] original = cipher.doFinal(encrypted);

        return original;
    }
}
