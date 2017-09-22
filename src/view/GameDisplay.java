package view;
import javax.swing.*;
import javax.swing.border.Border;

import model.AddImage;
import model.Card;
import model.CardButton;
import model.Player;

import java.awt.*;
import java.util.ArrayList;

//Display game board
public class GameDisplay extends JPanel{
    private Border borderRegion = BorderFactory.createRaisedBevelBorder();
    private Border borderBottom = BorderFactory.createLineBorder(Color.BLACK);

    JPanel sharedPanel = new JPanel(new BorderLayout(5,10));
    JPanel sharedTop = new JPanel(new GridLayout(1,3,10,5));//remove lbPackOfCard
    JPanel sharedBottom = new JPanel(new GridLayout(1,5,10,5));

    JPanel playersPanel = new JPanel(new BorderLayout(5,10));
    ArrayList<JPanel> listPanelOnhand = new ArrayList<>();
    ArrayList<PlayerView> listPlayerView = new ArrayList<>();
    ArrayList<JLabel[]> listLabelInfo = new ArrayList<>();

    //2 panel: sharedPanel, playersPanel
    //sharedPanel: button shared between players: lbPackofCard, lbbidCard, btPass, btCategories
    //playerPanel: Setup region for each player, and create the relationship Card-Button for each player
    //but havent got card yet.
    public GameDisplay(Player[] listPlayers){
        setLayout(new BorderLayout(5,10)); //BorderLayout(hrgap, vrgap)
        setBorder(borderRegion);

        //Panel 1. sharedPanel
        // 1st line: sharedTop: lbPackOfCards, lbBidCard, btPass
        // 2nd line: sharedBottom: 5 categories
        sharedPanel.add(sharedTop, BorderLayout.NORTH);
        sharedPanel.add(sharedBottom, BorderLayout.CENTER);
        sharedPanel.setBorder(borderRegion);
        sharedTop.setBorder(borderRegion);
        sharedBottom.setBorder(borderRegion);

        //Panel 2: playersPanel
        //1st: panelInfoMaster ~ listLabelInfo = ArrayList<JLabel[]>:  display player's info: Location, status
        //2nd: panelOnhandMaster  ~ listPanelOnhand = ArrayList<JPanel>     :display cardsOnhand
        JPanel infoMasterPanel = new JPanel(new GridLayout(1,listPlayers.length,15,10));
        JPanel onhandMasterPanel = new JPanel(new GridLayout(1,listPlayers.length));

        //display initial player info in setup step
        for (Player player: listPlayers){
            //Create labels
            JLabel lbForLoc = new JLabel("Player:");
            JLabel lbLoc = new JLabel(String.valueOf(player.getLocation()));
            JLabel lbForStatus = new JLabel("Status:");
            JLabel lbStatus = new JLabel(String.valueOf(player.getStatus()));

            JPanel eachInfoPanel = new JPanel(new GridLayout(2,2));
            eachInfoPanel.add(lbForLoc);
            eachInfoPanel.add(lbLoc);
            eachInfoPanel.add(lbForStatus);
            eachInfoPanel.add(lbStatus);
            eachInfoPanel.setBorder(borderBottom);
            //add labels to panelInfoMaster
            infoMasterPanel.add(eachInfoPanel);

            //add label to listLabelInfo
            JLabel[] tempJL = new JLabel[] {lbLoc, lbStatus};
            listLabelInfo.add(tempJL);
            System.out.println(lbLoc.getText()+ ":" +lbStatus.getText());

            //create PlayerView keep track: Player, and relationship between card and button
            PlayerView playerView = new PlayerView(player);
            listPlayerView.add(playerView);
        }

        //Assign each region for cards of each player
        for (Player player: listPlayers){
            //Create panel accommodating the cards
            JPanel panelOnhandOfEachPlayer = new JPanel();
            String strNameLoc = String.valueOf(player.getLocation());
            panelOnhandOfEachPlayer.setName(strNameLoc);
            panelOnhandOfEachPlayer.setBorder(borderRegion);
            //add panelCardsOfEachPlayer to panel master
            onhandMasterPanel.add(panelOnhandOfEachPlayer);
            //add panel to list
            listPanelOnhand.add(panelOnhandOfEachPlayer);
            panelOnhandOfEachPlayer.setSize(200,200);
        }
        playersPanel.setBorder(borderRegion);
        playersPanel.add(infoMasterPanel, BorderLayout.NORTH);
        playersPanel.add(onhandMasterPanel, BorderLayout.CENTER);

        add(sharedPanel,BorderLayout.NORTH);
        add(playersPanel,BorderLayout.CENTER);
    }//end of constructors

    public ArrayList<PlayerView> getListPlayerView() {
        return listPlayerView;
    }

    //Render player View at the start
    public void renderAllPlayerViewAtStart() {
        //attach buttons of each player to player region
        for (JPanel panelE: listPanelOnhand){
            int panelNumber = Integer.parseInt(panelE.getName());
            //with each panel name, get player view, by comparing the location of player with panel Name
            for (PlayerView pv: listPlayerView){
                int loc = pv.getPlayer().getLocation();
                if (panelNumber ==loc){
                    //render button here
                    ArrayList<CardButton> listButtonOnhandE = pv.getListCardButtonOnhand();
                    for (CardButton cb : listButtonOnhandE){
                        panelE.add(cb.getButton());
                    }
                }
            }
        }
    }

    //enable selected buttons
    public void enableSelectedButtonsOfPlayer(Player expectedPlayer,
                                              ArrayList<Card> listCardAllowedToDisplay) {
        //Disable cards from other players except from playerStart
        for (PlayerView pv: listPlayerView){
            //for each PlayerView, get the location
            int locPV = pv.getPlayer().getLocation();
            ArrayList<CardButton> listcb = pv.getListCardButtonOnhand();

            //if the location is the same as location of given player
            //Get the listcb of that player (larger)
            //narrow it down, only enable buttons selected (smaller)

            if (locPV == expectedPlayer.getLocation()){//enable those selected buttons
                //for each CardButton in listcb (larger list)
                //get the card, and compare it with the card given
                //if 2 card matched, enable it,
                //if 2 card is not match, disable it
                for (CardButton cb: listcb){
                    String nameCardButton = cb.getCard().getName();
                    boolean isMatch = false;
                    for(Card c: listCardAllowedToDisplay){
                        String nameCard = c.getName();
                        if (nameCardButton.equals(nameCard)){
                            isMatch=true;
                            cb.getButton().setIcon(((AddImage)c).getImage());
                            cb.getButton().setEnabled(true);
                        }
                    }
                    if (!isMatch){
                        cb.getButton().setEnabled(false);
                    }
                }

            }else {//disable all buttons of other players, except expected player
                for (CardButton cb: listcb){
                    cb.getButton().setEnabled(false);
                }
            }
        }
    }//end enableSelectedButtonsOfPlayer()

    public ArrayList<CardButton> getListCardButtonOnhandOfPlayer(Player playerActive) {
        ArrayList<CardButton> listCB = new ArrayList<>();
        for (PlayerView pv: listPlayerView){
            if (pv.getPlayer().getLocation() == playerActive.getLocation()){
                listCB = pv.getListCardButtonOnhand();
            }
        }
        return listCB;
    }

    public ArrayList<JPanel> getListPanelOnhand() {
        return listPanelOnhand;
    }

    public void addUtilities(JLabel lbPackOfCards, JLabel lbBidCard,
                             JLabel lbNotification, JButton btPass,
                             ArrayList<JButton> arButtonCategories) {
        //sharedTop.add(lbPackOfCards);
        sharedTop.add(lbBidCard);
        sharedTop.add(lbNotification);
        sharedTop.add(btPass);

        for (JButton btCat: arButtonCategories) {
            sharedBottom.add(btCat);
        }
    }

    public ArrayList<JLabel[]> getListLabelInfo() {
        return listLabelInfo;
    }

    public void changeStatusPlayer(Player person, Player.Status st) {
        for (JLabel[] e : listLabelInfo){
            String loc = e[0].getText();
            if (loc.equals(String.valueOf(person.getLocation()))){
                e[1].setText(st.name());
            }
        }
    }

}//end class
