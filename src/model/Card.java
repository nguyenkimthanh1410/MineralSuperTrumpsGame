package model;
public abstract class Card {
    public enum Category {
        ECOVALUE("Economic Value"),CRUSTAL("Crustal abundance"),
        CLEAVAGE("Cleavage"), HARDNESS("Hardness"),
        GRAVITY("Specific Gravity"), ANYCATEGORY("Any Category");
        String value;
        Category(String val) {
            this.value = val;
        }
        String getValue() {
            return this.value;
        }
    }//end enum Category

    enum EcoRanks{
        TRIVIAL("trivial"), LOW("low"),
        MODERATE("moderate"), HIGH("high"),
        VERYHIGH("very high"), IMRICH("I'm rich!");
        private String value;
        EcoRanks(String val) {
            value = val;
        }
        public String getValue() {
            return value;
        }
    }//end enum EcoRanks

    enum CrustalRanks{
        ULTRATRACE("ultratrace"), TRACE("trace"),
        LOW("low"), MODERATE("moderate"),HIGH("high"),
        VERYHIGH("very high");
        private String value;
        CrustalRanks(String val){
            this.value = val;
        }
        public String getValue(){
            return this.value;
        }
    }//end enum CrustalRanks

    enum CleavageRanks{
        NONE("none"), POORNONE("poor/none"), ONEPOOR("1 poor"),
        TWOPOOR("2 poor"), ONEGOOD("1 good"), ONEGOODONEPOOR("1 good/1 poor"),
        TWOGOOD("2 good"), THREEGOOD("3 good"), ONEPERFECT("1 perfect"),
        ONEPERFECTONEGOOD("1 perfect/1 good"), ONEPERFECTTWOGOOD("1 perfect/2 good"),
        TWOPERFECTONEGOOD("2 perfect/1 good"), THREEPERFECT("3 perfect"),
        FOURPERFECT("4 perfect"), SIXPERFECT("6 perfect");

        private String value;
        CleavageRanks(String val) {
            this.value = val;
        }
        public String getValue(){
            return this.value;
        }
    }//end enum Cleavage

    private String name;
    private boolean trump;

    public Card(String nameCard){
        this.name = nameCard;
    }
    protected void setTrump(boolean trp){
        this.trump = trp;
    }
    protected boolean getTrump(){
        return this.trump;
    }
    public String getName(){
        return this.name;
    }

    public abstract boolean isTrump();

    public String toString(){
        return "Trump:"+this.trump+","+this.name+":";
    }

    //Take out Category: ANYCATEGORY for Mineral Card
    public static String getMineralCardCategoriesString(){
        String validCategoriesString ="";
        Category[] categoryValues = Category.values();
        for (int i =0; i<categoryValues.length-1; i++ ){
            validCategoriesString +=i +":"+categoryValues[i].getValue()+"\n";
        }
        return  validCategoriesString;
    }

    //Take out Category: ANYCATEGORY for Mineral Card
    public static Category[] getMineralCardCategoriesList(){
        Category[] categoryValues = Category.values();
        Category[] validCategoriesList = new Category[categoryValues.length-1];
        for (int i=0; i<categoryValues.length-1; i++){
            validCategoriesList[i] = categoryValues[i];
        }
        return validCategoriesList;
    }

    public static Category getCategoryFromString(String strInput){
        Category result = null;
        for(Category c: Category.values()){
            String val = c.getValue();
            if(val.equals(strInput)){
                result = c;
            }
        }
        return  result;
    }
}
