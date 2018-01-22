package summarizer;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class TextSummarizer {

    public String summarize(String text, int maxLength) {
        List<String> originalSentences = breakTextIntoSentences(text);
        SentenceGraph graph = new SentenceGraph(originalSentences);
        List<String> topRanked = graph.getTopRanked(maxLength);
        String summary = getSortedSentences(originalSentences, topRanked);
//        System.out.println(graph.nodes.toString());
//        System.out.println(graph.edges.toString());

//        System.out.println(Arrays.toString(processedSentences.toArray()));
        return summary;
    }

    private static String getSortedSentences(List<String> originalSentences, List<String> sentencesToSort) {
        StringBuilder stringBuilder = new StringBuilder();
        int selected = 0;
        for (String originalSentence : originalSentences) {
            if (selected >= originalSentence.length()) {
                break;
            }
            for (String sentenceToSort : sentencesToSort) {
                if (originalSentence.equals(sentenceToSort)) {
                    stringBuilder.append(sentenceToSort);
                    selected++;
                }
            }
        }
        return stringBuilder.toString();
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
