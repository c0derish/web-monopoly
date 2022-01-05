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
    public CoreMonopolyGame.Roll rollDice() {
        CoreMonopolyGame.Roll roll = super.rollDice();
        JSONObject boardState = getBoardState();
        boardState.put("dice", roll);
        sendNewBoardState.apply(boardState);
        if (currentCard != null) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
            handleEventCard();
            sendNewBoardState.apply(boardState);
        }
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

    public boolean sendMoney(Integer from, String toDescriptor, Integer amount) {
        if (players[from].bank >= amount) {
            int to = 0;
            while (!Objects.equals(CoreMonopolyGame.playerDescriptors[to], toDescriptor)) { to++; }
            players[from].bank -= amount;
            players[to].bank += amount;
            lockers[from].release();
            lockers[to].release();
            return true;
        } else {
            return false;
        }
    }

    // TODO: lockers
    public boolean giveProperty(Integer from, String toDescriptor, String propertyName) {
        int property = 0;
        while (!Objects.equals(CoreMonopolyGame.propertyNames[property], propertyName)) { property++; }
        if (Objects.equals(properties[property].ownerID, from)) {
            int to = 0;
            while (!Objects.equals(CoreMonopolyGame.playerDescriptors[to], toDescriptor)) { to++; }
            properties[property].ownerID = to;
            return true;
        } else {
            return false;
        }
    }

    public boolean buildHouse(Integer player, String propertyName) {
        int property = 0;
        while (!Objects.equals(CoreMonopolyGame.propertyNames[property], propertyName)) { property++; }
        int price = 50 * (property / 10 + 1);
        if (Objects.equals(properties[property].ownerID, player) && properties[property].numHouses < 5 && players[player].bank >= price) {
            properties[property].numHouses += 1;
            players[player].bank -= price;
            return true;
        } else {
            return false;
        }
    }

    public boolean demolishHouse(Integer player, String propertyName) {
        int property = 0;
        while (!Objects.equals(CoreMonopolyGame.propertyNames[property], propertyName)) { property++; }
        if (Objects.equals(properties[property].ownerID, player) && properties[property].numHouses > 0) {
            properties[property].numHouses -= 1;
            players[player].bank += 50;
            return true;
        } else {
            return false;
        }
    }

    public boolean mortgage(Integer player, String propertyName) {
        int property = 0;
        while (!Objects.equals(CoreMonopolyGame.propertyNames[property], propertyName)) { property++; }
        if (Objects.equals(properties[property].ownerID, player) && !properties[property].mortgaged) {
            properties[property].mortgaged = true;
            players[player].bank += properties[property].basePrice / 2;
            return true;
        } else {
            return false;
        }
    }

    public boolean unmortgage(Integer player, String propertyName) {
        int property = 0;
        while (!Objects.equals(CoreMonopolyGame.propertyNames[property], propertyName)) { property++; }
        int price = properties[property].basePrice / 2;
        if (Objects.equals(properties[property].ownerID, player) && properties[property].mortgaged && players[player].bank >= price) {
            properties[property].mortgaged = false;
            players[player].bank -= price;
            return true;
        } else {
            return false;
        }
    }

    public boolean buyThisProperty() {
        if (properties[players[currentPlayer].location].basePrice != null
                && properties[players[currentPlayer].location].ownerID == null
                && players[currentPlayer].bank >= properties[players[currentPlayer].location].basePrice) {

            players[currentPlayer].bank -= properties[players[currentPlayer].location].basePrice;
            properties[players[currentPlayer].location].ownerID = currentPlayer;
            return true;
        } else {
            return false;
        }
    }

    public boolean payOutOfJail() {
        if (players[currentPlayer].inJail > 0 && players[currentPlayer].bank >= 50) {
            players[currentPlayer].bank -= 50;
            players[currentPlayer].inJail = 0;
            return true;
        } else {
            return false;
        }
    }

    public boolean getOutOfJailFree() {
        if (players[currentPlayer].inJail > 0 && players[currentPlayer].getOutOfJailFree) {
            players[currentPlayer].getOutOfJailFree = false;
            players[currentPlayer].inJail = 0;
            return true;
        } else {
            return false;
        }
    }

    public boolean payDebt() {
        if (players[currentPlayer].bank >= currentPlayerDebt) {
            players[currentPlayer].bank -= currentPlayerDebt;
            return true;
        } else {
            return false;
        }
    }

    public boolean endTurn() {
        if (currentPlayerDebt == 0) {
            nextTurn();
            return true; // do not release
        } else {
            return false;
        }
    }

    public boolean declareBankruptcy() {
        declareBankrupt(currentPlayer);
        return true;
    }

    public JSONObject getBoardState() {
        JSONObject boardState = new JSONObject();

        if (currentCard != null) {
            JSONObject cardInfo = new JSONObject();
            cardInfo.put("type", currentCardType);
            cardInfo.put("message", currentCard.message);

            boardState.put("event_card", cardInfo);
        }

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

        boardState.put("cells", cells);
        return boardState;
    }

    public PlayerState exportPlayer(int index) {
        ArrayList<String> actions = new ArrayList<>();
        if (currentPlayer == index) {
            if (!diceRolled) {
                actions.add(actionStrings.get(Actions.ROLL_DICE));
            }
            if (properties[players[index].location].ownerID == null) {
                actions.add(actionStrings.get(Actions.BUY_THIS_PROPERTY));
            }
            if (players[index].inJail > 0 && players[index].getOutOfJailFree) {
                actions.add(actionStrings.get(Actions.GET_OUT_OF_JAIL_FREE));
            }
            if (players[index].inJail > 0) {
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
                                new PlayerState.PropertyState(property.name, property.numHouses, property.mortgaged))
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
