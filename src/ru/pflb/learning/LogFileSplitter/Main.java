package ru.pflb.learning.LogFileSplitter;

import java.io.File;
import java.io.IOException;

public class Main {

    private static final int NUMBER_OF_PARTS = 5;

    public static void main(String[] args) {
        if (args.length != 2) {
            showHelp();
            return;
        }

        String outputDirName = "splitted";
        try {
            File dir = new File(outputDirName);
            if (!dir.exists()) {
                dir.mkdir();
            }

            LogFileSplitter.split(args[0], NUMBER_OF_PARTS, outputDirName + File.separator + args[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showHelp() {
        System.out.println("Запуск программы: java -jar <название_jar-файла> <имя_лог_файла> <постоянная_часть_имени_полученных_файлов>");
    }
}
