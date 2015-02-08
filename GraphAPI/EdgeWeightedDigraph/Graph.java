package GraphAPI.EdgeWeightedDigraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public class Graph {
    int v;
    int e;
    List<Edge>[] adj;
    double[] disTo;
    Edge[] parent;
    PriorityQueue<Integer> pq;

    public Graph(int n) {
        this.v = n;
        adj = new List[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new LinkedList<>();
        }
    }
    /* constructor from a file */ 
    public Graph(File file) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            v = Integer.parseInt(reader.readLine()); //number of elements (1st line)
            e = Integer.parseInt(reader.readLine()); //number of edges (2nd line)

            adj = new List[v];
            for (int i = 0; i < v; i++) {
                adj[i] = new LinkedList<>();
            }

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s");
                int from = Integer.parseInt(parts[0]);
                int to = Integer.parseInt(parts[1]);
                double weight = Double.parseDouble(parts[2]);
                adj[from].add(new Edge(from, to, weight));
            }
            reader.close();

        } catch (IOException ex) {
        }
    }

    /* add a new edge */
    public void addEdge(Edge e) {
        int v = e.from();
        adj[v].add(e);
    }

    /* relax an edge */
    public boolean relax(Edge e) {
        double newWeight = disTo[e.from()] + e.weight();
        if (disTo[e.to()] > newWeight) {
            disTo[e.to()] = newWeight;
            parent[e.to()] = e;
            return true;
        }
    return false;
    }

    /* relax an element */
    public void relax(int x) {
        for (Edge e : adj[x]) {
            if (relax(e)) {
                if(pq.contains(e.to())){
                    pq.remove(e.to());
                }
                pq.offer(e.to());
            }
        }
    }
    
    /* Dijkstra's algorithm shortest path */
    public void dijkstraSP(int s){
        disTo = new double[v];
        parent = new Edge[v];
        pq = new PriorityQueue<>();
        
        Arrays.fill(disTo, Double.POSITIVE_INFINITY);
        disTo[s] = 0;
        pq.offer(s);
        while(!pq.isEmpty()){
            relax(pq.remove());
        }
    }
    
    /* check if there is path between a and b */
    public boolean hasPathTo(int from, int to){
        dijkstraSP(from);
        return (disTo[to] < Double.POSITIVE_INFINITY);
    }
    
    /* return the shortest path from a to b */
    public Stack<Edge> getShortestPath(int a, int b){
        if(!hasPathTo(a, b)) 
            return null;
        
        Stack<Edge> path = new Stack<>();
        Edge tmp ;
        while((tmp = parent[b]) != null){
            path.push(tmp);
            b = tmp.from();
        }
        return path;
    }
    
    /* print the graph (obviously) */
    public void print(){
        for (int i = 0; i < v; i++) {
            System.out.print(i + " -> ");
            adj[i].stream().forEach(x->{System.out.print( x + " ");});
            System.out.println("");
        }
    }

    /* testing */
    public static void main(String[] args) {
        File file = new File("src/GraphAPI/EdgeWeightedDigraph/data.txt");
        Graph g = new Graph(file);

        System.out.println("------- Graph -------");
        g.print();
        
        int from=0, to=6;
        System.out.println("------- getShortestPath from " +from+ " To " +to+" -------");
        Stack<Edge> path = g.getShortestPath(0, 6);
        System.out.print(from);
        while (!path.isEmpty()) {            
            System.out.print(" -> " + path.pop().to());
        }
        
        System.out.println("Done");
    }
}




