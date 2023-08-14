public class IdentToken extends Token {
    final public int code;

    public IdentToken(int code, Position starting, Position following){
        super(DomainTag.IDENT, starting, following);
        this.code = code;
    }

    @Override
    public int getAttr() {
        return code;
    }
}
