package StableMarriage;
import java.util.*;
public class StableMarriage {

    public static void main(String[] args) {
        
        Person man1 = new Person("man1");
        Person man2  = new Person("man2");
        Person man3  = new Person("man3");

        Person woman1 = new Person("woman1");
        Person woman2  = new Person("woman2");
        Person woman3  = new Person("woman3");
    
        man1.setCandidates(woman2, woman3, woman1);
        man2.setCandidates(woman3, woman2, woman1);
        man3.setCandidates(woman2, woman1, woman3);
        
        woman1.setCandidates(man2, man1, man3);
        woman2.setCandidates(man3, man1, man2);
        woman3.setCandidates(man2, man1, man3);        
        
        List<Person> women = new ArrayList<>();
        women.addAll(Arrays.asList(woman1, woman2, woman3));
        
        engageEveryone(women);
        displayMarriage(women);
    }
    
    /* check if this mariage is stable */
    public static boolean isStable(List<Person> women, List<Person> men) {
        for (Person women1 : women) {
            for (Person men1 : men) {
                if (women1.prefers(men1) && men1.prefers(women1)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /* engage all women acording to their preferences */
    public static void engageEveryone(List<Person> women) {
        boolean done;
        do {
            done = true;
            for (Person w : women) {
                if (w.getPartner() == null) {
                    done = false;
                    Person man = w.getNext();
                    if (man.getPartner() == null || man.prefers(w))
                        w.engageTo(man);
                }
            }
        } while (!done);
    }
    
    /* print the couples name */
    private static void displayMarriage(List<Person> women) {
        for (Person woman : women) {
            System.out.println("( " + woman.getName() + ", " + woman.getPartner().getName() + " )");
        }
    }
}
