package main.scenarios;

import java.util.ArrayList;

import main.Item;
import main.Player;
import main.Scenario;
import main.ScenarioOutcome;
import main.ScenarioPrompt;

public class MedicalSuppliesScenario extends Scenario {

    public MedicalSuppliesScenario() {
        super("MEDICAL SUPPLIES", "You spot a stash of medical supplies tucked behind debris.");
        addOption("Use the supplies now (heal).");
        addOption("Pack them for later (gain an item).");
        addOption("Ignore them and keep moving.");
    }

    @Override
    public ScenarioPrompt execute(Player player) {
        setPlayer(player);
        return new ScenarioPrompt(getName(), getDescription(), getOptions());
    }

    @Override
    public ScenarioOutcome resolve(int choice) {
        return switch (choice) {
            case 1 -> resolveHealNow();
            case 2 -> resolvePack();
            case 3 -> resolveIgnore();
            default -> null;
        };
    }

    private ScenarioOutcome resolveHealNow() {
        String msg = "You quickly patch up wounds and steady the NPC.\n"
                + "The party feels a bit safer.";
        // Negative damage means healing using EscortAdventure's (-playerDamage) logic
        return new ScenarioOutcome(10, -15, -10, msg, loot, 0);
    }

    private ScenarioOutcome resolvePack() {
        ArrayList<Item> found = new ArrayList<>();
        found.add(new Item("Medkit", "Supplies that could help in future scenarios.", "uncommon"));
        String msg = "You pack the supplies into your bag.\n"
                + "This could help if things get worse.";
        return new ScenarioOutcome(10, 0, 0, msg, found, 0);
    }

    private ScenarioOutcome resolveIgnore() {
        String msg = "You decide not to waste time.\n"
                + "The escort continues.";
        return new ScenarioOutcome(0, 0, 0, msg, loot, 0);
    }
}

