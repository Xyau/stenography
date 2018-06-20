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
        byte[] size = new byte[]{0b00000000,
                                 0b00000001,
                          (byte) 0b10101011,
                          (byte) 0b11100010,
        };
        assertThat(encoder.encodeRGB(4,size,4,0)).isEqualTo(0b10000000000000000);
        assertThat(encoder.encodeRGB(0,size,4,0)).isEqualTo(0);
        assertThat(encoder.encodeRGB(0,size,1,-1)).isEqualTo((int)0b11111111111111101111111011111110);
        assertThat(encoder.encodeRGB(3,size,1,-1)).isEqualTo((int)0b11111111111111101111111011111110);
        assertThat(encoder.encodeRGB(6,size,1,-1)).isEqualTo((int)0b11111111111111101111111011111110);
        assertThat(encoder.encodeRGB(9,size,1,-1)).isEqualTo((int)0b11111111111111101111111011111110);
        assertThat(encoder.encodeRGB(13,size,1,0)).isEqualTo(0b10000000000000000);
        assertThat(encoder.encodeRGB(14,size,1,0)).isEqualTo(0b10000000100000000);

        assertThat(encoder.encodeRGB(0,new byte[]{(byte) 0b10000000},1,0)).isEqualTo(0b1);
        assertThat(encoder.encodeRGB(0,new byte[]{(byte) 0b10000000},2,0)).isEqualTo(0b10);
        assertThat(encoder.encodeRGB(0,new byte[]{(byte) 0b10011000},2,0)).isEqualTo(0b000000100000000100000010);
        assertThat(encoder.encodeRGB(0,new byte[]{0b01100000},1,0)).isEqualTo(0b10000000100000000);
        assertThat(encoder.encodeRGB(0,new byte[]{-1},1,0)).isEqualTo(0b10000000100000001);
        assertThat(encoder.encodeRGB(0,new byte[]{0},1,-1)).isEqualTo((int)0b11111111111111101111111011111110);
    }

    @Test
    public void testDecodeRGB(){

        assertThat(encoder.decodeRGB(0b100000001,1)).isEqualTo(0b110);
        assertThat(encoder.decodeRGB(0b11111111111111101111111011111110,1)).isEqualTo(0b000);
        assertThat(encoder.decodeRGB(65793,1)).isEqualTo(7);
        assertThat(encoder.decodeRGB(~65793,1)).isEqualTo(0);

    }

    @Test
    public void testDecodeLSEB(){
        assertThat(encoder.decodeRGBLSE(0b11111111111101111111111011111111)).isEqualTo(2);
        assertThat(encoder.decodeRGBLSE(0b11111111111101111111111111111110)).isEqualTo(1);
        assertThat(encoder.decodeRGBLSE(0b11111111111111101111101111111111)).isEqualTo(2);
        assertThat(encoder.decodeRGBLSE(0b11111111111111111111111011111110)).isEqualTo(1);
        assertThat(encoder.decodeRGBLSE(0b11111111111111101111111011111110)).isEqualTo(0);
        assertThat(encoder.decodeRGBLSE(0b11111111111111101111111111111110)).isEqualTo(2);
        assertThat(encoder.decodeRGBLSE(0b11111111111111101111111011111111)).isEqualTo(4);
    }


    @Test
    public void testCount255or254Bytes(){
        assertThat(encoder.count254or255Bytes(0)).isEqualTo(0);
        assertThat(encoder.count254or255Bytes(0b00000000111111111111111111111111)).isEqualTo(3);
        assertThat(encoder.count254or255Bytes(0b11111111)).isEqualTo(1);
        assertThat(encoder.count254or255Bytes(0b11111110)).isEqualTo(1);
        assertThat(encoder.count254or255Bytes(0b111111110000000011111111)).isEqualTo(2);
        assertThat(encoder.count254or255Bytes(0b111111110000000011111111)).isEqualTo(2);
        assertThat(encoder.count254or255Bytes(0b000000001111111111111111)).isEqualTo(2);
        assertThat(encoder.count254or255Bytes(0b11111111000000001111111111111111)).isEqualTo(2);
        assertThat(encoder.count254or255Bytes(0b00000000000000001111111111111111)).isEqualTo(2);
    }

    // 00000000
    // 11111111
    @Test
    public void testEncodeRGBLSE(){
        assertThat(encoder.encodeRGBLSB(0,new byte[]{0},-1))
                .isEqualTo(0b11111111111111101111111011111110);

        assertThat(encoder.encodeRGBLSB(0,new byte[]{(byte) 0b10111111 },0b11111111111111111111011011111110))
                .isEqualTo(0b11111111111111101111011011111111);

        assertThat(encoder.encodeRGBLSB(0,new byte[]{(byte) 0b11111111 },0))
                .isEqualTo(0);

        assertThat(encoder.encodeRGBLSB(2,new byte[]{(byte) 0b00101111 },0b111111111111110111111110))
                .isEqualTo(0b111111101111110111111111);
    }

    @Test
    public void testEncodeDecodeLSE(){
        BufferedImage bufferedImage = ImageUtils.createImage(255,10,10);
        byte[] payload = "In the off ".getBytes();
        BufferedImage altered = encoder.encodeInImageLSBE(bufferedImage,payload);

        byte[] recovered = encoder.decodeImageLSBE(altered);
        assertThat(recovered).startsWith(payload);
    }
}
