public class Position implements Comparable<Position> {
    final private String text;
    private int row, col, pos;

    public Position(String text) {
        this.text = text;
        row = col = 1;
        pos = 0;
    }

    public Position(String text, int row, int col, int pos) {
        this.text = text;
        this.row = row;
        this.col = col;
        this.pos = pos;
    }

    public Position(Position other) {
        this(other.text, other.row, other.col, other.pos);
    }


    @Override
    public int compareTo(Position o) {
        return Integer.compare(pos, o.pos);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", row, col);
    }

    public char cp() {
        if (pos == text.length()) {
            return (char) -1;
        }
        return text.charAt(pos);
    }

    public boolean isEnd() {
        return pos == text.length();
    }

    public boolean isWhiteSpace() {
        return !isEnd() && Character.isWhitespace(cp());
    }


    public boolean isDigit() {
        return !isEnd() && Character.isDigit(cp());
    }

    public boolean isLatitude() {
        return !isEnd() && cp() == 'S' || cp() == 'N';
    }

    public boolean isLongitude() {
        return !isEnd() && cp() == 'W' || cp() == 'E';
    }

    public boolean isDecimalSeparator() {
        return !isEnd() && cp() == '.';
    }

    public boolean isMinutesSeparator() {
        return !isEnd() && cp() == 'D';
    }

    public boolean isMinuteSymbol() {
        return !isEnd() && cp() == '\'';
    }

    public boolean isSecondSymbol() {
        return !isEnd() && cp() == '\"';
    }

    public boolean isNewLine() {
        if (pos == text.length()) {
            return true;
        }
        if (cp() == '\r' && pos + 1 < text.length()) {
            return (text.charAt(pos + 1) == '\n');
        }
        return (cp() == '\n');
    }

    public void next() {
        if (pos < text.length()) {
            if (isNewLine()) {
                if (text.charAt(pos) == '\r')
                    ++pos;
                ++row;
                col = 1;
            } else {
                ++col;
            }
            ++pos;
        }
    }
}
