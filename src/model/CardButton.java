package model;
import javax.swing.*;

public class CardButton {
    Card card;
    JButton button;
    public static String strPathImgBackCover = "src/images/Slide66.jpg";

    public CardButton(Card card) {
        this.card = card;
        button = new JButton();
        button.setIcon(Helper.createImageIcon(strPathImgBackCover));
        button.setRolloverEnabled(true);
    }

    public JButton getButton() {
        return button;
    }

    public Card getCard() {
        return card;
    }

    public ImageIcon getImageIconBackCover(){
        return Helper.createImageIcon(strPathImgBackCover);
    }
}
