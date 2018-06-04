package util;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    public static BufferedImage createImage(int rgb, int height, int width){
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image.setRGB(i,j,rgb);
            }
        }
        return image;
    }

    public static boolean writeImage(BufferedImage image, String path){
        File out = new File(path);
        try {
            ImageIO.write(image,"bmp",out);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static BufferedImage readImage(String path){
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
