public class Item {
    private int id;
    private String name;
    private int price;
    private String description;
    private String effectType; // heal potion mana potion buff_attack potion
    private int effectValue;
    private String itemType;
    private EquipmentSlot equipmentSlot;
    private int attackBonus;
    private int defenseBonus;
    private int healthBonus;
    private int manaBonus;
    private int requiredLevel;
    private boolean isEquiped;

    private Player player;

    public Item(){}

    public Item(int id, String name, int price, String description, String effectType, int effectValue) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.effectType = effectType;
        this.effectValue = effectValue;
    }

    public Item(int id, String name, int price, String description,
                String itemType, EquipmentSlot equipmentSlot, int attackBonus,
                int defenseBonus, int healthBonus, int manaBonus, int requiredLevel) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.itemType = itemType;
        this.equipmentSlot = equipmentSlot;
        this.attackBonus = attackBonus;
        this.defenseBonus = defenseBonus;
        this.healthBonus = healthBonus;
        this.manaBonus = manaBonus;
        this.requiredLevel = requiredLevel;
    }

    // Getter
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getEffectType() {
        return effectType;
    }

    public int getEffectValue() {
        return effectValue;
    }

    public String getItemType() {
        return itemType;
    }

    public EquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    public int getDefenseBonus() {
        return defenseBonus;
    }

    public int getHealthBonus() {
        return healthBonus;
    }

    public int getManaBonus() {
        return manaBonus;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    // Setter
    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        if ("CONSUMABLE".equals(itemType)) {
            return String.format("%s - %s (Цена: %d)", name, description, price);
        } else {
            return String.format("%s - %s (Атака: +%d, Защита: +%d, Ур. %d) - Цена: %d",
                    name, description, attackBonus, defenseBonus, requiredLevel, price);
        }
    }

    public String getDetailedInfo() {
        if ("CONSUMABLE".equals(itemType)) {
            return String.format("%s\n%s\nЦена: %d золота", name, description, price);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(name).append("\n");
            sb.append(description).append("\n");
            sb.append("Тип: ").append(itemType).append("\n");
            if (equipmentSlot != null) {
                sb.append("Слот: ").append(equipmentSlot.getDisplayName()).append("\n");
            }
            if (attackBonus > 0) sb.append("Атака: +").append(attackBonus).append("\n");
            if (defenseBonus > 0) sb.append("Защита: +").append(defenseBonus).append("\n");
            if (healthBonus > 0) sb.append("Здоровье: +").append(healthBonus).append("\n");
            if (manaBonus > 0) sb.append("Мана: +").append(manaBonus).append("\n");
            sb.append("Требуемый уровень: ").append(requiredLevel).append("\n");
            sb.append("Цена: ").append(price).append(" золота");
            return sb.toString();
        }
    }
}

