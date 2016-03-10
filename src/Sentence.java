import java.util.LinkedList;
import java.util.Stack;

public class Sentence
{
    private Stack<LogicalOperators> parserStack;
    private LinkedList<Object> parserList;

    private Stack<Variable> interpretStack;

    //Constructor, parse string s into stacks for interpretation in the future
    public Sentence(String s)
    {
        parserStack = new Stack<>();
        parserList = new LinkedList<>();
        parse(s);
    }
    //Helper method for parsing
    private void parse(String s)
    {
        String[] array = s.split(" ");

        for (String anArray : array) {
            switch (anArray) {
                case "NOT":
                case "AND":
                case "OR":
                case "IMPLY":
                case "IFF": {
                    LogicalOperators op = LogicalOperators.valueOf(anArray);

                    while (!(parserStack.empty() || (parserStack.peek().compareTo(LogicalOperators.LP) == 0))) {
                        if (parserStack.peek().compareTo(op) < 0 ||
                                (parserStack.peek().compareTo(op) == 0 && op != LogicalOperators.NOT)) {
                            LogicalOperators ops = parserStack.pop();
                            parserList.add(ops);
                        } else {
                            break;
                        }
                    }

                    parserStack.push(op);
                    break;
                }
                case "(": {
                    LogicalOperators op = LogicalOperators.valueOf("LP");
                    parserStack.push(op);
                    break;
                }
                case ")":
                    while (parserStack.peek().compareTo(LogicalOperators.LP) != 0) {
                        LogicalOperators ops = parserStack.pop();
                        parserList.add(ops);
                    }

                    parserStack.pop();
                    break;
                default:
                    Variable v = new Variable(anArray);
                    parserList.add(v);
                    break;
            }
        }

        while(!parserStack.empty())
        {
            LogicalOperators ops = parserStack.pop();
            parserList.add(ops);
        }
    }

    //Helper Method for debugging
    public void printPosfixList()
    {

        for (Object aParserList : parserList) {
            System.out.print(aParserList + "\t");
        }
    }



    //Returns true if the sentence is satisfied by the given model
    public boolean satisfy(Model model)
    {
        return false;
    }


}
