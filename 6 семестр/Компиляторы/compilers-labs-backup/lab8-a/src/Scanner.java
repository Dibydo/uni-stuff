import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Scanner {
    final public String text;
    private final Compiler compiler;
    private final Position cur;
    private final List<Fragment> comments;

    public Collection<Fragment> getComments() {
        return comments;
    }

    public Scanner(String text, Compiler compiler) {
        this.compiler = compiler;
        this.text = text;
        cur = new Position(this.text);
        comments = new ArrayList<>();
    }

    public Token nextToken() {
        while (cur.cp() != (char) -1) {
            while (cur.isWhiteSpace()) {
                cur.inc();
            }
            Position start = cur.clone();

            if (cur.cp() == '/') {
                cur.inc();
                if (cur.cp() == '*') {
                    cur.inc();
                    boolean isClosed = false;

                    while (!isClosed) {
                        while (cur.cp() != '*') {
                            if (cur.isEnd()) {
                                compiler.addMessage(false, cur.clone(), "Unexpected end of comment");
                                compiler.addComment(start.clone(), cur.clone());
                                return new Token(DomainTag.COMMENT, start.clone(), cur.clone(), 'N');
                            }
                            cur.inc();
                        }
                        cur.inc();
                        if (cur.cp() == '/') {
                            isClosed = true;
                            cur.inc();
                        }
                    }
                    compiler.addComment(start.clone(), cur.clone());
                    return new Token(DomainTag.COMMENT, start.clone(), cur.clone(), 'N');
                } else {
                    compiler.addMessage(true, cur.clone(), "Unexpected characted");
                }
            } else if (cur.cp() == '"') {
                cur.inc();

                char value = cur.cp();
                cur.inc();

                if (cur.isEnd()){
                    compiler.addMessage(false, cur.clone(), "Unexpected end of term");
                    return new Token(DomainTag.TERM, start.clone(), cur.clone(), value);
                }
                cur.inc();
                return new Token(DomainTag.TERM, start.clone(), cur.clone(), value);
            } else if (cur.isUpperCase()) {
                char value = cur.cp();
                cur.inc();

                return new Token(DomainTag.NTERM, start.clone(), cur.clone(), value);
            } else {
                switch (cur.cp()) {
                    case '(':
                        cur.inc();
                        return new Token(DomainTag.LPAREN, start.clone(), cur.clone(), 'N');
                    case ')':
                        cur.inc();
                        return new Token(DomainTag.RPAREN, start.clone(), cur.clone(), 'N');
                    case '{':
                        cur.inc();
                        return new Token(DomainTag.LFPAREN, start.clone(), cur.clone(), 'N');
                    case '}':
                        cur.inc();
                        return new Token(DomainTag.RFPAREN, start.clone(), cur.clone(), 'N');
                    case '[':
                        cur.inc();
                        return new Token(DomainTag.LSPAREN, start.clone(), cur.clone(), 'N');
                    case ']':
                        cur.inc();
                        return new Token(DomainTag.RSPAREN, start.clone(), cur.clone(), 'N');
                    case ',':
                        cur.inc();
                        return new Token(DomainTag.COMMA, start.clone(), cur.clone(), 'N');
                    default:
                        compiler.addMessage(true, cur.clone(), "Unexpected character");
                        cur.inc();
                        break;
                }
            }
        }

        return new Token(DomainTag.EOF, cur.clone(), cur.clone(), 'N');
    }
}
