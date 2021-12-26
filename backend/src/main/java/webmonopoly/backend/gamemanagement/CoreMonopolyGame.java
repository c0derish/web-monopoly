package webmonopoly.backend.gamemanagement;

import java.util.*;
import java.util.stream.Collectors;

public class CoreMonopolyGame {

    Random dice = new Random();
    int currentPlayer;
    int currentPlayerDebt;
    boolean diceRolled;
    Player[] players;
    Property[] properties;
    LinkedList<Decks.Chance> chanceCards;
    LinkedList<Decks.ComChest> comChestCards;

    public CoreMonopolyGame(int numPlayers) {
        currentPlayer = 0;
        currentPlayerDebt = 0;
        diceRolled = false;
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new Player(playerDescriptors[i]);
        }
        for (int i = 0; i < 40; i++) {
            properties[i] = new Property(CoreMonopolyGame.propertyNames[i], CoreMonopolyGame.propertyPrices[i]);
        }
        chanceCards = Arrays.stream(
                (new Decks.Shuffler<Decks.Chance>()).call(Decks.Chance.values())
        ).collect(Collectors.toCollection(LinkedList::new));
        comChestCards = Arrays.stream(
                (new Decks.Shuffler<Decks.ComChest>()).call(Decks.ComChest.values())
        ).collect(Collectors.toCollection(LinkedList::new));
    }

    public void nextTurn() {
        assert currentPlayerDebt == 0;
        currentPlayer = (currentPlayer + 1) % players.length;
        diceRolled = false;
    }

    public int rollDice() {
        int roll = dice.nextInt(6) + 1;
        diceRolled = true;
        players[currentPlayer].location = (players[currentPlayer].location + roll) % 40;
        return roll;
    }

    private Object drawCard(LinkedList<Object> cards) {
        Object card = cards.pop();
        cards.add(card);
        return card;
    }

    public static String[] playerDescriptors = { "blue", "green", "red", "goldenrod", "purple", "cyan" };

    public static String[] propertyNames = {
            "go", "old_kent_road", "community_chest", "whitechapel_road", "income_tax", "kings_cross_station",
            "the_angel_islington", "chance", "euston_road", "pentonville_road", "jail", "pall_mall",
            "electric_company", "whitehall", "northumberland_avenue", "marylebone_station", "bow_street",
            "community_chest", "marlborough_street", "vine_street", "free_parking", "strand", "chance",
            "fleet_street", "trafalgar_square", "fenchurch_station", "leicester_square", "coventry_street",
            "water_works", "piccadilly", "go_to_jail", "regent_street", "oxford_street", "community_chest",
            "bond_street", "liverpool_station", "chance", "park_lane", "super_tax", "mayfair"
    };

    public static Integer[] propertyPrices = {
            null, 60, null, 60, null, 200, 100, null, 100, 120, null, 140, 120, 140, 160, 200, 180, null, 180, 200,
            null, 220, null, 220, 240, 200, 260, 260, 120, 280, null, 300, 300, null, 320, 200, null, 350, null, 400
    };

    public static Map<Actions, String> actionStrings = Map.ofEntries(
            Map.entry(Actions.BUY_THIS_PROPERTY, "Buy This Property"),
            Map.entry(Actions.PAY_OUT_OF_JAIL, "Pay £50 To Get Out Of Jail"),
            Map.entry(Actions.GET_OUT_OF_JAIL_FREE, "Get Out Of Jail Free"),
            Map.entry(Actions.PAY_DEBT, "Pay Debt"), // TODO: Append debt value on frontend
            Map.entry(Actions.END_TURN, "End Turn"),
            Map.entry(Actions.DECLARE_BANKRUPTCY, "Declare Bankruptcy")
    );

    public enum Actions {
        BUY_THIS_PROPERTY,
        PAY_OUT_OF_JAIL,
        GET_OUT_OF_JAIL_FREE,
        PAY_DEBT,
        END_TURN,
        DECLARE_BANKRUPTCY
    }

    static class Player {
        Integer location;
        Integer bank;
        boolean inJail;
        boolean getOutOfJailFree;
        String descriptor; // The string repr of the colour 'red', or the piece if pieces are being used e.g 'car'

        public Player(String descriptor) {
            location = 0;
            bank = 1500;
            inJail = false;
            getOutOfJailFree = false;
            this.descriptor = descriptor;
        }

        public Player(int location, int bank, boolean inJail, boolean getOutOfJailFree, String descriptor) {
            this.location = location;
            this.bank = bank;
            this.inJail = inJail;
            this.getOutOfJailFree = getOutOfJailFree;
            this.descriptor = descriptor;
        }
    }

    static class Property {
        String name;
        Integer basePrice;
        Integer ownerID = null;
        boolean morgaged = false;
        int numHouses = 0;

        public Property(String name, Integer basePrice) { this.name = name; this.basePrice = basePrice; }

        public Property(String name, int owner, boolean morgage, int n_houses) {
            this.name = name;
            ownerID = owner;
            morgaged = morgage;
            numHouses = n_houses;
        }
    }

}
