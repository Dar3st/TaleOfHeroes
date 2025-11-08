import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ItemLoader {
    private List<Item> items;

    public ItemLoader(){
        this.items = new ArrayList<>();
        loadItemsList();
    }

    private void loadItemsList() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("items.json");

            if (inputStream == null) {
                System.out.println("Не удалось загрузить данные items.json");
                createDefaultItems();
                return;
            }

            JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
            var itemsArray = jsonObject.getAsJsonArray("items");

            Gson gson = new Gson();
            for (var itemElement : itemsArray) {
                JsonObject itemJson = itemElement.getAsJsonObject();

                String itemType = itemJson.has("itemType") ? itemJson.get("itemType").getAsString() : "CONSUMABLE";

                if ("CONSUMABLE".equals(itemType)) {
                    // Загрузка расходников
                    Item item = gson.fromJson(itemJson, Item.class);
                    items.add(item);
                } else {
                    // Загрузка экипировки
                    int id = itemJson.get("id").getAsInt();
                    String name = itemJson.get("name").getAsString();
                    int price = itemJson.get("price").getAsInt();
                    String description = itemJson.get("description").getAsString();
                    EquipmentSlot slot = EquipmentSlot.valueOf(itemJson.get("equipmentSlot").getAsString());
                    int attackBonus = itemJson.has("attackBonus") ? itemJson.get("attackBonus").getAsInt() : 0;
                    int defenseBonus = itemJson.has("defenseBonus") ? itemJson.get("defenseBonus").getAsInt() : 0;
                    int healthBonus = itemJson.has("healthBonus") ? itemJson.get("healthBonus").getAsInt() : 0;
                    int manaBonus = itemJson.has("manaBonus") ? itemJson.get("manaBonus").getAsInt() : 0;
                    int requiredLevel = itemJson.has("requiredLevel") ? itemJson.get("requiredLevel").getAsInt() : 1;

                    Item item = new Item(id, name, price, description, itemType, slot,
                            attackBonus, defenseBonus, healthBonus, manaBonus, requiredLevel);
                    items.add(item);
                }
            }

            inputStream.close();
            System.out.println("Успешно загружено " + items.size() + " предметов");

        } catch (Exception e) {
            System.out.println("Ошибка загрузки данных JSON " + e.getMessage());
            createDefaultItems();
        }
    }

    private void createDefaultItems() {
        // Расходники
        items.add(new Item(1, "Малое зелье здоровья", 10, "Восстанавливает 20 HP", "HEAL", 20));
        items.add(new Item(2, "Среднее зелье здоровья", 25, "Восстанавливает 50 HP", "HEAL", 50));
        items.add(new Item(3, "Малое зелье маны", 15, "Восстанавливает 15 MP", "MANA", 15));

        // Экипировка
        items.add(new Item(4, "Деревянный меч", 30, "Простой деревянный меч", "WEAPON",
                EquipmentSlot.MAIN_HAND, 3, 0, 0, 0, 1));
        items.add(new Item(5, "Кожаный доспех", 50, "Легкий кожаный доспех", "ARMOR",
                EquipmentSlot.CHEST, 0, 5, 10, 0, 1));
        items.add(new Item(6, "Железный шлем", 40, "Простой железный шлем", "ARMOR",
                EquipmentSlot.HEAD, 0, 3, 5, 0, 1));
    }

    public List<Item> getAllItems(){
        return items;
    }
}
