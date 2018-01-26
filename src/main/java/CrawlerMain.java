import crawler.BBteamCrawler;
import crawler.Crawler;

import java.io.IOException;
import java.util.List;

import static utils.FilesUtils.createFile;
import static utils.Validation.isUrlValid;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class CrawlerMain {

    private static final String BB_TEAM_BASE_URL = "bb-team.org";
    private final static String PATH_TO_DOCS = "resources/docs/";

    private static Crawler getCrawler(String url) {
        if (url.contains(BB_TEAM_BASE_URL)) {
            return new BBteamCrawler();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        Crawler crawler = getCrawler(BB_TEAM_BASE_URL);
        String documentTitle;
        String documentContent;

        if (crawler == null) {
            return;
        }
        List<String> baseUrls = crawler.getBaseUrls();
        for (String baseUrl : baseUrls) {
            List<String> urls = crawler.getArticlesUrls(baseUrl);
            for (String url : urls) {
                if (!isUrlValid(url)) {
                    continue;
                }
                documentContent = crawler.getArticleContent(url);
                documentTitle = url.substring(url.lastIndexOf("/") + 1, url.length()) + ".txt";
                createFile(PATH_TO_DOCS, documentTitle, documentContent);
            }
        }
    }
}
