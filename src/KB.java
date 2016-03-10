import java.util.ArrayList;

/**
* A Knowledge Base that consists a list of sentences
* */

public class KB
{
    private ArrayList<Sentence> sentences;
    private ArrayList<String> names;

    public KB(ArrayList<Sentence> sentences,ArrayList<String> names)
    {
        this.names = names;
        this.sentences = sentences;
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

    public boolean TT_CHECK_ALL(Sentence s, ArrayList<String> names,Model model)
    {
        if(names.isEmpty()) {
            return !this.PL_TRUE(model) || s.satisfy(model);
        } else {
            String P = names.get(0);
            ArrayList<String> rest = new ArrayList<>(names.subList(1,names.size()-1));

            Variable p = new Variable(P);
            p.setValue(true);
            boolean r1 = TT_CHECK_ALL(s,rest,model.union(p));
            p.setValue(false);
            boolean r2 = TT_CHECK_ALL(s,rest,model.union(p));
            return r1 && r2;
        }
    }
}
