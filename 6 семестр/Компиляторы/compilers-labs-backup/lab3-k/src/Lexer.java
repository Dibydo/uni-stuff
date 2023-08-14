import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private final String IDENT_REGEXP = "[a-zA-Z][a-zA-Z0-9]*";

    private final String VAR_NAME_REGEXP = "[ste]([a-zA-Z0-9]|\\.[a-zA-Z0-9]+)";
    private final String REGEXP = "(?<varname>" + VAR_NAME_REGEXP + ")|(?<ident>"
            + IDENT_REGEXP + ")";

    private final Position position;
    private final Matcher matcher;
    private final String program;
    private final int length;

    Lexer(String input) {
        this.position = new Position();
        this.program = input;
        this.length = input.length();
        this.matcher = Pattern.compile(REGEXP).matcher(input);
    }

    void printError() {
        System.err.println("syntax error" + this.position);
    }

    private char getCurrChar() {
        return this.program.charAt(this.position.getIndex());
    }

    private void incPosition() {
        position.incPos();
        if (this.getCurrChar() == '\n') {
            position.incLine();
            position.resetPos();
        }
        position.incIndex();
    }

    private void skipSpace() {
        while (this.position.getIndex() < this.length &&
                (this.getCurrChar() == ' '
                        || this.getCurrChar() == '\n'
                        || this.getCurrChar() == '\r')) {
            this.incPosition();
        }
    }

    Token nextToken() {
        this.skipSpace();
        if (this.position.getIndex() >= this.length) {
            return new Token(DomainTag.EOF, this.position.clone(), "");
        }

        boolean isFound = this.matcher.find();
        if (!isFound) {
            this.printError();
            return new Token(DomainTag.EOF, this.position.clone(), "");
        }
        if (this.matcher.start() != this.position.getIndex()) {
            this.printError();
            while (this.matcher.start() != this.position.getIndex()) {
                this.incPosition();
            }
        }
        DomainTag tokTag = null;
        if (matcher.group("varname") != null) {
            tokTag = DomainTag.VAR_NAME;
        } else if (matcher.group("ident") != null) {
            tokTag = DomainTag.IDENT;
        }

        String tokValue = this.matcher.group();
        Position tokPos = this.position.clone();
        while (this.position.getIndex() < this.matcher.end()) {
            this.incPosition();
        }
        return new Token(tokTag, tokPos, tokValue);
    }
}
