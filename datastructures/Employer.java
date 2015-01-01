package datastructures;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Employer {

    public static enum Gender {
        H, F
    };
    private long id;
    private String name;
    private int age;
    private Gender gender;
    private double salary;

    public Employer(long id, String name, int age, Gender gender, double salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.salary = salary;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public double getSalary() {
        return salary;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employer{" + "id=" + id + ", name=" + name + ", age=" + age + ", gender=" + gender + ", salary=" + salary + '}';
    }

    public static List<Employer> employerList() {
        Employer e1 = new Employer(5, "adam", 22, Employer.Gender.H, 5000);
        Employer e2 = new Employer(6, "mohamed", 23, Employer.Gender.H, 6000);
        Employer e3 = new Employer(7, "sara", 24, Employer.Gender.F, 6000);
        Employer e4 = new Employer(8, "maha", 25, Employer.Gender.F, 8000);
        Employer e5 = new Employer(8, "Ema", 25, Employer.Gender.F, 4000);

        List<Employer> employer = Arrays.asList(e1, e2, e3, e4, e5);
        return employer;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        List<Employer> l = Employer.employerList();

        System.out.println("--- test 1 ---");
        l.stream().forEach(System.out::println);

        System.out.println("--- test 2 ---");
        l.stream().filter(x -> x.getGender() == Employer.Gender.F).forEach(System.out::println);

        System.out.println("--- test 3 ---");
        Consumer<Employer> c1 = x->x.setSalary(x.getSalary() * 1.5);
        c1 = c1.andThen(x -> System.out.println(x.getSalary()));
        l.stream().filter(x -> x.getGender() == Employer.Gender.F)
                .peek(x -> System.out.println("peek -> "+x))
                .forEach(c1);
        
        System.out.println("--- test 4 ---");
        l.stream().sorted(Comparator.comparing(Employer::getSalary)).forEach(System.out::println);
        
        System.out.println("--- test 5 ---"); 
        List<Employer> l1 = l.stream().sorted(Comparator.comparing(Employer::getSalary)).collect(Collectors.toList());
        l1.stream().forEach(System.out::println);
    }
}
