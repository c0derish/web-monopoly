package webmonopoly.backend.model;

public class PropertyRequest {

    String property;

    public PropertyRequest(String property) { this.property = property; }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
