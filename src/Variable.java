/**
 * Variable (Symbol) class
 * Author: Shuyang Liu
 * */

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
        return "Variable{" +
                "name='" + name + '\'' +"\n"+
                ", value=" + value +"\n"+
                '}'+"\n";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Variable) {
            if (this.getName().equals(((Variable) obj).getName())) {
                return true;
            }
        }
        return super.equals(obj);
    }
}
