public abstract class Token {
    final public DomainTag tag;
    final public Fragment coords;

    protected Token(DomainTag tag, Position starting, Position following){
        this.tag = tag;
        this.coords = new Fragment(starting, following);
    }

    public abstract int getAttr();
}
