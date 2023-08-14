import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeographicalCoordinates {
    public static void printError(int row, int col) {
        System.err.println("syntax error (" + row + ", " + col + ")");
    }

    public static void printToken(int row, int col, String value) {
        System.out.printf("COORDS (%d, %d): %s\n", row, col, value);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder inputBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            inputBuilder.append(scanner.nextLine());
            inputBuilder.append('\n');
        }
        String input = inputBuilder.toString();

        String regex = "[SENW]([1-9]\\d*|0)(\\.\\d+|D((([1-5]\\d)|\\d)')?((([1-5]\\d)|\\d)\")?)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        int pos = 0, col = 1, row = 1;
        while (true) {
            while (pos < input.length() && (input.charAt(pos) == ' '
                    || input.charAt(pos) == '\n'
                    || input.charAt(pos) == '\r')) {
                ++col;
                if (input.charAt(pos) == '\n') {
                    col = 1;
                    ++row;
                }
                ++pos;
            }
            if (pos >= input.length()) {
                break;
            }
            if (!matcher.find()) {
                printError(row, col);
                break;
            }
            if (pos < matcher.start()) {
                printError(row, col);
                while (pos != matcher.start()) {
                    ++col;
                    if (input.charAt(pos) == '\n') {
                        col = 1;
                        ++row;
                    }
                    ++pos;
                }
            }
            printToken(row, col, matcher.group());
            while (pos < matcher.end()) {
                ++col;
                if (input.charAt(pos) == '\n') {
                    col = 1;
                    ++row;
                }
                ++pos;
            }
        }
    }
}
