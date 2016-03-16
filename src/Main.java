import java.util.ArrayList;

/**
 * Main program
 * Author: Shuyang Liu
 * */

public class Main
{
    public static void main(String args[])
    {
        //Problem 1
        System.out.println(Debug.ANSI_YELLOW+"Problem 1: "+Debug.ANSI_RESET);

        ArrayList<Sentence> sentences0 = new ArrayList<>();
        sentences0.add(new Sentence("P"));
        sentences0.add(new Sentence("P IMPLY Q"));

        ArrayList<String> symbols0 = new ArrayList<>();
        symbols0.add("P");
        symbols0.add("Q");

        KB kb0 = new KB(sentences0,symbols0);
        Sentence q1 = new Sentence("Q");

        //Basic Model Checking Technique
        System.out.println(Debug.ANSI_CYAN+"Basic Model Checking Technique"+Debug.ANSI_RESET);
        System.out.println(kb0.TT_Entail(q1));
        //Converting to CNF
        kb0.CNF();
        //Resolution
        System.out.println(Debug.ANSI_PURPLE+"Resolution"+Debug.ANSI_RESET);
        System.out.println(kb0.PL_RESOLUTION(q1));


        //Problem 2
        System.out.println(Debug.ANSI_YELLOW+"Problem 2: "+Debug.ANSI_RESET);

        ArrayList<Sentence> sentences1 = new ArrayList<>();
        sentences1.add(new Sentence("NOT P11"));
        sentences1.add(new Sentence("B11 IFF ( P12 OR P21 )"));
        sentences1.add(new Sentence("NOT B11"));
        sentences1.add(new Sentence("B21 IFF ( P11 OR P22 OR P31 )"));
        sentences1.add(new Sentence("B21"));

        ArrayList<String> symbols1 = new ArrayList<>();
        symbols1.add("P11");
        symbols1.add("B11");
        symbols1.add("P12");
        symbols1.add("P21");
        symbols1.add("P31");
        symbols1.add("B21");
        symbols1.add("P22");

        KB kb1 = new KB(sentences1,symbols1);
        Sentence q2 = new Sentence("P12");
        //Basic Model Checking Technique
        System.out.println(Debug.ANSI_CYAN+"Basic Model Checking Technique"+Debug.ANSI_RESET);
        System.out.println(kb1.TT_Entail(q2));
        //Converting to CNF
        kb1.CNF();
        //Resolution
        System.out.println(Debug.ANSI_PURPLE+"Resolution"+Debug.ANSI_RESET);
        System.out.println(kb1.PL_RESOLUTION(q2));

        

    }
}
