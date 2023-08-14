public class Position implements Cloneable {
    private int line, pos, index;

    public int getLine(){
        return this.line;
    }

    public int getPos(){
        return this.pos;
    }

    public int getIndex(){
        return this.index;
    }

    public void incLine(){
        this.line++;
    }

    public void incPos(){
        this.pos++;
    }

    public void incIndex(){
        this.index++;
    }

    public void resetPos(){
        this.pos = 1;
    }

    public Position(){
        this.index = 0;
        this.pos = 1;
        this.line = 1;
    }

    @Override
    public Position clone() {
        try {
            return (Position) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", this.line, this.pos);
    }
}
