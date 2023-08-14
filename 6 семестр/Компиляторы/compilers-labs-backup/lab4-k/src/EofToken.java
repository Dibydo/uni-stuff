public class EofToken  extends Token{
    protected EofToken(Position starting, Position following) {
        super(DomainTag.EOF, starting, following);
    }

    @Override
    public int getAttr() {
        return -1;
    }
}
