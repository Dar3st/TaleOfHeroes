import java.util.List;
import java.util.Scanner;

public class Main {
    private static Player player;
    private static Scanner scanner = new Scanner(System.in);
    private static EnemyLoader enemyLoader;
    private static SkillLoader skillLoader;

    public static void main(String[] args) {
        System.out.println("Добро пожаловать в консольную RPG!");
        enemyLoader = new EnemyLoader();
        skillLoader = new SkillLoader();

        System.out.print("Введите имя вашего персонажа: ");
        String playerName = scanner.nextLine();
        player = new Player(playerName);

        System.out.println("Персонаж " + player.getName() + " создан! Удачи в приключениях!");

        boolean isRunning = true;
        while (isRunning && player.isAlive()) {
            printMainMenu();
            int choice = getPlayerChoice(1, 7);

            switch (choice) {
                case 1:
                    explore();
                    break;
                case 2:
                    player.printStats();
                    break;
                case 3:
                    showInventory();
                    break;
                case 4:
                    rest();
                    break;
                case 5:
                    shop();
                    break;
                case 6:
                    magicShop();
                    break;
                case 7:
                    isRunning = false;
                    break;
            }
        }

        System.out.println("Спасибо за игру!");
        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n=== ГЛАВНОЕ МЕНЮ ===");
        System.out.println("1. Отправиться в путешествие");
        System.out.println("2. Показать характеристики");
        System.out.println("3. Открыть инвентарь");
        System.out.println("4. Отдохнуть в таверне (восстановить здоровье и ману)");
        System.out.println("5. Магазин");
        System.out.println("6. Магазин умений");
        System.out.println("7. Выйти из игры");
    }

    private static int getPlayerChoice(int min, int max) {
        int choice;
        while (true) {
            System.out.print("Ваш выбор: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                if (choice >= min && choice <= max) {
                    scanner.nextLine(); // Очистка буфера
                    return choice;
                }
            } else {
                scanner.nextLine(); // Очистка буфера от некорректного ввода
            }
            System.out.println("Неверный ввод. Введите число от " + min + " до " + max + ".");
        }
    }

    private static void explore() {
        System.out.println("\nВы отправляетесь в опасные земли...");
        // Простой случайный encounter

        Enemy encounteredEnemy = enemyLoader.getRandomEnemy();
        System.out.println("Вы встретили: " + encounteredEnemy.getName() + "!");
        System.out.println("Здоровье: " + encounteredEnemy.getHealth() +
                ", Атака: " + encounteredEnemy.getAttack() +
                ", Защита: " + encounteredEnemy.getDefense());

        Battle battle = new Battle(player, encounteredEnemy);
        battle.start();
    }

    private static void showInventory() {
        System.out.println("\n=== ВАШ ИНВЕНТАРЬ ===");
        if (player.getInventory().isEmpty()) {
            System.out.println("Ваш инвентарь пуст.");
        } else {
            for (int i = 0; i < player.getInventory().size(); i++) {
                System.out.println((i + 1) + ". " + player.getInventory().get(i));
            }
            System.out.println("\nИспользовать предмет? (1 - Да, 0 - Нет)");
            int choice = getPlayerChoice(0, 1);
            if (choice == 1) {
                System.out.print("Введите номер предмета: ");
                int itemChoice = getPlayerChoice(1, player.getInventory().size());
                String itemToUse = player.getInventory().get(itemChoice - 1);
                player.useItem(itemToUse);
            }
        }
    }

    private static void rest() {
        int cost = 10;
        if (player.getGold() >= cost) {
            player.setGold(player.getGold() - cost);
            player.setHealth(player.getMaxHealth());
            player.setMana(player.getMaxMana());
            System.out.println("Вы хорошо отдохнули в таверне и полностью восстановили здоровье и ману. Потрачено " + cost + " золота.");
        } else {
            System.out.println("У вас недостаточно золота для отдыха в таверне! Нужно " + cost + " золота.");
        }
    }

    private static void shop(){
        System.out.println("=== Магазин ===");
        System.out.println("Ваше золото: " + player.getGold());
        System.out.println("1. Малое зелье здоровья - цена: 5");
        System.out.println("2. Среднее зелье здоровья - цена: 15");
        System.out.println("3. Большое зелье здоровья - цена: 30");
        System.out.println("0. Выход из магазина");
        System.out.print("Ваш выбор: ");

        int choice = getPlayerChoice(0, 3);

        switch (choice){
            case 1:
                if(player.getGold() < 10){
                    System.out.println("У вас недостаточно денег");
                    break;
                }else{
                    int cost = 10;
                    player.setGold(player.getGold() - cost);
                    player.getInventory().add("малое зелье здоровья");
                    System.out.println("Вы купили малое зелье здоровья");
                    break;
                }
            case 2:
                if(player.getGold() < 15){
                    System.out.println("У вас недостаточно денег");
                    break;
                }else{
                    int cost = 15;
                    player.setGold(player.getGold() - cost);
                    player.getInventory().add("среднее зелье здоровья");
                    System.out.println("Вы купили среднее зелье здоровья");
                    break;
                }
            case 3:
                if(player.getGold() < 30){
                    System.out.println("У вас недостаточно денег");
                    break;
                }else{
                    int cost = 30;
                    player.setGold(player.getGold() - cost);
                    player.getInventory().add("большое зелье здоровья");
                    System.out.println("Вы купили большое зелье здоровья");
                    break;
                }
            case 0:
                break;
        }
    }

    private static void magicShop(){
        SkillLoader skillLoader = new SkillLoader();
        List<Skill> availableSkills = skillLoader.getSkills();

        System.out.println("=== Магазин умений ===");
        System.out.println("Ваше золото: " + player.getGold());

        for(int i = 0; i < availableSkills.size(); i++){
            Skill skill = availableSkills.get(i);
            System.out.println((i+1) + ". " + skill.toString() + " - цена: " + skill.getPrice());
        }
        System.out.println("0. Выход");
        System.out.print("Ваш выбор: ");

        int choice = getPlayerChoice(0, 4);
        if(choice == 0) return;

        Skill selectedSkill = availableSkills.get(choice - 1);
        if(player.getGold() >= selectedSkill.getPrice()){
            player.setGold(player.getGold() - selectedSkill.getPrice());
            player.addSkill(selectedSkill);
            System.out.println("Вы изучили умение: " + selectedSkill.getName());
        }else{
            System.out.println("Недостаточно золота для изучения");
        }
    }
}