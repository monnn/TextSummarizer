import summarizer.TextSummarizer;

import static utils.FilesUtils.readFile;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class SummarizerMain {
    private final static String PATH_TO_DOCS = "resources/docs/";

    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        String originalText = scanner.nextLine();
        String originalText = readFile( PATH_TO_DOCS + "first.txt");

        int maxLength = 5;
        TextSummarizer textSummarizer = new TextSummarizer();
        String summarizedText = textSummarizer.summarize(originalText, maxLength);
        System.out.println(summarizedText);
    }
}
