package main;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

public class Encryptor {
    public static byte[] encrypt(String password, String initVector, byte[] message, String algorithm,String mode) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes());
            SecretKeySpec skeySpec = new SecretKeySpec(password.getBytes(), algorithm);
            Cipher cipher = Cipher.getInstance(algorithm + "/" + mode + "/PKCS5PADDING");

            if (mode.toUpperCase().equals("CBC") || mode.toUpperCase().equals("CFB") || mode.toUpperCase().equals("OFB"))
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            else if (mode.toUpperCase().equals("ECB"))
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            else throw new IllegalStateException("Wrong encryption mode");

            byte[] encrypted = cipher.doFinal(message);
            encrypted = Base64.getEncoder().encode(encrypted);

            return encrypted;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(String password, String initVector, byte[] encrypted, String algorithm, String mode) {
        try {

            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes());
            SecretKeySpec skeySpec = new SecretKeySpec(password.getBytes(), algorithm);
            Cipher cipher = Cipher.getInstance(algorithm + "/" + mode + "/PKCS5PADDING");


            if (mode.toUpperCase().equals("CBC") || mode.toUpperCase().equals("CFB") || mode.toUpperCase().equals("OFB"))
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            else if (mode.toUpperCase().equals("ECB"))
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            else throw new IllegalStateException("Wrong encryption mode");

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return original;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
