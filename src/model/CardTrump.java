package model;

public class CardTrump extends Card{
    private Category trumpConstant;
    public CardTrump(String nameTrump, Category cat) {
        super(nameTrump);
        trumpConstant = cat;
        setTrump(true);
    }
    public boolean isTrump() {
        return getTrump();
    }
    public Category getCategoryConst() {
        return this.trumpConstant;
    }
    public String toString() {
        String res = super.toString() + trumpConstant.toString();
        return res;
    }

}