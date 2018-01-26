package crawler;

import java.io.IOException;
import java.util.List;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public abstract class Crawler {
    public abstract List<String> getArticlesUrls(String url) throws IOException;

    public abstract String getArticleContent(String url) throws IOException;

    public abstract List<String> getBaseUrls();
}
