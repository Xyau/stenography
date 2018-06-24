package util;

import main.Configuration;
import main.Encryptor;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.in;

import java.security.Security;

public class EncryptorTest {
    @Test
    public void AES128Test(){
        String algoritm = Configuration.Algorithm.AES_128.toString();
        String method = Configuration.Method.CBC.toString();
        testEncryption("hey there", "123","1234567890123456","aes","cbc");
    }
    @Test
    public void AES196Test(){
        String algoritm = Utils.getMainAlgorithm(Configuration.Algorithm.AES_192);
        String method = Configuration.Method.CBC.toString();
        String password = "passs";
        String paddedPass = Utils.applyPaddle(password,Configuration.Algorithm.AES_192);
        testEncryption("hey there", paddedPass,
                "1234567890123456","aes","ecb");
    }

    public void testEncryption(String msg, String pasword, String initVector, String algorithm, String mode){
        byte[] msgToEncrypt = msg.getBytes();
        byte[] encrypted = Encryptor.encrypt(pasword, initVector, msgToEncrypt, algorithm, mode);
        byte[] decrypted = Encryptor.decrypt(pasword, initVector, encrypted, algorithm, mode);
        assertThat(decrypted).isEqualTo(msgToEncrypt);
    }


}
