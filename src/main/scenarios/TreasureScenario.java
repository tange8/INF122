package main.scenarios;

import main.*;

public class TreasureScenario extends Scenario {

    public TreasureScenario() {
        super("TREASURE CHEST",
                "An ornate chest sits against the wall, iron-banded and heavy with promise.\n" +
                        "A tarnished lock holds it shut. The air around it feels faintly... watchful.");
        addOption("Pick the lock.");
        addOption("Smash it open.");
        addOption("Examine it carefully for magical traps before opening.");
        addOption("Use a key.");
    }

    @Override
    public ScenarioPrompt execute(Player player) {
        setPlayer(player);
        return new ScenarioPrompt(super.getName(), super.getDescription(), super.getOptions());
    }

    @Override
    public ScenarioOutcome resolve(int choice) {
        switch (choice) {
            case 1: return resolvePick();
            case 2: return resolveSmash();
            case 3: return resolveExamine();
            case 4: return resolveKey();
            default: return null;
        }
    }
    
    private Item findKey() {
        for (Item item : activePlayer.getInventoryList()) {
            if (item.getName().equalsIgnoreCase("Key")) {
                return item;
            }
        }
        return null;
    }

    private ScenarioOutcome resolvePick() {
        if (activePlayer.getPlayerClass().getType() == PlayerClass.Type.ROGUE) {
            String message =
                    "You crouch, pull two slender tools from your sleeve, and listen.\n" +
                            "Three pins. Click, click, click. The lid swings open without a sound.\n" +
                            "Inside, gold — and beneath a false bottom, a little more besides.";
            return new ScenarioOutcome(30, 0, 0, message, loot, 80);
        }
        if (activePlayer.getPlayerClass().getType() == PlayerClass.Type.WARRIOR) {
            String message =
                    "You jam a knife into the lock and lever it around.\n" +
                            "A spring-loaded needle punches through the keyhole and into your thumb.\n" +
                            "Whatever was in the chest spills out as the lid flies open — mostly dust.";
            return new ScenarioOutcome(0, 10, 0, message, loot, 10);
        }
        String message =
                "You work at the lock with a hairpin and a great deal of patience.\n" +
                        "Eventually it gives. Inside: a respectable haul of coin.";
        return new ScenarioOutcome(15, 0, 0, message, loot, 40);
    }

    private ScenarioOutcome resolveSmash() {
        if (activePlayer.getPlayerClass().getType() == PlayerClass.Type.WARRIOR) {
            String message =
                    "You raise your weapon and bring it down with everything you have.\n" +
                            "The lid caves in with a satisfying crack. Gold coin spills across the floor.\n" +
                            "You scoop it up, not bothering to count.";
            return new ScenarioOutcome(25, 0, 0, message, loot, 70);
        }
        if (activePlayer.getPlayerClass().getType() == PlayerClass.Type.ROGUE) {
            String message =
                    "You slam your shoulder into the lid. It dents but holds.\n" +
                            "Cursing quietly, you pull out your picks and do it the right way.\n" +
                            "The chest opens. The haul is decent, if not what it could have been.";
            return new ScenarioOutcome(10, 5, 0, message, loot, 35);
        }
        // Mage
        String message =
                "You hurl yourself at the chest. It is not impressed.\n" +
                        "It rebounds you hard off the lid. You land on your back, staring at the ceiling.\n" +
                        "The chest sits there, intact, judging you.";
        return new ScenarioOutcome(0, 15, 0, message, loot, 0);
    }

    private ScenarioOutcome resolveExamine() {
        if (activePlayer.getPlayerClass().getType() == PlayerClass.Type.MAGE) {
            String message =
                    "You pass your hand slowly over the lid. There — a binding glyph etched into\n" +
                            "the inner face of the lock plate. You unravel it in seconds.\n" +
                            "The chest opens to reveal its contents completely undisturbed.";
            return new ScenarioOutcome(30, 0, 0, message, loot, 70);
        }
        if (activePlayer.getPlayerClass().getType() == PlayerClass.Type.WARRIOR) {
            String message =
                    "You squint at the chest for a moment, then open it.\n" +
                            "Nothing explodes. You count that as a thorough examination.\n" +
                            "Inside: a solid pile of gold.";
            return new ScenarioOutcome(15, 0, 0, message, loot, 50);
        }
        // Rogue
        String message =
                "You scan the chest's edges for trip-wires and press your ear to the lid.\n" +
                        "You don't find the glyph, but you find the lock — and that's enough.\n" +
                        "A small flash of light singes your eyebrows as it opens. Worth it.";
        return new ScenarioOutcome(15, 10, 0, message, loot, 55);
    }

    private ScenarioOutcome resolveKey() {
        Item key = findKey();
        if (key != null) {
            activePlayer.removeItemFromInventory(key);
            String message =
                    "You produce a key from your pack and slide it into the lock.\n" +
                            "The tumblers turn without resistance. The lid swings open with a contented click.\n" +
                            "Inside: a gleaming trove of coin and valuables, undisturbed and fully intact.";
            return new ScenarioOutcome(35, 0, 0, message, loot, 100);
        }
        String message =
                "You rummage through your pack — then your pockets — then your pack again.\n" +
                        "No key. You stand there empty-handed while the chest waits, unhelpfully locked.\n" +
                        "You'll have to try something else.";
        return new ScenarioOutcome(0, 0, 0, message, loot, 0);
    }
}
