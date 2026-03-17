package main.scenarios;

import main.Player;
import main.Scenario;
import main.ScenarioOutcome;
import main.ScenarioPrompt;

public class GoddessStatueScenario extends Scenario {

    public GoddessStatueScenario(){
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
}
