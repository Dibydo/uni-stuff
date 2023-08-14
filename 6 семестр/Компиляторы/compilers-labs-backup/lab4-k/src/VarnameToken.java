public class VarnameToken extends Token {
    final public int code;

    public VarnameToken(int code, Position starting, Position following) {
        super(DomainTag.VARNAME, starting, following);
        this.code = code;
    }

    @Override
    public int getAttr() {
        return code;
    }
}
