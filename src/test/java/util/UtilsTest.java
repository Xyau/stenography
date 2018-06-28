package util;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

public class UtilsTest {

    @Test
    public void testWriteImage(){
        String path = "./src/main/resources/bmps/t.bmp";
        BufferedImage bufferedImage = ImageUtils.createImage(4,1,1);
        ImageUtils.writeImage(bufferedImage,path);
        Utils.print(bufferedImage.getRGB(0,0));
        byte[] bytes = Utils.extractBytes(path);
        Utils.print(bytes);
    }
}
