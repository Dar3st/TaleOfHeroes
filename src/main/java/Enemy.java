public class Enemy {
    private String name;
    private int health;
    private int attack;
    private int defense;
    private int experienceReward;
    private int goldReward;
    private int spawnHeight;

    public Enemy(){}

    public Enemy(String name, int health, int attack, int defense, int experienceReward, int goldReward){
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.experienceReward = experienceReward;
        this.goldReward = goldReward;
        this.spawnHeight = 1;
    }

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public int getHealth(){return health;}
    public void setHealth(int health){this.health = health;}

    public int getAttack(){return attack;}
    public void setAttack(){this.attack = attack;}

    public int getDefense(){return defense;}
    public void setDefense(){this.defense = defense;}

    public int getExperienceReward(){return experienceReward;}
    public void setExperienceReward(){this.experienceReward = experienceReward;}

    public int getGoldReward(){return goldReward;}
    public void setGoldReward(){this.goldReward = goldReward;}

    public int getSpawnHeight(){return spawnHeight;}
    public void setSpawnHeight(){this.spawnHeight = spawnHeight;}

    public boolean isAlive(){
        return health > 0;
    }

    public void takeDamage(int damage){
        this.health -= damage;
        if(this.health < 0) this.health = 0;
    }

    @Override
    public String toString(){
        return String.format("%s (HP: %d, ATK: %d, DEF: %d", name, health, attack, defense);
    }
}
