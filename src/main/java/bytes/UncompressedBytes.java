package bytes;

public class UncompressedBytes {
    byte[] bytes;
    int bitsWithInfoMask;

    public UncompressedBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
