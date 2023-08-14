import java.io.FileReader;
import java.io.IOException;

public class Main {
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
        Scanner scanner = compiler.getScanner(input.toString());
        while (true){
            Token token = scanner.nextToken();
            if (token.getClass() == EofToken.class){
                printToken(token.tag, token.coords, "");
                break;
            }else{
                printToken(token.tag, token.coords, Double.toString(token.attr()));
            }
        }
        compiler.outputMessages();
    }
}
