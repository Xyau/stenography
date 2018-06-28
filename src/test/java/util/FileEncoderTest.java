package util;

import main.Encoder;
import main.FileEncoder;
import main.WrongSizeException;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class FileEncoderTest {
    FileEncoder fileEncoder = new FileEncoder();
    Encoder encoder = new Encoder();

    @Test
    public void testEncodeFile(){
        String orig = "./src/main/resources/bmps/penguin.bmp";
        String copy = "./src/main/resources/bmps/surprise";

        byte[] origFileBytes = fileEncoder.getFileAsBytes(orig);
        byte[] encodedFileBytes = fileEncoder.encodeSizeAndExtension(orig);

        try {
            File decodedFile = fileEncoder.decodeSizeAndExtension(encodedFileBytes,copy);

            byte[] recoveredFileBytes = fileEncoder.getFileAsBytes(decodedFile.getPath());
            assertThat(MessageDigest.getInstance("MD5").digest(recoveredFileBytes))
            .containsExactly(MessageDigest.getInstance("MD5").digest(origFileBytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e){
            fail("Exception");
        }
    }


    @Test
    public void testLadoWithReadme(){
        String origImagePath = "./src/main/resources/bmps/lado.bmp";
        String filePath = "./src/main/resources/bmps/README.txt";
        String alteredImagePath = "./src/main/resources/bmps/ladoWithReadme.bmp";
        String recoveredFilePath = "./src/main/resources/bmps/RecoveredReadme";

        testCustomLSB(origImagePath,filePath,alteredImagePath,recoveredFilePath,1);
    }

    @Test
    public void testLadoWithTPE(){
        String origImagePath = "./src/main/resources/bmps/lado.bmp";
        String filePath = "./src/main/resources/bmps/TPE.pdf";
        String alteredImagePath = "./src/main/resources/bmps/ladoTPE1.bmp";
        String recoveredFilePath = "./src/main/resources/bmps/recTPE1";

        testCustomLSB(origImagePath,filePath,alteredImagePath,recoveredFilePath,1);
    }

    @Test
    public void testLadoWithLSBE(){
        String origImagePath = "./src/main/resources/bmps/lado.bmp";
        String filePath = "./src/main/resources/bmps/itba.png";
        String alteredImagePath = "./src/main/resources/bmps/ladoITBA.bmp";
        String recoveredFilePath = "./src/main/resources/bmps/recITBA";

        testLSBE(origImagePath,filePath,alteredImagePath,recoveredFilePath);
    }

    public void testCustomLSB(String origImagePath, String filePath, String alteredImagePath, String recoveredFilePath, int bitsToUse){
        String[] splitExtension = filePath.split("\\.");
        String extension = splitExtension[splitExtension.length-1];
        BufferedImage origImage = ImageUtils.readImage(origImagePath);
        byte[] encodedFile = fileEncoder.encodeSizeAndExtension(filePath);
        BufferedImage alteredImg = encoder.encodeInImage(origImage,encodedFile,bitsToUse);
        ImageUtils.writeImage(alteredImg,alteredImagePath);

        BufferedImage recoveredAlteredImage = ImageUtils.readImage(alteredImagePath);
        byte[] recoveredEncodedFileBytes = encoder.decodeImage(recoveredAlteredImage,bitsToUse);

        try {
            fileEncoder.decodeSizeAndExtension(recoveredEncodedFileBytes,recoveredFilePath);
            byte[] recoveredFileBytes = fileEncoder.getFileAsBytes(recoveredFilePath+"."+extension);
            byte[] origFileBytes = fileEncoder.getFileAsBytes(filePath);
            assertThat(MessageDigest.getInstance("MD5").digest(recoveredFileBytes))
                    .containsExactly(MessageDigest.getInstance("MD5").digest(origFileBytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e){
            fail("exception");
        }
    }

    public void testLSBE(String origImagePath, String filePath, String alteredImagePath, String recoveredFilePath){
        String[] splitExtension = filePath.split("\\.");
        String extension = splitExtension[splitExtension.length-1];
        BufferedImage origImage = ImageUtils.readImage(origImagePath);
        byte[] encodedFile = fileEncoder.encodeSizeAndExtension(filePath);
        BufferedImage alteredImg = encoder.encodeInImageLSBE(origImage,encodedFile);
        ImageUtils.writeImage(alteredImg,alteredImagePath);

        BufferedImage recoveredAlteredImage = ImageUtils.readImage(alteredImagePath);
        byte[] recoveredEncodedFileBytes = encoder.decodeImageLSBE(recoveredAlteredImage);

        try {
            fileEncoder.decodeSizeAndExtension(recoveredEncodedFileBytes,recoveredFilePath);
            byte[] recoveredFileBytes = fileEncoder.getFileAsBytes(recoveredFilePath+"."+extension);
            byte[] origFileBytes = fileEncoder.getFileAsBytes(filePath);
            assertThat(MessageDigest.getInstance("MD5").digest(recoveredFileBytes))
                    .containsExactly(MessageDigest.getInstance("MD5").digest(origFileBytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e){
            fail("exception");
        }
    }

    @Test
    public void testLadoLSB1Decodes(){
        String ladoLSB1ImagePath =  "./src/main/resources/bmps/ladoLSB1.bmp";
        String recoveredLadoLSB1FilePath =  "./src/main/resources/bmps/ladoLSB1Recovered";
        String tpePath =  "./src/main/resources/bmps/TPE.pdf";
        int bitsToUse = 1;
        BufferedImage recoveredAlteredImage = ImageUtils.readImage(ladoLSB1ImagePath);
        byte[] recoveredEncodedFileBytes = encoder.decodeImage(recoveredAlteredImage,bitsToUse);

        try {
            fileEncoder.decodeSizeAndExtension(recoveredEncodedFileBytes,recoveredLadoLSB1FilePath);
            byte[] recoveredFileBytes = fileEncoder.getFileAsBytes(recoveredLadoLSB1FilePath+".pdf");
            byte[] origFileBytes = fileEncoder.getFileAsBytes(tpePath);
            assertThat(MessageDigest.getInstance("MD5").digest(recoveredFileBytes))
                    .containsExactly(MessageDigest.getInstance("MD5").digest(origFileBytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e){
            fail("exception");
        }
    }

    @Test
    public void testLadoLSB4Decodes(){
        String ladoLSB4ImagePath =  "./src/main/resources/bmps/ladoLSB4.bmp";
        String recoveredLadoLSB4FilePath =  "./src/main/resources/bmps/ladoLSB4Recovered";
        String tpePath =  "./src/main/resources/bmps/TPE.pdf";
        int bitsToUse = 4;
        BufferedImage recoveredAlteredImage = ImageUtils.readImage(ladoLSB4ImagePath);
        byte[] recoveredEncodedFileBytes = encoder.decodeImage(recoveredAlteredImage,bitsToUse);

        try {
            fileEncoder.decodeSizeAndExtension(recoveredEncodedFileBytes,recoveredLadoLSB4FilePath);
            byte[] recoveredFileBytes = fileEncoder.getFileAsBytes(recoveredLadoLSB4FilePath+".pdf");
            byte[] origFileBytes = fileEncoder.getFileAsBytes(tpePath);
            assertThat(MessageDigest.getInstance("MD5").digest(recoveredFileBytes))
                    .containsExactly(MessageDigest.getInstance("MD5").digest(origFileBytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e){
            fail("exception");
        }
    }

    @Test
    public void testLadoLSBEDecodes(){
        String ladoLSBEImagePath =  "./src/main/resources/bmps/ladoLSBE.bmp";
        String recoveredLadoLSBEFilePath =  "./src/main/resources/bmps/ladoLSBERecovered";
        String itbaImgPath =  "./src/main/resources/bmps/itba.png";

        BufferedImage recoveredAlteredImage = ImageUtils.readImage(ladoLSBEImagePath);
        byte[] recoveredEncodedFileBytes = encoder.decodeImageLSBE(recoveredAlteredImage);

        try {
            fileEncoder.decodeSizeAndExtension(recoveredEncodedFileBytes,recoveredLadoLSBEFilePath);
            byte[] recoveredFileBytes = fileEncoder.getFileAsBytes(recoveredLadoLSBEFilePath+".png");
            byte[] origFileBytes = fileEncoder.getFileAsBytes(itbaImgPath);
            assertThat(MessageDigest.getInstance("MD5").digest(recoveredFileBytes))
                    .containsExactly(MessageDigest.getInstance("MD5").digest(origFileBytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e){
            fail("exception");
        }
    }

    @Test
    public void testFileSizeEncode(){
        byte[] file = "12345678901234567890".getBytes();
        byte[] encodedFile = fileEncoder.encodeSize(file);
        byte[] recovered = new byte[0];
        try {
            recovered = fileEncoder.decodeSize(encodedFile);
        } catch (WrongSizeException e) {
            fail("exception");
        }
        assertThat(recovered).isEqualTo(file);

    }


    @Test
    public void viewDifferences(){
        BufferedImage good = ImageUtils.readImage("./src/main/resources/bmps/lado.bmp");
        BufferedImage mine = ImageUtils.readImage("./src/main/resources/bmps/ladoLSBE.bmp");

        System.out.println(good);
        System.out.println(mine);
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (int j = good.getHeight()-3; j < good.getHeight(); j++) {
            for (int i = 0; i < good.getWidth(); i++) {
//                sb.append(Integer.toBinaryString(good.getRGB(i,j)));
//                sb.append("\n");
//                sb.append(Integer.toBinaryString(mine.getRGB(i,j)));
//                sb.append("\n\n");
                sb.append(Integer.toBinaryString(good.getRGB(i,j))).append(" ");
                sb2.append(Integer.toBinaryString(mine.getRGB(i,j))).append(" ");
            }
            sb.append("\n");
            sb.append(sb2);
            sb2.setLength(0);
            sb.append("\n");
            sb.append("\n");
        }
        System.out.println(sb.toString());

        Utils.writeToFile(sb.toString(),"asda.txt");
    }

}
