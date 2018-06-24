package main;

import org.apache.commons.cli.*;

import static java.lang.System.exit;

public class Configuration {

    private FileEncoder fileEncoder = new FileEncoder();
    private Encoder encoder = new Encoder();
    private Function function;
    private String inputFile;
    private String imageFile;
    private String outputFile;
    private Stenography stenography;
    private Encryption encryption;
    private Method method;

    public Configuration(){}

    private static Options createOptions(){
        Options options = new Options();
        options.addOption("embed", false, "Embedding function.");
        options.addOption("extract", false, "Extracting function.");
        options.addOption("in", true, "Embedded file");
        options.addOption("p", true, "Image file.");
        options.addOption("out", true, "Output file.");
        options.addOption("steg", true, "Stenography method.");
        options.addOption("a", true, "Encryption algorithm.");
        options.addOption("m", true, "Encryption method. Options: ecb | cfb | ofb | cbc");
        options.addOption("pass", true, "Password.");
        options.addOption("h", "help", false, "Help.");
        return options;
    }

    public static Configuration parseOptions(String[] args){
        Options options = createOptions();
        CommandLineParser parser = new BasicParser();
        Configuration configuration = new Configuration();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("h")){
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("damped-harmonic-oscilator", options);
                exit(0);
            }
            if (cmd.hasOption("embed") && cmd.hasOption("extract")){
                finishWithError("Cannot embed and extract at the same time");
            }

            if (cmd.hasOption("embed")){
                configuration.setFunction(Function.EMBED);
            }else if(cmd.hasOption("extract")){
                configuration.setFunction(Function.EXTRACT);
            }else{
                finishWithError("No program function selected. Please use the -embed or -extract parameters.");
            }

            if (configuration.getFunction().equals(Function.EMBED) && cmd.hasOption("in")){
                configuration.setInputFile(cmd.getOptionValue("in"));
            }
            if (!cmd.hasOption("p") || !cmd.hasOption("out")){
                finishWithError("No image or output file.");
            }
            configuration.setImageFile(cmd.getOptionValue("p"));
            configuration.setOutputFile(cmd.getOptionValue("out"));

            if (!cmd.hasOption("steg")){
                finishWithError("No stenography method selected!");
            }
            configuration.setStenography(stenographyOptions(cmd.getOptionValue("steg")));
            if (cmd.hasOption("steg")){
                finishWithError("No stenography method selected!");
            }
            configuration.setMethod(methodOptions(cmd.getOptionValue("m")));
            configuration.setEncryption(encryptionOptions(cmd.getOptionValue("a")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return configuration;
    }

    private static Stenography stenographyOptions(String option){
        option = option.toUpperCase();
        switch (option){
            case "LSB1":
                return Stenography.LSB1;
            case "LSB4":
                return Stenography.LSB4;
            case "LSBE":
                return Stenography.LSBE;
            default:
                finishWithError("No valid Stenography method.");
                return null;
        }
    }

    private static Method methodOptions(String option){
        option = option.toUpperCase();
        switch (option){
            case "CBC":
                return Method.CBC;
            case "CFB":
                return Method.CFB;
            case "ECB":
                return Method.ECB;
            case "OFB":
                return Method.OFB;
            default:
                return null;
        }
    }

    private static Encryption encryptionOptions(String option){
        option = option.toUpperCase();
        switch (option){
            case "AES128":
                return Encryption.AES128;
            case "AES192":
                return Encryption.AES192;
            case "AES256":
                return Encryption.AES256;
            case "DES":
                return Encryption.DES;
            default:
                return null;
        }
    }

    public FileEncoder getFileEncoder() {
        return fileEncoder;
    }

    public void setFileEncoder(FileEncoder fileEncoder) {
        this.fileEncoder = fileEncoder;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public Encoder getEncoder() {
        return encoder;
    }

    public void setEncoder(Encoder encoder) {
        this.encoder = encoder;
    }

    public Stenography getStenography() {
        return stenography;
    }

    public void setStenography(Stenography stenography) {
        this.stenography = stenography;
    }

    public Encryption getEncryption() {
        return encryption;
    }

    public void setEncryption(Encryption encryption) {
        this.encryption = encryption;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public enum Function {
        EMBED,
        EXTRACT
    }

    public enum Stenography{
        LSB1,
        LSB4,
        LSBE
    }

    public enum Encryption{
        AES128,
        AES192,
        AES256,
        DES,
        NO_ENCRYPTION,
    }

    public enum Method {
        ECB,
        CFB,
        OFB,
        CBC
    }

    private static void finishWithError(String error){
        System.out.println(error);
        exit(1);
    }
}
