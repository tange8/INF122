package main.scenarios;

import main.*;

public class HazardScenario extends Scenario {

    public HazardScenario() {
        super("TRAP!",
                "A pressure plate shifts beneath your foot.\n" +
                        "A grinding of stone — then a hail of crossbow bolts erupts from the walls.");
        addOption("Raise your shield and brace.");
        addOption("Dive and roll clear.");
        addOption("Conjure a barrier to deflect the bolts.");
    }

    @Override
    public ScenarioPrompt execute(Player player) {
        setPlayer(player);
        return new ScenarioPrompt(super.getName(), super.getDescription(), super.getOptions());
    }

    @Override
    public ScenarioOutcome resolve(int choice) {
        switch (choice) {
            case 1: return resolveBrace();
            case 2: return resolveDodge();
            case 3: return resolveBarrier();
            default: return null;
        }
    }

    // Option 1 — Brace
    // Warriors have a shield and the constitution to use it well.
    // Others take a glancing hit; they have nothing to hide behind.
    private ScenarioOutcome resolveBrace() {
        if (activePlayer.getPlayerClass().getType() == PlayerClass.Type.WARRIOR) {
            String message =
                    "You throw your shield arm up and lock your stance.\n" +
                            "Bolts hammer the metal with a deafening clatter, and then silence.\n" +
                            "You lower your shield. A few scratches. Nothing more.";
            return new ScenarioOutcome(20, 5, 0, message, loot, 0);
        }
        String message =
                "You throw your arms up — it isn't much of a shield.\n" +
                        "Several bolts find their mark before the volley ends.\n" +
                        "You stagger forward, worse for wear.";
        return new ScenarioOutcome(5, 25, 0, message, loot, 0);
    }

    // Option 2 — Dodge
    // Rogues are built for exactly this — they slip through clean.
    // Warriors are too slow in their armour and catch more than they dodge.
    // Others get a partial dodge.
    private ScenarioOutcome resolveDodge() {
        if (activePlayer.getPlayerClass().getType() == PlayerClass.Type.ROGUE) {
            String message =
                    "Time slows. You read the angles in an instant and throw yourself sideways.\n" +
                            "Bolts shear the air where you stood a heartbeat ago.\n" +
                            "You land in a crouch, unmarked and already moving.";
            return new ScenarioOutcome(30, 0, 0, message, loot, 0);
        }
        if (activePlayer.getPlayerClass().getType() == PlayerClass.Type.WARRIOR) {
            String message =
                    "You lunge sideways, but your armour slows the turn.\n" +
                            "The bulk of the volley finds you anyway.\n" +
                            "You pull a bolt from your pauldron with a grunt.";
            return new ScenarioOutcome(5, 30, 0, message, loot, 0);
        }
        // Mage
        String message =
                "You fling yourself to one side — not graceful, but effective enough.\n" +
                        "Most bolts miss. One clips your shoulder on the way past.";
        return new ScenarioOutcome(10, 15, 0, message, loot, 0);
    }

    // Option 3 — Conjure a barrier
    // Mages snap a barrier up effortlessly and walk through unscathed.
    // Others try to will one into being — it doesn't work that way.
    private ScenarioOutcome resolveBarrier() {
        if (activePlayer.getPlayerClass().getType() == PlayerClass.Type.MAGE) {
            String message =
                    "A word of power, a sweep of the hand — a shimmering wall blooms before you.\n" +
                            "The bolts shatter against it like kindling.\n" +
                            "You release the spell and step through the settling dust without a scratch.";
            return new ScenarioOutcome(30, 0, 0, message, loot, 0);
        }
        String message =
                "You are a " + activePlayer.getPlayerClass().toString() + ". You raise your hands anyway.\n" +
                        "Nothing happens. The bolts do not get the memo.\n" +
                        "You pay for that moment of hopeful stillness.";
        return new ScenarioOutcome(0, 35, 0, message, loot, 0);
    }
}
