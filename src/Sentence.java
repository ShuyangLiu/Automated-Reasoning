import java.util.LinkedList;
import java.util.Stack;

public class Sentence
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
        String[] array = s.split(" ");

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

        while(!parserStack.empty()) {
            LogicalOperators ops = parserStack.pop();
            parserList.add(ops);
        }
    }

    //Returns true if the sentence is satisfied by the given model
    public boolean satisfy(Model model)
    {
        Stack<Variable> interpretStack = new Stack<>();
        boolean result = false;

        for (Object aParserList : parserList) {

            //System.out.println(ANSI_YELLOW+"[DEBUG]Item from list: "+aParserList+ANSI_RESET);

            if(aParserList instanceof Variable) {
                interpretStack.push((Variable) aParserList);
            } else {
                if (aParserList==LogicalOperators.NOT) {
                    Variable x1 = interpretStack.pop();

                    //System.out.println(ANSI_RED+"[DEBUG]Variable x1 is "+x1+ANSI_RESET);

                    boolean x;

                    if(!x1.getName().equals("result")) {
                        x = model.find(x1.getName());
                    } else {
                        x = x1.getValue();
                    }

                    result = Syntax.NOT(x);
                    //System.out.println(ANSI_BLUE+"[DEBUG]result: "+result+ANSI_RESET);
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
                    result = Syntax.IMPLY(x,y);
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

            //System.out.println(ANSI_GREEN+"[DEBUG]Variable x is "+x+ANSI_RESET);

            if(!x.getName().equals("result")) {
                return model.find(x.getName());
            } else {
                return x.getValue();
            }
        }

        //System.out.println(ANSI_GREEN+"[DEBUG]result: "+result+ANSI_RESET);

        return result;
    }


}
