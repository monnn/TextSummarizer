import summarizer.TextSummarizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class SummarizerMain {
    private final static String PATH_TO_DOCS = "resources/docs/";

    private static String getDocument(String documentName) {
        StringBuilder stringBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(documentName))) {stream.forEach(line -> stringBuilder.append(line).append(" "));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String originalText = scanner.nextLine();
//        String originalText = getDocument( PATH_TO_DOCS + "first.txt");

        int maxLength = 5;
        TextSummarizer textSummarizer = new TextSummarizer();
        String summarizedText = textSummarizer.summarize(originalText, maxLength);
        System.out.println(summarizedText);
    }
}
