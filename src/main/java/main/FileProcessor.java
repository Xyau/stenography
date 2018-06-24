package main;

import util.ImageUtils;
import util.Utils;

import java.awt.image.BufferedImage;

public class FileProcessor {
    public void processFile(Configuration configuration){
        FileEncoder fileEncoder = new FileEncoder();
        Encoder encoder = new Encoder();
        Encryptor encryptor = new Encryptor();

        BufferedImage image = ImageUtils.readImage(configuration.getImageFile());
        String outputPath = configuration.getOutputFile();
        String pass = configuration.getPassword();
        String paddedPass = Utils.applyPaddle(pass,configuration.getAlgorithm());
        String initVectpor = "asda";
        String algorithm = configuration.getAlgorithm().toString();
        String method = configuration.getMethod().toString();

        switch (configuration.getFunction()){
            case EMBED:
                String inputFilePath = configuration.getInputFile();

                byte[] inputFilePlusExtensionAndSize = fileEncoder.encodeSizeAndExtension(inputFilePath);
                byte[] encyptedFile = Encryptor.encrypt(paddedPass,initVectpor, inputFilePlusExtensionAndSize,algorithm,method);
                byte[] encodedEncrypedFile = fileEncoder.encodeSize(encyptedFile);

                BufferedImage alteredImage = encoder.encodeInImage(image,encodedEncrypedFile,configuration.getLSBType());
                ImageUtils.writeImage(alteredImage,outputPath);
                break;
            case EXTRACT:
                byte[] extractedPayload = encoder.decodeImage(image,configuration.getLSBType());
                byte[] encrypedPayload = fileEncoder.decodeSize(extractedPayload);
                byte[] decrypedPayload = Encryptor.decrypt(paddedPass,initVectpor,encrypedPayload,algorithm,method);

                fileEncoder.decodeSizeAndExtension(decrypedPayload,outputPath);
                break;
        }

    }
}
