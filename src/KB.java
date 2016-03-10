import java.util.ArrayList;

/**
* A Knowledge Base that consists a list of sentences
* */

public class KB
{
    /*For debugging using colors in console*/
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private ArrayList<Sentence> sentences;
    private ArrayList<String> names;

    public KB(ArrayList<Sentence> sentences,ArrayList<String> symbol)
    {
        this.names = symbol;
        //System.out.println(ANSI_PURPLE+"[DEBUG]names: "+names+ANSI_RESET);
        this.sentences = sentences;
    }

    public boolean PL_TRUE(Model model)
    {
        for(Sentence s : sentences) {
            if(!s.satisfy(model)) {

//                System.out.println(ANSI_YELLOW + "[DEBUG]"+
//                        new Object(){}.getClass().getEnclosingMethod().getName()+": " +
//                            "this sentence: "+s.getSentence() +
//                            " does not satisfy model: "+model + ANSI_RESET);

                return false;
            }
        }
        return true;
    }

    public boolean TT_Entail(Sentence s)
    {
        return TT_CHECK_ALL(s,this.names,new Model());
    }

    public boolean TT_CHECK_ALL(Sentence s, ArrayList<String> symbols,Model model)
    {
        if(symbols.isEmpty()) {
//            System.out.println("[DEBUG]" + this.getClass() + ": " + "model is " + model);
//            System.out.println("[DEBUG]" + this.getClass() + ": " + "KB is " + PL_TRUE(model));
//            System.out.println("[DEBUG]" + this.getClass() + ": " + "s is " + s.satisfy(model));

            return (!PL_TRUE(model)) || (s.satisfy(model));
        } else {
            String P = symbols.get(0);

            //System.out.println("[DEBUG]" + this.getClass() + ": " + "P is " + P);

            ArrayList<String> rest;

            if(symbols.size()==1) {
                rest = new ArrayList<>();
            }else {
                rest = new ArrayList<>(symbols.subList(1,symbols.size()));
            }

            //System.out.println(ANSI_CYAN+"[DEBUG]"+"rest is: "+rest+ANSI_RESET);

            Variable p = new Variable(P);
            p.setValue(true);
            boolean r1 = TT_CHECK_ALL(s,rest,model.union(p));

            //System.out.println(ANSI_CYAN+"[DEBUG]"+"rest is: "+rest+ANSI_RESET);

            p = new Variable(P);
            p.setValue(false);
            boolean r2 = TT_CHECK_ALL(s,rest,model.union(p));

            return r1 && r2;
        }
    }
}
