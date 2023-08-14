import java.util.ArrayList;
import java.util.List;

public class First {
    private List<Character> first;

    public First(List<Character> first){
        this.first = first;
    }

    public First(){
        this.first = new ArrayList<>();
    }

    public boolean add(Character c){
        if (!first.contains(c)){
            first.add(c);
            return true;
        }
        return false;
    }

    public void remove(Character c){
        first.remove(c);
    }

    public boolean contains(Character c){
        return first.contains(c);
    }

    public boolean addAll(First f){
        boolean changed = false;
        for (char c: f.first){
            if (add(c) == true){
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public String toString() {
        String s = "[";
        for (char c: first){
            s += c + ", ";
        }
        s += "]";
        return s;
    }
}
