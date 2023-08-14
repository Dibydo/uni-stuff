public class Position implements Comparable<Position>, Cloneable {
    final private String text;
    private int line, pos, index;

    public Position(String text) {
        this.text = text;
        line = pos = 1;
        index = 0;
    }

    public int getLine() {
        return line;
    }

    public int getPos() {
        return pos;
    }

    public int getIndex() {
        return index;
    }


    @Override
    public int compareTo(Position o) {
        return Integer.compare(index, o.index);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", line, pos);
    }

    public char cp() {
        if (index == text.length()){
            return (char)-1;
        }
        return text.charAt(index);
    }

    public boolean isWhiteSpace() {
        return index != text.length() && Character.isWhitespace(cp());
    }

    public boolean isLetter() {
        return index != text.length() && Character.isLetter(cp());
    }

    public boolean isLetterOrDigit() {
        return index != text.length() && Character.isLetterOrDigit(cp());
    }

    public boolean IsDigit() {
        return index != text.length() && Character.isDigit(cp());
    }

    public boolean isNewLine() {
        if (index == text.length()){
            return true;
        }
        if (cp() == '\r' && index + 1 < text.length()) {
            return (text.charAt(index+1) == '\n');
        }
        return (cp()== '\n');
    }

    public void inc(){
        if (index < text.length()){
            if (isNewLine()){
                if (text.charAt(index) == '\r')
                    ++index;
                ++line;
                pos = 1;
            }else{
                ++pos;
            }
            ++index;
        }
    }

    @Override
    public Position clone() {
        try {
            return (Position) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
