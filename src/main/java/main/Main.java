package main;

import main.encryptors.NoChangeEncryptor;
import util.ImageUtils;
import util.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Main {
    public static void main(String[] args) {
        String alteredImagePath = "./src/main/resources/bmps/ladoTPE1.bmp";
        String recoveredFilePath = "./src/main/resources/bmps/recTPE1";
        Boolean encript = false;

        FileEncoder fileEncoder = new FileEncoder();
        Encoder encoder = new Encoder();
        if(encript){
            //we encode some info into a bmp
            String inImagePath = "./src/main/resources/bmps/lado.bmp";
            String filePath = "./src/main/resources/bmps/TPE.pdf";
            String outImagePath = "./src/main/resources/bmps/ladoWithTPE.bmp";

            BufferedImage origImage = ImageUtils.readImage(inImagePath);
            //Encode the file lenght and the extension into the file
            byte[] encodedFile = fileEncoder.encodeFile(filePath);
            BufferedImage alteredImg = encoder.encodeInImage(origImage,encodedFile,LSBType.LSB1);
            ImageUtils.writeImage(alteredImg,alteredImagePath);
        } else {

        }
    }
}
