package controller;

import java.util.ArrayList;
import java.util.Scanner;

import model.CardTrump;
import model.GameLogic;
import model.Helper;
import model.Player;
import model.Card;
import model.Card.*;
import model.CardMineral;

//For running on Console
public class MainConsole {
    public static void main(String[] args){
        final String FILE_TO_PARSE = "src/card.txt";
        final int NUM_CARD_TRUMPS = 6;
        final int LOWER_BOUND_NUM_PLAYER =3;
        final int UPPER_BOUND_NUM_PLAYER =5;
        //For number of cards distributed to each player at the beginning of game
        int numCardsGiven = 8;

        //Get user input for number of player (3,5)
        //extract index of those from LOWER to UPPER_BOUND
        ArrayList<Integer> listIndicesNumPlayers = new ArrayList<Integer>();
        for (int i=LOWER_BOUND_NUM_PLAYER; i<(UPPER_BOUND_NUM_PLAYER+1);i++){
            listIndicesNumPlayers.add(i);
        }
        //Ask user to input
        System.out.println("========WELCOME TO THE WORLD OF SUPER TRUMPS GAME=========");
        System.out.printf("To start,please Enter Number of Players(3-5):\n>>>");
        //Validate user input within list of indices
        Helper helper = new Helper();
        int userInput = helper.validateUserInputInListNumericValues(listIndicesNumPlayers);
        int numPlayers = userInput;

        //List of trumps card
        CardTrump[] cardTrumpsArray =
        {new CardTrump("The Miner", Category.ECOVALUE),
                new CardTrump("The Petrologist", Category.CRUSTAL),
                new CardTrump("The Gemologist", Category.HARDNESS),
                new CardTrump("The Mineralogist", Category.CLEAVAGE),
                new CardTrump("The Geophysicist", Category.GRAVITY),
                new CardTrump("The Geologist", Category.ANYCATEGORY)};

        //2. Create packOfCards with Helper support
        //from 2 sources: file provided, and CardTrumpsArray
        ArrayList<Card> packOfCards = helper.createPackOfCards(FILE_TO_PARSE, cardTrumpsArray);
        //System.out.printf("Pack of card:\n%s",helper.getListCardInString(packOfCards));
        System.out.printf("Pack has: %d cards in total\n", packOfCards.size());
        System.out.println("END OF setupGame():");

        //1. Setup a game
        GameLogic gameLogic = new GameLogic(numPlayers,numCardsGiven,cardTrumpsArray,packOfCards);

        //2. Start the game
        Player dealer = gameLogic.startGame();
        //Object[] gameLogicResults = gameLogic.manageGame(dealer);
        //===========================

        System.out.println("\nSTART OF manageGame()");
        //This resultGame includes: totalNumRoundsTaken, lastPlayer
        Object[] result = new Object[2];

        //Display menu to let play choose the option
        //including: Pass, Place Mineral Card, Place Trump Card
        //GameLogic will validate before accept it
        ArrayList<Integer> intOptions = new ArrayList<>();
        intOptions.add(1);
        intOptions.add(2);
        String menu="\nPlease select option(1-2):\n" +
                "1.Pass your turn\n2.Place a higher value card\n";
        Helper helpMenu = new Helper();

        //6.7.Request any Mineral Card from a player, to get Card & Category
        System.out.println("Find Nearest of Dealer(Player " + dealer.getLocation() + ") to pick One Mineral to begin");

        Object[] resultAnyCardFromPlayerNearest = gameLogic.requestAnyMineralCardNearestTo(dealer);
        Card.Category firstCategoryChose = (Card.Category) resultAnyCardFromPlayerNearest[0];
        CardMineral firstMineralCardChosen = (CardMineral) resultAnyCardFromPlayerNearest[1];
        Player firstPlayerChosen = (Player) resultAnyCardFromPlayerNearest[2];

        //--------------------------------------------------------

        //The first player becomes bidder for the next player
        Player playerFaceChallenge = firstPlayerChosen;
        Card cardHigherThanBid = firstMineralCardChosen;
        Card.Category travCategory = firstCategoryChose;

        boolean flagEndGame = false;
        boolean flagOnlyOneHavingMineral = false;
        boolean flagLastOneInCurrentRound = (gameLogic.getNumNormalPlayers() == 1);
        boolean flagTrumpCardThrown = false;
        boolean flagWinnerFound = false;
        boolean flagEndOneRound = gameLogic.isEndOneRound(flagTrumpCardThrown, flagWinnerFound,flagOnlyOneHavingMineral);

        int totalNumRoundsTaken = 0;
        Player lastPlayer = playerFaceChallenge;
        int challengeSq = 0;

        Scanner scan = new Scanner(System.in);
        while (!flagEndGame) {
            Object[] resultOneChallenge = null;
            totalNumRoundsTaken += 1;

            Player playerThrownTrumpCard = null;

            while (!flagEndOneRound) {
                //One round starts here
                challengeSq += 1;

                //find the nearest one to give the challenge
                System.out.printf("\nSTART ROUND:%d, CHALLENGE:%d.\nChallenge from:\n" +
                                "travCategory:%s\nbidCard:%s\nbidPlayer:Player %d\n\n",
                        totalNumRoundsTaken, challengeSq,
                        travCategory,
                        ((CardMineral) cardHigherThanBid).speak(travCategory),
                        playerFaceChallenge.getLocation());
                System.out.printf("Challenge moved from Player %d =>", playerFaceChallenge.getLocation());

                //5. Find the nearest Normal Player to given player, let it become travPlayer
                playerFaceChallenge = gameLogic.getNearestNormalPlayerOf(playerFaceChallenge);
                System.out.printf("=>To nearest:Player %d\n", playerFaceChallenge.getLocation());


                //Ask playerFaceChallenge to choose option
                //whether it pass or place a a higher value card than bidCard
                //if having higher value card, list higher value cards will be suggested
                //if only having lower value card, the player automatically becomes pass
                int numCardsHigherThanBidCard =
                        playerFaceChallenge.getCardsHigherValueThan(cardHigherThanBid,travCategory).size();

                if(numCardsHigherThanBidCard>0) {
                    String menuTemp = "For reference:Player "+playerFaceChallenge.getLocation()+
                            ",you have those HIGHER value cards as follows:\n"
                            +playerFaceChallenge.getStringCardsHigherValueThan(cardHigherThanBid,travCategory)+menu;
                    int userOptionMenu = helpMenu.getOptionFromMenu(menuTemp, intOptions);

                    //If it choose to place a card, as follow
                    //If it decided to pass its turn, call for next player

                    switch (userOptionMenu) {

                        case 1://choose to pass its turn
                            System.out.printf("Before Player %d,%s pass its turn\n" +
                                            "cardsInPack:%d,cardsOnhand:%d\n",
                                    playerFaceChallenge.getLocation(), playerFaceChallenge.getStatus(),
                                    packOfCards.size(), playerFaceChallenge.getListCardsOnhand().size());

                            //playerFaceChallenge pick a card on the pack of Card, add it to its cardsOnhand
                            Card cardOnTopOfPack = playerFaceChallenge.removeCardOnTop(packOfCards);
                            playerFaceChallenge.addCard(cardOnTopOfPack);

                            //Change its status to Pass
                            playerFaceChallenge.setStatus(Player.Status.PASS);

                            //Decrease number of normal players
                            gameLogic.setNumNormalPlayers(gameLogic.getNumNormalPlayers() - 1);

                            System.out.printf("After pass:Player %d,%s\n" +
                                            "cardsInPack:%d,numCardsOnhand:%d\n",
                                    playerFaceChallenge.getLocation(), playerFaceChallenge.getStatus(),
                                    packOfCards.size(), playerFaceChallenge.getListCardsOnhand().size());

                            break;//end choose to pass its turn.
                        // Next, controller will process to find the nearest one for next challenge


                        case 2: //Choose remove a higher value card
                            //Request any card from user
                            cardHigherThanBid = gameLogic.requestPlaceHigherThanBidCard(playerFaceChallenge, cardHigherThanBid, travCategory);


                            //CASE 1&2: TRUMP CARD
                            //If Trump Card is placed,
                            //If Player having more Mineral
                            //      place one Mineral,
                            //      then handle Special case: Trump(Gravity) and Mineral(Magnetite)
                            //If Having no Mineral, ask next nearest to choose new one to start
                            if (cardHigherThanBid.isTrump()) {
                                playerThrownTrumpCard = playerFaceChallenge;

                                //Signal Trump Card has been thrown
                                flagTrumpCardThrown = true;

                                boolean isTrumpRequired = false;
                                int numMineralsOnhand = playerFaceChallenge.getListCardsOnhand(isTrumpRequired).size();

                                if (numMineralsOnhand > 0) {
                                    //Ask place a Mineral card
                                    System.out.printf("As You,Player %d has thrown a Trump Card\n" +
                                                    "You are allowed to place any Mineral before start New Round %d\n",
                                            playerFaceChallenge.getLocation(), (totalNumRoundsTaken + 1));

                                    boolean isTrumpCardRequested = false;
                                    CardMineral mineralChosenAfterHavingTrump =
                                            (CardMineral) gameLogic.requestAnyCardTrumpOrMineral(playerFaceChallenge, isTrumpCardRequested);
                                    System.out.printf("Player %d speaks:%s\n",
                                            playerFaceChallenge.getLocation(), mineralChosenAfterHavingTrump.speak(firstCategoryChose));


                                    //Handle Special case:
                                    //TrumpCard (The Geophysicist) and Magnetite Mineral card are thrown together
                                    //this player will be immediately the winner, no matter it still has cards on hand >1
                                    Card.Category cateOfGeophysicist = Card.Category.GRAVITY;
                                    String nameMagnetiteCard = "Magnetite";
                                    CardTrump cardTrumpHigherThanBid = (CardTrump) cardHigherThanBid;

                                    boolean isSpecialCase = ((cardTrumpHigherThanBid.getCategoryConst().equals(cateOfGeophysicist))
                                            && (mineralChosenAfterHavingTrump.getName().equals(nameMagnetiteCard)));

                                    if (isSpecialCase) {
                                        playerFaceChallenge.setStatus(Player.Status.WIN);
                                        gameLogic.setNumPlayersWin(gameLogic.getNumPlayersWin() + 1);
                                        gameLogic.setNumNormalPlayers(gameLogic.getNumNormalPlayers() - 1);
                                        System.out.printf("\nYou,Player %d WIN THE HAND,as you threw both %s & %s together.\n" +
                                                        "You are Removed from listPlayers & added to listWinners\n",
                                                playerFaceChallenge.getLocation(), cardTrumpHigherThanBid,
                                                mineralChosenAfterHavingTrump.getName());

                                        //signal a new winner
                                        flagWinnerFound = true;
                                    }
                                }//end Minerals onhand of Player threw TrumpCard>0
                                else {
                                    System.out.printf("playerFaceChallenge:Player %d place:\n(%s)\n" +
                                                    "but has no Mineral onhand.\n" +
                                                    "You don't make use of Trump to give out Mineral.\n" +
                                                    "The next player will choose a card for next round\n\n",
                                            playerFaceChallenge.getLocation(),
                                            cardHigherThanBid + "," + cardHigherThanBid.getName());
                                }//end Minerals onhand of Player threw TrumpCard =0

                            }//end cardHigherThanBid is TRUMP

                            else {
                                //Explicit cast to have Mineral
                                CardMineral cardMineralHigherThanBid = (CardMineral) cardHigherThanBid;
                            }

                            //CASE 3: Player placed cardHigherThanBid (Trump or Mineral):
                            // is the last one in this game round
                            //playerFaceChallenge could still have card or not
                            if (gameLogic.getNumNormalPlayers() == 1) {
                                System.out.printf("Player %d,you're the LAST ONE in ROUND:%d\n",
                                        playerFaceChallenge.getLocation(), totalNumRoundsTaken);
                                flagLastOneInCurrentRound = true;
                            }

                            //CASE 4: playerFaceChallenge has no card on hand? Yes, it becomes a new winner
                            if ((playerFaceChallenge.getListCardsOnhand().size() == 0)
                                    && (!playerFaceChallenge.getStatus().equals(Player.Status.WIN))) {
                                System.out.printf("Player %d,you has no card on hand,\n" +
                                                "after placing Card:(%s)\n",
                                        playerFaceChallenge.getLocation(), cardHigherThanBid);
                                playerFaceChallenge.setStatus(Player.Status.WIN);
                                gameLogic.setNumPlayersWin(gameLogic.getNumPlayersWin() + 1);
                                gameLogic.setNumNormalPlayers(gameLogic.getNumNormalPlayers() - 1);
                                //signal a new winner has been found
                                flagWinnerFound = true;
                            }

                            break;//end of case: 2 (Choose remove a card with higher value)
                    }//end switch(userOptionMenu)

                }//end if numCardsHigherThanBidCard>0

                else{//Player can only Pass, similar to case Pass
                    System.out.printf("You have no cards higher value than bidCard\n(%s)\n",cardHigherThanBid);
                    System.out.printf("Before Player %d,%s pass its turn\n" +
                                    "cardsInPack:%d,cardsOnhand:%d\n",
                            playerFaceChallenge.getLocation(), playerFaceChallenge.getStatus(),
                            packOfCards.size(), playerFaceChallenge.getListCardsOnhand().size());

                    //playerFaceChallenge pick a card on the pack of Card, add it to its cardsOnhand
                    Card cardOnTopOfPack = playerFaceChallenge.removeCardOnTop(packOfCards);
                    playerFaceChallenge.addCard(cardOnTopOfPack);

                    //Change its status to Pass
                    playerFaceChallenge.setStatus(Player.Status.PASS);

                    //Decrease number of normal players
                    gameLogic.setNumNormalPlayers(gameLogic.getNumNormalPlayers() - 1);

                    System.out.printf("After pass:Player %d,%s\n" +
                                    "cardsInPack:%d,numCardsOnhand:%d\n",
                            playerFaceChallenge.getLocation(), playerFaceChallenge.getStatus(),
                            packOfCards.size(), playerFaceChallenge.getListCardsOnhand().size());

                    break;//end choose to pass its turn.
                    // Next, controller will process to find the nearest one for next challenge

                }//end if numCardsHigherThanBidCard=0


                //Reach this point: Finishing process for current playerFaceChallenge
                //Process to find next playerFaceChallenge within this round
                //Pick a nextNearestPlayer, and request any card will do
                System.out.printf("ROUND:%d, END CHALLENGE:%d\n", totalNumRoundsTaken, challengeSq);


                flagEndOneRound = gameLogic.isEndOneRound(flagTrumpCardThrown, flagWinnerFound, flagOnlyOneHavingMineral);


                flagEndGame = gameLogic.isEndGame();

            }//end END ONE ROUND

            flagEndOneRound = gameLogic.isEndOneRound(flagTrumpCardThrown, flagWinnerFound, flagOnlyOneHavingMineral);
            flagEndGame = gameLogic.isEndGame();
            System.out.printf("END---ROUND:%d at CHALLENGE:%d\n", totalNumRoundsTaken, challengeSq);

            //Brought Pass Players back to normal
            //2 cases: Last one in current round  or  TrumpCard has been thrown
            int numPlayerBack =0;
            try{
                numPlayerBack = gameLogic.broughtPassPlayerToNormal(flagLastOneInCurrentRound, flagTrumpCardThrown, playerThrownTrumpCard);
            }catch (NullPointerException npe) {
                numPlayerBack = gameLogic.broughtPassPlayerToNormal(flagLastOneInCurrentRound, flagTrumpCardThrown, playerFaceChallenge);
            }
            System.out.println("Number Players are BROUGHT back to NORMAL:" + numPlayerBack);

            //Recalculate flag
            flagLastOneInCurrentRound = (gameLogic.getNumNormalPlayers() == 1);
            flagEndOneRound = gameLogic.isEndOneRound(flagTrumpCardThrown, flagWinnerFound, flagOnlyOneHavingMineral);
            flagEndGame = gameLogic.isEndGame();

            //Flag EndOneRound  is recalculated
            flagEndOneRound = gameLogic.isEndOneRound(flagTrumpCardThrown, flagWinnerFound, flagOnlyOneHavingMineral);
            System.out.printf("AFTER brought PASS Players back:flagEndOneRound:%s\n", flagEndOneRound);
            System.out.printf("Number of Normal Player:%d\n", gameLogic.getNumNormalPlayers());

            //flagEndGame is recalculated
            flagEndGame = gameLogic.isEndGame();
            System.out.printf("flagEndGame:%s\n", flagEndGame);

            //If game still keeps going, calculate data for the next Round
            //Change to a new category
            if (!flagEndGame) {

                //CASE 5: When playerFaceChallenge is the only one has Mineral Card, it will use this card to start a new Round, instead asking other
                Player currentPlayer = playerFaceChallenge;
                Player nearestOfCurrentPlayer = gameLogic.getNearestNormalPlayerOf(playerFaceChallenge);
                if (currentPlayer.getLocation() == nearestOfCurrentPlayer.getLocation()){
                    flagOnlyOneHavingMineral = true;
                }

                //For playerFaceChallengeForDisplayOnly
                // because, function:requestAnyMineralCardNearestTo() can cover nextTo original player

                resultAnyCardFromPlayerNearest = gameLogic.requestAnyMineralCardNearestTo(playerFaceChallenge);
                travCategory = (Card.Category) resultAnyCardFromPlayerNearest[0];
                cardHigherThanBid = (CardMineral) resultAnyCardFromPlayerNearest[1];
                playerFaceChallenge = (Player) resultAnyCardFromPlayerNearest[2];

                //CASE 4-1: AS playerFaceChallenge issue card,
                //It's neccessary to check cardsOnhand.
                // If zero, Yes, it becomes a new winner
                if (playerFaceChallenge.getListCardsOnhand().size() == 0) {
                    playerFaceChallenge.setStatus(Player.Status.WIN);
                    gameLogic.setNumPlayersWin(gameLogic.getNumPlayersWin() + 1);
                    gameLogic.setNumNormalPlayers(gameLogic.getNumNormalPlayers() - 1);
                    //signal a new winner has been found
                    flagWinnerFound = true;
                    System.out.printf("CONGRATULATION!!!:Player %d,you become a new WINNER\n\n",
                            playerFaceChallenge.getLocation());
                }

                //Check flags
                //System.out.println("Check flags");
                flagLastOneInCurrentRound = (gameLogic.getNumNormalPlayers() == 1);
                flagEndOneRound = gameLogic.isEndOneRound(flagTrumpCardThrown, flagWinnerFound, flagOnlyOneHavingMineral);
                flagEndGame = gameLogic.isEndGame();

            } else {
                System.out.printf("\nAfter Pass Players brought back,Nearest Player %d issued a Mineral\n" +
                        "then check again whether it has empty card.In this case,Yes,it becomes new Winner\n" +
                        "and after that, there is only one Player remaining. The game is over. Goodbye. See you next time.\n\n");
            }

        }//End flagEnd Game

        //construct values
        lastPlayer = playerFaceChallenge;
        result[0] = totalNumRoundsTaken;
        result[1] = lastPlayer;
        System.out.println("======================GAME OVER========================");
        System.out.printf("Here are the results:\nGame ends after %d rounds.\nLast player is:%d.\n",
                totalNumRoundsTaken,lastPlayer.getLocation());
        System.out.println("=============BYE BYE.SEE YOU NEXT TIME==================");

    }
}
