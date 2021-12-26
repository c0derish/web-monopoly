package webmonopoly.backend.model;

import java.util.List;

public class PlayerState {

    boolean yourTurn;
    Integer bank;
    List<PropertyState> properties;
    List<String> actions;
    Integer debt;
    List<String> otherPlayers;

    public PlayerState(boolean yourTurn, Integer bank, List<PropertyState> properties, List<String> actions, Integer debt, List<String> otherPlayers) {
        this.yourTurn = yourTurn;
        this.bank = bank;
        this.properties = properties;
        this.actions = actions;
        this.debt = debt;
        this.otherPlayers = otherPlayers;
    }

    public boolean getYourTurn() {
        return yourTurn;
    }

    public void setYourTurn(boolean yourTurn) {
        this.yourTurn = yourTurn;
    }

    public Integer getBank() {
        return bank;
    }

    public void setBank(Integer bank) {
        this.bank = bank;
    }

    public List<PropertyState> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyState> properties) {
        this.properties = properties;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public Integer getDebt() {
        return debt;
    }

    public void setDebt(Integer debt) {
        this.debt = debt;
    }

    public List<String> getOtherPlayers() {
        return otherPlayers;
    }

    public void setOtherPlayers(List<String> otherPlayers) {
        this.otherPlayers = otherPlayers;
    }

    public static class PropertyState {

        String name;
        Integer houses;
        Boolean morgaged;

        public PropertyState(String name, Integer houses, Boolean morgaged) {
            this.name = name;
            this.houses = houses;
            this.morgaged = morgaged;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getHouses() {
            return houses;
        }

        public void setHouses(Integer houses) {
            this.houses = houses;
        }

        public Boolean getMorgaged() {
            return morgaged;
        }

        public void setMorgaged(Boolean morgaged) {
            this.morgaged = morgaged;
        }
    }
}
