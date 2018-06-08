package util;

import main.Encoder;
import main.FileEncoder;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

public class FileEncoderTest {
    FileEncoder fileEncoder = new FileEncoder();
    Encoder encoder = new Encoder();

    @Test
    public void testEncodeFile(){
        String orig = "./src/main/resources/bmps/penguin.bmp";
        String copy = "./src/main/resources/bmps/surprise";

        byte[] origFileBytes = fileEncoder.getFileAsBytes(orig);
        byte[] encodedFileBytes = fileEncoder.encodeFile(orig);

        File decodedFile = fileEncoder.decodeFile(encodedFileBytes,copy);

        byte[] recoveredFileBytes = fileEncoder.getFileAsBytes(decodedFile.getPath());
        try {
            assertThat(MessageDigest.getInstance("MD5").digest(recoveredFileBytes))
            .containsExactly(MessageDigest.getInstance("MD5").digest(origFileBytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLSB1(){
        String origPath = "./src/main/resources/bmps/ladoLSB1.bmp";
        String destPath = "./src/main/resources/bmps/hidden";

        BufferedImage origImg = ImageUtils.readImage(origPath);
        byte[] decodedBytes = encoder.decodeImage(origImg,1);
        File decoded = fileEncoder.decodeFile(decodedBytes,destPath);

    }

    @Test
    public void testCustomLSB1(){
        String origImagePath = "./src/main/resources/bmps/lado.bmp";
        String filePath = "./src/main/resources/bmps/README.txt";
        String alteredImagePath = "./src/main/resources/bmps/ladoWithReadme.bmp";
        String recoveredFilePath = "./src/main/resources/bmps/RecoveredReadme";

        BufferedImage origImage = ImageUtils.readImage(origImagePath);
        byte[] encodedFile = fileEncoder.encodeFile(filePath);
        BufferedImage alteredImg = encoder.encodeInImage(origImage,encodedFile,1);
        ImageUtils.writeImage(alteredImg,alteredImagePath);

        BufferedImage recoveredAlteredImage = ImageUtils.readImage(alteredImagePath);
        byte[] recoveredEncodedFileBytes = encoder.decodeImage(recoveredAlteredImage,1);
        fileEncoder.decodeFile(recoveredEncodedFileBytes,recoveredFilePath);
        byte[] recoveredFileBytes = fileEncoder.getFileAsBytes(recoveredFilePath+".txt");
        byte[] origFileBytes = fileEncoder.getFileAsBytes(filePath);


        try {
            assertThat(MessageDigest.getInstance("MD5").digest(recoveredFileBytes))
                    .containsExactly(MessageDigest.getInstance("MD5").digest(origFileBytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
