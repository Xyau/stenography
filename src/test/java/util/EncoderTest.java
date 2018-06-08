package util;

import main.Compresser;
import main.Encoder;
import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EncoderTest {
    Encoder encoder = new Encoder();

    @Test
    public void testEncodeDecodeRGB1bit(){
        BufferedImage bufferedImage = ImageUtils.createImage(0,10,10);
        byte[] payload = "Hey it works again!".getBytes();
        BufferedImage altered = encoder.encodeInImage(bufferedImage,payload,1);

        byte[] recovered = encoder.decodeImage(altered,1);
        assertThat(recovered).startsWith(payload);
    }

    @Test
    public void testEncodeDecodeRGB2bit(){
        BufferedImage bufferedImage = ImageUtils.createImage(425566,10,7);
        byte[] payload = "Aleluya esto funciona lpm".getBytes();
        BufferedImage altered = encoder.encodeInImage(bufferedImage,payload,2);

        byte[] recovered = encoder.decodeImage(altered,2);
        assertThat(recovered).startsWith(payload);
    }

    @Test
    public void testEncodeRGB(){
        byte[] bytes = {-1};
        assertThat(encoder.encodeRGB(0,bytes,1,0)).isEqualTo(65793);
        bytes[0]=0;
        assertThat(encoder.encodeRGB(0,bytes,1,-1)).isEqualTo(~65793);
    }

    @Test
    public void testDecodeRGB(){
        assertThat(encoder.decodeRGB(65793,1)).isEqualTo(7);
        assertThat(encoder.decodeRGB(~65793,1)).isEqualTo(0);
    }
}
