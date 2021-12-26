package webmonopoly.backend.model;

public class ActionButtonRequest {

    String action;

    public ActionButtonRequest(String action) { this.action = action; }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
