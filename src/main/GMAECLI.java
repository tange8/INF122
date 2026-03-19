package main;

import java.util.Scanner;

public class GMAECLI {

    private GMAE gmae;
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new GMAECLI().run();
    }

    public GMAECLI() {
        this.gmae = new GMAE();
    }

    public void run() {
        
        System.out.println("=== Welcome to GuildQuest ===");

        Player player1 = createPlayer(1);
        Player player2 = createPlayer(2);

        System.out.println("Players created: ");
        System.out.println("Player 1: " + player1.getName());
        System.out.println("Player 2: " + player2.getName());


        //loops to let the player play multiple times
        while(true){

            int choice = chooseAdventure();
            MiniAdventure adventure = getAdventureByChoice(choice);

            //Giving the character a starting bonus before they play the game
            if (choice == 1) { // Dungeon Escape
                System.out.println("\n=== Starting Bonuses ===");
                giveStartingBonus(player1);
                giveStartingBonus(player2);
            }

            adventure.initialize(player1, player2);
            adventure.setup();

            System.out.println("\n=== Adventure Started: " + adventure.getName() + " ===");

            //loop through the game
            boolean finished = false;
            String input = " ";
            while (!finished) {
                ScenarioPrompt prompt = adventure.processInput(input);
                System.out.println(adventure.currentState());
                System.out.println(prompt.getPrompt());

                //tbd this 
                // 
                // if (Status.END.toString() == adventure.currentState()) {}
                
                if (adventure instanceof DungeonEscapeAdventure) {
                    if(((DungeonEscapeAdventure) adventure).isComplete()){

                        break;
                    }
                }
                System.out.print("\nEnter action: ");
                input = scanner.nextLine();

            }

            System.out.println("\n=== Adventure Complete! ===");

            //ask if they want to play another game
            System.out.print("Do you want to play another mini-adventure? (y/n): ");
            String again = scanner.nextLine().trim().toLowerCase();
            if (!again.equals("y")) {
                System.out.println("\nThanks for playing GuildQuest!");
                break;
            }


        }
        

    }

    private int chooseAdventure(){

        while (true) {

            System.out.println("=== Select Mini-Adventure ===");
            System.out.println("1. Dungeon Escape");
            System.out.println("2. Escort Game");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    return 1;
                case "2":
                    return 2;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }

    }

    private MiniAdventure getAdventureByChoice(int choice) {

        switch (choice) {
            case 1:
                return new DungeonEscapeAdventure();
            case 2:
                return new EscortAdventure(); //Filler until we name it
            default:
                return new DungeonEscapeAdventure();
        }
    }

    private Player createPlayer(int playerNumber) {

        System.out.println("Player " + playerNumber + " - Enter your name:");

        String name = scanner.nextLine();

        PlayerClass playerClass = chooseClass(playerNumber);

        return new Player(name, playerClass);
    }
    

    private PlayerClass chooseClass(int playerNumber){

        PlayerClass.Type[] types = PlayerClass.Type.values();

    while (true) {

        System.out.println("Player " + playerNumber + " - Choose a class: ");

        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i]);
        }

        String input = scanner.nextLine();

        try {
            int choice = Integer.parseInt(input);

            if (choice >= 1 && choice <= types.length) {
                return new PlayerClass(types[choice - 1]);
            }

        } catch (NumberFormatException e) {
            
        }

            System.out.println("Invalid choice, try again.");

        }
    }

    private void giveStartingBonus(Player player) {

        System.out.println("\n" + player.getName() + ", choose your starting bonus:");
        System.out.println("1. 100 Gold");
        System.out.println("2. Key + First Aid Kit");

        while (true) {
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    player.addGold(100);
                    System.out.println("Added 100 gold to " + player.getName());
                    return;
                case "2":
                    player.addItemToInventory(new Item("Key", "A simple key that might open something.", "Uncommon"));
                    player.addItemToInventory(new Item("First Aid Kit", "Basic healing supplies.", "Common"));
                    System.out.println("Added a Key and a First Aid Kit to " + player.getName() + "'s inventory");
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }
}
