package summarizer;

/**
 * @author Monica Shopova <monica.shopova@gmail.com>
 */
public class Edge {
    private SentenceNode from;
    private SentenceNode to;
    private int weight;

    public Edge(SentenceNode from, SentenceNode to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "summarizer.Edge{" +
                "from=" + from +
                ", to=" + to +
                ", weight=" + weight +
                '}';
    }
}
