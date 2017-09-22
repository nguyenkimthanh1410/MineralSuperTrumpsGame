package model;
import javax.swing.*;

public class CardMineralImage extends CardMineral implements AddImage {
    ImageIcon image;
    public CardMineralImage(Object[] valuesForACard, ImageIcon img) {
        super(valuesForACard);
        this.image = img;
    }

    public ImageIcon getImage(){
        return this.image;
    }
}
