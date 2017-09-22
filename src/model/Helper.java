package model;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Helper {
    //public static final String FILE_TO_PARSE = "card_test.txt";
    private String fileToParse;
    private int totalNumCards; //count number of cards in pack of cards

    private Object[] extractValuesForACard(String line) {
        Object[] values = new Object[6];
        String displayOutput = "";

        //Split each line into smaller components which are separated by comma
        String[] splitResult = line.split(",");

        //With each component, compare it with value of Category constants
        //With Hardness, Gravity, take direct value
        String name = splitResult[0];
        displayOutput +=name+",";
        values[0] = name;

        String hardnessVal = splitResult[1];
        displayOutput +=hardnessVal+",";
        values[1] = hardnessVal;

        String gravityVal = splitResult[2];
        displayOutput +=gravityVal +",";
        values[2] = gravityVal;

        //With Cleavage: compare value of each constant variable in Cleavage Category with input string
        //If match value, find the correct constant variable
        //If not, check input string
        String cvgValueInput = splitResult[3];
        //System.out.println(cvgValueInput);
        for(Card.CleavageRanks cleavageVar: Card.CleavageRanks.values()) {
            String cvgValue = cleavageVar.getValue();
            if (cvgValue.equals(cvgValueInput)) {
                Card.CleavageRanks cleavageConst = cleavageVar;
                //System.out.println(cleavageVar);
                displayOutput +=cleavageConst+",";
                values[3] = cleavageConst;
                break;
            }
        }

        //With Crustal: compare value of each constant variable in Crustal Category with input string
        //If match value, find the correct constant variable
        //If not, check input
        String crusValueInput = splitResult[4];
        //System.out.println(crusValueInput);
        for(Card.CrustalRanks crustalVar: Card.CrustalRanks.values()) {
            String cvgValue = crustalVar.getValue();
            if (cvgValue.equals(crusValueInput)) {
                Card.CrustalRanks crustalConst = crustalVar;
                //System.out.println(crustalConst);
                displayOutput +=crustalConst+",";
                values[4] = crustalConst;
                break;
            }
        }

        //With EcoValue: compare value of each constant variable in EcoValue Category with input string
        //If match value, find the correct constant variable
        //If not, check input
        String ecoValueInput = splitResult[5];
        //System.out.println(ecoValueInput);
        for(Card.EcoRanks ecoVar: Card.EcoRanks.values()) {
            String ecoValue = ecoVar.getValue();
            if (ecoValue.equals(ecoValueInput)) {
                Card.EcoRanks ecoConst = ecoVar;
                //System.out.println(ecoConst);
                displayOutput +=ecoConst;
                values[5] = ecoConst;
                break;
            }
        }
        //System.out.println(displayOutput);
        return values;
    }//end extractValuesForACard(line)

    public ArrayList<Card> createPackOfCards(String fileToParse, CardTrump[] cardTrumpsArray){
        this.fileToParse = fileToParse;
        ArrayList<Card> pack = new ArrayList<Card>();
        InputStream in = null;
        try {
            in = new FileInputStream(new File(fileToParse));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            //StringBuilder out = new StringBuilder();
            //Take out the heading of the data
            reader.readLine();
            String line;
            int countLine =0;//count number of line in text file given
            totalNumCards =0;//count number of cards in pack of cards

            //Read each line and extract values of each CardMineral
            //System.out.println("createPackOfCards():Values of each cards with conformed format done by Helper:");
            while ((line = reader.readLine()) != null) {
                Object[] valuesForACard = extractValuesForACard(line);
                countLine++;
                //System.out.println("Line "+countLine+":"+Arrays.toString(valuesForACard));
                CardMineral newCardMineral =  new CardMineral(valuesForACard);
                //System.out.println("I'm here!");
                //System.out.println(newCardMineral);
                pack.add(newCardMineral);
                //out.append(line);
            }
            reader.close();

            totalNumCards +=countLine ;
            //Add cardTrumps into the packOfCards
            for(CardTrump cardTrump: cardTrumpsArray){
                pack.add(cardTrump);
                totalNumCards++;
            }

        } catch (IOException e) {
            System.out.println("Please check source file, it might not exist");
            e.printStackTrace();
        }
        return pack;
    }


    public ArrayList<Card> createPackOfCardsUI(String fileToParse, CardTrump[] cardTrumpsArray){
        this.fileToParse = fileToParse;
        ArrayList<Card> pack = new ArrayList<Card>();
        InputStream in = null;
        try {
            in = new FileInputStream(new File(fileToParse));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            //StringBuilder out = new StringBuilder();
            //Take out the heading of the data
            reader.readLine();
            String line;
            int countLine =0;//count number of line in text file given
            totalNumCards =0;//count number of cards in pack of cards

            //Read each line and extract values of each CardMineral
            //System.out.println("createPackOfCards():Values of each cards with conformed format done by Helper:");
            while ((line = reader.readLine()) != null) {
                Object[] valuesForACard = extractValuesForACard(line);
                countLine++;
                String sourcePathImg = "src/images/Slide";
                //find img
                if (countLine <=9){
                    sourcePathImg = sourcePathImg+"0"+countLine+".jpg";
                }else {
                    sourcePathImg = sourcePathImg +countLine+".jpg";
                }
                ImageIcon img= createImageIcon(sourcePathImg);

                //System.out.println("Line "+countLine+":"+Arrays.toString(valuesForACard));
                CardMineralImage newCardMineralImg =  new CardMineralImage(valuesForACard,img);
                //System.out.println("I'm here!");
                //System.out.println(newCardMineral);
                pack.add(newCardMineralImg);
                //out.append(line);
            }
            reader.close();

            totalNumCards +=countLine ;
            //Add cardTrumps into the packOfCards
            for(CardTrump cardTrump: cardTrumpsArray){
                pack.add(cardTrump);
                totalNumCards++;
            }

        } catch (IOException e) {
            System.out.println("Please check source file, it might not exist");
            e.printStackTrace();
        }
        return pack;
    }


    //Display list of card with ArrayList<Card> as input
    public String getListCardInString(ArrayList<Card> cardsCollection){
        String result = "";
        for(int i = 0; i<cardsCollection.size(); i++){
            result += i+ ":"+cardsCollection.get(i)+"\n";
        }
        //System.out.println(result);
        return result;
    }

    public int getTotalNumberOfCardsInPack() {
        return totalNumCards;
    }

    public int validateUserInputInListNumericValues(List<Integer> listValidIntegers) {
        //Validate user input: must integer, and index for Mineral card
        //otherwise, ask again pick a number
        //Note: InputMissMatch, ClassCastException
        int userInput=0;
        Scanner scanner = new Scanner(System.in);
        boolean isInputInvalidNumber = true;
        do {
            try {
                userInput = scanner.nextInt();
                for(Integer i: listValidIntegers ){
                    if (userInput == i){
                        isInputInvalidNumber= false;// user choose the valid card
                    }
                }
                //if reaching to this point still not found the valid index of Mineral card
                //Inform user know what they are wrong
                if (isInputInvalidNumber){
                    System.out.println("You chose invalid index number");
                    System.out.print("Chose a NUMBER\n>>>");
                    scanner = new Scanner(System.in);
                }
            }catch (InputMismatchException e){//catch letter characters
                //e.printStackTrace(); take it out
                System.out.println("You chose characters, number required");
                System.out.print("Chose a NUMBER\n>>>");
                scanner = new Scanner(System.in);
            }
        }while(isInputInvalidNumber);

        //scanner.nextLine();//flush keyboard buffer
        //scanner.close(); Don't close it, as it close System.in too
        return userInput;
    }

    public int getOptionFromMenu(String menu, ArrayList<Integer> intOps) {
        System.out.printf("%s >>>",menu);
        int numOptionPicked = validateUserInputInListNumericValues(intOps);
        System.out.println("You picked option:" + numOptionPicked);
        return numOptionPicked;
    }

    public int validateUserInputInListNumericValuesUI(JTextField tfNumPlayers, int lowerBound, int upperBoundIncl) {
        ArrayList<Integer> listIndicesNumPlayers = new ArrayList<Integer>();
        for (int i = lowerBound; i < (upperBoundIncl + 1); i++) {
            listIndicesNumPlayers.add(i);
        }

        int userInput=0;
        boolean isInputInvalidNumber = true;
        try {
            userInput = Integer.parseInt(tfNumPlayers.getText());
            for(Integer i: listIndicesNumPlayers ){
                if (userInput == i){
                    isInputInvalidNumber= false;
                }
            }
            //if reaching to this point still not found the valid index of Mineral card
            //Inform user know what they are wrong
            if (isInputInvalidNumber){
                String message1 =
                        "You chose invalid number. Value must be within ("+lowerBound+"-"+upperBoundIncl+") inclusive";
                JOptionPane.showMessageDialog(null,message1,"Warning",JOptionPane.ERROR_MESSAGE);
                System.out.println(message1);
                userInput =0;//if invalid return userInput =0 by default
            }else {
                String message0 = "Helper: The game will start with " + userInput +" players now.";
                JOptionPane.showMessageDialog(null,message0,"Notification",JOptionPane.INFORMATION_MESSAGE);
                System.out.println(message0);
            }
        }catch (InputMismatchException e) {//catch letter characters in console
            //e.printStackTrace(); take it out
            String message2 = "InputMismatchException:You chose characters, number required";
            JOptionPane.showMessageDialog(null, message2, "Warning", JOptionPane.ERROR_MESSAGE);
            System.out.println(message2);
        }catch (NumberFormatException nfe) {//catch letter character in UI
            String message3 = "NumberFormatException: You chose characters, number required";
            JOptionPane.showMessageDialog(null, message3, "Warning", JOptionPane.ERROR_MESSAGE);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Tell me more about exception");
            System.out.println(e);
        }
        return userInput;
    }

    public static ImageIcon createImageIcon(String sourcePath){
        //sourcePath: "images/Slide65.jpg"
        return new ImageIcon(new ImageIcon(sourcePath).getImage().getScaledInstance(90,110, Image.SCALE_DEFAULT));
        //return new ImageIcon(sourcePath);
    }
}

