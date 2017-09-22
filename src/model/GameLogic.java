package model;
import java.util.ArrayList;
import java.util.Random;

public class GameLogic {

    protected int totalnumPlayers;
    private int numPlayersWin;
    private int numNormalPlayers;
    protected Player[] listPlayers;
    protected CardTrump[] cardTrumpsArray;

    protected int numCardsGivenEachInital;
    protected ArrayList<Card> packOfCards = new ArrayList<Card>();

    public GameLogic(){
    }

    public GameLogic(int numOfPlayersStart, int numberOfCardsForEachStart,
                     CardTrump[] arTrumps, ArrayList<Card> packOfCards) {
        this.totalnumPlayers = numOfPlayersStart;//keep it separately
        this.setNumNormalPlayers(numOfPlayersStart);
        this.setNumPlayersWin(0);

        this.numCardsGivenEachInital = numberOfCardsForEachStart;
        this.packOfCards = packOfCards;
        this.cardTrumpsArray = arTrumps;

        //1. Create players and assign location to it
        this.listPlayers = new Player[totalnumPlayers];
        listPlayers = createPlayers(totalnumPlayers);
    }


    //Start game to find out dealer, bidPlayer, bidCard, presentCategory
    public Player startGame() {
        System.out.println("\nSTART OF startGame():");
        Helper helper = new Helper();

        //1. Pick a random player and assign it to currentPlayer
        System.out.println("Pick a random player as Dealer");
        Random rand = new Random();
        int randomNum = rand.nextInt(totalnumPlayers);//nextInt is normally exclusive of the top value,
        //System.out.println("Random number:" + randomNum);
        Player dealer = listPlayers[randomNum];
        System.out.println("Dealer:Player "+dealer.getLocation());

        //2.Let dealer deal packOfCards
        System.out.printf("Let Player %d deal for(" +
                        "numPlayers=%d,cardsGivenEach=%d)\n",
                        dealer.getLocation(), totalnumPlayers,
                        numCardsGivenEachInital);
        Card[][] cardServingsInitial =
                dealer.dealCards(packOfCards, totalnumPlayers, numCardsGivenEachInital);
        //dealer.displayDealCardsResult(cardServingsInitial);

        //3. Receive 3 portions of cards and cards left from the packofCards
        //System.out.println("Remaining cards in pack:\n"+helper.getListCardInString(packOfCards));
        System.out.println("Cards remaining in the Pack:"+packOfCards.size());

        //4. Give each portion of cards to each player
        System.out.println("Give servings to players");
        giveCardsToPlayersAtStart(listPlayers, cardServingsInitial);
        //System.out.println("Player's properties after receiving initials cards");
        //displayPlayersProperties(listPlayers);
        System.out.println("END OF startGame():");
        return dealer;
    }//end startGame()

    //End of Game when: all players but one are already winners
    public boolean isEndGame() {
        boolean endGame = false;
        endGame = (getNumPlayersWin() == (totalnumPlayers - 1));
        return endGame;
    }

    //End round when one of those following conditions are met
    //1. Last Normal player in the round,
    //2. Trump Card is thrown
    //3. Winner of round is defined (when it has no cards remaining,
    // or it throw Trump Card of Specific Gravity and a Mineral Card of Magnetite
    //4. There is a only one player having a mineral, all others having trumps
    public boolean isEndOneRound(boolean flagTrumpCardThrown, boolean flagWinnerFound, boolean flagOnlyOneHavingMineral) {
        boolean endARound = false;
        boolean endARound1 = (getNumNormalPlayers() == 1);
        boolean endARound2 = flagTrumpCardThrown;
        boolean endARound3 = flagWinnerFound;
        boolean endARound4= flagOnlyOneHavingMineral;

        if (endARound1||endARound2||endARound3||endARound4){
            endARound = true;
        }
        return endARound;
    }

    //Find the next player in list of players in circular manner
    private Player nextPlayer(Player currentPlayer, Player[] listPlayers) {
        Player nextPlayer  = null;
        //Get the location of current player
        int travPointer = currentPlayer.getLocation();

        //If current player is the last one in the list
        //The next player will be the the first one in the list
        int lastIndex = listPlayers.length-1;
        if(travPointer==lastIndex){
            nextPlayer = listPlayers[0];
        }else {
            //otherwise, the next player will be the one after the current
            nextPlayer = listPlayers[travPointer+1];
        }
        return  nextPlayer;
    }

    public Player getNearestNormalPlayerOf(Player bidP) {
        Player curP = nextPlayer(bidP, listPlayers);
        Player.Status curPStat = curP.getStatus();
        while (!curPStat.equals(Player.Status.NORMAL)){
            curP = nextPlayer(curP, listPlayers);
            //I forgot to recalculate value curPStat-> run forever
            curPStat = curP.getStatus();
        }
        return curP;
    }

    protected Card.Category requestPlayerSelectCategory(Player requestedPlayer) {
        Card.Category categorySelected = null;
        //Get list of valid Categories for Mineral Cards
        Card.Category[] validCategoriesList = Card.getMineralCardCategoriesList();

        //Extract index of those Categories
        ArrayList<Integer> listIndices = new ArrayList<>();
        for (int i=0; i< validCategoriesList.length; i++){
            listIndices.add(i);
        }

        Helper helperObj = new Helper();
        //Ask user to input
        System.out.printf("Player %d,Let enter a number to select ONE CATEGORY:\n%s>>>",
                requestedPlayer.getLocation(),Card.getMineralCardCategoriesString());

        //Validate user input within list of indices
        int userInput = helperObj.validateUserInputInListNumericValues(listIndices);

        //Get valid Category for Mineral Card user chose
        categorySelected = validCategoriesList[userInput];
        while (categorySelected.equals(Card.Category.ANYCATEGORY)) {
            //7.Ask currentPlayer to pick a category
            System.out.printf("Player %d,as ANYCATEGORY chose,let be more SPECIFIC:\n",
                    requestedPlayer.getLocation());

            System.out.printf("Player %d,Let enter a number to select ONE CATEGORY:\n%s>>>",
                    requestedPlayer.getLocation(),Card.getMineralCardCategoriesString());

            //Validate user input within list of indices
            userInput = helperObj.validateUserInputInListNumericValues(listIndices);

            //Get valid Category for Mineral Card user chose
            categorySelected = validCategoriesList[userInput];
        }

        //Get into here, specific category has been chosen
        System.out.printf("\nPlayer %d,you've just selected: %s\n",
                requestedPlayer.getLocation(), categorySelected.getValue());
        return categorySelected;
    }

    //Bring back PASS Players on the previous round to play again(Change status from: PASS =>NORMAL)
    public int broughtPassPlayerToNormal(boolean flagLastOneInCurrentRound,
                                          boolean flagTrumpCardThrown,
                                          Player playerFaceChallenge){
        int numPlayerBack =0;
        String message ="";
        if (flagLastOneInCurrentRound) {
            message += "As Player " + playerFaceChallenge.getLocation() + " as last one,";
        }
        if(flagTrumpCardThrown) {
            message +="As previous Player threw a Trump Card,";
        }
        message +="all PASS previous back to NORMAL.";
        System.out.println(message);

        int numPass = totalnumPlayers - getNumNormalPlayers()-getNumPlayersWin();
        if (numPass>0) {
            /*
            System.out.printf("Before bring PASS back:Winners:%d,NormalPlayers:%d,Pass:%d,Total:%d\n",
                    numPlayersWin, numNormalPlayers,numPass, totalnumPlayers);
            */

            String strPlayersBroughtBack = "Player:";
            for (Player p : listPlayers) {
                if (p.getStatus().equals(Player.Status.PASS)) {
                    p.setStatus(Player.Status.NORMAL);
                    setNumNormalPlayers(getNumNormalPlayers() + 1);
                    numPlayerBack +=1;
                    strPlayersBroughtBack += p.getLocation()+", ";
                }
            }
            System.out.println("Players has been back to game: "+strPlayersBroughtBack);

            /*
            System.out.printf("After bring PASS back:Winners:%d,NormalPlayers:%d,Pass:%d,Total:%d\n",
                    numPlayersWin, numNormalPlayers,numPass, totalnumPlayers);
            */
        }else {
            System.out.println("There's no PASS Players at the moment.");
        }
        return numPlayerBack;
    }

    //Request any Mineral Card from a player, to get Card & Category
    public Object[] requestAnyMineralCardNearestTo(Player player){
        Object[] result = new Object[3];
        boolean isTrumpRequired = false;

        //Find the nearest player, assign to travPlayer
        Player travPlayer = getNearestNormalPlayerOf(player);
        int numMineralCards = travPlayer.getListCardsOnhand(isTrumpRequired).size();

        //If the travPlayer doesn't have any Mineral, move to next one
        //Until found player has Mineral, and ask it to pick a new one
        while (numMineralCards==0) {
            //5. Find the nearest Normal Player to given player, let it become travPlayer
            System.out.printf("As player %d has no Mineral.Pointer move to=>=>",
                    travPlayer.getLocation());
            travPlayer = getNearestNormalPlayerOf(travPlayer);
            numMineralCards = player.getListCardsOnhand(isTrumpRequired).size();
            System.out.printf("Player %d\n",travPlayer.getLocation());
        }

        //When out here, travPlayer has Minerals in its pocket
        System.out.printf("\nFinally,the Nearest having Minerals:Player %d\n", travPlayer.getLocation());

        //6.Ask travPlayer to pick a category
        Card.Category firstCategoryChose = requestPlayerSelectCategory(travPlayer);

        //System.out.printf("\nPlayer %d,you've just chosen Category:%s\n",
          //      travPlayer.getLocation(), firstCategoryChose.getValue());

        //7. Ask travPlayer to take out a Mineral
        boolean isTrumpCardRequested = false;
        CardMineral firstMineralCard = (CardMineral) requestAnyCardTrumpOrMineral(travPlayer, isTrumpCardRequested);
        System.out.printf("\nPlayer %d,you've removed:\n%s\n", travPlayer.getLocation(), firstMineralCard);
        result[0] = firstCategoryChose;
        result[1] = firstMineralCard;
        result[2]= travPlayer;
        return result;
    }

    //Function: request to pick a PURE card from cardsOnhand with either Trump or Mineral Mutual Exclusive
    //Perform Validation for narrow down selections
    //For Minerals: only Card, not specify Category pick yet
    //For Trump Cards: only Card, not specify if choose ANYCATEGORY
    public Card requestAnyCardTrumpOrMineral(Player requestedPlayer, boolean isTrumpCardRequested){
        //Including: Physical Card, and Category name
        Object [] resultOfRequest = new Object[2];

        Card cardPlayerPicked = null;
        Helper helper = new Helper();

        //Find indices of valid cards in collection of Cards on hand
        ArrayList<Integer> indicesOfValidCards = new ArrayList<Integer>();
        ArrayList<Card> cardsMineralOrTrumpMutualExclusive =
                requestedPlayer.getListCardsOnhand(isTrumpCardRequested);

        //Request the requestedPlayer to pick any Mineral
        //Display Trump Cards or Mineral Cards on cardsOnhand
        if (cardsMineralOrTrumpMutualExclusive.size()>0) {
            String strValidCardIndex = "Player "+ requestedPlayer.getLocation() +",let select a Card,you have those ";
            if (isTrumpCardRequested) {//Trump
                strValidCardIndex += "TRUMP cards below:\n";
                //List of valid indices
                for (int i = 0; i < cardsMineralOrTrumpMutualExclusive.size(); i++) {
                    indicesOfValidCards.add(i);
                    strValidCardIndex += i + ":" + cardsMineralOrTrumpMutualExclusive.get(i) + "\n";
                }
                System.out.printf("%sPlayer %d,let enter number to SELECT a TRUMP card\n>>>",
                        strValidCardIndex,requestedPlayer.getLocation());

            } else if (!isTrumpCardRequested) {//Mineral is required
                strValidCardIndex += "MINERAL cards below:\n";
                //List of valid indices
                for (int i = 0; i < cardsMineralOrTrumpMutualExclusive.size(); i++) {
                    indicesOfValidCards.add(i);
                    strValidCardIndex += i + ":" + cardsMineralOrTrumpMutualExclusive.get(i) + "\n";
                }
                System.out.printf("%sPlayer %d,let enter number to SELECT a MINERAL card\n>>>",
                        strValidCardIndex,requestedPlayer.getLocation());
            }
            int userInput = helper.validateUserInputInListNumericValues(indicesOfValidCards);

            cardPlayerPicked = cardsMineralOrTrumpMutualExclusive.get(userInput);
            requestedPlayer.removeCard(cardPlayerPicked);
        }
        return cardPlayerPicked;
    }

    public Card requestPlaceHigherThanBidCard(Player requestedPlayer,
                                               Card bidCard,
                                               Card.Category bidCategory) {
        Card cardPlayerPicked = null;
        Helper helper = new Helper();

        //find indices of valid cards in collection of Cards on hand
        ArrayList<Card> cardsHigherValue =
                requestedPlayer.getCardsHigherValueThan(bidCard,bidCategory);

        //Ony having cards higher value
        //Otherwise, let return null, let the caller handler NullException (OneChallenge)
        if (cardsHigherValue.size()>0) {
            ArrayList<Integer> indicesOfValidCards = new ArrayList<Integer>();
            String strValidCardIndex = "";
            //List of valid indices
            for (int i = 0; i < cardsHigherValue.size(); i++) {
                indicesOfValidCards.add(i);
                strValidCardIndex += i + ":" + cardsHigherValue.get(i) + "\n";
            }
            System.out.printf("Player %s,you have Cards Higher value than bidCard:(%s)\n" +
                            "in category:%s\n" +
                            "%sLet pick a higher value\n>>>",requestedPlayer.getLocation(),
                            ((CardMineral) bidCard).speak(bidCategory),
                            bidCategory, strValidCardIndex);

            int userInput = helper.validateUserInputInListNumericValues(indicesOfValidCards);

            //requestedPlayer remove the card given out
            cardPlayerPicked = cardsHigherValue.get(userInput);
            requestedPlayer.removeCard(cardPlayerPicked);
            System.out.printf("\nPlayer %d,you removed Card:\n(%s)\nupon request of placing a higher value card\n",
                    requestedPlayer.getLocation(), cardPlayerPicked);
        }

        return cardPlayerPicked;
    }

    //Give out initial cards to each player, after the dealer does its job
    private void giveCardsToPlayersAtStart(Player[] listPlayers, Card[][] cardsDealtResult) {
        for(int i =0; i<listPlayers.length; i++){
            listPlayers[i].receiveCardsDealt(cardsDealtResult[i]);
        }
    }

    //Create numPlayers and assign its location from [0,numPlayers-1]
    protected Player[] createPlayers(int numPlayers){
        Player[] listPlayersCreated = new Player[numPlayers];
        for (int numP =0; numP< numPlayers; numP++){
            Player player = new Player(numP);
            listPlayersCreated[numP]=player;
        }
        return listPlayersCreated;
    }

    private void displayNumberPlayerStatus() {
        int numPass = totalnumPlayers -getNumPlayersWin()-getNumNormalPlayers();
        System.out.println("Currently," +
                "Normal:"+getNumNormalPlayers()+"\nPass:"+numPass+"\nWinner:"+getNumPlayersWin()
                +",Total:"+ totalnumPlayers);
    }

    public void displayPlayersProperties(Player[] listPlayers) {
        System.out.println("Display All Players' properties");
        for (int i=0; i<listPlayers.length; i++){
            System.out.printf("Player %d: %s \n",i,listPlayers[i]);
        }
    }


    public Player[] getListPlayers() {
        return this.listPlayers;
    }

    public ArrayList<Card> getPackOfCards() {
        return this.packOfCards;
    }

	public int getNumPlayersWin() {
		return numPlayersWin;
	}

	public void setNumPlayersWin(int numPlayersWin) {
		this.numPlayersWin = numPlayersWin;
	}

	public int getNumNormalPlayers() {
		return numNormalPlayers;
	}

	public void setNumNormalPlayers(int numNormalPlayers) {
		this.numNormalPlayers = numNormalPlayers;
	}
}//end of GameLogic



