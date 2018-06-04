package util;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import static org.assertj.core.api.Assertions.*;

public class UtilsTest {

    @Test
    public void test(){
        int msg = 14250;
        int payload = 3;

        int alteredMsg = Utils.encodeInLowestBit(msg,payload,2);
        Utils.print(msg);
        Utils.print(payload);
        Utils.print(alteredMsg);
        assertThat(alteredMsg).isEqualTo(14251);
        int recoveredPayload = Utils.decodeInLowestBit(alteredMsg,2);
        assertThat(recoveredPayload).isEqualTo(payload);
        Utils.print(recoveredPayload);
    }

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
