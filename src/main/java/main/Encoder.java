package main;

import javafx.util.Pair;
import util.ImageUtils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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

    public BufferedImage encodeInImage(BufferedImage image, byte[] payload, LSBType lsbType){
        switch (lsbType) {
            case LSB1:
                return encodeInImage(image,payload,LSBType.LSB1);
            case LSB4:
                return encodeInImage(image,payload,LSBType.LSB4);
            case LSBE:
                return encodeInImageLSBE(image,payload);
        }
        throw new IllegalStateException();
    }

    public byte[] decodeImage(BufferedImage image, LSBType lsbType){
        switch (lsbType) {
            case LSB1:
                return decodeImage(image,1);
            case LSB4:
                return decodeImage(image,4);
            case LSBE:
                return decodeImageLSBE(image);
        }
        throw new IllegalStateException();
    }

    public BufferedImage encodeInImageLSBE(BufferedImage image, byte[] payload){
        int maxOffset = payload.length*8;

        BufferedImage alteredImage = ImageUtils.deepCopy(image);

        int offset = 0;
        for (int y = alteredImage.getHeight()-1; y >= 0; y--) {
            for (int x = 0; x < alteredImage.getWidth(); x++) {
                int origRGB = image.getRGB(x,y);

                if(offset > maxOffset){
                    return alteredImage;
                }
                int encodedRGB = encodeRGBLSB(offset,payload,origRGB);
                offset += count254or255Bytes(origRGB);
                alteredImage.setRGB(x,y,encodedRGB);
            }
        }
        throw new IllegalArgumentException("the image is too small for the payload, it fits "+
                    offset + "bytes");
    }

    public BufferedImage encodeInImage(BufferedImage image, byte[] payload, int bitsToUse){
        byte[] paddedPayload = padPayload(payload);
        int maxOffset = paddedPayload.length*8;

        BufferedImage alteredImage = ImageUtils.deepCopy(image);

        if(image.getWidth()*image.getHeight()*3*bitsToUse < paddedPayload.length*8){
            throw new IllegalArgumentException("the image is too small for the payload, it fits " +
                    image.getWidth()*image.getHeight()*3*bitsToUse + " bytes");
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

    public byte[] decodeImageLSBE(BufferedImage image){
        List<Pair<Integer, Integer>> infoBits = new ArrayList<>();
        for (int j = image.getHeight()-1; j >= 0; j--) {
            for (int i = 0; i < image.getWidth(); i++) {
                int rgb = image.getRGB(i,j);
                int decoded = decodeRGBLSE(rgb);
                int bitsUsed = count254or255Bytes(rgb);
                infoBits.add(new Pair<>(decoded,bitsUsed));
            }
        }
        return Compresser.compressBitsLSBE(infoBits);
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


    public int count254or255Bytes(int rgb){
        int bitsAltered = 0;
        for (int i = 0; i < 3; i++) {
            int b = Compresser.getByte(rgb,LEFT_TO_RIGHT?2-i:i);
            if(b == 254 || b == 255){
                bitsAltered++;
            }
        }
        return bitsAltered;
    }

    public int encodeRGBLSB(int offset, byte[] payload, int originalRGB){
        int alteredRGB = originalRGB;
        for (int i = 0; i < 3 && offset<payload.length*8; i++) {
            int b = Compresser.getByte(originalRGB,LEFT_TO_RIGHT?2-i:i);
            if(b == 254 || b == 255){
                alteredRGB = alteredRGB & ~getMask(1,LEFT_TO_RIGHT?2-i:i);
                int data = (decompressBits(offset,1,payload)<<(8*(LEFT_TO_RIGHT?2-i:i)));
                alteredRGB = alteredRGB | data;
                offset+=1;
            }
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

    public int decodeRGBLSE(int msg){
        int payload=0;
        for (int i = 0; i < 3; i++) {
            int b = Compresser.getByte(msg,i);
            if(b == 254 || b == 255){
                int bits = msg & getMask(1,i);
                bits = bits >> (8*i);
                payload = payload<<1;
                payload = payload | bits;
            }
        }
        return payload;
    }
}
