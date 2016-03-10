import java.util.ArrayList;
import java.util.List;


/**
 * A Model is a set of truth assignments (a list of variables)
 * Representing a possible world
 */


public class Model
{
    private List<Variable> model;

    public Model()
    {
        this.model = new ArrayList<>();
    }

    public void union(Variable v)
    {
        this.model.add(v);
    }

    public boolean find(String name)
    {
        for (Variable aModel : this.model) {
            if (aModel.getName().equals(name)) {
                return aModel.getValue();
            }
        }
        return false;
    }
}
