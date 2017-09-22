package model;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

public class Player {
    public enum Status {NORMAL, PASS, WIN;}

    private ArrayList<Card> cardsOnHand;
    private Status status;
    private int location;
    protected int numCardsOnhand = 0;

    public Player(int loc) {
        this.location = loc;
        this.status = Status.NORMAL;
        cardsOnHand = new ArrayList<Card>();
    }

    public void addCard(Card cd) {
        cardsOnHand.add(cd);
        numCardsOnhand += 1;
    }

    public Card removeCard(Card expectedCard) {
        Card cardRemoved = null;
        int index = cardsOnHand.indexOf(expectedCard);
        if (index != -1) {//26-Jan-2017: Correct from 1 to -1
            cardRemoved = cardsOnHand.remove(index);
            numCardsOnhand -= 1;
        } else {
            JOptionPane.showMessageDialog(null, "No exist: " + expectedCard, "Warning", JOptionPane.WARNING_MESSAGE);
        }
        return cardRemoved;
    }

    public Card[][] dealCards(ArrayList<Card> pack, int numPlayers, int numCardGiven) {

        //Shuffle the pack of card before dealing
        Collections.shuffle(pack);

        Helper helper = new Helper();
        //System.out.println("PackOfCards after shuffling:\n"+helper.getListCardInString(pack));

        Card[][] cardsForAllPlayers = new Card[numPlayers][numCardGiven];
        Card[] cardsForOnePlayer;

        //Remove number of cards required for 1 player, after another
        for (int numP = 0; numP < numPlayers; numP++) {
            cardsForOnePlayer = new Card[numCardGiven];
            for (int numC = 0; numC < numCardGiven; numC++) {
                cardsForOnePlayer[numC] = removeCardOnTop(pack);
            }
            cardsForAllPlayers[numP] = cardsForOnePlayer;
        }
        return cardsForAllPlayers;
    }

    public void displayDealCardsResult(Card[][] portionsOfCards) {
        System.out.println("\nCards dealt in each portion:");
        int countNumPortions = 0;
        for (Card[] cardsInOnePortion : portionsOfCards) {
            System.out.println("Portion " + countNumPortions + ":");
            for (int i = 0; i < cardsInOnePortion.length; i++) {
                System.out.printf("%d:%s \n", i, cardsInOnePortion[i]);
            }
            System.out.println();
            countNumPortions++;
        }
    }

    public Card removeCardOnTop(ArrayList<Card> packOfCards) {
        Card cardRemoved = null;
        //check whether list of cards is empty, if so, warning and return no card
        int numCardsInPack = packOfCards.size();
        if (numCardsInPack != 0) {
            int indexCardOnTop = numCardsInPack - 1;
            cardRemoved = packOfCards.remove(indexCardOnTop);
        } else {
            JOptionPane.showMessageDialog(null, "No cards left", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        return cardRemoved;
    }

    public void receiveCardsDealt(Card[] listCardsGiven) {
        for (Card cd : listCardsGiven) {
            addCard(cd);
        }
    }

    public void setStatus(Status st) {
        this.status = st;
    }

    public Status getStatus() {
        return this.status;
    }

    public int getLocation() {
        return this.location;
    }

    public String getStringCardsOnhand() {
        String result = "";
        for (int i = 0; i < cardsOnHand.size(); i++) {
            result += (i) + ":" + cardsOnHand.get(i) + "\n";
        }
        return result;
    }

    public ArrayList<Card> getListCardsOnhand() {
        return this.cardsOnHand;
    }

    //Cards satisfying includes:
    // Mineral Cards having higher value,
    // and all Trump Cards existing on Player's hand
    public ArrayList<Card> getCardsHigherValueThan(Card bidCard, Card.Category cat) {
        ArrayList<Card> cardsHigherValue = new ArrayList<>();
        //if it is Trump Card, get it
        //if it is Mineral Card, compare the card with bidCard, only get card with higher value
        for (Card card : cardsOnHand) {
            if (card.isTrump()) {
                cardsHigherValue.add(card);
            } else {
                CardMineral cdp = (CardMineral) card;
                CardMineral bid = (CardMineral) bidCard;
                double comparedValue = cdp.compare(bid, cat);
                if (comparedValue > 0) {
                    cardsHigherValue.add(cdp);
                }
            }
        }
        Helper helper = new Helper();
        String cardHigherValueStr = helper.getListCardInString(cardsHigherValue);
        //System.out.printf("\nDisplay cards higher value than %s:\n %s",
        //      ((CardMineral)bidCard).speak(cat), cardHigherValueStr);
        return cardsHigherValue;
    }

    public String getStringCardsHigherValueThan(Card bidCard, Card.Category cat) {
        ArrayList<Card> listHigherValue = getCardsHigherValueThan(bidCard, cat);
        Helper helper = new Helper();
        String cardHigherValueStr = helper.getListCardInString(listHigherValue);
        return cardHigherValueStr;
    }

    public ArrayList<Card> getListCardsOnhand(boolean isTrumpRequired) {
        ArrayList<Card> listCards = new ArrayList<Card>();

        if (isTrumpRequired) {//Get list TrumpCard
            for (Card card : cardsOnHand) {
                if (card.isTrump()) {
                    listCards.add(card);
                }
            }
        } else {//Get list MineralCard
            for (Card card : cardsOnHand) {
                if (!card.isTrump()) {
                    listCards.add(card);
                }
            }
        }
        return listCards;
    }

    public String getStringCardsOnhand(boolean isTrumpRequired) {
        String res = "";
        ArrayList<Card> listMinerals = getListCardsOnhand(isTrumpRequired);
        for (int i = 0; i < listMinerals.size(); i++) {
            res += "i:" + listMinerals.get(i) + "\n";
        }
        return res;
    }
    //display info about player including Location, status, all cards onhand
    public String toString() {
        return "Player:" + location + ",status:" + status +
                "\nAll cardsOnHand:\n" + getStringCardsOnhand();
    }


}