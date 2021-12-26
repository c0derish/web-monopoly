package webmonopoly.backend.model;

public class BoardResponse {

    String gameCode;
    Integer portNumber;

    public BoardResponse(String gameCode, Integer portNumber) {
        this.gameCode = gameCode;
        this.portNumber = portNumber;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public Integer getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(Integer portNumber) {
        this.portNumber = portNumber;
    }
}
