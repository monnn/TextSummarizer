package indexer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.List;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class Indexer {
    private IndexWriter indexWriter;
    private Directory index;

    public Indexer(List<TitledDocument> documents, StandardAnalyzer analyzer) throws IOException {
        this.index = new RAMDirectory();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        this.indexWriter = new IndexWriter(index, config);
        this.indexDocuments(documents);
        this.indexWriter.close();
    }

    private void indexDocuments(List<TitledDocument> documents) throws IOException {
        for (int i = 0; i < documents.size(); i++) {
            this.addDoc(documents.get(i));
        }
    }

    public Directory getIndex() {
        return index;
    }

    private void addDoc(TitledDocument titledDocument) throws IOException {
        Document doc = new Document();
        String title = titledDocument.getTitle();
        String content = titledDocument.getContent();
        doc.add(new TextField("title", title, Field.Store.YES));

        doc.add(new TextField("content", content, Field.Store.YES));
        this.indexWriter.addDocument(doc);
    }
}
