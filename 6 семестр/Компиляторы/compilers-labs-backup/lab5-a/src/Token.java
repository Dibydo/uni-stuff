public class Token {
    final public DomainTag tag;
    final public Fragment coords;
    final public String image;

    protected Token(DomainTag tag, Position starting, Position following, String image){
        this.tag = tag;
        this.coords = new Fragment(starting, following);
        this.image = image;
    }
}
