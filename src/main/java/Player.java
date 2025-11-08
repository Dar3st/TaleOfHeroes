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
    private int baseAttack;
    private int baseDefense;
    private int experience;
    private int level;
    private Integer gold;
    private List<Item> inventory;
    private List<Skill> skills;
    private boolean isAdmin;

    // Система экипировки
    private Map<EquipmentSlot, Item> equippedItems;
    private int totalAttack;
    private int totalDefense;

    public Player(String name) {
        this.name = name;
        this.isAdmin = name.equalsIgnoreCase("admin");

        if (isAdmin) {
            this.health = Integer.MAX_VALUE;
            this.maxHealth = Integer.MAX_VALUE;
            this.mana = Integer.MAX_VALUE;
            this.maxMana = Integer.MAX_VALUE;
            this.baseAttack = Integer.MAX_VALUE;
            this.baseDefense = Integer.MAX_VALUE;
            this.experience = 0;
            this.level = Integer.MAX_VALUE;
            this.gold = Integer.MAX_VALUE;
        } else {
            this.health = 100;
            this.maxHealth = 100;
            this.mana = 50;
            this.maxMana = 50;
            this.baseAttack = 10;
            this.baseDefense = 5;
            this.experience = 0;
            this.level = 1;
            this.gold = 50;
        }

        this.inventory = new ArrayList<>();
        this.skills = new ArrayList<>();
        this.equippedItems = new HashMap<>();
        calculateTotalStats();
    }

    // Система проверки экипировки

    public boolean equipItem(Item item) {
        if (item == null) return false;

        // Проверяем, можно ли экипировать этот предмет
        if (!"WEAPON".equals(item.getItemType()) &&
                !"ARMOR".equals(item.getItemType()) &&
                !"ACCESSORY".equals(item.getItemType())) {
            System.out.println("Этот предмет нельзя экипировать!");
            return false;
        }

        // Проверяем уровень
        if (level < item.getRequiredLevel()) {
            System.out.println("Ваш уровень слишком низок для экипировки этого предмета! Требуется уровень "
                    + item.getRequiredLevel());
            return false;
        }

        EquipmentSlot slot = item.getEquipmentSlot();
        if (slot == null) {
            System.out.println("Ошибка: у предмета не указан слот экипировки!");
            return false;
        }

        // Проверяем специальные случаи для колец
        if (slot == EquipmentSlot.RING_1 || slot == EquipmentSlot.RING_2) {
            // Если экипируем кольцо в первый слот, но он занят - пробуем второй
            if (slot == EquipmentSlot.RING_1 && equippedItems.containsKey(EquipmentSlot.RING_1)) {
                if (!equippedItems.containsKey(EquipmentSlot.RING_2)) {
                    slot = EquipmentSlot.RING_2;
                }
            }
        }
        // логика экипировки оружия во вторую руку
        if (slot == EquipmentSlot.MAIN_HAND || slot == EquipmentSlot.OFF_HAND){
            if(slot == EquipmentSlot.MAIN_HAND && equippedItems.containsKey(EquipmentSlot.MAIN_HAND)){
                if(!equippedItems.containsKey(EquipmentSlot.OFF_HAND)){
                    slot = EquipmentSlot.OFF_HAND;
                }
            }
        }
        // Снимаем предыдущий предмет в этом слоте, если есть
        Item previousItem = equippedItems.put(slot, item);

        // Удаляем предмет из инвентаря
        inventory.remove(item);

        // Добавляем предыдущий предмет обратно в инвентарь
        if (previousItem != null) {
            inventory.add(previousItem);
            System.out.println("Вы сняли: " + previousItem.getName());
        }

        System.out.println("Вы экипировали: " + item.getName() + " в слот " + slot.getDisplayName());
        calculateTotalStats();
        return true;
    }

    public boolean unequipItem(EquipmentSlot slot) {
        Item item = equippedItems.remove(slot);
        if (item != null) {
            inventory.add(item);
            System.out.println("Вы сняли: " + item.getName());
            calculateTotalStats();
            return true;
        } else {
            System.out.println("В этом слоте ничего не экипировано!");
            return false;
        }
    }

    private void calculateTotalStats() {
        totalAttack = baseAttack;
        totalDefense = baseDefense;
        int bonusHealth = 0;
        int bonusMana = 0;

        for (Item item : equippedItems.values()) {
            totalAttack += item.getAttackBonus();
            totalDefense += item.getDefenseBonus();
            bonusHealth += item.getHealthBonus();
            bonusMana += item.getManaBonus();
        }

        maxHealth = 100 + (level - 1) * 20 + bonusHealth;
        maxMana = 50 + (level - 1) * 10 + bonusMana;
        health = Math.min(health, maxHealth);
        mana = Math.min(mana, maxMana);
    }

    public void showEquipment() {
        System.out.println("\n=== ВАША ЭКИПИРОВКА ===");
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            Item item = equippedItems.get(slot);
            String itemName = (item != null) ? item.getName() : "[Пусто]";
            System.out.println(slot.getDisplayName() + ": " + itemName);
        }
        System.out.println("========================");
    }

    // Основные методы

    public void useItem(String itemName) {
        Item itemToUse = null;
        for (Item item : inventory) {
            if (item.getName().equals(itemName)) {
                itemToUse = item;
                break;
            }
        }

        if (itemToUse == null) {
            System.out.println("У вас нет предмета: " + itemName);
            return;
        }

        if ("CONSUMABLE".equals(itemToUse.getItemType())) {
            applyConsumableEffect(itemToUse);
            inventory.remove(itemToUse);
        } else {
            System.out.println("Этот предмет нельзя использовать, его нужно экипировать!");
        }
    }

    private void applyConsumableEffect(Item item) {
        String effectType = item.getEffectType();
        int value = item.getEffectValue();

        switch (effectType.toUpperCase()) {
            case "HEAL":
                int newHealth = Math.min(health + value, maxHealth);
                int healed = newHealth - health;
                health = newHealth;
                System.out.println("Вы использовали " + item.getName() + " и восстановили " + healed + " HP!");
                break;
            case "MANA":
                int newMana = Math.min(mana + value, maxMana);
                int manaRestored = newMana - mana;
                mana = newMana;
                System.out.println("Вы использовали " + item.getName() + " и восстановили " + manaRestored + " MP!");
                break;
            default:
                System.out.println("Неизвестный эффект предмета: " + effectType);
        }
    }

    public void useSkill(Skill skill, Enemy enemy) {
        if (isAdmin || mana >= skill.getCostMana()) {
            if (!isAdmin) {
                mana -= skill.getCostMana();
            }

            int damage = skill.getMagDamage();
            enemy.takeDamage(damage);
            System.out.println("Вы использовали " + skill.getName() + " и нанесли " + damage + " магического урона!");
        } else {
            System.out.println("Недостаточно маны для использования навыка!");
        }
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public void addExperience(int exp) {
        this.experience += exp;
        if (this.experience >= this.level * 100) {
            levelUp();
        }
    }

    public void levelUp() {
        if (!isAdmin) {
            level++;
            experience = 0;
            baseAttack += 2;
            baseDefense += 1;
            health = maxHealth;
            mana = maxMana;
            calculateTotalStats();

            System.out.println("\n*** ПОЗДРАВЛЯЕМ! Вы достигли " + level + " уровня! ***");
            System.out.println("Ваши показатели увеличены!");
            System.out.println("Здоровье: " + maxHealth);
            System.out.println("Мана: " + maxMana);
            System.out.println("Атака: " + baseAttack);
            System.out.println("Защита: " + baseDefense);
        }
    }

    // ========== ГЕТТЕРЫ ==========

    public String getName() { return name; }
    public int getHealth() { return isAdmin ? Integer.MAX_VALUE : health; }
    public void setHealth(int health) {
        if (isAdmin) {
            this.health = maxHealth;
        } else {
            this.health = Math.min(health, maxHealth);
        }
    }
    public int getMaxHealth() { return maxHealth; }
    public int getMana() { return isAdmin ? Integer.MAX_VALUE : mana; }
    public void setMana(int mana) {
        if (isAdmin) {
            this.mana = maxMana;
        } else {
            this.mana = Math.min(mana, maxMana);
        }
    }
    public int getMaxMana() { return maxMana; }
    public int getAttack() { return totalAttack; }
    public int getDefense() { return totalDefense; }
    public int getExperience() { return experience; }
    public int getLevel() { return level; }
    public int getGold() { return isAdmin ? Integer.MAX_VALUE : gold; }
    public void setGold(Integer gold) {
        if (isAdmin) {
            this.gold = Integer.MAX_VALUE;
        } else {
            this.gold = gold;
        }
    }
    public List<String> getInventory() {
        List<String> result = new ArrayList<>();
        for (Item item : inventory) {
            result.add(item.toString());
        }
        return result;
    }
    public List<Item> getInventoryItems() { return new ArrayList<>(inventory); }
    public List<Skill> getSkills() { return skills; }
    public boolean isAlive() { return isAdmin || health > 0; }
    public boolean isAdmin() { return isAdmin; }
    public Map<EquipmentSlot, Item> getEquippedItems() { return new HashMap<>(equippedItems); }

    public void printStats() {
        System.out.println("\n=== ХАРАКТЕРИСТИКИ " + name.toUpperCase() + " ===");
        if (isAdmin) {
            System.out.println("⚡ РЕЖИМ АДМИНИСТРАТОРА ⚡");
        }
        System.out.println("Уровень: " + level);
        System.out.println("Здоровье: " + getHealth() + "/" + maxHealth);
        System.out.println("Мана: " + getMana() + "/" + maxMana);
        System.out.println("Атака: " + totalAttack + " (база: " + baseAttack + ")");
        System.out.println("Защита: " + totalDefense + " (база: " + baseDefense + ")");
        System.out.println("Опыт: " + experience + "/" + (level * 100));
        System.out.println("Золото: " + getGold());

        // Показываем бонусы от экипировки
        if (!equippedItems.isEmpty()) {
            System.out.println("\n--- Бонусы от экипировки ---");
            for (Item item : equippedItems.values()) {
                if (item.getAttackBonus() > 0)
                    System.out.println(item.getName() + ": +" + item.getAttackBonus() + " к атаке");
                if (item.getDefenseBonus() > 0)
                    System.out.println(item.getName() + ": +" + item.getDefenseBonus() + " к защите");
                if (item.getHealthBonus() > 0)
                    System.out.println(item.getName() + ": +" + item.getHealthBonus() + " к здоровью");
                if (item.getManaBonus() > 0)
                    System.out.println(item.getName() + ": +" + item.getManaBonus() + " к мане");
            }
        }
    }
}