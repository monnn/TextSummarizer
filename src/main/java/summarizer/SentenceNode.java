package summarizer;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class SentenceNode {
    private int id;
    private int weight;
    private String sentence;

    public SentenceNode(String sentence) {
        this.sentence = sentence;
    }

    public String getSentence() {
        return sentence;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void increaseWeight(int weightToAdd) {
        this.weight += weightToAdd;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "summarizer.SentenceNode{" +
                "id=" + id +
                ", weight=" + weight +
                ", sentence='" + sentence + '\'' +
                '}';
    }
}
