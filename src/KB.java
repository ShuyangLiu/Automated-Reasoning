import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
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
        for (Sentence s:sentences){
            String m = s.getSentence();
            int start;
            int end;
            for(start=0; start<m.length(); start++) if (m.charAt(start) != ' ') break;
            for(end=m.length()-1; end>=0; end--) if (m.charAt(end) != ' ') break;
            m = m.substring(start,end+1);
            s.setSentence(m);
        }
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

    public static boolean canResolve(Sentence c1, Sentence c2)
    {
        //Determine if two clauses can be resolved
        //Assume c1 and c2 are in CNF
        LinkedList<Object> l1 = c1.getParserList();
        LinkedList<Object> l2 = c2.getParserList();

        Collection<Object> list = new ArrayList<>(l1);

        Iterator<Object> iterator = list.iterator();
        while(iterator.hasNext()){
            Object o = iterator.next();
            if (!l2.contains(o)){
                iterator.remove();
            }
        }

        if(list.size()!=0){
            for (Object object:list) {
                if(object instanceof  Variable) {
                    if ((l1.get(l1.indexOf(object) + 1) == LogicalOperators.NOT &&
                            l2.get(l2.indexOf(object) + 1) != LogicalOperators.NOT) ||
                            (l1.get(l1.indexOf(object) + 1) != LogicalOperators.NOT &&
                                    l2.get(l2.indexOf(object) + 1) == LogicalOperators.NOT)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Nullable
    public static String PL_RESOLVE(Sentence c1, Sentence c2)
    {
        if(!canResolve(c1,c2)){
            return null;
        }

        ArrayList<Var> s1 = new ArrayList<>();
        ArrayList<Var> s2 = new ArrayList<>();
        ArrayList<Var> s3 = new ArrayList<>();

        LinkedList<Object> l1 = c1.getParserList();
        LinkedList<Object> l2 = c2.getParserList();

        for(int i=0; i<l1.size(); i++) {
            if(l1.get(i) instanceof Variable){
                if(l1.get(i+1) == LogicalOperators.NOT){
                    Var s = new Var(false,((Variable) l1.get(i)).getName());
                    s1.add(s);
                    s3.add(s);
                } else {
                    Var s = new Var(true,((Variable) l1.get(i)).getName());
                    s1.add(s);
                    s3.add(s);
                }
            }
        }
        for(int i=0; i<l2.size(); i++) {
            if(l2.get(i) instanceof Variable){
                if(l2.get(i+1) == LogicalOperators.NOT){
                    Var s = new Var(false,((Variable) l2.get(i)).getName());
                    s2.add(s);
                    s3.add(s);
                } else {
                    Var s = new Var(true,((Variable) l2.get(i)).getName());
                    s2.add(s);
                    s3.add(s);
                }
            }
        }

        for (Var aS1 : s1) {
            s2.stream().filter(aS2 -> Var.isComplement(aS1, aS2)).forEach(aS2 -> {
                s3.remove(aS1);
                s3.remove(aS2);
            });
        }

        for (Var aS1 : s1) {
            s2.stream().filter(aS1::equals).forEach(aS2 -> s3.remove(aS1));
        }

        String sentence = "";

        for (Var aS3 : s3) {
            sentence = sentence + " "+ aS3.getName() + " " + " OR ";
        }

        if(!sentence.equals("")) {
            sentence = sentence.substring(0,sentence.length()-4);
        }

        return sentence;

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
