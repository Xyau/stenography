package util;

import main.Configuration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;

public class Utils {
    /*
    https://stackoverflow.com/questions/3211156/how-to-convert-image-to-byte-array-in-java
     */
    public static byte[] extractBytes(String ImageName){
        // open image
        File imgPath = new File(ImageName);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(imgPath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // get DataBufferBytes from Raster
        WritableRaster raster = bufferedImage .getRaster();
        DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();

        return ( data.getData() );
    }

    public static void print(int msg){
        System.out.println(Integer.toBinaryString(msg));
    }


    public static int byteToInt(byte b){
        return 0xFF&b;
    }


    public static void print(byte b){
        System.out.println(Integer.toBinaryString(byteToInt(b)));
    }
    
    public static void print(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder(32);
        for (int i = 0; i < bytes.length; i++) {
            stringBuilder.append(bytes[i]).append(" ");
        }
        System.out.println(stringBuilder.toString());
    }

    public static void print(Object[] objects){
        StringBuilder stringBuilder = new StringBuilder(32);
        for (int i = 0; i < objects.length; i++) {
            stringBuilder.append(objects[i]).append(" ");
        }
    }

    public static void writeToFile(String s, String path){
        try (PrintWriter out = new PrintWriter(path)) {
            out.println(s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String applyPaddle(String key, Configuration.Algorithm algorithm){
        switch (algorithm){
            case AES_128:
                return applyPaddle(key,16);
            case AES_192:
                return applyPaddle(key,24);
            case AES_256:
                return applyPaddle(key,32);
            case DES:
                return applyPaddle(key,8);
            case NO_ENCRYPTION:
                return key;
        }
        throw new IllegalStateException();
    }

    private static String applyPaddle(String key, int finalSize) {
        int currentSize = key.length();
        if (currentSize > finalSize) {
            throw new IllegalStateException("Key too big");
        }
        for (int i=0; i<finalSize-currentSize; i++) {
            key = key + key.charAt(i%currentSize);
        }
        return key;
    }

    public static String getMainAlgorithm(Configuration.Algorithm algorithm) {
        switch (algorithm) {
            case AES_128:
                return "aes";
            case AES_192:
                return "aes";
            case AES_256:
                return "aes";
            case DES:
                return "des";
        }
        return "none";
    }

    public static Integer getKeySize(Configuration.Algorithm algorithm) {
        switch (algorithm) {
            case AES_128:
                return 16;
            case AES_192:
                return 24;
            case AES_256:
                return 32;
            case DES:
                return 8;
        }
        return 0;
    }

    public static final int INDEX_KEY = 0;
    public static final int INDEX_IV = 1;
    public static final int ITERATIONS = 1;

    public static byte[][] EVP_BytesToKey(int key_len, int iv_len, MessageDigest md, byte[] data, int count){
        byte[][] both = new byte[2][];
        byte[] key = new byte[key_len];
        int key_ix = 0;
        byte[] iv = new byte[iv_len];
        int iv_ix = 0;
        both[0] = key;
        both[1] = iv;
        byte[] md_buf = null;
        int nkey = key_len;
        int niv = iv_len;
        int i = 0;
        if (data == null) {
            return both;
        }
        int addmd = 0;
        for (;;) {
            md.reset();
            if (addmd++ > 0) {
                md.update(md_buf);
            }
            md.update(data);
            md_buf = md.digest();
            for (i = 1; i < count; i++) {
                md.reset();
                md.update(md_buf);
                md_buf = md.digest();
            }
            i = 0;
            if (nkey > 0) {
                for (;;) {
                    if (nkey == 0)
                        break;
                    if (i == md_buf.length)
                        break;
                    key[key_ix++] = md_buf[i];
                    nkey--;
                    i++;
                }
            }
            if (niv > 0 && i != md_buf.length) {
                for (;;) {
                    if (niv == 0)
                        break;
                    if (i == md_buf.length)
                        break;
                    iv[iv_ix++] = md_buf[i];
                    niv--;
                    i++;
                }
            }
            if (nkey == 0 && niv == 0) {
                break;
            }
        }
        for (i = 0; i < md_buf.length; i++) {
            md_buf[i] = 0;
        }
        return both;
    }

}
