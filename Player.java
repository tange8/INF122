import java.util.ArrayList;
import java.util.Scanner;

/* 
add thise to our gmae main function so that we can select users as players 1 and 2

GuildQuest guildQuest = new GuildQuest();
Player p1 = Player.choosePlayer(1, guildQuest.getUsers(), scanner);
Player p2 = Player.choosePlayer(2, guildQuest.getUsers(), scanner); 
*/


public class Player {
    private String name;
    private Inventory inventory;
    private int wins = 0;
    private int losses = 0;
    private ArrayList<String> questHistory = new ArrayList<>();

    //constructor 
    private Player(String name) {
        this.name = name;
        this.inventory = new Inventory();
    }

    /*
        we call this in gmae main so that we can select a user from bryant's guildquest user list and make them the players of the game
     */
    public static Player choosePlayer(int playerNumber, ArrayList<User> users, Scanner scanner) {
        while (true) {
            System.out.println("--- Select User for Player " + playerNumber + " ---");
            for (int i = 0; i < users.size(); i++) {
                System.out.println(i + ": " + users.get(i).getUserID());
            }
            System.out.print("Select option: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 0 && choice < users.size()) {
                    String selectedName = users.get(choice).getUserID();
                    Player player = new Player(selectedName);
                    System.out.println("Player " + playerNumber + ": " + selectedName + " selected.");
                    System.out.println();
                    return player;
                } else {
                    System.out.println("Invalid option! Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option! Try again.");
            }
        }
    }

    //features of our player profile: (e.g., character name, preferred realm, inventory snapshot, quest history, achievements, etc.).

    /* records win and losses (can be called in mini games)
     * example: call this at the end of a mini game
     *player1.recordWin("Relic Hunt");
     */
    public void recordWin(String adventureName) {
        wins++;
        questHistory.add("WIN: " + adventureName);
    }

    public void recordLoss(String adventureName) {
        losses++;
        questHistory.add("LOSS: " + adventureName);
    }

    //INVENTORY SNAPSHOT
    //add/remove items and show inventory
    public void addItem(Item item) {
        inventory.addItem(item);
    }

    public void removeItem(Item item) {
        inventory.removeItem(item);
    }

    public Inventory getInventory() {
        return inventory;
    }

    //getters
    public String getName() { return name; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public ArrayList<String> getQuestHistory() { return questHistory; }

    //call this at the end of each minigame?
    public void displayProfile() {
        System.out.println("=== Player Profile ===");
        System.out.println("Name:   " + name);
        System.out.println("Wins:   " + wins + " | Losses: " + losses);
        System.out.print("Inventory: ");
        if (inventory.getInventory().isEmpty()) {
            System.out.print("(empty)");
        } else {
            for (Item i : inventory.getInventory()) {
                System.out.print(i.getName() + " | ");
            }
        }
        System.out.println();
        System.out.println("Quest History: " + questHistory);
        System.out.println();
    }
}