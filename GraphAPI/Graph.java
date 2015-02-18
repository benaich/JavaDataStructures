package GraphAPI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Graph {

    int v, e, connex;
    List<Integer>[] adj;
    boolean[] marked, color;
    int[] parent, id;
    boolean bipart = true, isCycle = false;
    Stack<Integer> reverseOrder;

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
                adj[from].add(to);
            }
            reader.close();

        } catch (IOException ex) {
        }
    }
    
    /* add edge for directed graph */
    public void addEdge(int v, int w)
    {
        adj[v].add(w);
    }
    
    public void init() {
        parent = new int[v];
        id = new int[v];
        marked = new boolean[v];
        color = new boolean[v];
        connex = 0;
        reverseOrder = new Stack<>();
    }

    /* Depth-First Search */
    public void dfs(int s) {
        id[s] = connex;
        marked[s] = true;
        adj[s].stream().forEach((x) -> {
            if (!marked[x]) {
                color[x] = !color[s];
                parent[x] = s;
                dfs(x);
            } else if (color[x] = color[s]) {
                bipart = false;
            } else {
                //not working need dfs(s, origin)
                isCycle = true;
            }
        });
        if(!adj[s].isEmpty())
            reverseOrder.push(s);
    }

    /* start a Depth-First Search */
    public void exProfondeur(int s) {
        init();
        dfs(s);
    }
    
    /* ckeck if a path exist between a and b */
    public boolean pathExist(int a, int b) {
        exProfondeur(a);
        return marked[b];
    }

    /* get a path between a and b */
    public Stack<Integer> getPath(int a, int b)
    {
        if (!pathExist(a, b)) return null;
        Stack<Integer> path = new Stack<>();
        
        while (a != b) {
            path.push(b);
            b = parent[b];
        }
        path.push(a);
        return path;
    }


    /* check if all element of the graph are connected */
    public boolean isConnexe() {
        exProfondeur(adj[0].get(0));
        for (boolean isMarked : marked) {
            if (!isMarked) {
                return false;
            }
        }
        return true;
    }

    /* count numbre of connexe */
    public void countConnexe() {
        for (int i = 0; i < v; i++) {
            if (!marked[i]) {
                dfs(i);
                connex++;
            }
        }
    }

    /* check if two components are connected */
    public boolean areConnected(int C1, int C2) {
        return id[C1] == id[C2];
    }

    /* get connexe by id */
    public List<Integer> getConnexe(int v) {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < v; i++) {
            if (id[i] == v) {
                list.add(i);
            }
        }
        return list;
    }

    /* get all connected componenets */
    public Map<Integer, List<Integer>> getAllConnexe(int v) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < id.length; i++) {
            List<Integer> l = map.get(id[i]);
            if (l == null) {
                l = new LinkedList<>();
            }
            l.add(i);
            map.put(id[i], l);
        }
        return map;
    }

    /* check if this graph has a cycle */
    public boolean hasCycle() {
        init();
        for (int i : id) {
            dfs(i);
        }
        return isCycle;
    }

    /* still figuring it out */
    boolean bipart() {
        init();
        dfs(0);
        return bipart;
    }

    /* breadth first search */
    public void bfs(int s) {
        LinkedList<Integer> q = new LinkedList<>();
        q.addLast(s);
        marked[s] = true;
        while (!q.isEmpty()) {
            s = q.getFirst();
            for (int w : adj[s]) {
                if (!marked[w]) {
                    q.addLast(w);
                    parent[w] = s;
                    marked[w] = true;
                }
            }
        }
    }
    
    public Stack<Integer> topologicalOrder(){
        init();
        for (int i = 0; i < v; i++) {
            if(!marked[i]) dfs(i);
        }
        return reverseOrder;
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
        File file = new File("src/GraphAPI/data.txt");
        Graph g = new Graph(file);
        
        System.out.println("------- Graph -------");
        g.print();
        
        System.out.println("------- hasCycle -------");
        if(g.hasCycle())
            System.out.println("yes");
        else System.out.println("no");
        
        System.out.println("------- pathExist(0, 4) -------");
        if(g.pathExist(0, 4))
            System.out.println("yes");
        else System.out.println("no");

        System.out.println("------- pathExist(3, 0) -------");
        if(g.pathExist(3, 0))
            System.out.println("yes");
        else System.out.println("no");

        System.out.println("------- path(3, 0) -------");
        Stack<Integer> path1 = g.getPath(3,0);
        while (!path1.isEmpty()) {            
            System.out.print(path1.pop() + " -> ");
        }
        System.out.println("");
        
        System.out.println("------- isConnexe -------");
        if(g.isConnexe())
            System.out.println("yes");
        else System.out.println("no");
        
        System.out.println("------- countConnex -------");
        g.countConnexe();
        System.out.println(g.connex);
        
        System.out.println("------- topologicalOrder -------");
        Stack<Integer> path = g.topologicalOrder();
        while (!path.isEmpty()) {            
            System.out.print(path.pop() + " -> ");
        }
        System.out.println("");
        

    }

}
