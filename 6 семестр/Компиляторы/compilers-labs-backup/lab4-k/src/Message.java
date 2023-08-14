public class Message {
    public final boolean isError;
    public final String text;


    public Message(boolean isError, String text) {
        this.isError = isError;
        this.text = text;
    }
}
