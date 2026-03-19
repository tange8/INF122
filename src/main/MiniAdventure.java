package main;

public interface MiniAdventure {

    String getName();
    void initialize(Player P1, Player P2);
    void setup();
    ScenarioPrompt processInput(String input);
    String currentState();
}
