import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;

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
public class IndexerMain {

    private final static String PATH_TO_SUMMARIES = "resources/summaries/";

    private static String readDocument(String documentPath) {
        StringBuilder stringBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(documentPath))) {stream.forEach(stringBuilder::append);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    private static List<TitledDocument> getSummaries() {
        List<TitledDocument> documents = new ArrayList<>();
        List<String> documentsPaths = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(Paths.get(PATH_TO_SUMMARIES))) {
            paths.filter(Files::isRegularFile).forEach(el -> documentsPaths.add(el.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String documentPath: documentsPaths) {
            String documentName = documentPath.substring(documentPath.lastIndexOf("/") + 1, documentPath.length());
            String documentContent = readDocument(documentPath);
            documents.add(new TitledDocument(documentName, documentContent));
        }
        return documents;
    }

    public static void main(String[] args) throws IOException, ParseException {
//        List<TitledDocument> documents = new ArrayList<>();
//        documents.add(new TitledDocument("Lucene in Action", "Lucene in Action"));
//        documents.add(new TitledDocument("Lucene for Dummies", "Lucene in Action"));
//        documents.add(new TitledDocument("Managing Gigabytes", "Lucene in Action"));
//        documents.add(new TitledDocument("The Art of Computer Science", "Lucene in Action"));

        List<TitledDocument> documents = getSummaries();

        StandardAnalyzer analyzer = new StandardAnalyzer();
        Indexer indexer = new Indexer(documents, analyzer);
        Searcher searcher = new Searcher(analyzer, indexer);
        String querystr = "ХРАНА";

        List<TitledDocument> results = searcher.performSearch(querystr);
        System.out.println("Number of hits: " + results.size());
        System.out.println(results.toString());
    }

}
