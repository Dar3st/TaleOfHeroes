public class Item {
    private int id;
    private String name;
    private int price;
    private String description;
    private String effectType; // heal potion mana potion buff_attack potion
    private int effectValue;

    private Player player;

    public Item(){}

    public Item(int id, String name, int price, String description, String effectType, int effectValue){
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.effectType = effectType;
        this.effectValue = effectValue;
    }
    public int getId(){return id;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public int getPrice(){return price;}
    public void setPrice(int price){this.price = price;}

    public String getDescription(){return description;}
    public void setDescription(String description){this.description = description;}

    public String getEffectType(){return effectType;}
    public void setEffectType(String effectType){this.effectType = effectType;}

    public int getEffectValue(){return effectValue;}
    public void setEffectValue(int effectValue){this.effectValue = effectValue;}


    public String toString(){
        return String.format("%s, цена : %d, Описание: %s", name, price, description);
    }
}
