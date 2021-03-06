package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class BBteamCrawler extends Crawler {

    @Override
    public Map<String, String> getBaseUrls() {
        Map<String, String> baseArticlesUrls = new HashMap();
        baseArticlesUrls.put("https://www.bb-team.org/hranene/statii", "eating");
        baseArticlesUrls.put("https://www.bb-team.org/trenirovki/statii", "exercising");
        return baseArticlesUrls;
    }

    @Override
    public List<String> getArticlesUrls(String baseUrl) throws IOException {
        Document doc = Jsoup.connect(baseUrl).get();
        Elements elements = doc.select("#top .container").last().select("a");
        List<String> articlesUrls = elements.stream().map(element -> element.attr("href")).collect(Collectors.toList());
        return articlesUrls;
    }

    @Override
    public String getArticleContent(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        Elements elements = doc.select("[itemProp=articleBody] p");
        if (elements != null) {
            String articleContent = elements.stream().map(element -> element.text()).reduce("", (acc, element) -> acc + " " + element);
            System.out.println(articleContent);
            return articleContent;
        }
        return null;
    }
}
