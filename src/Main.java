import java.util.ArrayList;

/**
 * Main program
 * */

public class Main
{
    public static void main(String args[])
    {
        /*Sentence s = new Sentence("P");

        Model m = new Model();
        Variable P = new Variable("P");
        P.setValue(true);
        Variable Q = new Variable("Q");
        Q.setValue(false);
        m.add(P);
        m.add(Q);

        System.out.println(s.satisfy(m));*/

        ArrayList<Sentence> sentences = new ArrayList<>();
        sentences.add(new Sentence("P"));
        sentences.add(new Sentence("P IMPLY Q"));

        ArrayList<String> symbols = new ArrayList<>();
        symbols.add("P");
        symbols.add("Q");

        KB kb = new KB(sentences,symbols);

        Sentence q = new Sentence("Q");

        System.out.println(kb.TT_Entail(q));


    }
}
