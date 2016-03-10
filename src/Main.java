/**
 * Main program
 * */

public class Main
{
    public static void main(String args[])
    {
        Sentence s = new Sentence("NOT P OR Q");
        s.printPosfixList();

        //System.out.println(LogicalOperators.NOT.compareTo(LogicalOperators.OR));
    }
}
