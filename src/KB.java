import java.util.ArrayList;

/**
* A Knowledge Base that consists a list of sentences
* */

public class KB
{
    ArrayList<Sentence> sentences;

    public KB(ArrayList<Sentence> sentences)
    {
        this.sentences = sentences;
    }

    public boolean doesEntails(Sentence s)
    {
        return false;
    }
}
