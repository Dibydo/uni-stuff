public class Token {
    final public DomainTag tag;
    final public Fragment coords;
    final public char value;

    protected Token(DomainTag tag, Position starting, Position following, char value){
        this.tag = tag;
        this.coords = new Fragment(starting, following);
        this.value = value;
    }
}
