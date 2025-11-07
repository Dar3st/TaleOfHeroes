public class Item {
    private int id;
    private String name;
    private int price;
    private String description;
    private Player player;

    public Item(){}

    public Item(int id, String name, int price, String description){
        this.id = id;
        this.name = name;
        if(player.isAdmin()){
            this.price = 0;
        }else{
            this.price = price;
        }
        this.description = description;
    }
    public int getId(){return id;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public int getPrice(){return price;}
    public void setPrice(int price){
        if(player.isAdmin()){
            this.price = 0;
        }else{
            this.price = price;
        }
    }

    public String getDescription(){return description;}
    public void setDescription(){this.description = description;}

    public String toString(){
        return String.format("%s, цена : %d, Описание: %s", name, price, description);
    }
}
