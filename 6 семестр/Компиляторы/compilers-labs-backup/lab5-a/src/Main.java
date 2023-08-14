import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static int[][] table = {
            //      0       1       2       3       4       5       6       7       8       9       10      11      12      13
            //      ws      dg      lt      w       r       i       t       e       a       d       ^       -       "       \n
            {       1,      2,      3,      4,     8,      3,      3,      3,      3,      3,      12,      -1,     16,     1}, //0
            {       1,      -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     1},//1 WS
            {       -1,     2,      -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1},//2 NB
            {       -1,     3,      3,      3,      3,      3,      3,      3,      3,      3,      -1,     -1,     -1,     -1},//3 IDENT
            {       -1,     3,      3,      3,      5,      3,      3,      3,      3,      3,      -1,     -1,     -1,     -1},//4
            {       -1,     3,      3,      3,      3,      6,      3,      3,      3,      3,      -1,     -1,     -1,     -1},//5
            {       -1,     3,      3,      3,      3,      3,      7,      3,      3,      3,      -1,     -1,     -1,     -1},//6
            {       -1,     3,      3,      3,      3,      3,      3,      11,     3,      3,      -1,     -1,     -1,     -1},//7
            {       -1,     3,      3,      3,      3,      3,      3,      9,      3,      3,      -1,     -1,     -1,     -1},//8
            {       -1,     3,      3,      3,      3,      3,      3,      3,      10,     3,      -1,     -1,     -1,     -1},//9
            {       -1,     3,      3,      3,      3,      3,      3,      3,      3,      11,     -1,     -1,     -1,     -1},//10
            {       -1,     3,      3,      3,      3,      3,      3,      3,      3,      3,      -1,     -1,     -1,     -1},//11 KW
            {       -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     13,     -1,     -1},//12
            {       -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     15,     14,     -1,     -1},//13
            {       -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     15,     -1,     -1,     -1},//14
            {       -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1},//15
            {       16,     16,     16,     16,     16,     16,     16,     16,     16,     16,     16,     16,     17,     -1},//16
            {       -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1,     -1},//17
    };

    public static DomainTag[] tagNumbers = {null, DomainTag.WHITESPACE, DomainTag.NUMBER, DomainTag.IDENT, null, null, null, null,
            null, null, null, DomainTag.KEYWORD, null, null, null, DomainTag.OP, DomainTag.COMMENT, DomainTag.COMMENT};
    public static void printToken(DomainTag tag, Fragment fragment, String attr){
        System.out.printf("%s %s: %s\n", tag, fragment, attr);
    }

    public static void main(String[] args){
        StringBuilder input = new StringBuilder();
        try(FileReader fileReader = new FileReader("input.txt")){
            int c;
            while((c=fileReader.read())!=-1){
                input.append((char) c);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        Compiler compiler = new Compiler();
        Scanner scanner = compiler.getScanner(input.toString(), new Automata(table, tagNumbers));
        while (true){
            Token token = scanner.nextToken();
            if (token.tag == DomainTag.EOF){
                printToken(token.tag, token.coords, "");
                break;
            }else if (token.tag != DomainTag.WHITESPACE){
                printToken(token.tag, token.coords, token.image);
            }
        }
        compiler.outputMessages();
    }
}
