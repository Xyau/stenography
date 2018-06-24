package util;

import main.Encryptor;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.security.Security;

public class EncryptorTest {
    @Test
    public void encryptorTest(){
        String msgToEncrypt = "Encrypt This";
        String encrypted = Encryptor.encrypt("passpasspasspasspasspasspasspass", "vectorve",
                msgToEncrypt, "aes", "ecb");
        String decrypted =
                Encryptor.decrypt("passpasspasspasspasspasspasspass", "vectorve", encrypted,
                        "aes", "ecb");
        assertThat(decrypted).isEqualTo(msgToEncrypt);
    }
}
