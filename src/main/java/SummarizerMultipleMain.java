import summarizer.TextSummarizer;

import java.util.ArrayList;
import java.util.List;

import static utils.FilesUtils.createFile;
import static utils.FilesUtils.readFile;
import static utils.FilesUtils.getFilesNames;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class SummarizerMultipleMain {
    private final static String PATH_TO_DOCS = "resources/docs/";
    private final static String PATH_TO_SUMMARIES = "resources/summaries/";

    public static void main(String[] args) {
        // summarize multiple articles - read them from PATH_TO_DOCS path and write the summaries to PATH_TO_SUMMARIES
        List<String> documentsPaths = getFilesNames(PATH_TO_DOCS);
        List<String> summaries = new ArrayList<>();

        for (String documentPath: documentsPaths) {
            String originalText = readFile(documentPath);
            int maxLength = 10;
            TextSummarizer textSummarizer = new TextSummarizer();
            String summary = textSummarizer.summarize(originalText, maxLength);
            String documentName = documentPath.substring(documentPath.lastIndexOf("/") + 1, documentPath.length());
            createFile(PATH_TO_SUMMARIES, documentName, summary);
            summaries.add(summary);
            System.out.println(summary);
        }
    }
}
