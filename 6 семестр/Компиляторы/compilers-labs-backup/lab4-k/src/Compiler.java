import java.util.*;

public class Compiler {
    private Map<Position, Message> messages;

    private Map<String, Integer> identNameCodes;
    private List<String> identNames;

    private Map<String, Integer> varNameCodes;
    private List<String> varNames;

    public Compiler(){
        messages = new HashMap<>();
        identNameCodes = new HashMap<>();
        identNames = new ArrayList<>();
        varNameCodes = new HashMap<>();
        varNames = new ArrayList<>();
    }

    public int addIdentName(String name){
        if (identNameCodes.containsKey(name))
            return identNameCodes.get(name);
        else{
            int code = identNames.size();
            identNames.add(name);
            identNameCodes.put(name, code);
            return code;
        }
    }

    public int addVarName(String name){
        if (varNameCodes.containsKey(name))
            return varNameCodes.get(name);
        else{
            int code = varNames.size();
            varNames.add(name);
            varNameCodes.put(name, code);
            return code;
        }
    }


    public String getIdentName(int code){
        return identNames.get(code);
    }

    public String getVarName(int code){
        return varNames.get(code);
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
