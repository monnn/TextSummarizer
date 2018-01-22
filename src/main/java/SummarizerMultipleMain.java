import summarizer.TextSummarizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class SummarizerMultipleMain {
    private final static String PATH_TO_DOCS = "resources/docs/";
    private final static String PATH_TO_SUMMARIES = "resources/summaries/";

    private static String getDocument(String documentName) {
        StringBuilder stringBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(documentName))) {
            stream.forEach(stringBuilder::append);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        // and write it to file
        List<String> documentsPaths = new ArrayList<>();
        List<String> summaries = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(PATH_TO_DOCS))) {
            paths.filter(Files::isRegularFile).forEach(el -> documentsPaths.add(el.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String documentPath: documentsPaths) {
            String originalText = getDocument(documentPath);
            int maxLength = 5;
            TextSummarizer textSummarizer = new TextSummarizer();
            String summary = textSummarizer.summarize(originalText, maxLength);
            String documentName = documentPath.substring(documentPath.lastIndexOf("/") + 1, documentPath.length());
            Path summaryPath = Paths.get(PATH_TO_SUMMARIES + documentName);
           try {
                Files.createFile(summaryPath);
               Files.write(Paths.get(summaryPath.toAbsolutePath().toString()), summary.getBytes());
           } catch (IOException e) {
                e.printStackTrace();
           }
            summaries.add(summary);
            System.out.println(summary);
        }
    }
}
