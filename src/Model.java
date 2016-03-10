import java.util.ArrayList;
import java.util.List;


/**
 * A Model is a set of truth assignments (a list of variables)
 * Representing a possible world
 */


public class Model
{
    /*For debugging using colors in console*/
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private List<Variable> model;

    public Model()
    {
        this.model = new ArrayList<>();
    }

    public Model union(Variable v)
    {
        Model n = new Model();
        this.model.forEach(n::add);
        n.add(v);

        return n;
    }

    public void add(Variable v)
    {
        this.model.add(v);
    }

    public boolean find(String name)
    {
        for (Variable aModel : this.model) {
            if (aModel.getName().equals(name)) {

                //System.out.println(ANSI_CYAN+"[DEBUG]find: "+name+" is "+aModel.getValue()+ANSI_RESET);

                return aModel.getValue();
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Model{" +
                "model=" + model +
                '}';
    }
}
