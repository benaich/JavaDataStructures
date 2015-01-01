package HuffmanCode;

import java.io.Serializable;

public class Paire implements Comparable<Paire>, Serializable {

    int fr;
    int car;

    public Paire(int fr, int car) {
        this.fr = fr;
        this.car = car;
    }

    public Paire(int fr) {
        this.fr = fr;
    }

    public int compareTo(Paire p) {
        if (fr > p.fr) {
            return 1;
        } else if (fr < p.fr) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return car+"("+fr+")";
    }
    
}
