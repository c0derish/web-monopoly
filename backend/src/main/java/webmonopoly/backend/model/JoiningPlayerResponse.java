package webmonopoly.backend.model;

public class JoiningPlayerResponse {

    Integer playerID;
    String descriptor;

    public JoiningPlayerResponse(int playerID, String descriptor) {
        this.playerID = playerID;
        this.descriptor = descriptor;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public void setPlayerID(Integer playerID) {
        this.playerID = playerID;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }
}
