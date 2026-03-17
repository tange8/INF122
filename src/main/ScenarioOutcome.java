package main;

import java.util.ArrayList;

public class ScenarioOutcome {

    private int scoreAwarded = 0;
    private int playerDamage = 0;
    private int npcDamage = 0;
    private String outcomeMessage;
    private ArrayList<Item> loot;
    private int goldAward;

    public ScenarioOutcome(int scoreAwarded, int playerDamage, int npcDamage, String outcomeMessage, ArrayList<Item> loot, int goldAward) {
        this.scoreAwarded = scoreAwarded;
        this.playerDamage = playerDamage;
        this.npcDamage = npcDamage;
        this.outcomeMessage = outcomeMessage;
        this.loot = loot;
        this.goldAward = goldAward;
    }
}
