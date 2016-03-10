
public class Variable
{
    private String name;
    private boolean value;

    //Initially the truth value is not specified
    public Variable(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public boolean getValue()
    {
        return value;
    }

    public void setValue(boolean value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "\n"+"Variable{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}'+"\n";
    }
}
