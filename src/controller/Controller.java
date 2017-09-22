package controller;
import javax.swing.*;

import model.CardMineralImage;
import model.AddImage;
import model.Card;
import model.Card.*;
import model.Card.Category;
import model.CardButton;
import model.CardMineral;
import model.CardTrump;
import model.GameLogic;
import model.Helper;
import model.Player;
import view.GameDisplay;
import view.MainFrame;
import view.PlayerView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Controller {
    private MainFrame mainFrame;
    private GameLogic gameLogic;
    private GameDisplay gameDisplayPanel;
    private Player[] listPlayers;
    private ArrayList<Card> packOfCards;

    final static String FILE_TO_PARSE = "src/card.txt";
    final static int NUM_CARDS_FOR_EACH_START = 8;
    private final static int LOWER_BOUND_NUM_PLAYER = 3;
    private final static int UPPER_BOUND_NUM_PLAYER = 5;
    private final static int NUM_PLAYER_DEFAULT = 3;

    JLabel lbPackOfCards = new JLabel("PackOfCard");
    JLabel lbBidCard = new JLabel("bidCard");
    JLabel lbNotification = new JLabel("Notification");
    JButton btPass = new JButton("PASS");
    String strPathImgPack = "src/images/Slide65.jpg";
    String strPathImgBidCard = "src/images/Slide66.jpg";
    String strPathImgDealer = "src/images/Slide02.jpg";
    ArrayList<JButton> listButtonCategories = new ArrayList<>();

    Player playerActive;
    Card cardSelected;
    Card.Category categorySelected;
    CardTrump trumpSelected;
    protected int numNormalPlayers;
    protected int numPlayersWin;
    protected int totalnumPlayers;
    Card cardPickAfterTrumpThrown;

    boolean flagEndGame = false;
    boolean flagOnlyOneHavingMineral = false;
    boolean flagLastOneInCurrentRound = (numNormalPlayers == 1);
    boolean flagTrumpCardThrown = false;
    boolean flagWinnerFound = false;
    boolean flagEndOneRound = false;

    //Holding all card ~button (both Mineral and Trumps) including players holding and packofcards
    ArrayList<CardButton> listAllCardButton = new ArrayList<>();

    public Controller(MainFrame mf, GameLogic gl){
        this.mainFrame = mf;
        this.gameLogic = gl;
        mainFrame.addListener(this);

        System.out.println("Start UI");
        this.mainFrame.setVisible(true);
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Remind to select number of players
        String welcome = "<html>Welcome to Mineral Super Trump Game card<br>" +
                "Please select number of player to continue</html>";
        JOptionPane.showMessageDialog(null,welcome,"Welcome",JOptionPane.INFORMATION_MESSAGE);
    }

    public void btGoClick(int numPlayers) {
        numNormalPlayers = numPlayers;
        totalnumPlayers = numPlayers;
        CardTrump[] cardTrumps =
                {new model.CardTrumpImage("The Miner",
                        Category.ECOVALUE, Helper.createImageIcon("src/images/Slide55.jpg")),
                new model.CardTrumpImage("The Petrologist",
                        Category.CRUSTAL, Helper.createImageIcon("src/images/Slide56.jpg")),
                new model.CardTrumpImage("The Gemologist",
                        Card.Category.HARDNESS, Helper.createImageIcon("src/images/Slide57.jpg")),
                new model.CardTrumpImage("The Mineralogist",
                        Card.Category.CLEAVAGE, Helper.createImageIcon("src/images/Slide58.jpg")),
                new model.CardTrumpImage("The Geophysicist",
                        Card.Category.GRAVITY, Helper.createImageIcon("src/images/Slide59.jpg")),
                new model.CardTrumpImage("The Geologist",
                        Card.Category.ANYCATEGORY, Helper.createImageIcon("src/images/Slide60.jpg"))};

        //2. Create packOfCards with Helper from 2 sources: file provided, and CardTrumpsArray
        Helper helper = new Helper();
        packOfCards = helper.createPackOfCardsUI(FILE_TO_PARSE, cardTrumps);
        String msg ="Game will start with: "+numPlayers+" players and " + packOfCards.size()+" cards.";
        JOptionPane.showMessageDialog(null,msg,"Notification",JOptionPane.INFORMATION_MESSAGE);

        gameLogic = new GameLogic(numPlayers, NUM_CARDS_FOR_EACH_START,cardTrumps,packOfCards);
        listPlayers = gameLogic.getListPlayers();

        //add JLabel lbPackOfCards, lbBidCard, lbNotification;
        //JButton btPass to GameDisplay
        gameDisplayPanel = new GameDisplay(listPlayers);
        ImageIcon imageIconPack =
                new ImageIcon(new ImageIcon(strPathImgPack).getImage().getScaledInstance(50, 75, Image.SCALE_DEFAULT));
        lbPackOfCards.setIcon(imageIconPack);
        lbPackOfCards.setHorizontalAlignment(SwingConstants.CENTER);
        //lbBidCard
        ImageIcon imageIcon =
                new ImageIcon(new ImageIcon(strPathImgBidCard).getImage().getScaledInstance(50, 75, Image.SCALE_DEFAULT));
        lbBidCard.setIcon(imageIcon);
        lbBidCard.setHorizontalAlignment(SwingConstants.CENTER);
        //add label for notification
        //Add button Pass
        //5 JButton for categories
        JButton btCatEco = new JButton(Card.Category.ECOVALUE.toString());
        JButton btCatCrustal = new JButton(Card.Category.CRUSTAL.toString());
        JButton btCatHardness = new JButton(Card.Category.HARDNESS.toString());
        JButton btCatCleavage = new JButton(Card.Category.CLEAVAGE.toString());
        JButton btCatGravity = new JButton(Card.Category.GRAVITY.toString());

        //add to arraylist of button categories
        listButtonCategories.add(btCatEco);
        listButtonCategories.add(btCatCrustal);
        listButtonCategories.add(btCatHardness);
        listButtonCategories.add(btCatCleavage);
        listButtonCategories.add(btCatGravity);

        //addUtilities to sharedTop panel
        gameDisplayPanel.addUtilities(lbPackOfCards,lbBidCard,lbNotification,btPass, listButtonCategories);

        //add gamesDisplayPanel to mainFrame
        mainFrame.add(gameDisplayPanel, BorderLayout.CENTER);
        lbNotification.setText("Please Press Pick Dealer");
    }

    //button PickDealerClick: Dealer define and dealing the cards
    public void btPickDealerClick() {
        Player dealer = gameLogic.startGame();//Start a game
        listPlayers = gameLogic.getListPlayers();

        //Announce who is dealer
        String message = "<html>Player "+dealer.getLocation() +
                "<br> is selected randomly as the Dealer</html>";
        lbNotification.setText(message);
        JOptionPane.showMessageDialog(null,message,"Notification",JOptionPane.INFORMATION_MESSAGE);

        for (JLabel[] e : gameDisplayPanel.getListLabelInfo()){
            String loc = e[0].getText();
            if (loc.equals(String.valueOf(dealer.getLocation()))){
                e[0].setIcon(new ImageIcon(
                        new ImageIcon(strPathImgDealer).getImage().getScaledInstance(10,20, Image.SCALE_DEFAULT)));
            }
        }

        //Create CardButton objects for initial cards holding by each player
        ArrayList<PlayerView> listPlayerView = gameDisplayPanel.getListPlayerView();
        for (PlayerView pv: listPlayerView){
            ArrayList<CardButton> listCardButtonEachPlayer = new ArrayList<>();
            for (Card card: pv.getPlayer().getListCardsOnhand()){
                //create cardButton object, to keep association between card and button
                CardButton cb = new CardButton(card);

                //create arlistCardButton holding association between card and button
                listCardButtonEachPlayer.add(cb);
                listAllCardButton.add(cb);
            }
            pv.setCardButtonOnhand(listCardButtonEachPlayer);
        }

        //Create CardButton for cards inside packOfCard
        for (Card card: packOfCards){
            CardButton cbp = new CardButton(card);
            listAllCardButton.add(cbp);
        }

        //render PlayerView on the gameDisplayPanel
        gameDisplayPanel.renderAllPlayerViewAtStart();
        //mainFrame.validate();

    /*====End processing of display of result startGame()================*/
        //add Action listener to all cards (Mineral, Trump, category, Pass)
        //addListenerToCardButton();
        //separate into 2 groups: Mineral and Trump
        ArrayList<CardButton> listMineralButton = new ArrayList<>();
        ArrayList<CardButton> listTrumpButton = new ArrayList<>();
        for (CardButton cb: listAllCardButton){
            if (cb.getCard().isTrump()){
                listTrumpButton.add(cb);
            }else {
                listMineralButton.add(cb);
            }
        }
        addListenerToMineralButton(listMineralButton);
        addListenerToTrumpButton(listTrumpButton);

        addListenerToCategoryButton();
        addListenerToPassButton();
        lbNotification.setText("Finish addListener to cards,cate,pass");
        btPass.setEnabled(false);
        requestMineralOfPlayerNextTo(dealer);

    }//end btPickDealerClick()

    private void processEndGame() {
        //disable all buttons
        for (Player p: listPlayers){
            disableAllButtonOfOnePlayer(p);
        }
        String msg ="<html>The last is Player "+ playerActive.getLocation() +
                "<br>The game is over. See you next time</html>";
        JOptionPane.showMessageDialog(null,msg,"End Game", JOptionPane.INFORMATION_MESSAGE);
    }

    private void proccessEndOneRound() {
        JOptionPane.showMessageDialog(null,"Start Process Ending a Round","End Round", JOptionPane.INFORMATION_MESSAGE);
        //disable all button of activePlayer
        disableAllButtonOfOnePlayer(playerActive);

        //get back all players PASS
        int numPlayerBack = broughtPassPlayerToNormal();

        //Recalculate flag
        flagTrumpCardThrown = false;
        //Flag EndOneRound  is recalculated
        flagLastOneInCurrentRound = (numNormalPlayers == 1);
        flagEndOneRound = false;
        flagWinnerFound = false;

        flagEndGame = isEndGame();
        System.out.printf("After Process End Round: flagEndGame:%s, flagEndRound:%s, lastOne:%s\n",
                flagEndGame,flagEndOneRound,flagLastOneInCurrentRound);
        //reset input
        categorySelected = null;
        cardSelected = null;
    }

    private void enableCategoryButtons() {
        //disable all category button
        for (JButton bt: listButtonCategories){
            bt.setEnabled(true);
        }
    }

    private int broughtPassPlayerToNormal() {
        int numPlayerBack =0;
        String message ="<html>";
        if (flagLastOneInCurrentRound) {
            message += "As Player " + playerActive.getLocation() + " as last one,";
        }
        if(flagTrumpCardThrown) {
            message +="<br>As Player"+playerActive.getLocation()+" threw a Trump Card,";
        }
        message +="all Pass Player back to NORMAL.<br>";
        //System.out.println(message);

        int numPass = totalnumPlayers-numNormalPlayers-numPlayersWin;
        if (numPass>0) {
            String strPlayersBroughtBack = "Player:";
            for (Player p : listPlayers) {
                if (p.getStatus().equals(Player.Status.PASS)) {
                    //setStatus for Player
                    p.setStatus(Player.Status.NORMAL);
                    //change status in display
                    gameDisplayPanel.changeStatusPlayer(p, Player.Status.NORMAL);
                    numNormalPlayers += 1;
                    numPlayerBack +=1;
                    strPlayersBroughtBack += p.getLocation()+", ";
                }
            }
            //System.out.println("Players has been back to game: "+strPlayersBroughtBack);
            message +="Players has been back: "+strPlayersBroughtBack+"<br>";

        }else {
            message +="There's no PASS Players at the moment.<br>";
            //System.out.println("There's no PASS Players at the moment.");
        }
        message +="Player "+playerActive.getLocation()+" will start the new round<br>" +
                "Let choose a category, then a Mineral</html>";
        lbNotification.setText(message);
        return numPlayerBack;
    }

    private void requestMineralOfPlayerNextTo(Player dealer) {
        //gameLogicUI.manageGame(dealer);
        //pick the first player to start
        Player playerStart = gameLogic.getNearestNormalPlayerOf(dealer);
        //assign playerActive
        playerActive = playerStart;

        //disable all Button, except categories
        for (Player p: listPlayers) {
            disableAllButtonOfOnePlayer(p);
        }
        //disable btPass
        btPass.setEnabled(false);
        //notify player
        String messageFP = "<html>Dealer: Player " + dealer.getLocation()
                + "<br>Next turn, Player " + playerStart.getLocation() + " has:" +
                "<br>Total cards: " + playerStart.getListCardsOnhand().size() +
                "<br>Mineral: " +    playerStart.getListCardsOnhand(false).size() +
                "<br>Trump: " + playerStart.getListCardsOnhand(true).size() +
                "<br>Please pick a Category, then a Mineral</html>";
        lbNotification.setText(messageFP);
    }//end requestMineralOfPlayerNextTo()

    private void addListenerToTrumpButton(ArrayList<CardButton> listTrumpButton) {
        for (CardButton cb : listTrumpButton) {
            cb.getButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    flagTrumpCardThrown = true;
                    trumpSelected = (CardTrump) cb.getCard();
                    //remove the CardButton object from both listPlayerView and listPanelOnhand
                    removeCardButton(playerActive, cb);
                    lbNotification.setText("<html>You chose Trump Card: " +
                            trumpSelected.getName() +
                            "<br>Choose any Mineral</html>");
                    //disable all trump if having some
                    ArrayList<Card> listMineral = playerActive.getListCardsOnhand(false);
                    gameDisplayPanel.enableSelectedButtonsOfPlayer(playerActive, listMineral);
                    //remove bidCard
                    cardSelected = null;
                    //take down the bidCard display
                    lbBidCard.setIcon(Helper.createImageIcon(CardButton.strPathImgBackCover));
                    lbBidCard.setText("");

                    if (listMineral.size()==0) {
                        flagEndOneRound = true;
                        proccessEndOneRound();
                    }

                    //Check winner
                    isWinner(playerActive);
                }//end actionPerformed()
            });
        }
    }

    private void addListenerToMineralButton(ArrayList<CardButton> listMinerals) {
        for (CardButton cbMineral: listMinerals){
            cbMineral.getButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (flagTrumpCardThrown){
                        //Assign Mineral card is chosen after Trump is thrown
                        cardPickAfterTrumpThrown = cbMineral.getCard();

                        //Handle Special case:
                        //TrumpCard (The Geophysicist) and Magnetite Mineral card are thrown together
                        //this player will be immediately the winner, no matter it still has cards on hand >1
                        Card.Category cateOfGeophysicist = Card.Category.GRAVITY;
                        String nameMagnetiteCard = "Magnetite";
                        boolean isSpecialCase = ((trumpSelected.getCategoryConst().equals(cateOfGeophysicist))
                                && (cardPickAfterTrumpThrown.getName().equals(nameMagnetiteCard)));

                        if (isSpecialCase) {
                            playerActive.setStatus(Player.Status.WIN);
                            numPlayersWin += 1;
                            numNormalPlayers -= 1;
                            String msg = "<html>You, Player "+playerActive.getLocation()+
                                    " win the hand " +
                                    "<br>as you threw both "+trumpSelected.getName()+
                                    " and "+ cardPickAfterTrumpThrown.getName()+
                                    "<br>You are a brand-new Winner</html>";
                            lbNotification.setText(msg);
                            //signal a new winner
                            //flagWinnerFound = true;
                            //Change its status to Winner in model, and view
                            playerActive.setStatus(Player.Status.WIN);
                            gameDisplayPanel.changeStatusPlayer(playerActive, Player.Status.WIN);
                        }else {
                            //remove a card only, and start a new round
                            String msg1 ="<html>Player "+playerActive.getLocation()+
                                    ", you've choose a Trump: "+ trumpSelected.getName()+
                                    "<br>then pick Mineral: "+ cardPickAfterTrumpThrown.getName()+"</html>";
                            lbNotification.setText(msg1);
                            JOptionPane.showMessageDialog(null,msg1,"Trump & Mineral together", JOptionPane.INFORMATION_MESSAGE);
                            removeCardButton(playerActive, cbMineral);
                        }

                        flagEndOneRound = true;
                    }//end Minerals onhand of Player threw TrumpCard>0

                    if (!flagTrumpCardThrown){
                        cardSelected = cbMineral.getCard();
                        //check whether category is pre-defined
                        //if yes, Notify user + plus disable all buttons
                        //if not, Error Message, ask user to choose a category
                        String message = "<html>Controller: Player " + playerActive.getLocation() +
                                " selected:<br>nameCardButton: " + cardSelected.getName() +
                                "<br>Value: " + ((CardMineral) cardSelected).getCategoryValue(categorySelected)
                                + "<br>category: " + categorySelected + "<html>";
                        lbNotification.setText(message);

                        playerActive = processMineralCardClick(playerActive, categorySelected, cardSelected);


                        //enable card of next player
                        //disable all other buttons of active player
                        //enableAllButtonOfOnePlayer(playerActive);
                        ArrayList<Card> listCardHigherThanSelectedCard =
                                playerActive.getCardsHigherValueThan(cardSelected, categorySelected);
                        String messageFP = "<html>Player " + playerActive.getLocation() + " has:" +
                                "<br>Total cards with higher value than bidCard: "
                                + listCardHigherThanSelectedCard.size() + "</html>";
                        lbNotification.setText(messageFP);

                        if (listCardHigherThanSelectedCard.size() > 0) {
                            gameDisplayPanel.enableSelectedButtonsOfPlayer(playerActive,listCardHigherThanSelectedCard);
                        } else {//pass
                            String msg = "<html>Player " + playerActive.getLocation() +
                                    " has no higher value card." +
                                    "<br>You must pass your turn</html>";
                            lbNotification.setText(msg);
                        }
                    }//!flagTrumpCardThrown

                    flagWinnerFound = isWinner(playerActive);

                    //Check EndRound and EndGame
                    if (isEndOneRound(flagTrumpCardThrown,flagWinnerFound)){
                        proccessEndOneRound();
                        if (!isEndGame()) {
                            //find the next player
                            playerActive = gameLogic.getNearestNormalPlayerOf(playerActive);
                            lbNotification.setText("Next turn, Player " + playerActive.getLocation());
                            //enable all category button
                            enableCategoryButtons();
                        }
                        if (isEndGame()){
                            processEndGame();
                        }
                    }
                }//end actionPerformed()
            });
        } //addListenerToMineralButton
    }

    private boolean isWinner(Player person){
       boolean result = false;
        //Check number of card onhand of
        if (person.getListCardsOnhand().size()==0){
            numPlayersWin += 1;
            numNormalPlayers -= 1;
            //signal a new winner has been found
            //flagWinnerFound = true;
            String msgWinner ="Brand-new winner: Player " + person.getLocation();
            lbNotification.setText(msgWinner);
            JOptionPane.showMessageDialog(null,msgWinner,"Announcement Winner", JOptionPane.INFORMATION_MESSAGE);
            System.out.println(msgWinner);
            //signal a new winner
            //flagWinnerFound = true;
            //Change its status to Winner in model, and view
            person.setStatus(Player.Status.WIN);
            gameDisplayPanel.changeStatusPlayer(person, Player.Status.WIN);
            result = true;
        }
        return result;
    }

    private void addListenerToPassButton() {
        btPass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Card> listCardHigherThanSelectedCard = new ArrayList<Card>();
                do {
                    //player pick a card on the pack of Card, add it to its cardsOnhand
                    Card cardOnTopOfPack = playerActive.removeCardOnTop(packOfCards);
                    CardButton cbOnTopOfPack = getCardButtonFrom(cardOnTopOfPack);
                    addCardButton(playerActive, cbOnTopOfPack);

                    //Change its status to Pass in model, and view
                    playerActive.setStatus(Player.Status.PASS);
                    gameDisplayPanel.changeStatusPlayer(playerActive, Player.Status.PASS);

                    //Decrease number of normal players
                    numNormalPlayers -= 1;
                    //notification
                    String msg = "<html>Player " + playerActive.getLocation() +
                            " pass the turn. <br>Add a Card: " + cbOnTopOfPack.getCard().getName() +
                            "<br>has " + playerActive.getListCardsOnhand().size() + "cards onhand." +
                            "<br>Pack has: " + packOfCards.size() + " cards." +
                            "<br>Normal players: " + numNormalPlayers + "</html>";
                    lbNotification.setText(msg);
                    JOptionPane.showMessageDialog(null,msg,"Notification Pass", JOptionPane.INFORMATION_MESSAGE);
                    //disable all buttons of that player
                    disableAllButtonOfOnePlayer(playerActive);

                    //call Model to getNearestNormalPlayerOf
                    playerActive = gameLogic.getNearestNormalPlayerOf(playerActive);
                    lbNotification.setText("Next turn, player: " + playerActive.getLocation());

                    //enable cards of next player with higher value only than bidCard
                    listCardHigherThanSelectedCard =
                            playerActive.getCardsHigherValueThan(cardSelected, categorySelected);
                    JOptionPane.showMessageDialog(null,"Next turn, player: "+playerActive.getLocation()+
                            " cards higher: "+listCardHigherThanSelectedCard.size(),
                            "Next turn",JOptionPane.INFORMATION_MESSAGE);

                    if (numNormalPlayers==1){
                        flagEndOneRound=true;
                        break;
                    }
                }while (listCardHigherThanSelectedCard.size()==0);

                //At this point, playerActive has card higher value than selected card
                gameDisplayPanel.enableSelectedButtonsOfPlayer(playerActive, listCardHigherThanSelectedCard);
                String messageFP = "<html>Finally, found Player " + playerActive.getLocation() + " has:" +
                        "<br>Total cards with higher value than bidCard: "
                        + listCardHigherThanSelectedCard.size() + "</html>";
                lbNotification.setText(messageFP);
                if (flagEndOneRound){
                    proccessEndOneRound();
                    disableAllButtonOfOnePlayer(playerActive);
                    enableCategoryButtons();
                    String msg = "Player "+playerActive.getLocation()+" is the only one left, others passed";
                    JOptionPane.showMessageDialog(null,msg,"Announcement in btPassClick",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private CardButton getCardButtonFrom(Card card) {
        CardButton res = null;
        for (CardButton cardButton: listAllCardButton){
            if (cardButton.getCard().getName().equals(card.getName())){
                res = cardButton;
            }
        }
        return res;
    }

    private void addListenerToCategoryButton() {
        for (JButton btcate: listButtonCategories ){
            btcate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Check whether card null, if yes, notify user to selected card
                    // when a category is selected, disable all categories button
                    categorySelected = Card.Category.valueOf(btcate.getText());
                    String message = "You, Player " + playerActive.getLocation()+
                            " chose Category: "+ categorySelected;
                    lbNotification.setText(message);

                    //disable all category button
                    for (JButton bt: listButtonCategories){
                        bt.setEnabled(false);
                    }
                    ArrayList<Card> listMineralOfPlayerStart = playerActive.getListCardsOnhand(false);
                    gameDisplayPanel.enableSelectedButtonsOfPlayer(playerActive, listMineralOfPlayerStart);

                    if(cardSelected == null) {//notfiy user to choose card
                        String msg = "<html>Player "+playerActive.getLocation()+", you chose: "+btcate.getText()+
                                ".<br>Remember to choose a Card to continue</html>";
                        lbNotification.setText(msg);
                    }
                }
            });
        }
    }

    private Player processMineralCardClick(Player person, Card.Category cateSelect, Card cardSelect) {
        CardButton cardButtonClicked = getCardButtonFrom(cardSelect);
        //remove the CardButton object from both listPlayerView and listPanelOnhand
        removeCardButton(person, cardButtonClicked);

       //disable all other buttons of active player
        disableAllButtonOfOnePlayer(playerActive);
        btPass.setEnabled(true);

        //show cardname into the cardBid
        String msgBidCard = "<html>" + cardSelect.getName() +
                "<br>" +((CardMineral) cardSelect).getCategoryValue(cateSelect) +
                "<br>" + cateSelect + "</html>";
        lbBidCard.setText(msgBidCard);
        lbBidCard.setIcon(((AddImage)cardSelect).getImage());

        //isWinner check before go to the next Player
        isWinner(person);

        //call Model to getNearestNormalPlayerOf
        Player next = gameLogic.getNearestNormalPlayerOf(person);
        lbNotification.setText("Next turn, player: " + next.getLocation());
        return  next;
    }

    private void enableAllButtonOfOnePlayer(Player player) {
        ArrayList<CardButton> listCardB1 = gameDisplayPanel.getListCardButtonOnhandOfPlayer(player);
        for(CardButton cb: listCardB1){
            cb.getButton().setIcon(cb.getImageIconBackCover());
            cb.getButton().setEnabled(true);
            System.out.println("Here to enable card of next player "+ player.getLocation());
            //System.out.println("Add actionlistener to cardbutton:" + cb.getCard().getName());
        }
    }

    private void disableAllButtonOfOnePlayer(Player player) {
        ArrayList<CardButton> listCardB = gameDisplayPanel.getListCardButtonOnhandOfPlayer(player);
        for(CardButton cb: listCardB){
            cb.getButton().setIcon(cb.getImageIconBackCover());
            cb.getButton().setEnabled(false);
        }
    }

    //remove a CardButton object from a player
    //remove from PlayerView
    //remove from listPanelOnhand
    //remove from listonhand
    private void removeCardButton(Player person, CardButton cb) {
        //remove it from ArrayList inside PlayerView
        for (PlayerView pv: gameDisplayPanel.getListPlayerView()){
            if (pv.getListCardButtonOnhand().contains(cb)) {
                pv.getListCardButtonOnhand().remove(cb);
            }
        }

        //remove it from list PanelOnhand
        ArrayList<JPanel> listPanelOnhand = gameDisplayPanel.getListPanelOnhand();
        for (JPanel p: listPanelOnhand){
            if (p.getName().equals(String.valueOf(person.getLocation()))){
                p.remove(cb.getButton());
            }
        }
        lbNotification.setText("Card removed: "+ cb.getCard().getName());
        //remove from listonhand
        person.removeCard(cb.getCard());
    }

    //add a CardButton object to a Player
    //add it to PlayerView
    //add it to ListPanelOnhand
    //add list onhand
    private void addCardButton(Player person, CardButton cbAdded) {
        //add it into ArrayList inside PlayerView
        for (PlayerView pv: gameDisplayPanel.getListPlayerView()){
            if (pv.getPlayer().getLocation()==person.getLocation()){
                pv.getListCardButtonOnhand().add(cbAdded);
            }
        }
        //add card to list PanelOnhand
        ArrayList<JPanel> listPanelOnhand = gameDisplayPanel.getListPanelOnhand();
        for (JPanel p: listPanelOnhand){
            if (p.getName().equals(String.valueOf(person.getLocation()))){
                p.add(cbAdded.getButton());
            }
        }
        //add to listonhand
        person.addCard(cbAdded.getCard());
        //enable a card, showup the card
        cbAdded.getButton().setIcon(((AddImage)cbAdded.getCard()).getImage());
        lbNotification.setText("Card added: "+ cbAdded.getCard().getName());
    }

    public void btRestartClick() {
        //http://stackoverflow.com/questions/1234912/how-to-programmatically-close-a-jframe
        this.mainFrame.setVisible(false); //you can't see me!
        this.mainFrame.dispose(); //Destroy the JFrame object
        new App();
    }

    //End of Game when: all players but one are already winners
    public boolean isEndGame() {
        boolean endGame = false;
        endGame = (numPlayersWin == (totalnumPlayers - 1));
        return endGame;
    }

    //End round when one of those following conditions are met
    //1. Last Normal player in the round,
    //2. Trump Card is thrown
    //3. Winner of round is defined (when it has no cards remaining,
    // or it throw Trump Card of Specific Gravity and a Mineral Card of Magnetite
    //4. There is a only one player having a mineral, all others having trumps
    public boolean isEndOneRound(boolean flagTrumpCardThrown, boolean flagWinnerFound) {
        boolean endARound = false;
        boolean endARound1 = (numNormalPlayers == 1);
        boolean endARound2 = flagTrumpCardThrown;
        boolean endARound3 = flagWinnerFound;

        if (endARound1||endARound2||endARound3){
            endARound = true;
        }
        return endARound;
    }

	public static int getNumPlayerDefault() {
		return NUM_PLAYER_DEFAULT;
	}

	public static int getLowerBoundNumPlayer() {
		return LOWER_BOUND_NUM_PLAYER;
	}

	public static int getUpperBoundNumPlayer() {
		return UPPER_BOUND_NUM_PLAYER;
	}

}
