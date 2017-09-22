package controller;
import java.io.*;
import java.util.ArrayList;

//NOTE: For testing purpose: After each testing (i.e,TEST 0), comment it out (TEST 0)
//then move to the next (TEST 1). There are 6 TESTS:

//TEST 0: Abstract Card class
//TEST 1: Test CardMineral class
//TEST 2. Test CardTrump class
//TEST 3. Test Player class
//TEST 4.START: For testing the whole program in one go. Test 4 includes: TEST for 3 other classes: App, GameLogic, Helper class
//TEST 5: Testing Reading file

public class TestClass {
    //public static void main(String[] args){
        /*
        //TEST 0: Abstract Card class
        System.out.println("\nTesting Abstract Card class");
        System.out.println("\nTest: Constructor");
        //Card cardTest = new Card();//It can't instantiate as abstract class

        System.out.println("\nTest: setTrump()");
        CardTrump cardTrump1 = new CardTrump("The Universe", Card.Category.CLEAVAGE);
        System.out.println("Instantiate a Trump: cardTrump1's properties:\n"+cardTrump1);
        System.out.println("Before setTrump(): isTrump return "+cardTrump1.getTrump());
        cardTrump1.setTrump(false);
        System.out.println("After setTrump(false): isTrump return "+cardTrump1.getTrump());

        System.out.println("\nTest: getName()");
        System.out.println("Name of cardTrump1: "+cardTrump1.getName());


        System.out.println("\nTest: getMineralCardCategoriesList()");
        Card.Category[] listCategoriesForMinerals = Card.getMineralCardCategoriesList();
        System.out.printf("Here are %d categories can be used for Minerals.\n",listCategoriesForMinerals.length);
        for (int i=0; i<listCategoriesForMinerals.length;i++){
            System.out.printf("%d:%s\n",i,listCategoriesForMinerals[i]);
        }
        System.out.println("Note: ANYCATEGORY can't be used");


        System.out.println("\nTest: getMineralCardCategoriesString()");
        System.out.println("All Categories in used for Minerals\n"+Card.getMineralCardCategoriesString());


        System.out.println("\nTest: getCategoryFromString()");
        String categoryConstantstr = "Economic Value";
        Card.Category categoryConstant = Card.getCategoryFromString(categoryConstantstr);
        System.out.println("Constant string is: "+categoryConstantstr);
        System.out.println("Category type: "+categoryConstant);
        */

        /*
        //TEST 1: Test CardMineral Class
        //1. Test CardMineral class
        System.out.println("Test CardMineral class");


        System.out.println("\nTest: Constructor 1");
        CardMineral firstMineralCard = new CardMineral("Calcite-Test");
        System.out.println("firstMineralCard: "+firstMineralCard);

        System.out.println("\nTest: Constructor 2");
        Object[] valuesForACard_mc2 = new Object[6];
        valuesForACard_mc2[0] = "Glaucophane";
        valuesForACard_mc2[1] = 6;//Card.Category.HARDNESS
        valuesForACard_mc2[2] =3.2;//Card.Category.GRAVITY
        valuesForACard_mc2[3] = Card.CleavageRanks.TWOGOOD;//Card.Category.CLEAVAGE
        valuesForACard_mc2[4] = Card.CrustalRanks.LOW;//Card.Category.CRUSTAL
        valuesForACard_mc2[5] = Card.EcoRanks.TRIVIAL;//Card.Category.ECOVALUE
        CardMineral secondMineralCard = new CardMineral(valuesForACard_mc2);
        System.out.println("secondMineralCard: "+secondMineralCard);

        Object[] valuesForACard_mc3 = new Object[6];
        valuesForACard_mc3[0] = "Quartz";
        valuesForACard_mc3[1] = 7;//Card.Category.HARDNESS
        valuesForACard_mc3[2] = 2.65;//Card.Category.GRAVITY
        valuesForACard_mc3[3] = Card.CleavageRanks.POORNONE;//Card.Category.CLEAVAGE
        valuesForACard_mc3[4] = Card.CrustalRanks.HIGH;//Card.Category.CRUSTAL
        valuesForACard_mc3[5] = Card.EcoRanks.MODERATE;//Card.Category.ECOVALUE
        CardMineral thirdMineralCard = new CardMineral(valuesForACard_mc3);
        System.out.println("thirdMineralCard: "+thirdMineralCard);

        System.out.println("\nTest: isTrump()");
        System.out.println("secondMineralCard.isTrump()="+secondMineralCard.isTrump());

        System.out.println("\nTest: addCategory()");

        CardMineral fourthMineralCard = new CardMineral("Logan Sapphire");
        System.out.println("Before adding categories(Key,Value),fourthMineralCard has: "+fourthMineralCard.toString());

        Card.Category cateHardnessKey = Card.Category.HARDNESS;
        double cateHardnessValue = 9.0;

        Card.Category cateGravityKey = Card.Category.GRAVITY;
        double cateGravityValue = 4.03;

        Card.Category cateCleavageKey = Card.Category.CLEAVAGE;
        Card.CleavageRanks cateCleavageValue = Card.CleavageRanks.ONEPERFECTTWOGOOD;

        Card.Category cateCustalKey = Card.Category.CRUSTAL;
        Card.CrustalRanks cateCrustalValue = Card.CrustalRanks.HIGH;

        Card.Category cateEcoKey = Card.Category.ECOVALUE;
        Card.EcoRanks cateEcoValue = Card.EcoRanks.IMRICH;

        //add category
        fourthMineralCard.addCategory(cateHardnessKey,cateHardnessValue);
        fourthMineralCard.addCategory(cateGravityKey,cateGravityValue);
        fourthMineralCard.addCategory(cateCleavageKey,cateCleavageValue);
        fourthMineralCard.addCategory(cateCustalKey,cateCrustalValue);
        fourthMineralCard.addCategory(cateEcoKey,cateEcoValue);
        System.out.printf("After adding categories(Key,Value)\nfourthMineralCard has:%s\n",
                fourthMineralCard.toString());


        System.out.println("\nTest: getCategoryValue()");
        System.out.printf("fourthMineralCard\n(%s)\nhas:\n", fourthMineralCard);
        Card.Category cateLookingForValue;
        String constantVar;
        for (int i=0; i<Card.getMineralCardCategoriesList().length; i++){
            cateLookingForValue = Card.getMineralCardCategoriesList()[i];
            constantVar = fourthMineralCard.getCategoryValue(cateLookingForValue);
            System.out.printf("%s of:%s\n",cateLookingForValue,constantVar);
        }

        System.out.println("\nTest: compare():");
        System.out.println("Compare fourthMineralCard and thirdMineralCard:");
        System.out.println("fourthMineralCard:\n"+fourthMineralCard);
        System.out.println("thirdMineralCard:\n"+thirdMineralCard);
        for (int i=0; i<Card.getMineralCardCategoriesList().length; i++){
            cateLookingForValue = Card.getMineralCardCategoriesList()[i];
            double result = fourthMineralCard.compare(thirdMineralCard,cateLookingForValue);
            System.out.println("in "+ cateLookingForValue +": "+result+" (positive means higher value)");
        }

        System.out.println("\nTest: speak():");
        for (int i=0; i<Card.getMineralCardCategoriesList().length; i++) {
            cateLookingForValue = Card.getMineralCardCategoriesList()[i];
            System.out.printf("fourthMineralCard speak:%s\n",fourthMineralCard.speak(cateLookingForValue));
        }

        */

        /*
        //TEST 2. Test CardTrump class
        System.out.println("Test CardTrump class");

        System.out.println("\nTest: Constructor");
        CardTrump ct_55 = new CardTrump("The Miner", Card.Category.ECOVALUE);
        CardTrump ct_56 = new CardTrump("The Petrologist", Card.Category.CRUSTAL);
        CardTrump ct_57 = new CardTrump("The Gemologist", Card.Category.HARDNESS);
        CardTrump ct_58 = new CardTrump("The Mineralogist", Card.Category.CLEAVAGE);
        CardTrump ct_59 = new CardTrump("The Geophysicist", Card.Category.GRAVITY);
        CardTrump ct_60 = new CardTrump("The Geologist", Card.Category.ANYCATEGORY);

        System.out.println("\nTest: isTrump()");
        CardTrump ct = ct_60;
        System.out.println("isTrump="+ ct.isTrump());

        System.out.println("\nTest: getCategoryConst()");
        System.out.println("ct_55 belongs to: "+ ct_55.getCategoryConst());
        System.out.println(ct);
        */


        /*
        //TEST 3. Test Player class
        System.out.println("Test Player Class");
        Player playerTest = new Player(1);
        System.out.print("Player's Properties: "+playerTest);

        //Create cards Mineral and Trump
        CardMineral cm1 = new CardMineral("Glaucophane");
        cm1.addCategory(Card.Category.HARDNESS,6);
        cm1.addCategory(Card.Category.GRAVITY,3.2);
        cm1.addCategory(Card.Category.CLEAVAGE, Card.CleavageRanks.TWOGOOD);
        cm1.addCategory(Card.Category.CRUSTAL, Card.CrustalRanks.LOW);
        cm1.addCategory(Card.Category.ECOVALUE, Card.EcoRanks.TRIVIAL);
        System.out.println(cm1);

        CardMineral cm2 = new CardMineral("Quartz");
        cm2.addCategory(Card.Category.HARDNESS,7);
        cm2.addCategory(Card.Category.GRAVITY,2.65);
        cm2.addCategory(Card.Category.CLEAVAGE, Card.CleavageRanks.POORNONE);
        cm2.addCategory(Card.Category.CRUSTAL, Card.CrustalRanks.HIGH);
        cm2.addCategory(Card.Category.ECOVALUE, Card.EcoRanks.MODERATE);
        System.out.println(cm2);

        CardTrump ct_55 = new CardTrump("The Miner", Card.Category.ECOVALUE);
        CardTrump ct_56 = new CardTrump("The Petrologist", Card.Category.CRUSTAL);
        CardTrump ct_57 = new CardTrump("The Gemologist", Card.Category.HARDNESS);

        System.out.println("\nTest:addCard()");
        playerTest.addCard(cm1);
        playerTest.addCard(cm2);
        playerTest.addCard(ct_55);
        playerTest.addCard(ct_56);
        playerTest.addCard(ct_57);

        System.out.println("\nTest:removeCard()");
        playerTest.removeCard(ct_55);

        //Create packOfCard
        final String FILE_TO_PARSE = "card.txt";
        CardTrump[] cardTrumpsArray = new CardTrump[5];
        cardTrumpsArray[0]= new CardTrump("The Mineralogist", Card.Category.CLEAVAGE);
        cardTrumpsArray[1] = new CardTrump("The Geophysicist", Card.Category.GRAVITY);
        cardTrumpsArray[2] = new CardTrump("The Geologist", Card.Category.ANYCATEGORY);
        cardTrumpsArray[3] = new CardTrump("The Petrologist", Card.Category.CRUSTAL);
        cardTrumpsArray[4] = new CardTrump("The Gemologist", Card.Category.HARDNESS);

        Helper theHelper = new Helper();
        ArrayList<Card> packOfCards= theHelper.createPackOfCards(FILE_TO_PARSE,cardTrumpsArray);
        int numPlayers = 5;
        int numCardsGivenEach = 2;

        System.out.println("\nTest:dealCards()");
        Card[][] servings = playerTest.dealCards(packOfCards,numPlayers,numCardsGivenEach);

        System.out.println("\nTest:dealCards() done by playerTest");
        playerTest.displayDealCardsResult(servings);

        System.out.println("\nTest:removeCardOnTop() done by cm2");
        Card cardRemoved = playerTest.removeCardOnTop(packOfCards);
        System.out.println("Card on the top: "+cardRemoved);

        System.out.println("\nTest:receiveCardsDealt() done by cm2");
        Player playerTest2 = new Player(2);
        Card[] listCardsGiven = servings[0];
        playerTest2.receiveCardsDealt(listCardsGiven);
        System.out.printf("\nPlayer %d has Cards onhand after receiving serving dealt:\n%s",
                playerTest2.getLocation(),playerTest2.getStringCardsOnhand());

        System.out.println("\nTest:setStatus() for playerTest3");
        Player playerTest3 = new Player(3);
        System.out.printf("Player %d's status before setting:%s",
                playerTest3.getLocation(),playerTest3.getStatus());
        playerTest3.setStatus(Player.Status.WIN);
        System.out.printf("Player %d's status after setting:%s",
                playerTest3.getLocation(),playerTest3.getStatus());

        System.out.println("\nTest:getCardsHigherValueThan()");
        Card.Category categoryTest = Card.Category.ECOVALUE;
        ArrayList<Card> cardsHigherValueThan = playerTest2.getCardsHigherValueThan(cm2,categoryTest);
        System.out.printf("Display cardsHighValue than:(%s) in category (%s)\n:%s",
                cm2,categoryTest, playerTest2.getStringCardsHigherValueThan(cm2,categoryTest));

        System.out.println("\nTest:getListCardsOnhand()");
        System.out.printf("List Mineral cards:%s\n",playerTest2.getStringCardsOnhand(false));
        System.out.printf("List Trump cards:%s\n",playerTest2.getStringCardsOnhand(true));

        System.out.print(playerTest2);
        */

        /**
         * TEST 4.START: For testing the whole program in one go
         * Including: TEST for App, GameLogic, Helper class
         */

        /*
        final String FILE_TO_PARSE = "card_test.txt";
        final int NUM_CARD_TRUMPS = 2; //Actual values given: 6

        int NUM_CARDS_FOR_EACH_START = 2;
        int numPlayers = 3;//Actual values given: 3-5

        CardTrump[] cardTrumpsArray = new CardTrump[NUM_CARD_TRUMPS];
        CardTrump ct_55 = new CardTrump("The Miner", Card.Category.ECOVALUE);
        cardTrumpsArray[0] = ct_55;
        CardTrump ct_60 = new CardTrump("The Geologist", Card.Category.ANYCATEGORY);
        cardTrumpsArray[1] = ct_60;//CHANGE AFTER TESTING

        //1. Setup a game
        GameLogic gameLogic = new GameLogic(numPlayers, NUM_CARDS_FOR_EACH_START,FILE_TO_PARSE,cardTrumpsArray);
        gameLogic.setupGame();

        //2. Start a game
        Player dealer = gameLogic.startGame();

        Object[] gameLogicResults = gameLogic.manageGame(dealer);
        gameLogic.endGame(gameLogicResults);

        */
        /**
         * TEST 4.END: For testing the whole program in one go
         */

        /*
        //TEST 5: Testing Reading file
        InputStream in = null;
        try {
            in = new FileInputStream(new File(FILE_TO_PARSE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                out.append(line);
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(out.toString());   //Prints the string content read from input stream
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

         /* It's sticky, sometimes it doesn't work
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(FILE_TO_PARSE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //ArrayList<Card> listAllCards = new ArrayList();
        scanner.next();
        scanner.next();
        while (scanner.hasNext()) {
            String row = scanner.next();
            System.out.println(row);
        }
        */

    }

//}

