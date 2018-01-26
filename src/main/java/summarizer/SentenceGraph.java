package summarizer;

import java.text.BreakIterator;
import java.util.*;

import static utils.FilesUtils.readFileByLines;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class SentenceGraph {
    private List<SentenceNode> nodes;
    private List<Edge> edges;
    private static final double SIMILARITY_THRESHOLD = 10;
    private final static String PATH_TO_STOPWORDS_FILE = "resources/stopwords.txt";
    private Stemmer stemmer;

    public SentenceGraph(List<String> originalSentences) {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.stemmer = new Stemmer();

        originalSentences.forEach(this::addNode);
        this.rank();
    }

    public List<String> getTopRanked(int k) {
        List<String> topRankedSentences = new ArrayList<>();
        Collections.sort(nodes, (node1, node2) -> Integer.signum(node2.getWeight() - node1.getWeight()));
        for (int i = 0; i < k; i++) {
            if (nodes.size() <= i) {
                break;
            }
            topRankedSentences.add(nodes.get(i).getSentence());
        }
        return topRankedSentences;
    }

    private void addNode(String sentence) {
        this.nodes.add(new SentenceNode(sentence));
    }

    private SentenceGraph addEdge(SentenceNode first, SentenceNode second, int weight) {
        this.edges.add(new Edge(first, second, weight));
        return this;
    }

    private int calculateSentenceSimilarity(String sentence1, String sentence2) {
        List<String> wordsFirstSentence = extractWords(sentence1);
        List<String> wordsSecondSentence = extractWords(sentence2);
        double maxWords = Math.max(wordsFirstSentence.size(), wordsSecondSentence.size());
        double similarity = 0;
        double maxSimilarity;
        String maxSimilarityWord;
        double currentSimilarity;

        while (wordsFirstSentence.size() > 0 && wordsSecondSentence.size() > 0) {
            String firstWord = wordsFirstSentence.get(0);
            wordsFirstSentence.remove(0);
            maxSimilarity = 0;
            maxSimilarityWord = wordsSecondSentence.get(0);

            for (String secondWord : wordsSecondSentence) {
                currentSimilarity = calculateWordsSimilarity(firstWord, secondWord);
                if (currentSimilarity > maxSimilarity) {
                    maxSimilarity = currentSimilarity;
                    maxSimilarityWord = secondWord;
                }
            }
            wordsSecondSentence.remove(maxSimilarityWord);
            similarity += maxSimilarity;
        }
        similarity = similarity / maxWords;

        return (int)(similarity * 100);
    }

    private static String cleanSentence(String originalSentence) {
        String cleanSentence = originalSentence;
        Set<String> stopWords = readFileByLines(PATH_TO_STOPWORDS_FILE);

        for (String stopWord : stopWords) {
            cleanSentence = cleanSentence.replace(stopWord, " ");
        }
        return cleanSentence.substring(0, cleanSentence.length() - 1).replace(", ", " ");
    }

    private List<String> extractWords(String sentence) {
        // break sentences to words and remove stop words
        List<String> words = new ArrayList<>();
        String cleanedSentence = cleanSentence(sentence);
        BreakIterator wordIterator = BreakIterator.getWordInstance(Locale.US);
        wordIterator.setText(cleanedSentence.toLowerCase());
        int start = wordIterator.first();

        for (int end = wordIterator.next(); end != BreakIterator.DONE; start = end, end = wordIterator.next()) {
            String word = cleanedSentence.substring(start, end).toLowerCase();
            if (!word.equals(" ") && !word.equals(",") && !word.equals(".")) {
                words.add(word);
            }
        }
        return words;
    }

    private int calculateWordsSimilarity(String word1, String word2) {
        word1 = stemmer.stem(word1);
        word2 = stemmer.stem(word2);
        double maxLength = Math.max(word1.length(), word2.length());
        int longestCommonSubstringLength = longestSubstring(word1, word2);
        double similarity = longestCommonSubstringLength / maxLength;
        return (int) (similarity * 100);
    }

    public int calculateWordsDistance(String word1, String word2) {
        // using Levenshtein distance
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();
        int[] diffs = new int [word2.length() + 1];
        for (int j = 0; j < diffs.length; j++)
            diffs[j] = j;
        for (int i = 1; i <= word1.length(); i++) {
            diffs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= word2.length(); j++) {
                int cj = Math.min(1 + Math.min(diffs[j], diffs[j - 1]), word1.charAt(i - 1) == word2.charAt(j - 1) ? nw : nw + 1);
                nw = diffs[j];
                diffs[j] = cj;
            }
        }
        return diffs[word2.length()];
    }

    private int longestSubstring(String word1, String word2) {
        StringBuilder sb = new StringBuilder();
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        int[][] num = new int[word1.length()][word2.length()];
        int maxLength = 0;
        int lastSubstringStart = 0;
        int currentSubstringStart;

        for (int i = 0; i < word1.length(); i++) {
            for (int j = 0; j < word2.length(); j++) {
                if (word1.charAt(i) == word2.charAt(j)) {
                    if ((i == 0) || (j == 0)) {
                        num[i][j] = 1;
                    } else {
                        num[i][j] = 1 + num[i - 1][j - 1];
                    }
                    if (num[i][j] > maxLength) {
                        maxLength = num[i][j];
                        currentSubstringStart = i - num[i][j] + 1;
                        if (lastSubstringStart == currentSubstringStart) {
                            sb.append(word1.charAt(i));
                        } else {
                            lastSubstringStart = currentSubstringStart;
                            sb = new StringBuilder();
                            sb.append(word1.substring(lastSubstringStart, i + 1));
                        }
                    }
                }
            }
        }
        return sb.length();
    }

    private void rank() {
        int weight;
        SentenceNode firstNode;
        SentenceNode secondNode;
        for (int i = 0; i < this.nodes.size(); i++) {
            firstNode = nodes.get(i);
            for (int j = 1; j < this.nodes.size(); j++) {
                if (i == j) {
                    continue;
                }
                secondNode = nodes.get(j);
                weight = calculateSentenceSimilarity(firstNode.getSentence(), secondNode.getSentence());
                if (weight > SIMILARITY_THRESHOLD) {
                    addEdge(firstNode, secondNode, weight);
                }
            }
        }

        updateNodesWeight();
    }

    private void updateNodesWeight() {
        for (Edge edge: this.edges) {
            edge.getFrom().increaseWeight(edge.getWeight());
            edge.getTo().increaseWeight(edge.getWeight());
        }
    }
}
