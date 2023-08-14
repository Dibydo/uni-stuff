import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Lit {
    public enum Type{
        TERM,
        NTERM,
        EXPR
    }
    public Type type;
    public char term;
    public char nterm;
    public Expr expr;

    public Lit(Type type, char s){
        this.type = type;
        if (type == Type.TERM){
            term = s;
        } else if (type == Type.NTERM){
            nterm = s;
        }
        expr = null;
    }

    public Lit(Expr expr){
        this.type = Type.EXPR;
        this.expr = expr;
    }

    public First getFirst( Map<Character, First> firsts){
        First first = new First();
        if (type==Type.TERM){
            first.add(term);
            return first;
        }else if (type==Type.EXPR){
            return expr.getFirst(firsts);
        }else{
            return firsts.get(nterm);
        }
    }
}
