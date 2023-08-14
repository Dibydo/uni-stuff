public class Automata {
    private int[][] transitionTable;
    private DomainTag[] stateDomains;

    public Automata(int[][] transitionTable, DomainTag[] stateDomains) {
        this.transitionTable = transitionTable;
        this.stateDomains = stateDomains;
    }

    public int symbolCode(char s) {
        int symbolCode;
        switch (s) {
            case 'w':
                symbolCode = 3;
                break;
            case 'r':
                symbolCode = 4;
                break;
            case 'i':
                symbolCode = 5;
                break;
            case 't':
                symbolCode = 6;
                break;
            case 'e':
                symbolCode = 7;
                break;
            case 'a':
                symbolCode = 8;
                break;
            case 'd':
                symbolCode = 9;
                break;
            case '^':
                symbolCode = 10;
                break;
            case '_':
                symbolCode = 11;
                break;
            case '"':
                symbolCode = 12;
                break;
            case '\n':
                symbolCode = 13;
                break;
            default:
                if (Character.isWhitespace(s))
                    symbolCode = 0;
                else if (Character.isDigit(s))
                    symbolCode = 1;
                else if (Character.isLetter(s))
                    symbolCode = 2;
                else symbolCode = -1;
        }
        return symbolCode;
    }

    public int getNextState(int state, char symbol){
        return this.transitionTable[state][symbolCode(symbol)];
    }

    public DomainTag getStateName(int state){
        return this.stateDomains[state];
    }

    public boolean isExpected(char symbol){
        return symbolCode(symbol) != -1;
    }
}
