import java.util.ArrayList;

/**
 * Main program
 * */

public class Main
{
    public static void main(String args[])
    {
//        Sentence s = new Sentence("NOT P");
//
//        Model m = new Model();
//        Variable P = new Variable("P");
//        P.setValue(false);
//        Variable Q = new Variable("Q");
//        Q.setValue(false);
//        Variable W = new Variable("W");
//        W.setValue(false);
//        m.add(P);
//        m.add(Q);
//        m.add(W);
//
//        System.out.println("[DEBUG]Sentence List: "+s);
//        System.out.println(s.satisfy(m));

//        ArrayList<Sentence> sentences = new ArrayList<>();
//        sentences.add(new Sentence("NOT P11"));
//        sentences.add(new Sentence("B11 IFF ( P12 OR P21 )"));
//        sentences.add(new Sentence("NOT B11"));
//        sentences.add(new Sentence("B21 IFF ( P11 OR P22 OR P31 )"));
//        sentences.add(new Sentence("B21"));
//
//
//        ArrayList<String> symbols = new ArrayList<>();
//        symbols.add("P11");
//        symbols.add("B11");
//        symbols.add("P12");
//        symbols.add("P21");
//        symbols.add("P31");
//        symbols.add("B21");
//        symbols.add("P22");
//
//        KB kb = new KB(sentences,symbols);
//
//        Sentence q = new Sentence("P12");
//
//        System.out.println(kb.TT_Entail(q));

//        CNFConverter convert = new CNFConverter(new Sentence("A IFF B"));
//        convert.printClauses();

        //System.out.print(LogicalOperators.AND.toString());

        Sentence s = new Sentence("( A IMPLY B ) OR ( C AND D )");
        System.out.println(CNFConverter.ListToString(s.getParserList()));
        CNFConverter convert = new CNFConverter(s);
        convert.printClauses();

        //System.out.println(CNFConverter.modify("( ( A OR C ) AND D )"));
    }
}
