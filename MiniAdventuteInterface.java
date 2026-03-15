public interface MiniAdventure{

    //Initialize the game with two players
    void startGame(Player player1, Player player2);

    //Players will input a command and based off what is entered a certain action will occur
    void handleInput(Player player, String command);

    //updates the gameplay
    void update();

    //gets the current state of the gameplay
    String getCurrentState();

    //checks to see if anyone has won
    boolean isFinished();

    //returns result of the minigame
    String getResult();

    //reset so that game can be played again
    void reset();
}