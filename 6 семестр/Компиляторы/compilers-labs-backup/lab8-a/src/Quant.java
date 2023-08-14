import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Quant {
    public enum Type{
        FPAREN,
        SPAREN,
        LIT
    }
    Type type;
    Expr expr;
    Lit lit;

    Quant(Type type, Expr expr){
        this.type = type;
        this.expr = expr;
        this.lit = null;
    }

    Quant(Lit lit){
        this.expr = null;
        this.type = Type.LIT;
        this.lit = lit;
    }

    public First getFirst( Map<Character, First> firsts) {
        First first = new First();
        if (type==Type.LIT){
            return lit.getFirst(firsts);
        }else if (type==Type.SPAREN){
            first.addAll(expr.getFirst(firsts));
            first.add((char)-1);
            return first;
        }else{
            first.add((char)0);
            first.addAll(expr.getFirst(firsts));
            return first;
        }
    }
}
