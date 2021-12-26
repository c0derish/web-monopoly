package webmonopoly.backend.model;

public class GivePropertyRequest {

    String property;
    String recipient;

    public GivePropertyRequest(String property, String recipient) {
        this.property = property; this.recipient = recipient;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}
