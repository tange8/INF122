import java.util.Scanner;


public class GameEngine {


    //Main Menu:
        //Select Game 1
        //Select Game 2

    public void runAdventure(MiniAdventure adventure, Player player1, Player player2){

        //asks the user for input
        Scanner scanner = new Scanner(System.in);

        //starts the game with two players
        adventure.initialize(player1, player2);

        //while loop to keep the game going until it is considered "finished"
        while(!adventure.isFinished()){

            System.out.println(adventure.getCurrentState());

            System.out.println("Player 1 turn: ");

            //continous input so that the game can run
            adventure.handleInput(player1, scanner.nextLine());

            //if player 1 has won end the game if not player 2 can go
            if(adventure.isFinished()){
                break;
            }

            System.out.println("Player 2 turn: ");
            adventure.handleInput(player2, scanner.nextLine());

            adventure.update();
        }

        System.out.println("\n" + adventure.getResult());
    }
}