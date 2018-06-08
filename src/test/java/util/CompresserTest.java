package util;

import main.Compresser;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CompresserTest {

    @Test
    public void testCompresser(){
        assertThat(Compresser.compressBits(new int[]{1,1},2)).startsWith(new byte[]{80});
        assertThat(Compresser.compressBits(new int[]{3,2},2)).startsWith((new byte[]{-32}));
        assertThat(Compresser.compressBits(new int[]{11,2},2)).startsWith((new byte[]{-32}));
        assertThat(Compresser.compressBits(new int[]{13,8},4)).startsWith((new byte[]{-40}));
    }

    @Test
    public void testGetMask(){
        assertThat(Compresser.getMask(1,0)).isEqualTo(1);
        assertThat(Compresser.getMask(2,0)).isEqualTo(3);
        assertThat(Compresser.getMask(1,1)).isEqualTo(256);
        assertThat(Compresser.getMask(2,1)).isEqualTo(768);
    }

    @Test
    public void testDecompressBits(){
        byte[] bytes = {2,3,-1};

        assertThat(Compresser.decompressBits(6,2,bytes)).isEqualTo(2);
        assertThat(Compresser.decompressBits(0,1,bytes)).isEqualTo(0);
        assertThat(Compresser.decompressBits(1,1,bytes)).isEqualTo(0);
        assertThat(Compresser.decompressBits(7,1,bytes)).isEqualTo(0);
        assertThat(Compresser.decompressBits(6,1,bytes)).isEqualTo(1);
        assertThat(Compresser.decompressBits(8,2,bytes)).isEqualTo(0);
        assertThat(Compresser.decompressBits(14,2,bytes)).isEqualTo(3);
    }

    @Test
    public void testGetByte(){
        int x = 255;

        assertThat(Compresser.getByte(x,0)).isEqualTo(255);
        assertThat(Compresser.getByte(x,1)).isEqualTo(0);
        assertThat(Compresser.getByte(x,2)).isEqualTo(0);
        assertThat(Compresser.getByte(x,3)).isEqualTo(0);

        x = x<<8;
        assertThat(Compresser.getByte(x,0)).isEqualTo(0);
        assertThat(Compresser.getByte(x,1)).isEqualTo(255);
        assertThat(Compresser.getByte(x,2)).isEqualTo(0);
        assertThat(Compresser.getByte(x,3)).isEqualTo(0);

        x = x<<8;
        assertThat(Compresser.getByte(x,0)).isEqualTo(0);
        assertThat(Compresser.getByte(x,1)).isEqualTo(0);
        assertThat(Compresser.getByte(x,2)).isEqualTo(255);
        assertThat(Compresser.getByte(x,3)).isEqualTo(0);

        x = x<<8;
        assertThat(Compresser.getByte(x,0)).isEqualTo(0);
        assertThat(Compresser.getByte(x,1)).isEqualTo(0);
        assertThat(Compresser.getByte(x,2)).isEqualTo(0);
        assertThat(Compresser.getByte(x,3)).isEqualTo(255);
    }

}
