package main;

public class Main {
    public static void main(String[] args) {
        Configuration configuration = Configuration.parseOptions(args);

        FileProcessor fileProcessor = new FileProcessor();
        fileProcessor.processFile(configuration);
    }
}
