package summarizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class Stemmer {
    private Map<String, String> stemmingRules;
    private int STEM_BOUNDARY;

    private static final String PATH_TO_STEMMING_RULES = "resources/stemming_rules_UTF-8.txt";
    private static final Pattern VOCALS = Pattern.compile("[^ауиоеюя]*[ауиоеюя]");
    private static final Pattern STEM_PATTERN = Pattern.compile("([а-я]+)\\s==>\\s([а-я]+)\\s([0-9]+)");

    public Stemmer() {
        this.STEM_BOUNDARY = 1;
        this.stemmingRules = loadStemmingRules();
    }

    private Map<String, String> loadStemmingRules() {
        Map<String, String> stemmingRules = new HashMap<>();

        try (Stream<String> stream = Files.lines(Paths.get(PATH_TO_STEMMING_RULES))) {
            stream.forEach(line -> {
                Matcher m = STEM_PATTERN.matcher(line);
                if (m.matches() && m.groupCount() == 3 && Integer.parseInt(m.group(3)) > STEM_BOUNDARY) {
                    stemmingRules.put(m.group(1), m.group(2));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stemmingRules;
    }

    public String stem(String word) {
        Matcher m = VOCALS.matcher(word);
        if (!m.lookingAt()) {
            return word;
        }
        for (int i = m.end() + 1; i < word.length(); i++) {
            String suffix = word.substring(i);
            if ((!suffix.equals("") && (this.stemmingRules.get(suffix)) != null)) {
                return word.substring(0, i) + suffix;
            }
        }
        return word;
    }
}
