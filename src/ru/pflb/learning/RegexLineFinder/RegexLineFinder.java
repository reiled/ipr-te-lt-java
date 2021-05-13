package ru.pflb.learning.RegexLineFinder;

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

public class RegexLineFinder {

    private RegexLineFinder() {
        // Restrict object creation
    }

    public static void find(String pattern, String pathToLogFiles, String outputFileName) throws IOException {
        Pattern matcherPattern = Pattern.compile(pattern);
        BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(outputFileName));
        Stream<Path> paths = Files.walk(Paths.get(pathToLogFiles));

        ArrayList<Path> files = paths.filter(Files::isRegularFile).collect(Collectors.toCollection(ArrayList::new));
        String line;

        for (Path filePath : files) {
            BufferedReader bufferedReader = Files.newBufferedReader(filePath);

            while ((line = bufferedReader.readLine()) != null) {
                Matcher matcher = matcherPattern.matcher(line);

                if (matcher.find()) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
            }

            bufferedReader.close();
        }

        paths.close();
        bufferedWriter.close();
    }
}
