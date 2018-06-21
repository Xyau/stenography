package main;

import util.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Go run the test FileEnconderTest::TestCustomLSB1 to see how to use the classes");

        String encrypted = Encryptor.encrypt("passpasspasspasspasspasspasspass", "vectorve", "Encrypted Message", "aes", "ecb");
        System.out.println(encrypted);

        String decrypted =
                Encryptor.decrypt("passpasspasspasspasspasspasspass", "vectorve", encrypted, "aes", "ecb");
        System.out.println(decrypted);
    }
}
