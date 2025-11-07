import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class EnemyLoader {
    private List<Enemy> enemies;
    private Random random;

    public EnemyLoader() {
        this.enemies = new ArrayList<>();
        this.random = new Random();
        loadEnemiesFromJson();
    }

    private void loadEnemiesFromJson() {
        try {
            // Получаем поток к файлу JSON
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("enemies.json");

            if (inputStream == null) {
                System.out.println("Файл enemies.json не найден! Создаю стандартных врагов.");
                createDefaultEnemies();
                return;
            }

            // Парсим JSON
            JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
            var enemiesArray = jsonObject.getAsJsonArray("enemies");

            Gson gson = new Gson();
            for (var enemyElement : enemiesArray) {
                JsonObject enemyJson = enemyElement.getAsJsonObject();
                Enemy enemy = gson.fromJson(enemyJson, Enemy.class);
                enemies.add(enemy);
            }

            inputStream.close();
            System.out.println("Загружено врагов: " + enemies.size());

        } catch (Exception e) {
            System.out.println("Ошибка загрузки врагов из JSON: " + e.getMessage());
            createDefaultEnemies();
        }
    }

    private void createDefaultEnemies() {
        // Резервные враги, если JSON не загрузился
        enemies.add(new Enemy("Слабый Гоблин", 40, 10, 5, 30, 15));
        enemies.add(new Enemy("Злой Орк", 70, 18, 12, 60, 35));
        enemies.add(new Enemy("ДРЕВНИЙ ДРАКОН", 150, 25, 20, 200, 100));
    }

    public Enemy getRandomEnemy() {
        // Взвешенный случайный выбор на основе spawnWeight
        int totalWeight = enemies.stream().mapToInt(this::getSpawnWeight).sum();
        int randomValue = random.nextInt(totalWeight);
        int currentWeight = 0;

        for (Enemy enemy : enemies) {
            currentWeight += getSpawnWeight(enemy);
            if (randomValue < currentWeight) {
                return new Enemy(
                        enemy.getName(),
                        enemy.getHealth(),
                        enemy.getAttack(),
                        enemy.getDefense(),
                        enemy.getExperienceReward(),
                        enemy.getGoldReward()
                );
            }
        }

        return enemies.get(0); // fallback
    }

    private int getSpawnWeight(Enemy enemy) {
        // Используем рефлексию для получения spawnWeight, если он есть
        try {
            var field = enemy.getClass().getDeclaredField("spawnWeight");
            field.setAccessible(true);
            return field.getInt(enemy);
        } catch (Exception e) {
            return 1; // вес по умолчанию
        }
    }

    public List<Enemy> getAllEnemies() {
        return new ArrayList<>(enemies);
    }
}