import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder inputBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            inputBuilder.append(scanner.nextLine());
            inputBuilder.append('\n');
        }
        Lexer lexer = new Lexer(inputBuilder.toString());
        while (true) {
            Token current = lexer.nextToken();
            if (current.getTag() == DomainTag.EOF) {
                break;
            }
            System.out.println(current);
        }
    }
}
