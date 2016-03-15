import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
/**
 * CNF converter
 * */
public class CNFConverter
{
    public Stack<Sentence> clauses;
    private Stack<Object> tmp;

    //constructor
    public CNFConverter(Sentence s)
    {
        clauses = new Stack<>();
        tmp = new Stack<>();
        clauses.push(s);
        convert();
        separate();
    }

    public ArrayList<Sentence> get()
    {
        ArrayList<Sentence> list = new ArrayList<>();
        while(!tmp.empty()) {
            tmp.pop();
        }
        while(!clauses.empty()) {
            list.add(clauses.peek());
            tmp.push(clauses.pop());
        }
        while(!tmp.empty()) {
            clauses.push((Sentence) tmp.pop());
        }
        return list;
    }

    private void convert()
    {
        doubleImplication();
        singleImplication();
        negation();
        Sentence sentence = distribute(clauses.pop().getParserList());
        clauses.push(sentence);
        printClause();
    }

    private void separate()
    {
        Sentence sentence = clauses.pop();
        String s = sentence.getSentence();
        ArrayList<String> modified = modify(s);
        for (String clause:modified) {
            //System.out.println("[DEBUG] clause :: "+clause);
            clauses.push(new Sentence(clause));
        }
    }

    public void printClauses()
    {
        while(!tmp.empty()) {
            tmp.pop();
        }
        while(!clauses.empty()) {
            System.out.println(Debug.ANSI_CYAN+clauses.peek().getSentence()+Debug.ANSI_RESET);
            tmp.push(clauses.pop());
        }
        while(!tmp.empty()) {
            clauses.push((Sentence) tmp.pop());
        }
    }

    private static String LPRP(String s)
    {
        int LP = 0;
        int RP = 0;

        for(int i=0; i<s.length(); i++) {
            if(s.charAt(i)=='(') {
                LP++;
            }
            if(s.charAt(i)==')') {
                RP++;
            }
        }

        if(LP>RP) {
            int i = LP - RP;
            while(i>0){
                for(int q=0; q<s.length(); q++) {
                    if(s.charAt(q)=='(') {
                        s = s.substring(q+1);
                        i--;
                        break;
                    }
                }
            }
        }

        if(LP<RP) {
            int i = RP-LP;
            while(i>0){
                for(int q=s.length()-1; q>=0; q--) {
                    if(s.charAt(q)==')') {
                        s = s.substring(0,q-1);
                        i--;
                        break;
                    }
                }
            }
        }

        return s;
    }

    public static ArrayList<String> modify(String clause)
    {
        ArrayList<String> list = new ArrayList<>();
        if(clause.contains("AND")) {
            String[] c = clause.split("AND");
            for (String C:c) {
                C = LPRP(C);
                //System.out.println("[DEBUG] C: "+C);
                list.add(C);
            }
        } else {
            list.add(clause);
        }

        return list;
    }

    private void doubleImplication()
    {
        while(!clauses.empty()) {
            Sentence s = clauses.pop();
            for(Object aParserList : s.getParserList()) {
                if (aParserList instanceof Variable) {
                    Sentence sub = new Sentence(((Variable) aParserList).getName());
                    tmp.push(sub);
                } else if (aParserList == LogicalOperators.IFF) {
                    //(a <=> b) == (a => b) ^ (b <= a)
                    Sentence B = (Sentence) tmp.pop();
                    Sentence A = (Sentence) tmp.pop();
                    String a = A.getSentence();
                    String b = B.getSentence();
                    String n = " (" +" (" + " " + a + " " + "IMPLY" + " " + b + " " + ")" + " " + "AND" +
                            " " + "(" + " " + b + " " + "IMPLY" + " " + a + " " + ") "+ ") ";
                    Sentence N = new Sentence(n);
                    tmp.push(N);
                } else if ((aParserList instanceof LogicalOperators)) {
                    String rebuild = "";
                    //rebuild and push to stack
                    if (aParserList == LogicalOperators.NOT) {
                        Sentence A = (Sentence) tmp.pop();
                        rebuild = " ( NOT " + A.getSentence()+" ) ";
                    } else if (aParserList == LogicalOperators.AND) {
                        Sentence B = (Sentence) tmp.pop();
                        Sentence A = (Sentence) tmp.pop();
                        rebuild = " ( "+A.getSentence() + " AND " + B.getSentence()+" ) ";
                    } else if (aParserList == LogicalOperators.OR) {
                        Sentence B = (Sentence) tmp.pop();
                        Sentence A = (Sentence) tmp.pop();
                        rebuild = " ( "+A.getSentence() + " OR " + B.getSentence()+" ) ";
                    } else if (aParserList == LogicalOperators.IMPLY) {
                        Sentence B = (Sentence) tmp.pop();
                        Sentence A = (Sentence) tmp.pop();
                        rebuild = " ( "+A.getSentence() + " IMPLY " + B.getSentence()+" ) ";
                    } else {
                        System.err.println("Something went wrong! exit");
                        System.exit(1);
                    }
                    Sentence n = new Sentence(rebuild);
                    tmp.push(n);
                }
            }
        }
        while(!tmp.empty()) {
            clauses.push((Sentence) tmp.pop());
        }

        System.out.println(Debug.ANSI_YELLOW+clauses.peek().getSentence()+Debug.ANSI_RESET);
    }

    private void singleImplication()
    {
        while (!clauses.empty()) {
            Sentence s = clauses.pop();
            for (Object aParserList : s.getParserList()) {
                if (aParserList instanceof Variable) {
                    Sentence sub = new Sentence(((Variable) aParserList).getName());
                    tmp.push(sub);
                } else if (aParserList == LogicalOperators.IMPLY) {
                    //a => b == ~a v b
                    Sentence B = (Sentence) tmp.pop();
                    Sentence A = (Sentence) tmp.pop();
                    String a = A.getSentence();
                    String b = B.getSentence();
                    String n = " ( " + " NOT " + a + " " + "OR" + " " + b + " ) ";
                    Sentence N = new Sentence(n);
                    tmp.push(N);
                } else if ((aParserList instanceof LogicalOperators)) {
                    String rebuild = "";
                    //rebuild and push to stack
                    if (aParserList == LogicalOperators.NOT) {
                        Sentence A = (Sentence) tmp.pop();
                        rebuild = " ( NOT " + A.getSentence() + " ) ";
                    } else if (aParserList == LogicalOperators.AND) {
                        Sentence B = (Sentence) tmp.pop();
                        Sentence A = (Sentence) tmp.pop();
                        rebuild = " ( " + A.getSentence() + " AND " + B.getSentence() + " ) ";
                    } else if (aParserList == LogicalOperators.OR) {
                        Sentence B = (Sentence) tmp.pop();
                        Sentence A = (Sentence) tmp.pop();
                        rebuild = " ( " + A.getSentence() + " OR " + B.getSentence() + " ) ";
                    } else {
                        System.err.println("Something went wrong! exit");
                        System.exit(1);
                    }
                    Sentence n = new Sentence(rebuild);
                    tmp.push(n);
                }
            }
        }
        while (!tmp.empty()) {
            clauses.push((Sentence) tmp.pop());
        }

        System.out.println(Debug.ANSI_RED+clauses.peek().getSentence()+Debug.ANSI_RESET);

    }

    private void negation()
    {
        Sentence s = clauses.peek();
        LinkedList<Object> list = s.getParserList();
        String postfix = CNFConverter.ListToString(list);
        while(postfix.contains("AND NOT") ||
                postfix.contains("OR NOT") ||
                postfix.contains("NOT NOT"))
        {
            clauses.pop();
            for (int i=0; i<list.size(); i++)
                if (list.get(i) instanceof Variable) {
                    Sentence sub = new Sentence(((Variable) list.get(i)).getName());
                    tmp.push(sub);
                } else if (list.get(i) instanceof LogicalOperators && i < list.size() - 1) {
                    if (list.get(i + 1) == LogicalOperators.NOT &&
                            list.get(i) == LogicalOperators.AND) {
                        String b = ((Sentence) tmp.pop()).getSentence();
                        String a = ((Sentence) tmp.pop()).getSentence();
                        String rebuild = " ( NOT " + a + " OR " + " NOT " + b + " ) ";
                        tmp.push(new Sentence(rebuild));
                        i += 1;
                    } else if (list.get(i + 1) == LogicalOperators.NOT &&
                            list.get(i) == LogicalOperators.OR) {
                        String b = ((Sentence) tmp.pop()).getSentence();
                        String a = ((Sentence) tmp.pop()).getSentence();
                        String rebuild = " ( NOT " + a + " AND " + " NOT " + b + " ) ";
                        tmp.push(new Sentence(rebuild));
                        i += 1;
                    } else if (list.get(i + 1) == LogicalOperators.NOT &&
                            list.get(i) == LogicalOperators.NOT) {
                        i += 1;
                    } else {
                        if (list.get(i) == LogicalOperators.AND) {
                            Sentence B = (Sentence) tmp.pop();
                            Sentence A = (Sentence) tmp.pop();
                            String rebuild = " ( " + A.getSentence() + " AND " + B.getSentence() + " ) ";
                            tmp.push(new Sentence(rebuild));
                        } else if (list.get(i) == LogicalOperators.OR) {
                            Sentence B = (Sentence) tmp.pop();
                            Sentence A = (Sentence) tmp.pop();
                            String rebuild = " ( " + A.getSentence() + " OR " + B.getSentence() + " ) ";
                            tmp.push(new Sentence(rebuild));
                        } else if (list.get(i) == LogicalOperators.NOT) {
                            Sentence A = (Sentence) tmp.pop();
                            String rebuild = " ( NOT " + A.getSentence() + " ) ";
                            tmp.push(new Sentence(rebuild));
                        }
                    }
                } else {
                    if (list.get(i) == LogicalOperators.AND) {
                        Sentence B = (Sentence) tmp.pop();
                        Sentence A = (Sentence) tmp.pop();
                        String rebuild = " ( " + A.getSentence() + " AND " + B.getSentence() + " ) ";
                        tmp.push(new Sentence(rebuild));
                    } else if (list.get(i) == LogicalOperators.OR) {
                        Sentence B = (Sentence) tmp.pop();
                        Sentence A = (Sentence) tmp.pop();
                        String rebuild = " ( " + A.getSentence() + " OR " + B.getSentence() + " ) ";
                        tmp.push(new Sentence(rebuild));
                    } else if (list.get(i) == LogicalOperators.NOT) {
                        Sentence A = (Sentence) tmp.pop();
                        String rebuild = " ( NOT " + A.getSentence() + " ) ";
                        tmp.push(new Sentence(rebuild));
                    }
                }

            while (!tmp.empty()) {
                clauses.push((Sentence) tmp.pop());
            }

            s = clauses.peek();
            list = s.getParserList();
            postfix = CNFConverter.ListToString(list);
        }

        System.out.println(Debug.ANSI_CYAN+clauses.peek().getSentence()+Debug.ANSI_RESET);
    }

    private Sentence distribute(LinkedList<Object> list)
    {
        //String postfix = CNFConverter.ListToString(list);
        Stack<Object> tp = new Stack<>();
        while (checkList(list)!=null)
        {
            //Pair p = checkList(list);
            ArrayList<Pair> pairs = matchList(list);
            if(pairs.size()!=0) {
                int start;
                int end;
                int index = 0;
                for (Pair pair:pairs) {
                    start = pair.getX() + 1;
                    end = pair.getY() - 1;

                    if (start == end) {
                        //distribute
                        for (int i = index; i < start - 1; i++) {
                            if (list.get(i) instanceof Variable) {
                                Sentence sub = new Sentence(((Variable) list.get(i)).getName());
                                tp.push(sub);
                            } else if (list.get(i) == LogicalOperators.OR) {
                                Sentence B = (Sentence) tp.pop();
                                Sentence A = (Sentence) tp.pop();
                                String rebuild = " ( " + A.getSentence() + " OR " + B.getSentence() + " ) ";
                                tp.push(new Sentence(rebuild));
                            } else if (list.get(i) == LogicalOperators.NOT) {
                                Sentence A = (Sentence) tp.pop();
                                String rebuild = " ( NOT " + A.getSentence()+" ) ";
                                tp.push(new Sentence(rebuild));
                            } else if (list.get(i) == LogicalOperators.AND) {
                                Sentence B = (Sentence) tp.pop();
                                Sentence A = (Sentence) tp.pop();
                                String rebuild = " ( " + A.getSentence() + " AND " + B.getSentence() + " ) ";
                                tp.push(new Sentence(rebuild));
                            }
                        }
                        if (list.get(start) instanceof Variable) {
                            Sentence B = (Sentence) tp.pop();
                            Sentence A = (Sentence) tp.pop();
                            Variable C = (Variable) list.get(start);
                            String distribute = " ( " +" ( " + A.getSentence() + " OR " + C.getName() + " ) " +
                                    " AND " + " ( " + B.getSentence() + " OR " + C.getName() + " ) "+ " ) ";
                            tp.push(new Sentence(distribute));
                        } else if (list.get(start) == LogicalOperators.AND) {
                            Sentence D = (Sentence) tp.pop();
                            Sentence C = (Sentence) tp.pop();
                            Sentence B = (Sentence) tp.pop();
                            Sentence A = (Sentence) tp.pop();
                            String distribute = " ( " +" ( " +" ( " + A.getSentence() + " OR " + B.getSentence() + " ) " +
                                    " AND " + " ( " + A.getSentence() + " OR " + C.getSentence() + " ) " + " ) "+
                                    " AND " + " ( " + A.getSentence() + " OR " + D.getSentence() + " ) "+ " ) ";
                            tp.push(new Sentence(distribute));
                        }
                    } else if (start > end) {
                        //distribute
                        for (int i = index; i < end; i++) {
                            if (list.get(i) instanceof Variable) {
                                Sentence sub = new Sentence(((Variable) list.get(i)).getName());
                                tp.push(sub);
                            } else if (list.get(i) == LogicalOperators.OR) {
                                Sentence B = (Sentence) tp.pop();
                                Sentence A = (Sentence) tp.pop();
                                String rebuild = " ( " + A.getSentence() + " OR " + B.getSentence() + " ) ";
                                tp.push(new Sentence(rebuild));
                            } else if (list.get(i) == LogicalOperators.NOT) {
                                Sentence A = (Sentence) tp.pop();
                                String rebuild = " ( NOT " + A.getSentence()+" ) ";
                                tp.push(new Sentence(rebuild));
                            } else if (list.get(i) == LogicalOperators.AND) {
                                Sentence B = (Sentence) tp.pop();
                                Sentence A = (Sentence) tp.pop();
                                String rebuild = " ( " + A.getSentence() + " AND " + B.getSentence() + " ) ";
                                tp.push(new Sentence(rebuild));
                            }
                        }

                        Sentence c = (Sentence) tp.pop();
                        Sentence b = (Sentence) tp.pop();
                        Sentence a = (Sentence) tp.pop();

                        String distribute = " ( " + " ( " + a.getSentence() + " OR " + b.getSentence() + " ) " +
                                " AND " + " ( " + a.getSentence() + " OR " + c.getSentence() + " ) "+ " ) ";

                        tp.push(new Sentence(distribute));
                    } else {
                        //recursion
                        LinkedList<Object> subList = new LinkedList<>();
                        for (int i = start; i <= end; i++) {
                            subList.add(list.get(i));
                        }
                        Sentence c = distribute(subList);
                        for (int i = index; i < start - 1; i++) {
                            if (list.get(i) instanceof Variable) {
                                Sentence sub = new Sentence(((Variable) list.get(i)).getName());
                                tp.push(sub);
                            } else if (list.get(i) == LogicalOperators.OR) {
                                Sentence B = (Sentence) tp.pop();
                                Sentence A = (Sentence) tp.pop();
                                String rebuild = " ( " + A.getSentence() + " OR " + B.getSentence() + " ) ";
                                tp.push(new Sentence(rebuild));
                            } else if (list.get(i) == LogicalOperators.NOT) {
                                Sentence A = (Sentence) tp.pop();
                                String rebuild = " ( NOT " + A.getSentence()+" ) ";
                                tp.push(new Sentence(rebuild));
                            } else if (list.get(i) == LogicalOperators.AND) {
                                Sentence B = (Sentence) tp.pop();
                                Sentence A = (Sentence) tp.pop();
                                String rebuild = " ( " + A.getSentence() + " AND " + B.getSentence() + " ) ";
                                tp.push(new Sentence(rebuild));
                            }
                        }
                        Sentence b = (Sentence) tp.pop();
                        Sentence a = (Sentence) tp.pop();
                        String distribute = " ( " +" ( " + a.getSentence() + " OR " + c.getSentence() + " ) " +
                                " AND " + " ( " + b.getSentence() + " OR " + c.getSentence() + " ) "+ " ) ";
                        tp.push(new Sentence(distribute));
                    }
                    index = end + 2;
                }

                for (int i = index; i < list.size(); i++) {
                    if (list.get(i) == LogicalOperators.AND) {
                        Sentence B = (Sentence) tp.pop();
                        Sentence A = (Sentence) tp.pop();
                        String rebuild = " ( " + A.getSentence() + " AND " + B.getSentence() + " ) ";
                        tp.push(new Sentence(rebuild));
                    } else if (list.get(i) == LogicalOperators.OR) {
                        Sentence B = (Sentence) tp.pop();
                        Sentence A = (Sentence) tp.pop();
                        String rebuild = " ( " + A.getSentence() + " OR " + B.getSentence() + " ) ";
                        tp.push(new Sentence(rebuild));
                    } else if (list.get(i) instanceof Variable) {
                        Sentence sub = new Sentence(((Variable) list.get(i)).getName());
                        tp.push(sub);
                    } else if (list.get(i) == LogicalOperators.NOT) {
                        Sentence A = (Sentence) tp.pop();
                        String rebuild = " ( NOT " + A.getSentence()+" ) ";
                        tp.push(new Sentence(rebuild));
                    }
                }
            }
            //re-assign list
            list = ((Sentence) tp.peek()).getParserList();
        }

        //return sentence
        if(!tp.empty()) {
            return (Sentence) tp.pop();
        } else {
            //rebuild other types of sentence
            for (Object aList : list) {
                if (aList == LogicalOperators.AND) {
                    Sentence B = (Sentence) tp.pop();
                    Sentence A = (Sentence) tp.pop();
                    String rebuild = " ( " + A.getSentence() + " AND " + B.getSentence() + " ) ";
                    tp.push(new Sentence(rebuild));
                } else if (aList == LogicalOperators.OR) {
                    Sentence B = (Sentence) tp.pop();
                    Sentence A = (Sentence) tp.pop();
                    String rebuild = " ( " + A.getSentence() + " OR " + B.getSentence() + " ) ";
                    tp.push(new Sentence(rebuild));
                } else if (aList instanceof Variable) {
                    Sentence sub = new Sentence(((Variable) aList).getName());
                    tp.push(sub);
                } else if (aList == LogicalOperators.NOT) {
                    Sentence A = (Sentence) tp.pop();
                    String rebuild = " ( NOT " + A.getSentence()+" ) ";
                    tp.push(new Sentence(rebuild));
                }
            }
            return (Sentence) tp.pop();
        }
    }

    public void printClause()
    {
        System.out.println(Debug.ANSI_GREEN+clauses.peek().getSentence()+Debug.ANSI_RESET);
    }

    public static String ListToString(LinkedList<Object> list)
    {
        String result = "";
        for (Object item : list) {
            if (item instanceof Variable) {
                result += ((Variable) item).getName() + " ";
            } else if (item instanceof LogicalOperators) {
                result += item.toString() + " ";
            }
        }
        return result;
    }

    public static ArrayList<Pair> matchList(LinkedList<Object> list)
    {
        ArrayList<Pair> pairs = new ArrayList<>();
        int global = 0;
        while(checkList(list)!=null)
        {
            Pair p = checkList(list);
            p.setX(p.getX()+global);
            p.setY(p.getY()+global);
            pairs.add(p);
            int start = checkList(list).getY()+1;
            global = start;
            //System.out.println(Debug.ANSI_YELLOW+list.size()+"::"+start+Debug.ANSI_RESET);
            list = Rest(start,list);
        }
        return pairs;
    }

    private static LinkedList<Object> Rest(int start,LinkedList<Object> list)
    {
        LinkedList<Object> newList = new LinkedList<>();
        for(int i=start; i<list.size(); i++) {
            newList.add(list.get(i));
        }
        //System.out.println(newList);
        return newList;
    }

    public static Pair checkList(LinkedList<Object> list)
    {
        int i,j;
        i=0;
        while (i<list.size()) {
            if (list.get(i) == LogicalOperators.AND)
                break;
            i++;
        }
        j=0;
        while (j<list.size()) {
            if (list.get(j) == LogicalOperators.OR && j > i && i < list.size())
                break;
            j++;
        }

        if (i < j && i < list.size() && j<list.size()) {
            int q ;
            for (q=j; q>=0; q--) {
                if(list.get(q) == LogicalOperators.AND) {
                    break;
                }
            }
            if((j-q)<=2) {
                return new Pair(q, j);
            }
            return null;
        } else {
            return null;
        }
    }




}
