package main;

import util.ImageUtils;
import util.Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.awt.image.BufferedImage;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class FileProcessor {
    public void processFile(Configuration configuration) throws NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, WrongSizeException {
        FileEncoder fileEncoder = new FileEncoder();
        Encoder encoder = new Encoder();

        BufferedImage image = ImageUtils.readImage(configuration.getImageFile());
        String outputPath = configuration.getOutputFile();
        String pass = configuration.getPassword();
        String algorithm = Utils.getMainAlgorithm(configuration.getAlgorithm());
        String method = configuration.getMethod().toString();
        Integer keySize = Utils.getKeySize(configuration.getAlgorithm());

        switch (configuration.getFunction()){
            case EMBED:
                String inputFilePath = configuration.getInputFile();
                byte[] inputFilePlusExtensionAndSize = fileEncoder.encodeSizeAndExtension(inputFilePath);
                if (configuration.getAlgorithm().equals(Configuration.Algorithm.NO_ENCRYPTION)) {
                    BufferedImage alteredImage = encoder.encodeInImage(image,inputFilePlusExtensionAndSize,configuration.getLSBType());
                    ImageUtils.writeImage(alteredImage,outputPath);
                    break;
                }
                byte[] encyptedFile = Encryptor.encrypt(pass, inputFilePlusExtensionAndSize,algorithm,method, keySize);
                byte[] encodedEncrypedFile = fileEncoder.encodeSize(encyptedFile);

                BufferedImage alteredImage = encoder.encodeInImage(image,encodedEncrypedFile,configuration.getLSBType());
                ImageUtils.writeImage(alteredImage,outputPath);
                break;
            case EXTRACT:
                byte[] extractedPayload = encoder.decodeImage(image,configuration.getLSBType());
                if (configuration.getAlgorithm().equals(Configuration.Algorithm.NO_ENCRYPTION)){
                    fileEncoder.decodeSizeAndExtension(extractedPayload,outputPath);
                    break;
                }
                byte[] encrypedPayload = fileEncoder.decodeSize(extractedPayload);
                byte[] decrypedPayload = Encryptor.decrypt(pass,encrypedPayload,algorithm,method, keySize);

                fileEncoder.decodeSizeAndExtension(decrypedPayload,outputPath);
                break;
        }

    }
}
