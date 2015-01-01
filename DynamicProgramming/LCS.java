package DynamicProgramming;

public class LCS {

    public static enum Direction {

        H, V, O;
    };

    public class Paire {

        public int cal;
        public Direction d;

        public Paire(int cal, Direction d) {
            this.cal = cal;
            this.d = d;
        }

        public Paire() {
            this.cal = 0;
        }

        @Override
        public String toString() {
            return cal + "-" + d;
        }
    }

    Paire[][] p;
    String u, v;

    public LCS(String u, String v) {
        this.u = u;
        this.v = v;
        p = new Paire[u.length() + 1][v.length() + 1];

        for (int i = 0; i < u.length() + 1; i++) {
            for (int j = 0; j < v.length() + 1; j++) {
                p[i][j] = new Paire();
            }
        }
    }

    public void init() {
        for (int i = 1; i < u.length() + 1; i++) {
            for (int j = 1; j < v.length() + 1; j++) {
                if (u.charAt(i - 1) == v.charAt(j - 1)) {
                    p[i][j].cal = 1 + p[i - 1][j - 1].cal;
                    p[i][j].d = Direction.O;
                } else if (p[i - 1][j].cal > p[i][j - 1].cal) {
                    p[i][j].cal = p[i - 1][j].cal;
                    p[i][j].d = Direction.V;
                } else {
                    p[i][j].cal = p[i][j - 1].cal;
                    p[i][j].d = Direction.H;
                }
            }
        }
    }

    public String result(int i, int j) {
        if (p[i][j].cal == 0) {
            return "";
        }
        if (p[i][j].d == Direction.O) {
            return result(i - 1, j - 1) + u.charAt(i - 1);
        }
        if (p[i][j].d == Direction.H) {
            return result(i, j - 1);
        }
        return result(i - 1, j);
    }
    
    public static String run(String u, String v){
        LCS app = new LCS(u,v);
        app.init();
        return app.result(u.length(), v.length());
    }

    public void print() {
        System.out.print("\t\t");
        for (int i = 1; i < v.length() + 1; i++) {
            System.out.print(v.charAt(i - 1) + "\t");
        }
        System.out.println("");
        for (int i = 0; i < u.length() + 1; i++) {
            if (i == 0) {
                System.out.print("\t");
            } else {
                System.out.print(u.charAt(i - 1) + "\t");
            }
            for (int j = 0; j < v.length() + 1; j++) {
                System.out.print(p[i][j] + "\t");
            }
            System.out.println("");
        }
    }
    public static String doYouMean(String query, String[] text){
        String result[] = new String[text.length];
        for (int i = 0; i < text.length; i++) {
            result[i] = LCS.run(query, text[i]);
        }
        int max = 0;
        for (int i = 1; i < result.length; i++) {
            if(result[max].length() < result[i].length())
                max = i;
        }
        
        return text[max];
    }
    public static void main(String[] args) {
        System.out.println(LCS.run("abcdbdab","bdcaba" ));
        
        String dbText[] = new String[]{"house of cards", "game of throne", "breaking bad"};
        
        System.out.println("Do you mean " + LCS.doYouMean("gamz of trone", dbText));
    }

}
