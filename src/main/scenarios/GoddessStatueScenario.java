package main.scenarios;

import main.*;

public class GoddessStatueScenario extends Scenario {

    public GoddessStatueScenario() {
        super("GODDESS STATUE",
                "A stone idol of a goddess rises from the dungeon floor,\n" +
                        "her outstretched palms worn smooth by countless offerings.\n" +
                        "A faint warmth radiates from her carved eyes.");
        addOption("Place gold in her palms as an offering.");
        addOption("Kneel and offer a prayer.");
        addOption("Chip away at the statue and pocket the gemstone eyes.");
    }

    @Override
    public ScenarioPrompt execute(Player player) {
        setPlayer(player);
        return new ScenarioPrompt(super.getName(), super.getDescription(), super.getOptions());
    }

    @Override
    public ScenarioOutcome resolve(int choice) {
        switch (choice) {
            case 1: return resolveOffering();
            case 2: return resolvePrayer();
            case 3: return resolveDesecrate();
            default: return null;
        }
    }

    // Option 1 — Offer gold
    // Mages carry divine texts and know the proper rites, earning a full blessing.
    // Anyone else with gold gets a modest heal. Too poor? Nothing.
    private ScenarioOutcome resolveOffering() {
        if (activePlayer.getPlayerClass().getType() == PlayerClass.Type.MAGE) {
            String message =
                    "You recite the ancient rite of offering from memory, placing gold in her palms\n" +
                            "with practiced reverence. The statue's eyes flare golden.\n" +
                            "A wave of warmth washes over you, knitting your wounds closed.";
            return new ScenarioOutcome(25, -30, 0, message, loot, -30);
        }
        if (activePlayer.getGold() >= 30) {
            String message =
                    "You drop a handful of coins into the outstretched palms.\n" +
                            "The warmth intensifies briefly, and you feel a little steadier on your feet.";
            return new ScenarioOutcome(15, -15, 0, message, loot, -30);
        }
        String message =
                "You search your pockets. Not a coin to spare.\n" +
                        "The statue stares forward, unmoved. She expects more than good intentions.";
        return new ScenarioOutcome(0, 0, 0, message, loot, 0);
    }

    // Option 2 — Pray
    // Mages commune with the divine easily and earn a generous reward.
    // Warriors show rare humility and earn a small blessing.
    // Rogues are too faithless — nothing happens.
    private ScenarioOutcome resolvePrayer() {
        if (activePlayer.getPlayerClass().getType() == PlayerClass.Type.MAGE) {
            String message =
                    "You close your eyes and reach out with your mind.\n" +
                            "The goddess answers. Light fills the room, and you feel your strength renewed,\n" +
                            "a pouch of gold coins materializing at the statue's base.";
            return new ScenarioOutcome(30, -25, 0, message, loot, 40);
        }
        if (activePlayer.getPlayerClass().getType() == PlayerClass.Type.WARRIOR) {
            String message =
                    "You remove your helm and take a knee, though the words feel foreign.\n" +
                            "The warmth lingers a moment longer than expected.\n" +
                            "It isn't much, but it's something.";
            return new ScenarioOutcome(10, -10, 0, message, loot, 0);
        }
        // Rogue
        String message =
                "You bow your head, one eye still open.\n" +
                        "The statue does not move. The goddess, it seems, knows a thief when she sees one.";
        return new ScenarioOutcome(0, 0, 0, message, loot, 0);
    }

    // Option 3 — Desecrate
    // Warriors can pry the gems out cleanly and escape before the punishment lands.
    // Others fumble it — the goddess's wrath finds them.
    private ScenarioOutcome resolveDesecrate() {
        if (activePlayer.getPlayerClass().getType() == PlayerClass.Type.WARRIOR) {
            String message =
                    "You draw your blade and lever the gemstones free with a practiced flick.\n" +
                            "The room shudders. You don't wait to see what happens next.\n" +
                            "By the time the statues crack rends the air, you're already gone — gems in hand.";
            return new ScenarioOutcome(20, 10, 0, message, loot, 80);
        }
        String message =
                "You dig at the statue's eyes with your fingers.\n" +
                        "The stone begins to tremble. A crack splits the base.\n" +
                        "A pulse of divine force hurls you across the room.\n" +
                        "You wake on the floor. The eyes are gone. So is your dignity.";
        return new ScenarioOutcome(0, 35, 0, message, loot, 0);
    }
}
