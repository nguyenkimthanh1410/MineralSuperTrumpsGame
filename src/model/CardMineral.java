package model;
import java.util.HashMap;
import java.util.Map;

public class CardMineral extends Card{
    private Map<Category, Object> map;

    public CardMineral(String nameCardPlay){
        super(nameCardPlay);
        map = new HashMap<>();
        setTrump(false);
    }

    public CardMineral(Object[] valuesForACard) {
        //name
        this((String) valuesForACard[0]);
        // super((String) valuesForACard[0]);
        // map=new HashMap<>();
        // setTrump(false);
        //System.out.println("Constructor: "+Arrays.toString(valuesForACard));
        //hardness value
        Object hardVal = valuesForACard[1];
        addCategory(Category.HARDNESS, hardVal);
        //gravity value
        Object gravityVal = valuesForACard[2];
        addCategory(Category.GRAVITY, gravityVal);
        //Cleavage value
        Object cleavageVal = valuesForACard[3];
        addCategory(Category.CLEAVAGE,cleavageVal);
        //Crustal value
        Object crustalVal = valuesForACard[4];
        addCategory(Category.CRUSTAL,crustalVal);
        //EcoValue value
        Object ecoVal = valuesForACard[5];
        addCategory(Category.ECOVALUE, ecoVal);
    }

    public boolean isTrump(){
        return getTrump();
    }

    public void addCategory(Category cat, Object value){
        map.put(cat,value);
    }

    public String getCategoryValue(Category cat){
        String res = map.get(cat).toString();
        return res;
    }

    //method return >0: this card higher value card compared
    public double compare(CardMineral cp, Category cat){
        double res = 0.0;
        switch (cat){
            case ECOVALUE:
                String v1 = this.getCategoryValue(cat);
                EcoRanks v1_eco = EcoRanks.valueOf(v1);
                String v2 = cp.getCategoryValue(cat);
                EcoRanks v2_eco = EcoRanks.valueOf(v2);
                res = v1_eco.compareTo(v2_eco);
                break;
            case CRUSTAL:
                CrustalRanks v1_crustal = CrustalRanks.valueOf(this.getCategoryValue(cat));
                CrustalRanks v2_crustal = CrustalRanks.valueOf(cp.getCategoryValue(cat));
                res = v1_crustal.compareTo(v2_crustal);
                break;
            case CLEAVAGE:
                CleavageRanks v1_cleavage = CleavageRanks.valueOf(this.getCategoryValue(cat));
                CleavageRanks v2_cleavage = CleavageRanks.valueOf(cp.getCategoryValue(cat));
                res = v1_cleavage.compareTo(v2_cleavage);
                break;
            case HARDNESS:
                double v1_hardness = Double.parseDouble(this.getCategoryValue(cat));
                double v2_hardness = Double.parseDouble(cp.getCategoryValue(cat));
                res = v1_hardness - v2_hardness;
                break;
            case GRAVITY:
                double v1_gravity = Double.parseDouble(this.getCategoryValue(cat));
                double v2_gravity = Double.parseDouble(cp.getCategoryValue(cat));
                res = v1_gravity - v2_gravity;
                break;
        }
        return res;
    }

    public String toString(){
        String res = "";
        res += super.toString();
        for(Category c : map.keySet()){
            Category key = c;
            Object value = map.get(key).toString();
            res += key+"="+value+",";//getValue(): get actual value assigned for enum
        }
        return res;
    }

    public String speak(Category cat){
        String announcement = getName() + ", " + cat.getValue()+", ";
        switch (cat){
            case ECOVALUE:
                announcement += ((EcoRanks) map.get(cat)).getValue();
                break;
            case CRUSTAL:
                announcement += ((CrustalRanks) map.get(cat)).getValue();
                break;
            case CLEAVAGE:
                announcement += ((CleavageRanks) map.get(cat)).getValue();
                break;
            case HARDNESS:
                announcement += getCategoryValue(cat);
                break;
            case GRAVITY:
                announcement += getCategoryValue(cat);
                break;
        }
        return announcement;
    }
}


