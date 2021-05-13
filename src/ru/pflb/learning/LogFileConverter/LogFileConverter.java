package ru.pflb.learning.LogFileConverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogFileConverter {

    private LogFileConverter() {
        // Restrict object creation
    }

    /***
     * Converts given set of log files in directory to one CSV and writes it to file
     * @param csvDelimiter delimiter for csv file
     * @param pathToLogFiles path to directory that contains log files
     * @param outputFileName output csv file name
     * @throws IOException indicates io errors (e.g.: file not found)
     */
    public static void convertToCSV(String csvDelimiter, String pathToLogFiles, String outputFileName) throws IOException {
        // (\d{2}\.\d{2}\.\d{4})\h(\d{2}:\d{2}:\d{2}\.\d{3})\h(\D*:)\h(\d*\.\d*\.\d*\.\d*:\d*\.?[\w]*|[\w]*\.[\w]*)?\h?(.*)
        // Capture groups:
        // Group 1: date                (\d{2}\.\d{2}\.\d{4})
        // Group 2: time                (\d{2}:\d{2}:\d{2}\.\d{3})
        // Group 3: message type        (\D*:)
        // Group 4: service (ip/type)   (\d*\.\d*\.\d*\.\d*:\d*\.?[\w]*|[\w]*\.[\w]*)?
        // Group 5: message body        (.*)

        StringBuilder stringBuilder = new StringBuilder();
        Pattern logEntryPattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4})\\h(\\d{2}:\\d{2}:\\d{2}\\.\\d{3})\\h(\\D*:)\\h(\\d*\\.\\d*\\.\\d*\\.\\d*:\\d*\\.?[\\w]*|[\\w]*\\.[\\w]*)?\\h?(.*)");
        BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(outputFileName));
        Stream<Path> paths = Files.walk(Paths.get(pathToLogFiles));

        ArrayList<Path> files = paths.filter(Files::isRegularFile).collect(Collectors.toCollection(ArrayList::new));
        String line;

        for (Path filePath : files) {
            BufferedReader bufferedReader = Files.newBufferedReader(filePath);

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.setLength(0); // clear string builder

                if (line.contains(csvDelimiter)) {
                    if (csvDelimiter.equals(";")) {
                        line = line.replace(csvDelimiter, ",");
                    } else {
                        line = line.replace(csvDelimiter, ";");
                    }
                }

                Matcher matcher = logEntryPattern.matcher(line);

                if (matcher.find()) {
                    stringBuilder.append(matcher.group(1))
                            .append(csvDelimiter)
                            .append(matcher.group(2))
                            .append(csvDelimiter)
                            .append(matcher.group(3))
                            .append(csvDelimiter)
                            .append(matcher.group(4) != null ? matcher.group(4) : "")
                            .append(csvDelimiter)
                            .append(matcher.group(5));

                    bufferedWriter.write(stringBuilder.toString());
                    bufferedWriter.newLine();
                }
            }

            bufferedReader.close();
        }

        paths.close();
        bufferedWriter.close();
    }
}
