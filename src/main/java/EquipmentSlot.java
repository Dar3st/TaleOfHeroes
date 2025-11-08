public enum EquipmentSlot {
    MAIN_HAND("Основная рука"),
    OFF_HAND("Вторая рука"),
    HEAD("Голова"),
    CHEST("Грудь"),
    HANDS("Руки"),
    LEGS("Ноги"),
    FEET("Ступни"),
    RING_1("Кольцо 1"),
    RING_2("Кольцо 2"),
    NECK("Шея"),
    BELT("Пояс");

    private final String displayName;

    EquipmentSlot(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}