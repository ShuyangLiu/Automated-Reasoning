/**
 * Main program
 * */

public class Main
{
    public static void main(String args[])
    {
        Sentence s = new Sentence("NOT ( NOT P OR Q )");
        s.printPostfixList();
        System.out.println();

        Model m = new Model();
        Variable P = new Variable("P");
        P.setValue(false);
        Variable Q = new Variable("Q");
        Q.setValue(false);
        m.union(P);
        m.union(Q);

        System.out.println(s.satisfy(m));
    }
}
