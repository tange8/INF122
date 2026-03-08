public interface MiniAdventure{


    void startGame(Player 1, Player2);

    void handleInput(Player player, String command);

    void update();

    String getCurrentState();

    boolean isFinished();

    String getResult();

    void reset();
}