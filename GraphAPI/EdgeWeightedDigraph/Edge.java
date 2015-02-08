package GraphAPI.EdgeWeightedDigraph;

public class Edge implements Comparable<Edge>{
    private final int from;
    private final int to;
    private final double weight;

    public Edge(int from, int to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public int from() {
        return from;
    }

    public int to() {
        return to;
    }

    public double weight() {
        return weight;
    }

    @Override
    public int compareTo(Edge o) {
        return (weight() > o.weight()) ? 1 
                : (weight() == o.weight()) ? 0 
                : -1;
    }

    @Override
    public String toString() {
        return from + "->" + to + " " + weight + " ";
    }
    
    
    
}
