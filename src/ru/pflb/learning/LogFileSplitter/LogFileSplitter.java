package ru.pflb.learning.LogFileSplitter;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LogFileSplitter {

    private LogFileSplitter() {
        // Restrict object creation
    }

    /***
     * Counts number of lines in text file
     * @param filePath path to file
     * @return number of lines in text file
     * @throws IOException indicates io errors (e.g.: file not found)
     */
    private static int getLinesCount(String filePath) throws IOException {
        FileReader fileReader = new FileReader(filePath);
        LineNumberReader lineNumberReader = new LineNumberReader(fileReader);

        while (lineNumberReader.skip(Long.MAX_VALUE) > 0) {
            // do nothing
        }
        int count = lineNumberReader.getLineNumber();

        lineNumberReader.close();
        fileReader.close();

        return count;
    }

    /***
     * Creates array of number of lines per file
     * @param linesCount total number of lines
     * @param numberOfParts number of files
     * @return array of lines count per file
     */
    private static int[] partitionLinesCount(int linesCount, int numberOfParts) {
        int[] numberOfLinesPerPart = new int[numberOfParts];
        for (int i = 0; i < numberOfParts; ++i) {
            numberOfLinesPerPart[i] = linesCount / numberOfParts;
        }
        numberOfLinesPerPart[0] += linesCount % numberOfParts;

        return numberOfLinesPerPart;
    }

    /***
     * Splits large text file into multiple files
     * @param filePath path to text file
     * @param numberOfParts number of parts to split
     * @param persistentPartOfName persistent part of output file name
     */
    public static void split(String filePath, int numberOfParts, String persistentPartOfName) throws IOException {
        int nLines = getLinesCount(filePath);
        int[] linesPartition = partitionLinesCount(nLines, numberOfParts);

        BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(filePath), Charset.forName("CP1251"));

        for (int i = 0; i < numberOfParts; ++i) {
            BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(persistentPartOfName + "_" + i + ".split"));

            for (int j = 0; j < linesPartition[i]; ++j) {
                bufferedWriter.write(bufferedReader.readLine());
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
        }

        bufferedReader.close();
    }
}
