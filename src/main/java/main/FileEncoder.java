package main;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.ByteBuffer;

public class FileEncoder {
    public byte[] encodeSizeAndExtension(String path){
        byte[] fileBytes = getFileAsBytes(path);
        File file = new File(path);
        byte[] size = intToByteArray((int) file.length());
        String extension = "."+file.getName().split("\\.")[1]+"\0";
        byte[] extensionBytes = extension.getBytes();
        byte[] encodedFile = new byte[4+fileBytes.length+extensionBytes.length];
        System.arraycopy(size,0,encodedFile,0,size.length);
        System.arraycopy(fileBytes,0,encodedFile,size.length,fileBytes.length);
        System.arraycopy(extensionBytes,0,encodedFile,size.length+fileBytes.length,extensionBytes.length);
        return encodedFile;
    }

    public byte[] encodeSize(byte[] bytes){
        byte[] size = intToByteArray(bytes.length);
        byte[] encodedBytes = new byte[bytes.length+4];
        System.arraycopy(size,0,encodedBytes,0,size.length);
        System.arraycopy(bytes,0,encodedBytes,size.length,bytes.length);
        return encodedBytes;
    }

    public byte[] decodeSize(byte[] bytes) throws WrongSizeException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        int size = byteBuffer.getInt();
        if (size > bytes.length){
            throw new WrongSizeException();
        }
        byte[] fileBytes = new byte[size];
        System.arraycopy(bytes,4,fileBytes,0,size);
        return fileBytes;
    }

    public File decodeSizeAndExtension(byte[] encodedFile, String path) throws WrongSizeException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(encodedFile);
        int size = byteBuffer.getInt();
        if (size > encodedFile.length){
            throw new WrongSizeException();
        }
        byte[] fileBytes = new byte[size];
        System.arraycopy(encodedFile,4,fileBytes,0,size);
        byte[] extension = new byte[encodedFile.length-4-size];
        System.arraycopy(encodedFile,4+size,extension,0,extension.length);
        String ext=readExtension(extension);
        try {
            FileUtils.writeByteArrayToFile(new File(path+ext), fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(path+ext);
    }

    public String readExtension(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        if(bytes.length < 1 || bytes[0] != '.'){
            throw new IllegalStateException("Wrongly formatted file!");
        }
        for (int i = 0; i < bytes.length; i++) {
            if(bytes[i] != 0){
                sb.append((char) bytes[i]);
            } else {
                return sb.toString();
            }
        }
        throw new IllegalStateException("Not found /0 in the end");
    }

    public byte[] getFileAsBytes(String path){
        File file = new File(path);
        if(!file.exists()){
            throw new IllegalArgumentException("Inexistent file! "+file);
        }
        RandomAccessFile f = null;
        try {
            f = new RandomAccessFile(path, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] fileBytes = new byte[(int)file.length()];
        try {
            f.readFully(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Problem with reading file: "+file);
        }
        return fileBytes;
    }

    public int bytesToArray(byte first, byte second, byte third, byte fourth){
        int x = fourth;
        x = x | (first<<24);
        x = x | (second<<16);
        x = x | (third<<8);
        return x;
    }

    public byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }
}
