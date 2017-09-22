package model;
import javax.swing.*;

public class CardTrumpImage extends CardTrump implements AddImage {
    private ImageIcon image;
    public CardTrumpImage(String name, Category cat, ImageIcon img) {
        super(name,cat);
        this.image = img;
    }

    @Override
    public ImageIcon getImage() {
        return this.image;
    }
}
