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
        System.out.println("Question: Show that {P, P ⇒ Q} \u22A8 Q");

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
        System.out.println(Debug.ANSI_YELLOW+"\n\nProblem 2: "+Debug.ANSI_RESET);
        System.out.println("Question: Prove whether P1,2 is true or not (that is, whether there is a pit at location [1,2])");

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

        //Problem 3
        System.out.println(Debug.ANSI_YELLOW+"\n\nProblem 3: "+Debug.ANSI_RESET);

        ArrayList<Sentence> sentences2 = new ArrayList<>();
        // If the unicorn is mythical, then it is immortal
        sentences2.add(new Sentence(" mythical IMPLY immortal "));
        // if it is not mythical, then it is a mortal mammal
        sentences2.add(new Sentence(" ( NOT mythical ) IMPLY ( ( NOT immortal ) AND mammal ) "));
        // If the unicorn is either immortal or a mammal, then it is horned
        sentences2.add(new Sentence("( immortal OR mammal ) IMPLY horned"));
        //The unicorn is magical if it is horned
        sentences2.add(new Sentence("horned IMPLY magical"));

        ArrayList<String> symbols2 = new ArrayList<>();
        symbols2.add("mythical");
        symbols2.add("immortal");
        symbols2.add("mammal");
        symbols2.add("horned");
        symbols2.add("magical");

        //Question01: Can we prove that the unicorn is mythical?
        KB kb2 = new KB(sentences2,symbols2);
        Sentence Qa = new Sentence("mythical");
        System.out.println("Question01: Can we prove that the unicorn is mythical?");
            //Basic Model Checking Technique
            System.out.println(Debug.ANSI_CYAN+"Basic Model Checking Technique"+Debug.ANSI_RESET);
            System.out.println(kb2.TT_Entail(Qa));
            //Converting to CNF
            kb2.CNF();
            //Resolution
            System.out.println(Debug.ANSI_PURPLE+"Resolution"+Debug.ANSI_RESET);
            System.out.println(kb2.PL_RESOLUTION(Qa));

        //Question02: Can we prove that the unicorn is magical?
        kb2 = new KB(sentences2,symbols2);
        Sentence Qb = new Sentence("magical");
        System.out.println("\nQuestion02: Can we prove that the unicorn is magical?");
            //Basic Model Checking Technique
            System.out.println(Debug.ANSI_CYAN+"Basic Model Checking Technique"+Debug.ANSI_RESET);
            System.out.println(kb2.TT_Entail(Qb));
            //Converting to CNF
            kb2.CNF();
            //Resolution
            System.out.println(Debug.ANSI_PURPLE+"Resolution"+Debug.ANSI_RESET);
            System.out.println(kb2.PL_RESOLUTION(Qb));

        //Question03: Can we prove that the unicorn is horned?
        kb2 = new KB(sentences2,symbols2);
        Sentence Qc = new Sentence("horned");
        System.out.println("\nQuestion03: Can we prove that the unicorn is horned?");
            //Basic Model Checking Technique
            System.out.println(Debug.ANSI_CYAN+"Basic Model Checking Technique"+Debug.ANSI_RESET);
            System.out.println(kb2.TT_Entail(Qc));
            //Converting to CNF
            kb2.CNF();
            //Resolution
            System.out.println(Debug.ANSI_PURPLE+"Resolution"+Debug.ANSI_RESET);
            System.out.println(kb2.PL_RESOLUTION(Qc));

    }
}
