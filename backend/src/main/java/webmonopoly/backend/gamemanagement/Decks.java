package webmonopoly.backend.gamemanagement;

import java.util.*;

public class Decks {

    static Map<Chance, Card> chanceCards = Map.ofEntries(
            Map.entry(Chance.ADVANCE_TO_GO, new AdvanceTo(0, "Go", false)),
            Map.entry(Chance.ADVANCE_TO_TFGSQ, new AdvanceTo(24, "Trafalgar Square", true)),
            Map.entry(Chance.ADVANCE_TO_MAYFR, new AdvanceTo(39, "Mayfair", false)),
            Map.entry(Chance.ADVANCE_TO_PLML, new AdvanceTo(11, "Pall Mall", true)),
            Map.entry(Chance.BANK_DIVIDEND, new BankDiv()),
            Map.entry(Chance.GOOJAIL, new GOOJail()),
            Map.entry(Chance.GOBACK, new GoBack()),
            Map.entry(Chance.GOJAIL, new GoJail()),
            Map.entry(Chance.GENERAL_REPAIRS, new GeneralRepairs()),
            Map.entry(Chance.SPEEDING_FINE, new SpeedingFine()),
            Map.entry(Chance.TRIP_TO_KX, new TripToKingsX()),
            Map.entry(Chance.BUILDING_LOAN, new BuildingLoan()),
            Map.entry(Chance.DRUNK, new DrunkInCharge()),
            Map.entry(Chance.CROSSWORD_COMP, new CrosswordComp())
    );

    static Map<ComChest, Card> comChestCards = Map.ofEntries(
            Map.entry(ComChest.ADVANCE_TO_GO, new AdvanceTo(0, "Go", false)),
            Map.entry(ComChest.BANK_ERROR, new BankError(200)),
            Map.entry(ComChest.DOCTORS_FEE, new DoctorsFee()),
            Map.entry(ComChest.SALE_OF_STOCK, new SaleOfStock()),
            Map.entry(ComChest.GOOJAIL, new GOOJail()),
            Map.entry(ComChest.GOJAIL, new GoJail()),
            Map.entry(ComChest.HOLIDAY_FUND, new HolidayFund()),
            Map.entry(ComChest.INCOME_TAX_REFUND, new IncomeTaxRefund()),
            Map.entry(ComChest.BIRTHDAY, new Birthday()),
            Map.entry(ComChest.LIFE_INSURANCE, new LifeInsurance()),
            Map.entry(ComChest.HOSPITAL_FEES, new HospitalFees()),
            Map.entry(ComChest.SCHOOL_FEES, new SchoolFees()),
            Map.entry(ComChest.STREET_REPAIRS, new StreetRepairs()),
            Map.entry(ComChest.BEAUTY_CONTEST, new BeautyContest()),
            Map.entry(ComChest.INHERIT, new Inherit()),
            Map.entry(ComChest.BACK_TO_OLD_KENT_RD, new BackToOldKent()),
            Map.entry(ComChest.PREFERENCE_SHARES, new PrefShares())
    );

    public enum Chance {
        ADVANCE_TO_GO,
        ADVANCE_TO_TFGSQ,
        ADVANCE_TO_MAYFR,
        ADVANCE_TO_PLML,
        BANK_DIVIDEND,
        GOOJAIL,
        GOBACK,
        GOJAIL,
        GENERAL_REPAIRS,
        SPEEDING_FINE,
        TRIP_TO_KX,
        BUILDING_LOAN,
        DRUNK,
        CROSSWORD_COMP
    }

    public enum ComChest {
        ADVANCE_TO_GO,
        BANK_ERROR,
        DOCTORS_FEE,
        SALE_OF_STOCK,
        GOOJAIL,
        GOJAIL,
        HOLIDAY_FUND,
        INCOME_TAX_REFUND,
        BIRTHDAY,
        LIFE_INSURANCE,
        HOSPITAL_FEES,
        SCHOOL_FEES,
        STREET_REPAIRS,
        BEAUTY_CONTEST,
        INHERIT,
        BACK_TO_OLD_KENT_RD,
        PREFERENCE_SHARES
    }

    public static class Shuffler<T> {
        T[] call(T[] arr) {
            Random rand = new Random();
            for (int i = 0; i < arr.length * 2; i++) {
                int i1 = rand.nextInt(arr.length), i2 = rand.nextInt(arr.length);
                T tmp = arr[i1];
                arr[i1] = arr[i2];
                arr[i2] = tmp;
            }
            return arr;
        }
    }

    abstract static class Card {
        String message;
        public abstract void transform(CoreMonopolyGame mg);
    }

    static class PrefShares extends Card {

        PrefShares() { this.message = "Receive interest on 7% preference shares: £25"; }

        @Override
        public void transform(CoreMonopolyGame mg) { mg.players[mg.currentPlayer].bank += 25; }
    }

    static class BackToOldKent extends Card {

        BackToOldKent() { this.message = "Go back to Old Kent Road. Do not pass Go, do not collect £200"; }

        @Override
        public void transform(CoreMonopolyGame mg) {
            mg.players[mg.currentPlayer].location = 1;
        }
    }

    static class Inherit extends Card {

        Inherit() { this.message = "You inherit £100"; }

        @Override
        public void transform(CoreMonopolyGame mg) { mg.players[mg.currentPlayer].bank += 100; }
    }

    static class BeautyContest extends Card {

        BeautyContest() { this.message = "You have won second prize in a beauty contest. Collect £10"; }

        @Override
        public void transform(CoreMonopolyGame mg) { mg.players[mg.currentPlayer].bank += 10; }
    }

    static class StreetRepairs extends Card {

        StreetRepairs() { this.message = "You are assessed for street repairs. £40 per house. £115 per hotel"; }

        @Override
        public void transform(CoreMonopolyGame mg) {
            int amount = 0;
            for (CoreMonopolyGame.Property p : mg.properties) {
                if (p.ownerID == mg.currentPlayer) {
                    amount += (p.numHouses == 5) ? 115 : p.numHouses * 40;
                }
            }
            mg.currentPlayerDebt += amount;
        }
    }

    static class SchoolFees extends Card {

        SchoolFees() { this.message = "Pay school fees of £50"; }

        @Override
        public void transform(CoreMonopolyGame mg) { mg.currentPlayerDebt += 50; }
    }

    static class HospitalFees extends Card {

        HospitalFees() { this.message = "Pay hospital fees of £100"; }

        @Override
        public void transform(CoreMonopolyGame mg) { mg.currentPlayerDebt += 100; }
    }

    static class LifeInsurance extends Card {

        LifeInsurance() { this.message = "Life insurance matures. Collect £100"; }

        @Override
        public void transform(CoreMonopolyGame mg) { mg.players[mg.currentPlayer].bank += 100; }
    }

    static class Birthday extends Card {

        Birthday() { this.message = "It is your birthday. Collect £10 from every player"; }

        @Override
        public void transform(CoreMonopolyGame mg) {
            int sum = 0;
            for (int i = 0; i < mg.players.length; i++) {
                if (i == mg.currentPlayer || mg.players[i].bank < 10) {
                    continue;
                }
                mg.players[i].bank -= 10;
                sum += 10;
            }
            mg.players[mg.currentPlayer].bank += sum;
        }
    }

    static class IncomeTaxRefund extends Card {

        IncomeTaxRefund() { this.message = "Income tax refund. Collect £20"; }

        @Override
        public void transform(CoreMonopolyGame mg) { mg.players[mg.currentPlayer].bank += 20; }
    }

    static class HolidayFund extends Card {

        HolidayFund() { this.message = "Holiday fund matures. Receive £100"; }

        @Override
        public void transform(CoreMonopolyGame mg) { mg.players[mg.currentPlayer].bank += 100; }
    }

    static class SaleOfStock extends Card {

        SaleOfStock() { this.message = "From sale of stock you get £50."; }

        @Override
        public void transform(CoreMonopolyGame mg) { mg.players[mg.currentPlayer].bank += 50; }
    }

    static class DoctorsFee extends Card {

        DoctorsFee() { this.message = "Doctor's fee. Pay £50"; }

        @Override
        public void transform(CoreMonopolyGame mg) { mg.currentPlayerDebt += 50; }
    }

    static class CrosswordComp extends Card {

        CrosswordComp() { this.message = "You have won a crossword competition. Collect £100"; }

        @Override
        public void transform(CoreMonopolyGame mg) {
            mg.players[mg.currentPlayer].bank += 100;
        }
    }

    static class DrunkInCharge extends Card {

        DrunkInCharge() { this.message = "\"Drunk in charge\" fine £20"; }

        @Override
        public void transform(CoreMonopolyGame mg) {
            mg.currentPlayerDebt += 20;
        }
    }

    static class BuildingLoan extends Card {

        BuildingLoan() { this.message = "Your building loan matures. Collect £150"; }

        @Override
        public void transform(CoreMonopolyGame mg) {
            mg.players[mg.currentPlayer].bank += 150;
        }
    }

    static class TripToKingsX extends Card {

        TripToKingsX() { this.message = "Take a trip to Kings Cross Station. If you pass Go, collect £200"; }

        @Override
        public void transform(CoreMonopolyGame mg) {
            Card advanceToKX = new AdvanceTo(5, "King's Cross Station", true);
            advanceToKX.transform(mg);
        }
    }

    static class SpeedingFine extends Card {

        SpeedingFine() { this.message = "Speeding fine £15"; }

        @Override
        public void transform(CoreMonopolyGame mg) {
            mg.currentPlayerDebt += 15;
        }
    }

    static class GeneralRepairs extends Card {

        GeneralRepairs() { this.message = "Make general repairs on all your property. For each house pay £25. For each hotel pay £100"; }

        @Override
        public void transform(CoreMonopolyGame mg) {
            int amount = 0;
            for (CoreMonopolyGame.Property p : mg.properties) {
                if (p.ownerID == mg.currentPlayer) {
                    amount += Math.min(p.numHouses * 25, 100);
                }
            }
            mg.currentPlayerDebt += amount;
        }
    }

    static class GoJail extends Card {

        GoJail() { this.message = "Go to Jail. Go directly to Jail, do not pass Go, do not collect £200"; }

        @Override
        public void transform(CoreMonopolyGame mg) {
            mg.players[mg.currentPlayer].location = 10;
            mg.players[mg.currentPlayer].inJail = 3;
        }
    }

    static class GoBack extends Card {

        GoBack() { this.message = "Go back 3 spaces"; }

        @Override
        public void transform(CoreMonopolyGame mg) {
            mg.players[mg.currentPlayer].location -= 3;
        }
    }

    static class GOOJail extends Card {

        GOOJail() { this.message = "Get out of jail free"; }

        @Override
        public void transform(CoreMonopolyGame mg) {
            mg.players[mg.currentPlayer].getOutOfJailFree = true;
        }
    }

    static class BankDiv extends Card {

        BankDiv() { this.message = "Bank pays you dividend of £50"; }

        @Override
        public void transform(CoreMonopolyGame mg) {
            mg.players[mg.currentPlayer].bank += 50;
        }
    }

    static class AdvanceTo extends Card {

        int destination;

        AdvanceTo(int destination, String destString, boolean passGoExtension) {
            this.message = "Advance to " + destString + ".";
            if (passGoExtension) {
                this.message += " If you pass Go, collect £200";
            }
            this.destination = destination;
        }

        @Override
        public void transform(CoreMonopolyGame mg) {
            int playerLoc = mg.players[mg.currentPlayer].location;
            if (playerLoc > destination) {
                mg.players[mg.currentPlayer].bank += 200;
            }
            mg.players[mg.currentPlayer].location = destination;
        }
    }

    static class BankError extends Card {

        int amount;

        BankError(int amount) {
            this.message = "Bank error in your favour. Collect £" + amount + "!";
            this.amount = amount;
        }

        @Override
        public void transform(CoreMonopolyGame mg) {
            mg.players[mg.currentPlayer].bank += amount;
        }
    }
}
