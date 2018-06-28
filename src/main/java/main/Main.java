package main;

import java.nio.BufferUnderflowException;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) {
        try{
            Configuration configuration = Configuration.parseOptions(args);

            FileProcessor fileProcessor = new FileProcessor();
            fileProcessor.processFile(configuration);
            if (configuration.getFunction().equals(Configuration.Function.EXTRACT)){
                System.out.println("Congratulations! Your file was decoded.");
            }else{
                System.out.println("Congratulations! Your file was encoded.");
            }
        }catch (NegativeArraySizeException e){
           System.out.println("Wrong passphrase, encryption algorithm or steganography method.");
           exit(1);
        }catch (BufferUnderflowException e){
            e.printStackTrace();
            System.out.println("Wrong passphrase, encryption algorithm or steganography method.");
            exit(1);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            exit(1);
        }

    }
}
