public class Skill {
    private int id;
    private String name;
    private int costMana;
    private int magDamage;
    private byte level;
    private int price;

    public Skill(){}

    public Skill(int id, String name, int costMana, int magDamage, byte level, int price){
        this.id = id;
        this.name = name;
        this.costMana = costMana;
        this.magDamage = magDamage;
        this.level = level;
        this.price = price;
    }

    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public int getCostMana(){return costMana;}
    public void setCostMana(int costMana){this.costMana = costMana;}

    public int getMagDamage(){return magDamage;}
    public void setMagDamage(int magDamage){this.magDamage = magDamage;}

    public byte getLevel(){return level;}
    public void setLevel(byte level) {this.level = level;}

    public int getPrice(){return price;}
    public void setPrice(int price){this.price = price;}

    @Override
    public String toString(){
        return String.format("%d. %s уровень %d, MP: %d, Damage: %d", id, name, level, costMana, magDamage);
    }
}
