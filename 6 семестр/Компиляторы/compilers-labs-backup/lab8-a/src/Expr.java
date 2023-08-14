import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Expr {
    public Concat concat;
    public Expr expr;
    public boolean comma;

    public Expr(Concat concat){
        this.concat = concat;
        this.comma = false;
        this.expr = null;
    }

    public Expr(Concat concat, Expr expr){
        this.concat = concat;
        this.comma = true;
        this.expr = expr;
    }

    public First getFirst( Map<Character, First> firsts) {
        First first = new First();
        if (comma){
            first.addAll(concat.getFirst(firsts));
            first.addAll(expr.getFirst(firsts));
            return first;
        }else{
            return concat.getFirst(firsts);
        }
    }
}
