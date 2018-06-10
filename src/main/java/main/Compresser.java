package main;

public class Compresser {
    public static int getMask(int bitsToUse, int byteNumber){
        int[] bits = {1,3,7,15,31,63,127};
        return bits[bitsToUse-1]<<(8*byteNumber);
    }

    /**
     * This takes bitsToUse bits from bytes starting from offset and returns the
     * @param offset where to start reading from bytes
     * @param bitsToDecompress amount of bits to read from the payload
     * @param bytes the byte stream coming from the payload
     * @return
     */
    public static int decompressBits(int offset, int bitsToDecompress, byte[] bytes){
        int res=0;
        for (int i = 0; i < bitsToDecompress; i++) {
            res=res<<1;
            int bits=0;
            bits = bytes[offset/8]&(1<<(7-offset%8));
            bits = bits >> (7-offset%8);
            res = res | bits;
            offset++;
        }
        return res;
    }

    public static byte[] compressBits(int[] payloads, int bitsPerPayload){
        byte[] compressedPayload = new byte[(payloads.length*bitsPerPayload/8)+1];
        for (int i = 0; i < payloads.length * bitsPerPayload; i++) {
            int payloadMaskShift = ((bitsPerPayload) - 1 - i%(bitsPerPayload));
            int mask = 1 << payloadMaskShift;
            int bit = (mask & payloads[i/bitsPerPayload])>>payloadMaskShift;
            bit = bit << 7-i%8;
            compressedPayload[i/8] = new Integer(compressedPayload[i/8] | bit).byteValue();
        }
        return compressedPayload;
    }

    //Takes the 8 bits of the byteNumber byte
    public static int getByte(int num,int byteNumber){
        int mask = 255<<(byteNumber*8);
        int x =(num&mask)>>(byteNumber*8);
        return x==-1?255:x;
    }
}

