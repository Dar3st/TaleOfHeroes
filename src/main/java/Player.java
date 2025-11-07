import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private String name;
    private int health;
    private int mana;
    private int maxHealth;
    private int maxMana;
    private int attack;
    private int defense;
    private int experience;
    private int level;
    private Integer gold;
    private List<Skill> skills;
    private List<Item> inventory;
    private Map<String, Item> itemMap;
    private boolean isAdmin;

    public Player(String name){
        this.name = name;
        this.isAdmin = name.equalsIgnoreCase("admin");
        if(isAdmin){
            this.health = Integer.MAX_VALUE;
            this.maxHealth = Integer.MAX_VALUE;
            this.maxMana = Integer.MAX_VALUE;
            this.attack = Integer.MAX_VALUE;
            this.defense = Integer.MAX_VALUE;
            this.experience = 0;
            this.level = Integer.MAX_VALUE;
            this.gold = Integer.MAX_VALUE;
        }else{
            this.health = 100;
            this.maxHealth = 100;
            this.maxMana = 100;
            this.attack = 15;
            this.defense = 10;
            this.experience = 0;
            this.level = 1;
            this.gold = 100;
        }
        this.inventory = new ArrayList<>();
        this.skills = new ArrayList<>();
        this.itemMap = new HashMap<>();
    }

    public String getName(){return name;}
    public int getHealth(){
        if(isAdmin) return maxHealth;
        return health;}
    public void setHealth(int health){
        if(isAdmin){
            this.health = maxHealth;
        }else {
            this.health = Math.min(health, maxHealth);
        };
    }
    public int getMaxHealth(){return maxHealth;}

    public int getMana(){
        if(isAdmin) return maxMana;
        return mana;}
    public void setMana(int mana){
        if(isAdmin){
            this.mana = maxMana;
        }else{
            this.health = Math.min(mana, maxMana);
        }
    }
    public int getMaxMana(){return maxMana;};

    public int getAttack(){return attack;}

    public int getDefense(){return defense;}

    public int getExperience(){return experience;}

    public int getLevel(){return level;}

    public int getGold(){
        if(isAdmin) return Integer.MAX_VALUE;
        return gold;}

    public void setGold(Integer gold){
        if(isAdmin){
        this.gold = Integer.MAX_VALUE;
        }else{
            this.gold = gold;
        }
    }

    public List<String> getInventory() {
        List<String> result = new ArrayList<>();
        for(Item item : inventory){
            result.add(item.toString());
        }
        return result;
    }
    public List<Item> getInventoryItems(){
        return new ArrayList<>(inventory);
    }

    public List<Skill> getSkills(){return skills;}
    public void addSkill(Skill skill){
        skills.add(skill);
    }

    public void addItem(Item item){
        inventory.add(item);
        itemMap.put(item.getName(), item);
    }
    public void addExperience(int exp){
        this.experience += exp;
        if(this.experience >= this.level * 100){
            levelUp();
        }
    }

    public void levelUp(){
        if(!isAdmin){
            level++;
            experience = 0;
            maxHealth += 50;
            maxMana += 25;
            health = maxHealth;
            mana = maxMana;
            attack += 5;
            defense += 3;
            System.out.println("Поздравляем! Вы достигли " + level + " уровня");
            System.out.println("Ваши показатели были увеличены: \n" +
                    "Здоровье: " + 20 + "\n" +
                    "Мана: " + 50 + "\n" +
                    "Атака: " + 5 + "\n" +
                    "Защита: " + 3);
        }
    }

    public boolean isAlive(){
        if(isAdmin) return true;
        return health > 0;
    }

    public void useItem(String itemName){
        Item item = itemMap.get(itemName);
        if(item == null){
            System.out.println("У вас нет предмета - " + itemName);
            return;
        }

        boolean used = applyItemEffect(item);
        if(used){
            inventory.remove(item);
            itemMap.remove(itemName);
            System.out.println("Вы использовали: " + itemName);
        }
    }

    private boolean applyItemEffect(Item item){
        if(item == null) return false;

        switch (item.getEffectType().toUpperCase()){
            case "HEAL" :
                int healAmount = item.getEffectValue();
                int newHealth = Math.min(health + healAmount, maxHealth);
                int actualHeal = newHealth - health;
                health = newHealth;
                System.out.println("Восстановили " + actualHeal + " HP");
                return true;
            case "MANA":
                int manaAmount = item.getEffectValue();
                int newMana = Math.min(mana + manaAmount, maxMana);
                int actualMana = newMana - mana;
                mana = newMana;
                System.out.println("Восстановили " + actualMana + " MP");
                return true;
            case "BUFF_ATTACK":
                int attackAmount = item.getEffectValue();
                this.attack = getAttack() + attackAmount;
                return true;
            default:
                System.out.println("Неизвестный эффект предмета: " + item.getEffectType());
                return false;
        }
    }
    public void useSkill(Skill skill, Enemy enemy){
        if(isAdmin || mana >= skill.getCostMana()){
            if(!isAdmin){
                mana -= skill.getCostMana();
            }

            int damage = skill.getMagDamage();
            enemy.takeDamage(damage);
            System.out.println("Вы использовали " + skill.getName() + " и нанесли " + damage + " магического урона!");
        }else{
            System.out.println("Недостаточно маны для использования навыка!");
        }
    }

    public void printStats() {
        System.out.println("\n=== Характеристики " + name + " ===");
        if(isAdmin){
            System.out.println("⚡ РЕЖИМ АДМИНИСТРАТОРА ⚡");
        }
        System.out.println("Уровень: " + level);
        System.out.println("Здоровье: " + getHealth() + "/" + maxHealth);
        System.out.println("Мана: " + getMana() + "/" + maxMana);
        System.out.println("Атака: " + attack);
        System.out.println("Защита: " + defense);
        System.out.println("Опыт: " + experience + "/" + (level * 100));
        System.out.println("Золото: " + getGold());
    }

    public boolean isAdmin(){
        return isAdmin;
    }
}
