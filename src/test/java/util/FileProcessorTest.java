package util;

import main.Configuration;
import main.FileEncoder;
import main.FileProcessor;
import main.LSBType;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

public class FileProcessorTest {
    FileProcessor fileProcessor = new FileProcessor();
    FileEncoder fileEncoder = new FileEncoder();

    @Test
    public void testAES(){
        LSBType lsbType = LSBType.LSB1;
        String imagePath = "./src/main/resources/bmps/penguin.bmp";
        String inputFilePath = "./src/main/resources/bmps/itba.png";
        String outputImgPath = "./src/main/resources/bmps/penguinWithItba.bmp";
        String recoveredFilePath = "./src/main/resources/bmps/recoveredItba";
        Configuration.Algorithm algorithm = Configuration.Algorithm.DES;
        Configuration.Method method = Configuration.Method.ECB;
        String password = "hey";

        Configuration encriptConfiguration = buildEncriptConfiguration(lsbType,imagePath, inputFilePath,outputImgPath,
                algorithm,method,password);

        fileProcessor.processFile(encriptConfiguration);

        Configuration decriptConfiguration = buildDecriptConfiguration(lsbType,outputImgPath, recoveredFilePath,
                recoveredFilePath,algorithm,method,password);

        fileProcessor.processFile(decriptConfiguration);

        byte[] recoveredFileBytes = fileEncoder.getFileAsBytes(recoveredFilePath+".png");
        byte[] origFileBytes = fileEncoder.getFileAsBytes(inputFilePath);

        try {
            assertThat(MessageDigest.getInstance("MD5").digest(recoveredFileBytes))
                    .containsExactly(MessageDigest.getInstance("MD5").digest(origFileBytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private Configuration buildDecriptConfiguration(LSBType lsbType, String imagePath, String inputFile,
                                                    String outputFilePath, Configuration.Algorithm algorithm, Configuration.Method method,
                                                    String password){
        Configuration encriptConfig = buildEncriptConfiguration(lsbType,imagePath,inputFile, outputFilePath,algorithm,method,password);
        encriptConfig.setFunction(Configuration.Function.EXTRACT);
        return encriptConfig;
    }

    private Configuration buildEncriptConfiguration(LSBType lsbType, String imagePath, String inputFile,
                                             String outputFilePath, Configuration.Algorithm algorithm, Configuration.Method method,
                                             String password){
        Configuration configuration = new Configuration();
        configuration.setStenography(lsbType);
        configuration.setPassword(password);
        configuration.setFunction(Configuration.Function.EMBED);
        configuration.setAlgorithm(algorithm);
        configuration.setMethod(method);
        configuration.setImageFile(imagePath);
        configuration.setInputFile(inputFile);
        configuration.setOutputFile(outputFilePath);
        return configuration;
    }
}
