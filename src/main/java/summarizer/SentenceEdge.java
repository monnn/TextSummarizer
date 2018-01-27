package summarizer;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class SentenceEdge {
    private SentenceNode from;
    private SentenceNode to;
    private int weight;

    public SentenceEdge(SentenceNode from, SentenceNode to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public SentenceNode getFrom() {
        return from;
    }

    public SentenceNode getTo() {
        return to;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "summarizer.SentenceEdge{" +
                "from=" + from +
                ", to=" + to +
                ", weight=" + weight +
                '}';
    }
}
