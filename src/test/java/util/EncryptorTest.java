package util;

import main.Configuration;
import main.Encryptor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;

public class EncryptorTest {
    @Test
    public void AES128Test(){
        String algoritm = Configuration.Algorithm.AES_128.toString();
        String method = Configuration.Method.CBC.toString();
        testEncryption("hey there", "123","aes","cbc", 16);
    }
    @Test
    public void AES196Test(){
        String algoritm = Utils.getMainAlgorithm(Configuration.Algorithm.AES_192);
        String method = Configuration.Method.CBC.toString();
        String password = "passs";
//        String paddedPass = Utils.applyPaddle(password,Configuration.Algorithm.AES_192);
        testEncryption("hey there", password,"aes","ecb", 24);
    }

    public void testEncryption(String msg, String pasword, String algorithm, String mode, Integer keySize){
        byte[] msgToEncrypt = msg.getBytes();
        byte[] encrypted = new byte[0];
        try {
            encrypted = Encryptor.encrypt(pasword, msgToEncrypt, algorithm, mode, keySize);
            byte[] decrypted = Encryptor.decrypt(pasword, encrypted, algorithm, mode, keySize);
            assertThat(decrypted).isEqualTo(msgToEncrypt);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception");
        }
    }


}
