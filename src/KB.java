import java.util.ArrayList;
import java.util.stream.Collectors;

/**
* A Knowledge Base that consists a list of sentences
* */

public class KB
{
    private ArrayList<Sentence> sentences;
    private ArrayList<String> names;

    public KB(ArrayList<Sentence> sentences,ArrayList<String> symbol)
    {
        this.names = symbol;
        //System.out.println(Debug.ANSI_PURPLE+"[DEBUG]names: "+names+Debug.ANSI_RESET);
        this.sentences = sentences;
    }

    public void CNF()
    {
        ArrayList<Sentence>tmp = new ArrayList<>();
        for (Sentence s:sentences) {
            System.out.println(Debug.ANSI_PURPLE+"S: "+s.getSentence()+Debug.ANSI_RESET);
            System.out.println(Debug.ANSI_PURPLE+"S: "+CNFConverter.ListToString(s.getParserList())+Debug.ANSI_RESET);

            ArrayList<Sentence> list = (new CNFConverter(s).get());
            tmp.addAll(list.stream().collect(Collectors.toList()));
        }
        this.sentences = tmp;
    }

    public void printKB()
    {
        System.out.println("Current KB: ");
        for (Sentence s:sentences) {
            System.out.println(Debug.ANSI_YELLOW+s.getSentence()+Debug.ANSI_RESET);
        }
    }

    public void addSentence(Sentence s)
    {
        sentences.add(s);
    }

    @Override
    public String toString() {
        return "KB{" +
                "sentences=" + sentences +
                ", names=" + names +
                '}';
    }

    public ArrayList<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(ArrayList<Sentence> sentences) {
        this.sentences = sentences;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    public boolean PL_TRUE(Model model)
    {
        for(Sentence s : sentences) {
            if(!s.satisfy(model)) {
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
            return (!PL_TRUE(model)) || (s.satisfy(model));
        } else {
            String P = symbols.get(0);
            ArrayList<String> rest;
            if(symbols.size()==1) {
                rest = new ArrayList<>();
            }else {
                rest = new ArrayList<>(symbols.subList(1,symbols.size()));
            }

            Variable p = new Variable(P);
            p.setValue(true);
            boolean r1 = TT_CHECK_ALL(s,rest,model.union(p));

            p = new Variable(P);
            p.setValue(false);
            boolean r2 = TT_CHECK_ALL(s,rest,model.union(p));

            return r1 && r2;
        }
    }
}
