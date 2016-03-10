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

    public List<Variable> getModel() {
        return model;
    }

    public void setModel(List<Variable> model) {
        this.model = model;
    }
}
