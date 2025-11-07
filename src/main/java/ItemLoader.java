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

    private void loadItemsList(){
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("items.json");

            if(inputStream == null){
                System.out.println("Не удалось загрузить данные items.json");
            }

            JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
            var itemsArray = jsonObject.getAsJsonArray("items");

            Gson gson = new Gson();
            for(var itemElement : itemsArray){
                JsonObject itemJson = itemElement.getAsJsonObject();
                Item item = gson.fromJson(itemJson, Item.class);
                items.add(item);
            }

            inputStream.close();
            System.out.println("Успешно загружено " + items.size() + " предметов ");

        }catch (Exception e){
            System.out.println("Ошибка загрузки данных JSON " + e.getMessage());
        }
    }

    public List<Item> getAllItems(){
        return items;
    }
}
