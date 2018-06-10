package util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;

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
}
