public class Scanner {
    final public String text;

    private final Compiler compiler;
    private final Position cur;
    private int state;
    private Automata automata;


    public Scanner(String text, Compiler compiler, Automata automata) {
        this.compiler = compiler;
        this.text = text;
        cur = new Position(this.text);
        this.automata = automata;
    }


    public Token nextToken() {
        state = 0;
        Position start = new Position(cur);

        while (cur.cp() != (char) -1) {
            boolean isError = false;
            if (!automata.isExpected(cur.cp()) || (automata.symbolCode(cur.cp()) == 11 && !(state == 12 || state == 13 || state == 16))) {
                compiler.addMessage(true, new Position(cur), "Unexpected character");
                cur.next();
            } else {
                int nextState = automata.getNextState(state, cur.cp());
                if (nextState == -1) {
                    if (automata.getStateName(state) == null) {
                        isError = true;
                        compiler.addMessage(true, new Position(cur), "Not final state");
                    } else
                        return new Token(automata.getStateName(state),
                                new Position(start),
                                new Position(cur),
                                text.substring(start.getPos(), cur.getPos()));
                }
                if (isError) {
                    state = 0;
                } else {
                    state = nextState;
                }
                cur.next();
            }
        }
        return new Token(DomainTag.EOF, cur, cur, "EOF");
    }
}
