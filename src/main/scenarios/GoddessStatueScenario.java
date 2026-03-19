package main.scenarios;

import java.util.ArrayList;

import main.Item;
import main.Player;
import main.Scenario;
import main.ScenarioOutcome;
import main.ScenarioPrompt;

public class GoddessStatueScenario extends Scenario {

    public GoddessStatueScenario(){
        super("GODDESS STATUE",
                "A serene statue hums with ancient magic. The NPC seems calmer near it.");
        addOption("Pray for protection (help the NPC).");
        addOption("Offer gold for guidance.");
        addOption("Take a small relic from the altar.");
    }

    @Override
    public ScenarioPrompt execute(Player player) {
        setPlayer(player);
        return new ScenarioPrompt(super.getName(), super.getDescription(), super.getOptions());
    }

    @Override
    public ScenarioOutcome resolve(int choice){
        return switch (choice) {
            case 1 -> resolvePray();
            case 2 -> resolveOfferGold();
            case 3 -> resolveRelic();
            default -> null;
        };
    }

    private ScenarioOutcome resolvePray() {
        String msg = "You whisper a prayer. A warm light surrounds the NPC.\n"
                + "It feels like the next danger will be easier to survive.";
        // heal NPC a bit (negative npcDamage)
        return new ScenarioOutcome(15, 0, -10, msg, loot, 0);
    }

    private ScenarioOutcome resolveOfferGold() {
        if (activePlayer.getGold() >= 20) {
            String msg = "You place a small offering. The air grows still.\n"
                    + "You feel more confident about the path ahead.";
            return new ScenarioOutcome(25, -10, 0, msg, loot, -20);
        }
        String msg = "You search your pockets, but don't have enough to offer.\n"
                + "Nothing happens.";
        return new ScenarioOutcome(0, 0, 0, msg, loot, 0);
    }

    private ScenarioOutcome resolveRelic() {
        ArrayList<Item> found = new ArrayList<>();
        found.add(new Item("SmallRelic", "A relic taken from the statue. It radiates faint warmth.", "rare"));
        String msg = "You take a small relic. The statue's glow dims.\n"
                + "You gained an item, but you feel a slight sting of guilt.";
        return new ScenarioOutcome(20, 5, 0, msg, found, 0);
    }
}
