package summarizer;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static utils.FilesUtils.readFileByLines;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class TextSummarizer {

    private final static String PATH_TO_PRONOUNS_FILE = "resources/pronouns.txt";

    public String summarize(String text, int maxLength) {
        List<String> originalSentences = breakTextIntoSentences(text);
        SentenceGraph graph = new SentenceGraph(originalSentences);
        List<String> topRanked = graph.getTopRanked(maxLength);
        String summary = getSortedSentences(originalSentences, topRanked);
        return summary;
    }

    private String getSortedSentences(List<String> originalSentences, List<String> sentencesToSort) {
        StringBuilder stringBuilder = new StringBuilder();
        int selected = 0;
        String originalSentence;
        for (int i = 0; i < originalSentences.size(); i++) {
            originalSentence = originalSentences.get(i);
            if (selected >= sentencesToSort.size()) {
                break;
            }
            for (String sentenceToSort : sentencesToSort) {
                if (originalSentence.equals(sentenceToSort)) {
                    if (containsPronounFirstHalf(sentenceToSort) && i > 0 && !sentencesToSort.contains(originalSentences.get(i - 1))) {
                        // add the previous sentence also
                        stringBuilder.append(originalSentences.get(i - 1));
                        selected++;
                    }
                    stringBuilder.append(sentenceToSort);
                    selected++;
                }
            }
        }
        return stringBuilder.toString();
    }
    private boolean containsPronounFirstHalf(String sentence) {
        String firstHalf = sentence.substring(0, sentence.length() / 2);
        Set<String> pronouns = readFileByLines(PATH_TO_PRONOUNS_FILE);
        for (String pronoun : pronouns) {
            if (firstHalf.contains(pronoun)) {
                return true;
            }
        }
        return false;
    }

    private static List<String> breakTextIntoSentences(String text) {
        List<String> sentences = new ArrayList<>();
        BreakIterator sentenceIterator = BreakIterator.getSentenceInstance(Locale.US);
        sentenceIterator.setText(text);
        int start = sentenceIterator.first();
        for (int end = sentenceIterator.next(); end != BreakIterator.DONE; start = end, end = sentenceIterator.next()) {
            sentences.add(text.substring(start, end));
        }

        return sentences;
    }
}
