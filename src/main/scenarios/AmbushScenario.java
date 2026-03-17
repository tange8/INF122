package main.scenarios;

import main.*;

public class AmbushScenario extends Scenario {

    public AmbushScenario(){
        super("AMBUSH",
                "Bandits approach from all sides.");
        addOption("Unsheathe your weapons and fight.");
        addOption("Bargain with the bandits.");
        addOption("SMOKEBOMB!");
    }

    @Override
    public ScenarioPrompt execute(Player player) {
        setPlayer(player);
        return new ScenarioPrompt(super.getName(), super.getDescription(), super.getOptions());
    }

    @Override
    public ScenarioOutcome resolve(int choice){
        switch(choice){
            case 1:
                return resolveFight();
            case 2:
                return resolveBargain();
            case 3:
                return resolveBomb();
            default: return null;
        }
    }

    private ScenarioOutcome resolveFight(){
        if (activePlayer.getPlayerClass().getType() == PlayerClass.Type.WARRIOR) {
            String message = "You skillfully weave through the bandits, slaying on after the other.\n" +
                    "In the aftermath, you notice a few gashes in your arm,\n" +
                    "immediately distracted by the bag of gold next to one of the bandits.";
            return new ScenarioOutcome(30, 5, 0, message, loot, 50);
        }
        String message = "You find yourself overwhelmed by their numbers and suffer great injuries\n" +
                "before managing to scurry away.";
        return new ScenarioOutcome(5, 30, 10, message, loot, 0);
    }

    private ScenarioOutcome resolveBargain(){
        if (activePlayer.getGold() >= 50) {
            String message = "Asking for mercy, you wave a bag of gold around.\n" +
                    "Pocketing the bag, they let you go free.";
            return new ScenarioOutcome(20, 0, 0, message, loot, -50);
        }
        String message = "Asking for mercy, you wave a bag of gold around.\n" +
                "One bandit picks up the bag, weighing it in his hand. \"You must think us saints.\"\n" +
                "Overwhelmed by their numbers, you scurry away with major injuries and no gold.";
        return new ScenarioOutcome(0, 30, 20, message, loot, -(activePlayer.getGold()));
    }

    private ScenarioOutcome resolveBomb(){
        if (activePlayer.getPlayerClass().getType() == PlayerClass.Type.ROGUE) {
            String message = "You slyly reach into your gadget pouch and pull out a black sphere.\n" +
                    "Throwing it to the ground, the sphere erupts into a burst of smoke.\n" +
                    "You escape unscathed, pocketing a bag of gold you stole on the way out.";
            return new ScenarioOutcome(30, 0, 0, message, loot, 50);
        }
        String message = "You are a " + activePlayer.getPlayerClass().toString() + ". You have no such thing.\n" +
                "In that moment of contemplation, the bandits grow impatient and begin their assault.\n" +
                "You escape, but suffer major injuries in return.";
        return new ScenarioOutcome(0, 30, 20, message, loot, -(activePlayer.getGold()));
    }
}
