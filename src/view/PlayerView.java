package view;
import model.CardButton;
import model.Player;

import java.util.ArrayList;

public class PlayerView {
    private Player player;
    private ArrayList<CardButton> listCardButtonOnhand;

    public PlayerView(Player p) {
        this.player = p;
    }


    public Player getPlayer() {
        return this.player;
    }

    public void setCardButtonOnhand(ArrayList<CardButton> listCardButtonEachPlayer) {
        this.listCardButtonOnhand = listCardButtonEachPlayer;
    }

    public ArrayList<CardButton> getListCardButtonOnhand() {
        return listCardButtonOnhand;
    }

    public void removeCardButton(CardButton cb) {
        listCardButtonOnhand.remove(cb);
    }
}
