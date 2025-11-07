import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Battle {
    private Player player;
    private Enemy enemy;
    private Scanner scr;

    public Battle(Player player, Enemy enemy){
        this.player = player;
        this.enemy = enemy;
        this.scr = new Scanner(System.in);
    }

    public void start(){
        System.out.println("\n=== НАЧАЛСЯ БОЙ ===");
        System.out.println("Против вас " + enemy.getName() + "!");

        while(player.isAlive() && enemy.isAlive()){
            printBattleStatus();
            System.out.println("\nВыберите действие:");
            System.out.println("1. Атаковать");
            System.out.println("2. Использовать предмет");
            System.out.println("3. Использовать умения");
            System.out.println("4. Попытаться сбежать");

            int choice = getPlayerChoice(1, 4);

            switch (choice){
                case 1:
                    performPlayerAttack();
                    break;
                case 2:
                    useItemInBattle();
                    break;
                case 3:
                    useSkillInBattle();
                    break;
                case 4:
                    if(tryToEscape()){
                        System.out.println("Вам удалось сбежать с поля боя!");
                        return;
                    }else{
                        System.out.println("Вам не удалось сбежать, бой продолжается!");
                    }
                    break;
            }

            if(enemy.isAlive()){
                performEnemyAttack();
            }
        }

        if(player.isAlive()){
            victory();
        }else{
            defeat();
        }
    }

    private void printBattleStatus(){
        System.out.println("\n----------------------------");
        System.out.println(player.getName() + " HP: " + player.getHealth() + "/" + player.getMaxHealth());
        System.out.println(enemy.getName() + " HP: " + enemy.getHealth());
        System.out.println("----------------------------");
    }

    private int getPlayerChoice(int min, int max){
        int choice;
        while(true){
            System.out.print("Ваш выбор: ");
            if(scr.hasNextInt()){
                choice = scr.nextInt();
                if(choice >= min && choice <= max){
                    scr.nextLine();
                    return choice;
                }
            }else{
                scr.nextLine();
            }
            System.out.println("Неверный ввод. Введите число от " + min + " до " + max + ".");
        }
    }

    private void performPlayerAttack() {
        Random rand = new Random();
        // Введем элемент случайности в урон (от 80% до 120% от силы атаки)
        int damageVariation = (int) (player.getAttack() * 0.2);
        int damage = player.getAttack() - damageVariation + rand.nextInt(damageVariation * 2 + 1);

        // Учитываем защиту врага
        damage = Math.max(1, damage - enemy.getDefense() / 3); // Минимальный урон 1

        enemy.takeDamage(damage);
        System.out.println("Вы нанесли " + damage + " урона " + enemy.getName() + "!");
    }

    private void performEnemyAttack() {
        Random rand = new Random();
        int damageVariation = (int) (enemy.getAttack() * 0.2);
        int damage = enemy.getAttack() - damageVariation + rand.nextInt(damageVariation * 2 + 1);

        // Учитываем защиту игрока
        damage = Math.max(1, damage - player.getDefense() / 3);

        player.setHealth(player.getHealth() - damage);
        System.out.println(enemy.getName() + " нанес вам " + damage + " урона!");
    }

    private void useItemInBattle(){
        List<Item> inventory = player.getInventoryItems();

        if(inventory.isEmpty()){
            System.out.println("Ваш инвентарь пуст");
            return;
        }

        System.out.println("Ваш инвентарь: ");
        for(int i = 0; i < inventory.size(); i++){
            System.out.println((i + 1) + ". " + inventory.get(i));
        }
        System.out.println("0. Отмена");

        int choice = getPlayerChoice(0, inventory.size());
        if(choice == 0) return;

        Item itemToUse = inventory.get(choice - 1);
        player.useItem(itemToUse.getName());
    }

    private void useSkillInBattle(){
        if(player.getSkills().isEmpty()){
            System.out.println("У вас нет доступных умений!");
            return;
        }

        System.out.println("Ваши умения: ");
        for(int i = 0; i < player.getSkills().size(); i++){
            Skill skill = player.getSkills().get(i);
            System.out.println((i+1) + ". " + skill.toString());
        }
        System.out.println("0. Отмена");

        int choice = getPlayerChoice(0, player.getSkills().size());
        if(choice == 0) return;

        Skill selectedSkill = player.getSkills().get(choice - 1);
        player.useSkill(selectedSkill, enemy);
    }

    private boolean tryToEscape(){
        Random rnd = new Random();
        return rnd.nextInt(100) < 40;
    }

    private void victory() {
        System.out.println("\n*** ПОБЕДА! Вы победили " + enemy.getName() + "! ***");
        player.addExperience(enemy.getExperienceReward());
        player.setGold(player.getGold() + enemy.getGoldReward());
        System.out.println("Вы получили " + enemy.getExperienceReward() + " опыта и " + enemy.getGoldReward() + " золота.");
    }

    private void defeat() {
        System.out.println("\n### ПОРАЖЕНИЕ... Вы пали в бою. ###");
        System.out.println("Игра окончена.");
        System.exit(0); // Завершаем игру при поражении
    }

}
