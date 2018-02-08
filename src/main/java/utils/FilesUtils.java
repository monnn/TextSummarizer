package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class FilesUtils {

    public static Set<String> readFileByLines(String pathToFile) {
        Set<String> stopWords = new HashSet<>();

        try (Stream<String> stream = Files.lines(Paths.get(pathToFile))) {
            stream.forEach(stopWords::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopWords;
    }

    public static String readFile(String pathToFile) {
        StringBuilder stringBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(pathToFile))) {
            stream.forEach(stringBuilder::append);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public static List<String> getFilesNames(String pathToFolder) {
        List<String> documentsPaths = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(pathToFolder))) {
            paths.filter(Files::isRegularFile).forEach(el -> documentsPaths.add(el.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documentsPaths;
    }

    public static void createFile(String directory, String fileName) {
        Path path = Paths.get(directory + fileName);
        if (Files.exists(path)) {
            return;
        }
        try {
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(String directory, String fileName, String contentToWrite) {
        Path path = Paths.get(directory + fileName);
        if (!Files.exists(path)) {
            return;
        }
        try {
            Files.write(Paths.get(path.toAbsolutePath().toString()), contentToWrite.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToFileMultiple(String directory, String fileName, String contentToWrite) {
        Path path = Paths.get(directory + fileName);
        contentToWrite = contentToWrite + '\n';

        if (!Files.exists(path)) {
            createFile(directory, fileName);
        }

        try {
            Files.write(Paths.get(path.toAbsolutePath().toString()), contentToWrite.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
