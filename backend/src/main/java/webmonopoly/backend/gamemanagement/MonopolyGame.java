package webmonopoly.backend.gamemanagement;

import org.json.*;
import webmonopoly.backend.controller.PlayerStateCycleLocker;
import webmonopoly.backend.model.PlayerState;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MonopolyGame extends CoreMonopolyGame {

    public PlayerStateCycleLocker[] lockers;
    public Function<JSONObject, Void> sendNewBoardState;

    public MonopolyGame(int numPlayers, Function<JSONObject, Void> sendNewBoardState) {
        super(numPlayers);
        lockers = new PlayerStateCycleLocker[numPlayers];
        for (int i = 0; i < numPlayers; i++) { lockers[i] = new PlayerStateCycleLocker(); }
        this.sendNewBoardState = sendNewBoardState;
    }

    private void releaseAll() {
        for (PlayerStateCycleLocker l : lockers) {
            l.release();
        }
    }

    public void init() {
        releaseAll();
    }

    @Override
    public int rollDice() {
        int roll = super.rollDice();
        JSONObject boardState = getBoardState();
        boardState.put("dice", roll);
        sendNewBoardState.apply(boardState);
        releaseAll();
        return roll;
    }

    @Override
    public void nextTurn() {
        super.nextTurn();
        int oldPlayer = (currentPlayer - 1) % players.length;
        lockers[oldPlayer].release();
        lockers[currentPlayer].release();
    }

    public JSONObject getBoardState() {
        JSONArray[] occupants = new JSONArray[40];
        for (int i = 0; i < occupants.length; i++) { occupants[i] = new JSONArray(); }
        for (Player p : players) { occupants[p.location].put(p.descriptor); }

        JSONObject[] cellJsons = new JSONObject[40];
        for (int i = 0; i < cellJsons.length; i++) {
            cellJsons[i] = new JSONObject();
            cellJsons[i].put("occupants", occupants[i]);
            cellJsons[i].put("houses", properties[i].numHouses);
        }

        JSONArray cells = new JSONArray();
        for (int i = 0; i < cellJsons.length; i++) {
            cells.put(cellJsons[i]);
        }

        JSONObject boardState = new JSONObject();
        boardState.put("cells", cells);
        return boardState;
    }

    public PlayerState exportPlayer(int index) {
        ArrayList<String> actions = new ArrayList<>();
        if (currentPlayer == index) {
            if (properties[players[index].location].ownerID == null) {
                actions.add(actionStrings.get(Actions.BUY_THIS_PROPERTY));
            }
            if (players[index].inJail && players[index].getOutOfJailFree) {
                actions.add(actionStrings.get(Actions.GET_OUT_OF_JAIL_FREE));
            }
            if (players[index].inJail) {
                actions.add(actionStrings.get(Actions.PAY_OUT_OF_JAIL));
            }
            if (currentPlayerDebt > 0) {
                actions.add(actionStrings.get(Actions.PAY_DEBT));
            } else {
                actions.add(actionStrings.get(Actions.END_TURN));
            }
            if (players[currentPlayer].bank == 0) {
                actions.add(actionStrings.get(Actions.DECLARE_BANKRUPTCY));
            }
        }
        return new PlayerState(
                currentPlayer == index,
                players[index].bank,
                Arrays.stream(properties)
                        .filter(property -> property.ownerID == index)
                        .map(property ->
                                new PlayerState.PropertyState(property.name, property.numHouses, property.morgaged))
                        .collect(Collectors.toList()),
                actions,
                (currentPlayer == index) ? currentPlayerDebt : 0,
                Arrays.stream(players)
                        .filter(player -> !Objects.equals(player.descriptor, players[index].descriptor))
                        .map(player -> player.descriptor)
                        .collect(Collectors.toList())
        );
    }

}
