package main;

import util.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Main {
    public static void main(String[] args) {
        System.out.println("asda");
        System.out.println(Integer.MAX_VALUE<<1);
        try {
            BufferedImage image = ImageIO.read(new File("./src/main/resources/bmps/t.bmp"));

            int height = image.getHeight();
            int width = image.getWidth();
            System.out.println("height: "+height);
            System.out.println("width: "+width);
            int x = Utils.encodeInLowestBit(123,4,2);
            Utils.print(x);

            boolean endedConfig = false;
            int bitsToUse = 1;
            ByteBuffer byteBuffer = ByteBuffer.allocate(height*width*4);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if(endedConfig){

                        int decode = Utils.decodeInLowestBit(image.getRGB(j,i),bitsToUse);
//                        byteBuffer.put()
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
