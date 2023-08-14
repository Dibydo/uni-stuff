import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void printToken(DomainTag tag, Fragment fragment, String attr){
        System.out.printf("%s %s: %s\n", tag, fragment, attr);
    }

    public static void main(String[] args){
        String input = "";
        try(FileReader fileReader = new FileReader("input.txt")){
            int c;
            while((c=fileReader.read())!=-1){
                input += (char)c;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        Compiler compiler = new Compiler();
        Scanner scanner = compiler.getScanner(input);
        while (true){
            Token token = scanner.nextToken();
            if (token.getClass() == EofToken.class){
                printToken(token.tag, token.coords, "");
                break;
            }else{
                printToken(token.tag, token.coords, Integer.toString(token.getAttr()));
            }
        }
        compiler.outputMessages();
    }
}
