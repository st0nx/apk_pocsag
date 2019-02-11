package pocsag.sender;

public class Message {
    private final long id;
    private final String message;
    private final boolean out;

    public Message(long id, String message, boolean out) {
        this.id = id;
        this.message = message;
        this.out = out;
    }

    public String getMessage() {
        return message;
    }

    public long getId() {
        return id;
    }

    public String getIdString() {
        return String.valueOf(id);
    }

    public boolean isOut() {
        return out;
    }
}
