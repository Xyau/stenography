package main;

import util.ImageUtils;
import util.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Main {
    public static void main(String[] args) {
        Configuration configuration = Configuration.parseOptions(args);

        FileProcessor fileProcessor = new FileProcessor();
        fileProcessor.processFile(configuration);
    }
}
