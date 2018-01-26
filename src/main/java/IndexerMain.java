import indexer.Indexer;
import indexer.Searcher;
import indexer.TitledDocument;
import org.apache.lucene.analysis.bg.BulgarianAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static utils.FilesUtils.getFilesNames;

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
        List<String> documentsPaths = getFilesNames(PATH_TO_SUMMARIES);

        for (String documentPath: documentsPaths) {
            String documentName = documentPath.substring(documentPath.lastIndexOf("/") + 1, documentPath.length());
            String documentContent = readDocument(documentPath);
            documents.add(new TitledDocument(documentName, documentContent));
        }
        return documents;
    }

    public static void main(String[] args) throws IOException, ParseException {
        Scanner scanner = new Scanner(System.in);
        List<TitledDocument> documents = getSummaries();

        BulgarianAnalyzer analyzer = new BulgarianAnalyzer();
        Indexer indexer = new Indexer(documents, analyzer);
        Searcher searcher = new Searcher(analyzer, indexer);

        while (scanner.hasNextLine()) {
            String query = scanner.nextLine();
            List<TitledDocument> results = searcher.performSearch(query);
            System.out.println("Number of hits: " + results.size());
            System.out.println(results.toString());
        }
    }
}
