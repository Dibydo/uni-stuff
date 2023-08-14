public class Scanner {
    final public String text;

    private final Compiler compiler;
    private final Position cur;

    public Scanner(String text, Compiler compiler) {
        this.compiler = compiler;
        this.text = text;
        cur = new Position(this.text);
    }

    private int getInteger() {
        StringBuilder numberStr = new StringBuilder();
        boolean isLeadingZero = true;
        do {
            if (cur.cp() != '0') {
                isLeadingZero = false;
            }
            if (!isLeadingZero) {
                numberStr.append(cur.cp());
            }
            cur.next();
        } while (cur.isDigit());
        return Integer.parseInt(numberStr.toString());
    }

    private double getFractional() {
        StringBuilder numberStr = new StringBuilder();
        do {
            numberStr.append(cur.cp());
            cur.next();
        } while (cur.isDigit());
        return Double.parseDouble("0." + numberStr);
    }

    private double getDegrees(int intPart, int minutes, int seconds, boolean isNegative) {
        int mul = isNegative ? -1 : 1;
        return mul * (intPart + (double) minutes / 60 + (double) seconds / 3600);
    }

    private boolean checkDegrees(double value, boolean isLatitude) {
        return (isLatitude && Math.abs(value) <= 90) || (!isLatitude && Math.abs(value) <= 180);
    }

    public Token nextToken() {
        while (cur.cp() != (char) -1) {
            while (cur.isWhiteSpace()) {
                cur.next();
            }
            Position start = new Position(cur);

            if (cur.isLatitude() || cur.isLongitude()) {
                boolean isLatitude = cur.isLatitude();
                boolean isNegative = (cur.cp() == 'S' || cur.cp() == 'W');
                cur.next();

                if (cur.isDigit()) {
                    if (cur.cp() == '0'){
                        compiler.addMessage(false, new Position(cur), "Leading zero in integer part");
                    }
                    int integerPart = getInteger();
                    if (cur.isDecimalSeparator()) {
                        cur.next();
                        if (cur.isDigit()) {
                            double fractionalPart = getFractional();
                            double value = (integerPart + fractionalPart) * (isNegative ? -1 : 1);
                            if (checkDegrees(value, isLatitude)) {
                                return new CoordsToken(value, start, new Position(cur));
                            } else {
                                compiler.addMessage(true, new Position(cur), "Too large degree");
                            }
                        } else {
                            compiler.addMessage(true, new Position(cur), "Expected fractional value");
                            cur.next();
                        }
                    } else if (cur.isMinutesSeparator()) {
                        cur.next();
                        if (cur.isDigit()) {
                            if (cur.cp() == '0'){
                                compiler.addMessage(false, new Position(cur), "Leading zero in minutes or seconds");
                            }
                            int value = getInteger();
                            if (value <= 59) {
                                if (cur.isMinuteSymbol()) {
                                    cur.next();

                                    if (cur.isDigit()) {
                                        if (cur.cp() == '0'){
                                            compiler.addMessage(false, new Position(cur), "Leading zero in minutes or seconds");
                                        }
                                        int seconds = getInteger();
                                        if (seconds <= 59) {
                                            if (cur.isSecondSymbol()) {
                                                double degrees = getDegrees(integerPart, value, seconds, isNegative);
                                                if (checkDegrees(degrees, isLatitude)) {
                                                    cur.next();
                                                    return new CoordsToken(degrees, start, cur);
                                                } else {
                                                    compiler.addMessage(true, new Position(cur), "Too large degree");
                                                    cur.next();
                                                }
                                            } else {
                                                compiler.addMessage(true, new Position(cur), "Expected \"");
                                                cur.next();
                                            }
                                        } else {
                                            compiler.addMessage(true, new Position(cur), "Too large fractional value");
                                            cur.next();
                                        }
                                    } else {
                                        double degrees = getDegrees(integerPart, value, 0, isNegative);
                                        if (checkDegrees(degrees, isLatitude)) {
                                            cur.next();
                                            return new CoordsToken(degrees, start, cur);
                                        } else {
                                            compiler.addMessage(true, new Position(cur), "Too large degree");
                                            cur.next();
                                        }
                                    }
                                } else if (cur.isSecondSymbol()) {
                                    double degrees = getDegrees(integerPart, 0, value, isNegative);
                                    if (checkDegrees(degrees, isLatitude)) {
                                        cur.next();
                                        return new CoordsToken(degrees, start, cur);
                                    } else {
                                        compiler.addMessage(true, new Position(cur), "Too large degree");
                                        cur.next();
                                    }
                                } else {
                                    compiler.addMessage(true, new Position(cur), "Expected \" or '");
                                    cur.next();
                                }
                            } else {
                                compiler.addMessage(true, new Position(cur), "Too large fractional value");
                                cur.next();
                            }
                        } else {
                            double degrees = getDegrees(integerPart, 0, 0, isNegative);
                            if (checkDegrees(degrees, isLatitude)) {
                                cur.next();
                                return new CoordsToken(degrees, start, cur);
                            } else {
                                compiler.addMessage(true, new Position(cur), "Too large degree");
                                cur.next();
                            }
                        }
                    } else {
                        compiler.addMessage(true, new Position(cur), "Expected separator");
                        cur.next();
                    }
                } else {
                    compiler.addMessage(true, new Position(cur), "Expected integer value");
                }
            } else if (!cur.isEnd()) {
                compiler.addMessage(true, new Position(cur), "Unexpected character");
                cur.next();
            }
        }
        return new EofToken(new Position(cur), new Position(cur));
    }
}
