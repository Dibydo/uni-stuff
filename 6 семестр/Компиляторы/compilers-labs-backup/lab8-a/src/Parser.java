import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    final private Scanner scanner;
    private Token Sym;


    public Parser(Scanner scanner) {
        this.scanner = scanner;
        Sym = scanner.nextToken();
    }

    private void printError(Token tok) {
        System.err.printf("Error while parsing token: {%s}", tok.coords);
    }

    public HashMap<Character, Expr> grammar() {
        HashMap<Character, Expr> rules = new HashMap<>();

        while (Sym.tag == DomainTag.COMMENT || Sym.tag == DomainTag.NTERM) {
            if (Sym.tag == DomainTag.COMMENT) {
                Sym = scanner.nextToken();
            } else {
                Pair rule = rule();
                if (rule == null){
                    return null;
                }
                rules.put(rule.nterm, rule.expr);
            }
        }
        if (Sym.tag != DomainTag.EOF) {
            printError(Sym);
            return null;
        }
        return rules;
    }

    private Pair rule() {
        char key;
        if (Sym.tag == DomainTag.NTERM) {
            key = Sym.value;
            Sym = scanner.nextToken();
        } else {
            printError(Sym);
            return null;
        }

        if (Sym.tag == DomainTag.LPAREN) {
            Sym = scanner.nextToken();
        } else {
            printError(Sym);
            return null;
        }

        Expr val = expr();
        if (val == null){
            return null;
        }

        if (Sym.tag == DomainTag.RPAREN) {
            Sym = scanner.nextToken();
        } else {
            printError(Sym);
            return null;
        }

        return new Pair(key, val);
    }

    private Expr expr() {
        Concat c = concat();
        if (c == null){
            return null;
        }

        if (Sym.tag == DomainTag.COMMA) {
            Sym = scanner.nextToken();

            Expr e = expr();
            if (e == null){
                return null;
            }
            return new Expr(c, e);
        }
        return new Expr(c);
    }

    private Concat concat() {
        Quant q = quant();
        if (q == null){
            return null;
        }

        if (Sym.tag == DomainTag.LFPAREN ||
                Sym.tag == DomainTag.LSPAREN ||
                Sym.tag == DomainTag.LPAREN ||
                Sym.tag == DomainTag.TERM ||
                Sym.tag == DomainTag.NTERM) {
            Concat c = concat();
            if (c == null){
                return null;
            }
            return new Concat(q, c);
        }
        return new Concat(q);
    }

    private Quant quant() {
        if (Sym.tag == DomainTag.LFPAREN) {
            Sym = scanner.nextToken();
            Expr e = expr();
            if (e == null){
                return null;
            }
            if (Sym.tag == DomainTag.RFPAREN) {
                Sym = scanner.nextToken();
            } else {
                printError(Sym);
                return null;
            }
            return new Quant(Quant.Type.FPAREN, e);
        } else if (Sym.tag == DomainTag.LSPAREN) {
            Sym = scanner.nextToken();
            Expr e = expr();
            if (e == null){
                return null;
            }
            if (Sym.tag == DomainTag.RSPAREN) {
                Sym = scanner.nextToken();
            } else {
                printError(Sym);
                return null;
            }
            return new Quant(Quant.Type.SPAREN, e);
        } else if (Sym.tag == DomainTag.TERM ||
                Sym.tag == DomainTag.NTERM ||
                Sym.tag == DomainTag.LPAREN) {
            Lit l = lit();
            if (l == null){
                return null;
            }
            return new Quant(l);
        } else {
            printError(Sym);
            return null;
        }
    }

    private Lit lit() {
        if (Sym.tag == DomainTag.TERM || Sym.tag == DomainTag.NTERM) {
            Lit.Type t;
            char v = Sym.value;
            if (Sym.tag == DomainTag.TERM) {
                t = Lit.Type.TERM;
            } else {
                t = Lit.Type.NTERM;
            }
            Sym = scanner.nextToken();
            return new Lit(t, v);
        } else if (Sym.tag == DomainTag.LPAREN) {
            Sym = scanner.nextToken();
            Expr e = expr();
            if (Sym.tag == DomainTag.RPAREN) {
                Sym = scanner.nextToken();
            } else {
                printError(Sym);
            }
            return new Lit(e);
        } else {
            printError(Sym);
            return null;
        }
    }
}
