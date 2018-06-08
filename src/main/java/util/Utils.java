package util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Utils {

    public static int encodeInLowestBit(byte[] msg, int payload, int bitsToUse) {
        Byte.toString( msg[1]);
        return 1;
    }

    public static int encodeInLowestBit(int msg, int payload, int bitsToUse){
//        System.out.println(Integer.toBinaryString(msg));
//        System.out.println(Integer.toBinaryString(payload));
        int msgEmpty = msg & (Integer.MAX_VALUE<<bitsToUse);
//        System.out.println(Integer.toBinaryString(msgEmpty));
        //Sanity check for more payload than it should
        if((Integer.highestOneBit(payload)) > bitsToUse){
            throw new IllegalArgumentException("payload has more info than it should");
        }
        int msgPayload = msgEmpty | payload;
//        System.out.println(Integer.toBinaryString(msgPayload));
        return msgPayload;
    }

    public static int encodeInLowestBitPerByte(int msg, int payload, int bitsToUse){
//        System.out.println(Integer.toBinaryString(msg));
//        System.out.println(Integer.toBinaryString(payload));
        int msgEmpty = msg & (Integer.MAX_VALUE<<bitsToUse);
//        System.out.println(Integer.toBinaryString(msgEmpty));
        //Sanity check for more payload than it should
        if((Integer.highestOneBit(payload)) > bitsToUse){
            throw new IllegalArgumentException("payload has more info than it should");
        }
        int msgPayload = msgEmpty | payload;
//        System.out.println(Integer.toBinaryString(msgPayload));
        return msgPayload;
    }

    public static int decodeInLowestBit(int msg, int bitsToUse){
        int mask = (~(Integer.MAX_VALUE<<bitsToUse));
        int payload = mask & msg;
//        print(mask);
//        print(payload);
        return payload;
    }

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

    public static void encodeToImagePosition(BufferedImage image,int x, int y, int payload, int bitsToUse){
        int msg = image.getRGB(x,y);
        int alteredMsg = encodeInLowestBit(msg,payload,bitsToUse);
        image.setRGB(x,y,alteredMsg);
    }

   public static int decodeFromImagePosition(BufferedImage image,int x, int y, int bitsToUse){
        int msg = image.getRGB(x,y);
        int payload = decodeInLowestBit(msg,bitsToUse);
        return payload;
    }

    public static int byteToInt(byte b){
        return 0xFF&b;
    }

    public static void encodeInImage(BufferedImage image, byte[] payload, int bitsToUse){
        for (int i = 0; i < bitsToUse; i++) {
            encodeToImagePosition(image,0,i,1,1);
        }
        encodeToImagePosition(image,0,bitsToUse,0,1);
        int index = 0;
        for (int i = 0; i < image.getWidth() && index < payload.length; i++) {
            for (int j = bitsToUse+1; j < image.getHeight() && index < payload.length; j++) {
                encodeToImagePosition(image,i,j,new Byte(payload[index++]).intValue(),bitsToUse);
            }
        }
    }

    public static byte[] decodeInImage(BufferedImage image){
        int j = 0;
        int payload;
        int bitsToUse = 0;
        do{
            payload = decodeFromImagePosition(image,0,j++,1);
            bitsToUse++;
        } while (payload == 1);

        ByteBuffer b = ByteBuffer.allocate(image.getHeight()*image.getWidth()*4);

//        for (int i = 0; i < image.getWidth() && index < payload.length; i++) {
//            for (int k = j; j < image.getHeight() && index < payload.length; j++) {
//                encodeToImagePosition(image,i,j,new Byte(payload[index++]).intValue(),bitsToUse);
//            }
//        }
        return b.array();
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
}
