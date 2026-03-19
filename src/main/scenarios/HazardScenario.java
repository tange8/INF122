package main.scenarios;

import java.util.ArrayList;

import main.Item;
import main.Player;
import main.Scenario;
import main.ScenarioOutcome;
import main.ScenarioPrompt;
import main.PlayerClass;

public class HazardScenario extends Scenario {

    public HazardScenario(){
        super("HAZARD",
                "A trap triggers: darts shoot from the walls as the NPC panics.");
        addOption("Take cover and wait it out.");
        addOption("Disarm the trap quickly.");
        addOption("Protect the NPC and push forward.");
    }

    @Override
    public ScenarioPrompt execute(Player player) {
        setPlayer(player);
        return new ScenarioPrompt(super.getName(), super.getDescription(), super.getOptions());
    }

    @Override
    public ScenarioOutcome resolve(int choice){
        return switch (choice) {
            case 1 -> resolveCover();
            case 2 -> resolveDisarm();
            case 3 -> resolveProtectNpc();
            default -> null;
        };
    }

    private ScenarioOutcome resolveCover() {
        String msg = "You dive behind a pillar and wait for the trap to finish.\n"
                + "You take a few scrapes, but avoid the worst of it.";
        return new ScenarioOutcome(10, 5, 0, msg, loot, 0);
    }

    private ScenarioOutcome resolveDisarm() {
        if (activePlayer.getPlayerClass().getType() == PlayerClass.Type.ROGUE) {
            ArrayList<Item> found = new ArrayList<>();
            found.add(new Item("TrapParts", "Salvaged parts from a trap. Could be useful.", "common"));
            String msg = "You quickly locate the mechanism and disable the trap.\n"
                    + "You even salvage some usable parts.";
            return new ScenarioOutcome(25, 0, 0, msg, found, 0);
        }
        String msg = "You fumble with the mechanism and get hit by darts.\n"
                + "The NPC also takes a hit in the chaos.";
        return new ScenarioOutcome(5, 15, 10, msg, loot, 0);
    }

    private ScenarioOutcome resolveProtectNpc() {
        String msg = "You shield the NPC and force your way through.\n"
                + "The NPC is safe, but you take the brunt of the trap.";
        return new ScenarioOutcome(15, 20, 0, msg, loot, 0);
    }
}
