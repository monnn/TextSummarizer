import crawler.BBteamCrawler;
import crawler.Crawler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static utils.FilesUtils.createFile;
import static utils.FilesUtils.writeToFile;
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
        String category;
        String fileName;

        if (crawler == null) {
            return;
        }
        Map<String, String> baseUrls = crawler.getBaseUrls();
        for (String baseUrl : baseUrls.keySet()) {
            category = baseUrls.get(baseUrl);
            List<String> urls = crawler.getArticlesUrls(baseUrl);
            for (String url : urls) {
                if (!isUrlValid(url)) {
                    continue;
                }
                documentContent = crawler.getArticleContent(url);
                documentTitle = url.substring(url.lastIndexOf("/") + 1, url.length()) + ".txt";
                fileName = category + "_" + documentTitle;
                createFile(PATH_TO_DOCS, fileName);
                writeToFile(PATH_TO_DOCS, fileName, documentContent);
            }
        }
    }
}
