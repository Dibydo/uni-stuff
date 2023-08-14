public class CoordsToken extends Token{
    private final double value;

    protected CoordsToken(double value, Position starting, Position following) {
        super(DomainTag.COORDS, starting, following);
        this.value = value;
    }

    @Override
    public double attr() {
        return value;
    }
}
