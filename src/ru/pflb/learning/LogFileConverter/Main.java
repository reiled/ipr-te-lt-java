package ru.pflb.learning.LogFileConverter;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        if (args.length != 3) {
            showHelp();
            return;
        }

        try {
            LogFileConverter.convertToCSV(args[0], args[1], args[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showHelp() {
        System.out.println("Запуск программы: java -jar <название_jar-файла> <тип_разделителя> <путь_до_лог_файла(ов)> <имя_нового_файла>");
    }
}
