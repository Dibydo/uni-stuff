import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Scanner {
    final public String text;

    private Compiler compiler;
    private Position cur;
    private List<Fragment> comments;

    public Collection<Fragment> getComments(){
        return comments;
    }

    public Scanner(String text, Compiler compiler){
        this.compiler = compiler;
        this.text = text;
        cur = new Position(this.text);
        comments = new ArrayList<Fragment>();
    }

    public Token nextToken(){
        while (cur.cp() != (char)-1){
            while (cur.isWhiteSpace()){
                cur.inc();
            }
            Position start = cur.clone();

            if (cur.cp() == 's' || cur.cp() == 't' || cur.cp() == 'e'){
                cur.inc();
                if (cur.isLetterOrDigit()){
                    cur.inc();
                    String name = text.substring(start.getIndex(), cur.getIndex());
                    return new VarnameToken(compiler.addVarName(name), start, cur);
                } else if (cur.cp() == '.'){
                    int i = -1;
                    do{
                        ++i;
                        cur.inc();
                    } while (cur.isLetterOrDigit());
                    if (i <= 0){
                        compiler.addMessage(true, start, "Invalid varname");
                        cur.inc();
                    } else {
                        String name = text.substring(start.getIndex(), cur.getIndex());
                        return new VarnameToken(compiler.addVarName(name), start, cur);
                    }
                } else {
                    String name = Character.toString(start.cp());
                    cur.inc();
                    return new IdentToken(compiler.addIdentName(name), start, cur);
                }
            } else if (cur.isLetter()){
                do {
                    cur.inc();
                }
                while (cur.isLetterOrDigit());
                String name = text.substring(start.getIndex(), cur.getIndex());
                return new IdentToken(compiler.addIdentName(name), start, cur);
            }else if (cur.cp() != (char) -1){
                compiler.addMessage(true, cur.clone(), "Unexpected character");
                cur.inc();
            }
        }
        return new EofToken(cur, cur);
    }
}
