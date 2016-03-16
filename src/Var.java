import java.util.Comparator;

public class Var implements Comparable<Var>, Comparator<Var>
{
    private boolean positive;

    public Var(boolean positive, String name) {
        this.positive = positive;
        this.name = name;
    }

    public Var(){
        //an empty constructor if used as a comparator
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }

    private String name;

    @Override
    public int compare(Var o1, Var o2) {
        return o1.compareTo(o2);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Var)){
            return false;
        }
        Var v2 = (Var) obj;
        return (this.getName().equals(v2.getName())) &&
                (this.isPositive() == v2.isPositive());
    }

    public static boolean isComplement(Var v1, Var v2)
    {
        return (v1.getName().equals(v2.getName())) &&
                (v1.isPositive() != v2.isPositive());
    }

    @Override
    public String toString() {
        return name+" : "+positive+"\n";
    }

    @Override
    public int compareTo(Var o) {
        return this.name.compareTo(o.getName());
    }
}
