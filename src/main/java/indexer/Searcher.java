package indexer;

import org.apache.lucene.analysis.bg.BulgarianAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class Searcher {
    private BulgarianAnalyzer analyzer;
    private Directory index;

    public Searcher(BulgarianAnalyzer analyzer, Indexer indexer) {
        this.analyzer = analyzer;
        this.index = indexer.getIndex();
    }

    public List<TitledDocument> performSearch(String query) throws IOException, ParseException {
        Query q = new QueryParser("content", this.analyzer).parse(query);

        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(this.index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;
        List<TitledDocument> result = new ArrayList<>();

        for(int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            result.add(new TitledDocument(d.get("title"), d.get("content")));
        }

        reader.close();
        return result;
    }
}
