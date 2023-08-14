import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void printToken(DomainTag tag, Fragment fragment, String attr) {
        System.out.printf("%s %s: %s\n", tag, fragment, attr);
    }

    public static void printFirst(Map<Character, First> first){
        for (Map.Entry<Character, First> entry: first.entrySet()){
            System.out.printf("First[%c] = %s\n", entry.getKey(), entry.getValue().toString());
        }
    }

    public static Map<Character, First> getFirst(HashMap<Character, Expr> rules) {
        Map<Character, First> firsts = new HashMap<>();
        for (char key: rules.keySet()){
            firsts.put(key, new First());
        }

        boolean changed = true;
        while (changed) {
            changed = false;
            boolean changed1 = false;
            for (Map.Entry<Character, Expr> rule : rules.entrySet()) {
                char key = rule.getKey();
                Expr value = rule.getValue();

                changed1 = firsts.get(key).addAll(value.getFirst(firsts));
            }
            if (changed1){
                changed = true;
            }
        }
        return firsts;
    }

    public static void main(String[] args) {
        String input = "";
        try (FileReader fileReader = new FileReader("input.txt")) {
            int c;
            while ((c = fileReader.read()) != -1) {
                input += (char) c;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        Compiler compiler = new Compiler();
        Scanner scanner = compiler.getScanner(input);
        Parser parser = new Parser(scanner);

        HashMap<Character, Expr> rules = parser.grammar();
        Map<Character, First> first = getFirst(rules);

        printFirst(first);
        compiler.outputMessages();
    }
}
