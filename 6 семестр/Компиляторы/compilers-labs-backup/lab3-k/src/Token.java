public class Token {
    final private DomainTag tag;
    final private Position coords;
    final private String value;

    public Token(DomainTag tag, Position coords, String attr) {
        this.tag = tag;
        this.coords = coords;
        this.value = attr;
    }

    public DomainTag getTag(){
        return this.tag;
    }

    public Position getCoords(){
        return this.coords;
    }

    public String getValue(){
        return this.value;
    }

    public String toString(){
        return String.format("%s %s: %s", this.tag, this.coords, this.value);
    }
}
