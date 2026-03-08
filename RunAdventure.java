import java.util.Scanner;


public class GameEngine {


    //Main Menu:
        //Select Game 1
        //Select Game 2

    public void runAdventure(MiniAdventure adventure, Player player1, Player player2){

        Scanner scanner = new Scanner(System.in);

        adventure.initialize(p1, p2);

        while(!adventure.isFinished()){

            System.out.println(adventure.getCurrentState());

            System.out.println("Player 1 turn: ");

            adventure.handleInput(p1, scanner.nextLine());

            if(adventure.isFinished()){
                break;
            }

            System.out.println("Player 2 turn: ");
            adventure.handleInput(p2, scanner.nextLine());

            adventure.update();
        }

        System.out.println("\n" + adventure.getResult());
    }
}