import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Concat {
    public Quant quant;
    public Concat concat;
    public boolean isConcat;

    public Concat(Quant quant){
        this.quant = quant;
        this.concat = null;
        isConcat = false;
    }

    public Concat(Quant quant, Concat concat){
        this.quant = quant;
        this.concat = concat;
        isConcat = true;
    }

    public First getFirst( Map<Character, First> firsts) {
        First first = new First();
        if (isConcat){
            first.addAll(quant.getFirst(firsts));
            if (first.contains((char)0)){
                first.remove((char)0);
                first.addAll(concat.getFirst(firsts));
            }
            return first;
        }else{
            return quant.getFirst(firsts);
        }
    }
}
