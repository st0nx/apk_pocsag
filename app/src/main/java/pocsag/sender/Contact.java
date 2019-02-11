package pocsag.sender;

public class Contact {
    private final String name;
    private final long id;
    private final String number;

    public Contact(String name, long id, String number) {
        this.name = name;
        this.id = id;
        this.number = number;
    }


    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }


    public long getId() {
        return id;
    }
}
