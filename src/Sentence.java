import java.util.LinkedList;
import java.util.Stack;

public class Sentence
{
    private Stack<LogicalOperators> parserStack;
    private LinkedList<Object> parserList;

    private String sentence;

    //Constructor, parse string s into stacks for interpretation in the future
    public Sentence(String s)
    {
        sentence = s;
        parserStack = new Stack<>();
        parserList = new LinkedList<>();
        parse(s);
    }

    public String getSentence() {
        return sentence;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "parserList=" + parserList +
                '}';
    }

    //Helper method for parsing
    private void parse(String s)
    {
        String[] array = s.split("\\s+");
        for (String anArray : array) {
            switch (anArray) {
                case "NOT":
                case "AND":
                case "OR":
                case "IMPLY":
                case "IFF": {
                    LogicalOperators op = LogicalOperators.valueOf(anArray);

                    while (!(parserStack.empty() ||
                            (parserStack.peek().compareTo(LogicalOperators.LP) == 0))) {
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
                case ")": {
                    while (parserStack.peek().compareTo(LogicalOperators.LP) != 0) {
                        LogicalOperators ops = parserStack.pop();
                        parserList.add(ops);
                    }

                    parserStack.pop();
                    break;
                }
                case "": {
                    break;
                }
                default: {
                    Variable v = new Variable(anArray);
                    parserList.add(v);
                    break;
                }
            }
        }

        while(!parserStack.empty()) {
            LogicalOperators ops = parserStack.pop();
            parserList.add(ops);
        }
    }

    public LinkedList<Object> getParserList() {
        return parserList;
    }

    //Returns true if the sentence is satisfied by the given model
    public boolean satisfy(Model model)
    {
        Stack<Variable> interpretStack = new Stack<>();
        boolean result = false;

        for (Object aParserList : parserList) {

            //System.out.println(Debug.ANSI_YELLOW+"[DEBUG]Item from list: "+
            // aParserList+Debug.ANSI_RESET);

            if(aParserList instanceof Variable) {
                interpretStack.push((Variable) aParserList);
            } else {
                if (aParserList==LogicalOperators.NOT) {
                    Variable x1 = interpretStack.pop();

                    //System.out.println(Debug.ANSI_RED+"[DEBUG]Variable x1 is "+x1+
                    // Debug.ANSI_RESET);

                    boolean x;

                    if(!x1.getName().equals("result")) {
                        x = model.find(x1.getName());
                    } else {
                        x = x1.getValue();
                    }

                    result = Syntax.NOT(x);
                    //System.out.println(Debug.ANSI_BLUE+"[DEBUG]result: "+result+
                    // Debug.ANSI_RESET);
                } else if (aParserList==LogicalOperators.AND) {
                    Variable x1 = interpretStack.pop();
                    Variable x2 = interpretStack.pop();
                    boolean x,y;
                    if(!x1.getName().equals("result")) {
                        x = model.find(x1.getName());
                    } else {
                        x = x1.getValue();
                    }
                    if(!x2.getName().equals("result")) {
                        y = model.find(x2.getName());
                    } else {
                        y = x2.getValue();
                    }
                    result = Syntax.AND(x,y);
                } else if (aParserList==LogicalOperators.OR) {
                    Variable x1 = interpretStack.pop();
                    Variable x2 = interpretStack.pop();
                    boolean x,y;
                    if(!x1.getName().equals("result")) {
                        x = model.find(x1.getName());
                    } else {
                        x = x1.getValue();
                    }
                    if(!x2.getName().equals("result")) {
                        y = model.find(x2.getName());
                    } else {
                        y = x2.getValue();
                    }
                    result = Syntax.OR(x,y);
                } else if (aParserList==LogicalOperators.IMPLY) {
                    Variable x1 = interpretStack.pop();
                    Variable x2 = interpretStack.pop();
                    boolean x,y;
                    if(!x1.getName().equals("result")) {
                        x = model.find(x1.getName());
                    } else {
                        x = x1.getValue();
                    }
                    if(!x2.getName().equals("result")) {
                        y = model.find(x2.getName());
                    } else {
                        y = x2.getValue();
                    }
                    result = Syntax.IMPLY(y,x);
                } else {
                    Variable x1 = interpretStack.pop();
                    Variable x2 = interpretStack.pop();
                    boolean x,y;
                    if(!x1.getName().equals("result")) {
                        x = model.find(x1.getName());
                    } else {
                        x = x1.getValue();
                    }
                    if(!x2.getName().equals("result")) {
                        y = model.find(x2.getName());
                    } else {
                        y = x2.getValue();
                    }
                    result = Syntax.IFF(x,y);
                }

                Variable r = new Variable("result");
                r.setValue(result);
                interpretStack.push(r);
            }
        }

        if(!interpretStack.isEmpty()) {
            Variable x = interpretStack.pop();

            //System.out.println(Debug.ANSI_GREEN+"[DEBUG]Variable x is "+x+
            // Debug.ANSI_RESET);

            if(!x.getName().equals("result")) {
                return model.find(x.getName());
            } else {
                return x.getValue();
            }
        }

        //System.out.println(Debug.ANSI_GREEN+"[DEBUG]result: "+result+
        // Debug.ANSI_RESET);

        return result;
    }


}
