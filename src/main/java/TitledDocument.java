/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class TitledDocument {
    private String title;
    private String content;
//    private int score;


    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public TitledDocument(String title, String content) {
        this.title = title;
        this.content = content;
//        this.score = score;
    }

    @Override
    public String toString() {
        return "TitledDocument{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}' + '\n';
    }
}
