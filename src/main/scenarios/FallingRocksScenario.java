package main.scenarios;

import java.util.ArrayList;

import main.Item;
import main.Player;
import main.Scenario;
import main.ScenarioOutcome;
import main.ScenarioPrompt;

public class FallingRocksScenario extends Scenario {

    public FallingRocksScenario() {
        super("FALLING ROCKS", "The ceiling cracks. Rocks begin to fall toward the party and the NPC!");
        addOption("Shield the NPC (take damage).");
        addOption("Sprint through (risk the NPC).");
        addOption("Look for a safer route (slow but safer).");
    }

    @Override
    public ScenarioPrompt execute(Player player) {
        setPlayer(player);
        return new ScenarioPrompt(getName(), getDescription(), getOptions());
    }

    @Override
    public ScenarioOutcome resolve(int choice) {
        return switch (choice) {
            case 1 -> resolveShieldNpc();
            case 2 -> resolveSprint();
            case 3 -> resolveSaferRoute();
            default -> null;
        };
    }

    private ScenarioOutcome resolveShieldNpc() {
        String msg = "You put yourself between the falling rocks and the NPC.\n"
                + "You get battered, but the NPC is mostly safe.";
        return new ScenarioOutcome(20, 15, 2, msg, loot, 0);
    }

    private ScenarioOutcome resolveSprint() {
        String msg = "You sprint forward and try to pull the NPC along.\n"
                + "You avoid most of the damage, but the NPC takes a heavy hit.";
        return new ScenarioOutcome(10, 5, 20, msg, loot, 0);
    }

    private ScenarioOutcome resolveSaferRoute() {
        ArrayList<Item> found = new ArrayList<>();
        found.add(new Item("Bandage", "Restores a little health later.", "common"));
        String msg = "You slow down and find a route with fewer cracks.\n"
                + "It costs time, but you find supplies and avoid serious harm.";
        return new ScenarioOutcome(15, 0, 0, msg, found, 0);
    }
}

