package view;
import javax.swing.*;

import controller.Controller;
import model.Helper;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

//Accomodate GameDisplay
public class MainFrame extends JFrame {
    Controller controller;
    final int WIDTH = 1200;
    final int HEIGHT = 700;
    JPanel mainPanel;
    JPanel menuPanel;
    //GameDisplay gameDisplayPanel;
    JTextField tfNumPlayer;
    JButton btGo;
    JButton btPickDealer;
    JButton btHelp;
    JButton btRestart;
    String msg = "<html>RULES FOR MINERAL SUPER TRUMP CARD<br><br>" +
            "Players takes turn to play in the circular. <br>" +
            "In its turn, it could pass or pick any card higher value than the bid card given to it<hr>" +
            "1. Note 1: Game starts with pick a Player as Dealer randomly. Let the Dealer to deal the pack of card<br>" +
            "2. Note 2: Choose a player (Player A) next to the Dealer,<br>" +
            "and request this Player (Player A) to pick any Mineral card, and its specific category (one of 6 categories)<br>" +
            "3. Note 3: Choose next player of Player A, let call Player B,<br>" +
            "ask the Player B whether it wants pass or continue its turn with a higher value card<br>" +
            "4. Note 4: If Player B, pass its turn, then it has to pick up one card from the pack of card,<br>" +
            "and not allowed to take another turn until it brought back.<br>" +
            "The pass players will come back to game when one of these conditions are met:<br>" +
            "- A Trump Card is thrown<br>" +
            "- The last player exists, others are passed<br>" +
            "- winner has been found. A Player becomes a winner by 2 either two conditions:<br>" +
            "    One, it has removed all cards it has.<br>" +
            "    Two, A special case, the player throw both a Trump Card of Specific Gravity and a Mineral Card of Magnetite<br>" +
            "5. Note 5: If Player choose a higher value with a Mineral Card.<br>" +
            "Choose a player next to that player, and request a higher value card<br>" +
            "6. Note 6: If Player choose a higher value with a Trump Card.<br>" +
            "If it still has Mineral cards, it is allowed to remove one Mineral card.<br>" +
            "After that, a new round start, by next player will pick a Mineral card<br>" +
            "7.Note 7: If Player choose a higher value with a Trump Card of Specific Gravity and the Magnetite is removed subsequently<br>" +
            "the Player will become a winner at once no matter how many cards remaining it has<br>" +
            "8. Note 8: The game keeps going and it only finishes when only one player remains, other players already won.</html>";    
    
    private int numPlayers = Controller.getNumPlayerDefault();

    public MainFrame(){
        super("Welcome to Mineral Super Trumps world!");
        setSize(WIDTH, HEIGHT);
        mainPanel = new JPanel(new BorderLayout(5,10));//menuPanel, and gameDisplayPanel; BorderLayout(hrgap, vrgap)
        setContentPane(mainPanel);

        //menuPanel
        menuPanel = new JPanel(new GridLayout(2,4,10,5));
        JLabel lbWelcome = new JLabel("Welcome to Mineral ST Game!");
        btHelp = new JButton("Help");
        //Assign initial value of number of player
        JLabel lbNumPlayer = new JLabel("Select number of players (3-5):");
        tfNumPlayer = new JTextField(String.valueOf(numPlayers), 20);
        btGo = new JButton("Go");
        btPickDealer = new JButton("Pick Dealer");
        btRestart = new JButton("Restart");
        menuPanel.add(lbWelcome);
        menuPanel.add(btRestart);
        menuPanel.add(new JLabel());
        menuPanel.add(btHelp);
        menuPanel.add(lbNumPlayer);
        menuPanel.add(tfNumPlayer);
        menuPanel.add(btGo);
        menuPanel.add(btPickDealer);

        //Add menuPanel to mainPanel
        mainPanel.add(menuPanel,BorderLayout.NORTH);
    }

    //btGo, btRestart, btPickDealer, btHelp
    public void addListener(Controller c){
        this.controller = c;
        btGo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get value from tfNumPlayer
                //pass it to controller
                //then controller will receive, pass it to gameLogic
                Helper helper = new Helper();
                numPlayers = helper.validateUserInputInListNumericValuesUI(tfNumPlayer,
                        Controller.getLowerBoundNumPlayer(), Controller.getUpperBoundNumPlayer());
                System.out.println("MainFrame/actionPerform: numPlayers = "+ numPlayers);
                if (numPlayers >0){//pass it to controller
                    controller.btGoClick(numPlayers);
                    btGo.setEnabled(false);
                }
            }
        });

        btRestart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.btRestartClick();
            }
        });

        btPickDealer.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                btPickDealer.setEnabled(false);
                controller.btPickDealerClick();
            }
        });

        //Add ActionListener
        btHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                JOptionPane.showMessageDialog(null,msg,"Help", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }//end addListener

}//end class
