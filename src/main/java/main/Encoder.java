package main;

import util.ImageUtils;
import util.Utils;

import java.awt.image.BufferedImage;

import static main.Compresser.decompressBits;
import static main.Compresser.getMask;

public class Encoder {

    /**
     * Returns the payload with added bytes to reach a payload size multiple of 3, so when
     * encoding, no bits are missing or extra
     */
    public byte[] padPayload(byte[] payload){
        if(payload.length%3==0){
            return payload;
        }
        byte[] paddedPayload = new byte[payload.length+(3-(payload.length%3))];
        System.arraycopy(payload,0,paddedPayload,0,payload.length);
        return paddedPayload;
    }

    public int offsetToPixelIndex(int offset, int bitsToUse){
        return offset/(3*bitsToUse);
    }

    public BufferedImage encodeInImage(BufferedImage image, byte[] payload, int bitsToUse){
        byte[] paddedPayload = padPayload(payload);
        int maxOffset = paddedPayload.length*8;

        BufferedImage alteredImage = ImageUtils.deepCopy(image);

        if(image.getWidth()*image.getHeight()*3*bitsToUse < paddedPayload.length*8){
            throw new IllegalArgumentException("the image is too small for the payload");
        }

        for (int offset = 0; offset < maxOffset;) {
            int x = offsetToPixelIndex(offset,bitsToUse)%image.getWidth();
            int y =image.getHeight()-1 - offsetToPixelIndex(offset,bitsToUse)/image.getWidth();

            int origRGB = image.getRGB(x,y);
//            Utils.print(origRGB);
            int alteredRGB = encodeRGB(offset,paddedPayload,bitsToUse,origRGB);
//            Utils.print(alteredRGB);

            alteredImage.setRGB(x,y,alteredRGB);
            offset+=3*bitsToUse;
        }

        return alteredImage;
    }

    public byte[] decodeImage(BufferedImage image, int bitsToUse){
        int[] decompressedPayload = new int[image.getWidth()*image.getHeight()];

        int index =0;
        for (int j = image.getHeight()-1; j >= 0; j--) {
            for (int i = 0; i < image.getWidth(); i++) {
                int rgb = image.getRGB(i,j);
                int decoded = decodeRGB(rgb,bitsToUse);
                decompressedPayload[index]= decoded;
                index++;
            }
        }
        return Compresser.compressBits(decompressedPayload,bitsToUse*3);
    }

    public int indexToOffset(int index, int bitsToUse){
        return index*3*bitsToUse;
    }
    /**
     * This encodes some bits of the payload into an int, without changing the original too much
     */
    public static boolean LEFT_TO_RIGHT = false;
    public int encodeRGB(int offset, byte[] payload, int bitsToUse, int originalRGB){
        int alteredRGB = originalRGB;
        for (int i = 0; i < 3; i++) {
            alteredRGB = alteredRGB & ~getMask(bitsToUse,LEFT_TO_RIGHT?2-i:i);
            int data = (decompressBits(offset,bitsToUse,payload)<<(8*(LEFT_TO_RIGHT?2-i:i)));
            alteredRGB = alteredRGB | data;
            offset+=bitsToUse;
        }
        return alteredRGB;
    }

    public int decodeRGB(int msg, int bitsToUSe){
        int payload=0;
        for (int i = 0; i < 3; i++) {
            int bits = msg & getMask(bitsToUSe,LEFT_TO_RIGHT?i:2-i);
            bits = bits >> (8*(LEFT_TO_RIGHT?i:2-i));
            payload = payload | (bits<<(bitsToUSe*(LEFT_TO_RIGHT?2-i:i)));
        }
        return payload;
    }
}
