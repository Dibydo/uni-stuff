import java.util.*;

public class Compiler {
    private final Map<Position, Message> messages;
    private final List<Fragment> comments;

    public Compiler(){
        messages = new HashMap<>();
        comments = new ArrayList<>();
    }

    public void addComment(Position starting, Position following){
        comments.add(new Fragment(starting, following));
    }

    public void addMessage(boolean isErr, Position pos, String text){
        messages.put(pos, new Message(isErr, text));
    }

    public void outputMessages(){
        SortedSet<Position> keys = new TreeSet<>(messages.keySet());
        for (Position key : keys){
            System.err.print(messages.get(key).isError ? "Error" : "Warning");
            System.err.print(" " + key + ": ");
            System.err.println(messages.get(key).text);
        }
    }

    public Scanner getScanner(String text){
        return new Scanner(text, this);
    }
}
