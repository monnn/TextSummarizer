import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.BreakIterator;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class SentenceGraph {
    private List<SentenceNode> nodes;
    private List<Edge> edges;
    private static final double SIMILARITY_THRESHOLD = 0.5;
    private final static String PATH_TO_STOPWORDS_FILE = "resources/stopwords.txt";


    public SentenceGraph(List<String> originalSentences) {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();

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
//        System.out.println("topRankedSentences: ");
//        System.out.println(topRankedSentences.toString());
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
        int minDistance;
        int currentDistance;
        int distance = 0;
        int maxLength = Math.max(sentence1.length(), sentence2.length());
        int similarity;
        String minDistanceWord;
        int additionalDistance = 0;

        while (wordsFirstSentence.size() > 0 && wordsSecondSentence.size() > 0) {
            String firstWord = wordsFirstSentence.get(0);
            wordsFirstSentence.remove(0);
            minDistance = 100;
            minDistanceWord = wordsSecondSentence.get(0);

            for (String secondWord : wordsSecondSentence) {
                currentDistance = calculateWordsDistance(firstWord, secondWord);
                if (currentDistance < minDistance) {
                    minDistance = currentDistance;
                    minDistanceWord = secondWord;
                }
            }
            wordsSecondSentence.remove(minDistanceWord);
            distance += minDistance;
        }
        if (wordsFirstSentence.size() > 0) {
            additionalDistance = wordsFirstSentence.stream().mapToInt(s -> s.length()).sum();

        } else if (wordsSecondSentence.size() > 0) {
            additionalDistance = wordsSecondSentence.stream().mapToInt(s -> s.length()).sum();
        }
        distance += additionalDistance;
        similarity = maxLength - distance;

//        System.out.println("distance is: " + distance);
//        System.out.println("similarity is: " + similarity);
        return similarity;
    }

    private static String cleanSentence(String originalSentence) {
        String cleanSentence = originalSentence;
        Set<String> stopWords = getStopWords();

        for (String stopWord : stopWords) {
            cleanSentence = cleanSentence.replace(stopWord, " ");
        }
        return cleanSentence.substring(0, cleanSentence.length() - 1).replace(", ", " ");
    }

    private static Set<String> getStopWords() {
        Set<String> stopWords = new HashSet<>();

        try (Stream<String> stream = Files.lines(Paths.get(PATH_TO_STOPWORDS_FILE))) {
            stream.forEach(stopWords::add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stopWords;
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
//        System.out.println("words are: " + words.size());
//        System.out.println(words.toString());
        return words;
    }

    private int calculateWordsDistance(String word1, String word2) {
        // perform levenshtein distance
        // add insertion and deletion
        // add max_distance_threshold (percent)
        // normalize similarity = 1 - totally similar, 0 - not similar
        // search for longest common substring
        int minLength;
        if (word1.length() < word2.length()) {
            minLength = word1.length();
        } else {
            minLength = word2.length();
        }
        int diff = Math.abs(word1.length() - word2.length());
        for (int i = 0; i < minLength; i++) {
            if (word1.charAt(i) != word2.charAt(i)) {
                diff++;
            }
        }
        return diff;
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
                    // use edges weight
                    firstNode.setWeight(firstNode.getWeight() + weight);
                    secondNode.setWeight(secondNode.getWeight() + weight);
                }
            }
        }
    }
}
