/**
 * A class of propositional logical syntax
 * */

public class Syntax
{
    public static boolean NOT(boolean var)
    {
        return !var;
    }

    public static boolean AND(boolean a , boolean b)
    {
        return a && b;
    }

    public static boolean OR(boolean a , boolean b)
    {
        return a || b;
    }

    public static boolean IMPLY(boolean a , boolean b) {
        return !a || b;
    }

    public static boolean IFF(boolean a , boolean b)
    {
        return(Syntax.IMPLY(a, b) && Syntax.IMPLY(b, a));
    }
}
