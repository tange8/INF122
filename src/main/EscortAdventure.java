package main;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import main.scenarios.AmbushScenario;
import main.scenarios.FallingRocksScenario;
import main.scenarios.MedicalSuppliesScenario;

public class EscortAdventure implements MiniAdventure {

    private static final int ROWS = 8;
    private static final int COLS = 8;

    private final String name = "Escort Adventure";

    private AdventureMap map;
    private Player p1;
    private Player p2;

    private int npcMaxHealth;
    private int npcHealth;

    private Status status;
    private String lastMessage;

    private final Map<Point, Scenario> p1Scenarios = new HashMap<>();
    private final Map<Point, Scenario> p2Scenarios = new HashMap<>();
    private final Set<Point> clearedP1 = new HashSet<>();
    private final Set<Point> clearedP2 = new HashSet<>();

    private Scenario activeScenario;
    private ScenarioPrompt activePrompt;
    private Point activeScenarioPoint;

    private boolean hasItem(Player player, String itemName) {
        if (player == null || itemName == null) return false;
        for (Item item : player.getInventoryList()) {
            if (item != null && itemName.equals(item.getName())) return true;
        }
        return false;
    }

    private boolean consumeItem(Player player, String itemName) {
        if (player == null || itemName == null) return false;
        for (Item item : player.getInventoryList()) {
            if (item != null && itemName.equals(item.getName())) {
                player.removeItemFromInventory(item);
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void initialize(Player P1, Player P2) {
        this.p1 = P1;
        this.p2 = P2;
        this.npcMaxHealth = 100;
        this.npcHealth = npcMaxHealth;
        this.status = Status.INIT;
        this.lastMessage = "";

        p1Scenarios.clear();
        p2Scenarios.clear();
        clearedP1.clear();
        clearedP2.clear();
        activeScenario = null;
        activePrompt = null;
        activeScenarioPoint = null;

        setup();

        // Party starts together
        map.placePlayer(1, 1, 1);
        map.placePlayer(2, 1, 1);
        status = Status.P1MOVE;
    }

    @Override
    public void setup() {
        map = new AdventureMap(ROWS, COLS);

        // Border walls
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (r == 0 || r == ROWS - 1 || c == 0 || c == COLS - 1) {
                    map.setTileType(r, c, TileType.WALL);
                }
            }
        }

        // Interior walls (simple maze, similar style to DungeonEscapeAdventure)
        int[][] walls = {
                {1, 4}, {2, 3}, {2, 6},
                {3, 1}, {3, 5}, {4, 4}, {5, 2},
                {5, 6}, {6, 2}, {6, 4}
        };
        for (int[] w : walls) map.setTileType(w[0], w[1], TileType.WALL);

        // Optional helpful items (keys) on the map
        map.setTileType(1, 5, TileType.KEY);
        map.setTileType(5, 1, TileType.KEY);

        // Objective
        map.setTileType(6, 6, TileType.OBJECTIVE);

        // Scenario tiles: each coordinate has a P1 scenario and a P2 scenario
        addScenarioTile(2, 5, new FallingRocksScenario(), new MedicalSuppliesScenario());
        addScenarioTile(3, 3, new AmbushScenario(), new FallingRocksScenario());
        addScenarioTile(4, 2, new MedicalSuppliesScenario(), new AmbushScenario());
        addScenarioTile(5, 3, new FallingRocksScenario(), new AmbushScenario());
    }

    private void addScenarioTile(int row, int col, Scenario p1Scenario, Scenario p2Scenario) {
        map.setTileType(row, col, TileType.SCENARIO);
        Point pt = new Point(row, col);
        p1Scenarios.put(pt, p1Scenario);
        p2Scenarios.put(pt, p2Scenario);
    }

    @Override
    public String currentState() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(getName()).append(" ===\n");
        sb.append(AdventureMap.legend());
        sb.append(map.render()).append("\n");

        Tile partyTile = map.findPlayer(1);
        if (partyTile != null) {
            sb.append("Party position: (").append(partyTile.getRow()).append(", ").append(partyTile.getCol()).append(")\n");
        }
        sb.append("P1 HP: ").append(p1.getHealth()).append("/").append(p1.getMaxHealth())
                .append(" | Gold: ").append(p1.getGold()).append(" | Score: ").append(p1.getScore()).append("\n");
        sb.append("P2 HP: ").append(p2.getHealth()).append("/").append(p2.getMaxHealth())
                .append(" | Gold: ").append(p2.getGold()).append(" | Score: ").append(p2.getScore()).append("\n");
        sb.append("NPC HP: ").append(npcHealth).append("/").append(npcMaxHealth).append("\n\n");

        if (lastMessage != null && !lastMessage.isBlank()) {
            sb.append(lastMessage).append("\n\n");
        }

        switch (status) {
            case P1MOVE -> sb.append("P1: enter a move direction (N/S/E/W)\n");
            case P2MOVE -> sb.append("P2: enter a move direction (N/S/E/W)\n");
            case P1RESPONSE, P2RESPONSE -> sb.append("Enter a choice number for the current scenario.\n");
            case END -> sb.append("Adventure complete.\n");
            default -> sb.append("Initializing...\n");
        }

        return sb.toString();
    }

    @Override
    public ScenarioPrompt processInput(String input) {
        if (status == null) return null;
        if (status == Status.END) return null;
        if (input == null) input = "";
        input = input.trim();

        return switch (status) {
            case P1MOVE -> handleMove(1, input);
            case P2MOVE -> handleMove(2, input);
            case P1RESPONSE -> handleResponse(1, input);
            case P2RESPONSE -> handleResponse(2, input);
            case P1INIT -> null;
            case INIT, END -> null;
        };
    }

    private ScenarioPrompt handleMove(int activePlayerNum, String input) {
        if (!isDirection(input)) {
            lastMessage = "Invalid move. Use N, S, E, or W.";
            return null;
        }

        Player activePlayer = (activePlayerNum == 1) ? p1 : p2;

        try {
            Tile newTile = map.movePlayer(1, input);
            if (newTile == null) {
                lastMessage = "You can't move right now.";
                return null;
            }
            map.placePlayer(2, newTile.getRow(), newTile.getCol());
        } catch (IllegalArgumentException ex) {
            lastMessage = "That move is blocked (" + ex.getMessage() + ").";
            return null;
        }

        // Tile-side effects: keys become items that can help
        applyKeyIfPresent(activePlayer);

        if (checkWinOrLose()) {
            status = Status.END;
            return null;
        }

        // Scenario check for this active player
        Tile partyTile = map.findPlayer(1);
        Point pt = new Point(partyTile.getRow(), partyTile.getCol());
        Scenario scenario = (activePlayerNum == 1) ? p1Scenarios.get(pt) : p2Scenarios.get(pt);
        boolean cleared = (activePlayerNum == 1) ? clearedP1.contains(pt) : clearedP2.contains(pt);

        if (scenario != null && !cleared) {
            activeScenario = scenario;
            activeScenarioPoint = pt;
            activePrompt = scenario.execute(activePlayer);
            status = (activePlayerNum == 1) ? Status.P1RESPONSE : Status.P2RESPONSE;
            lastMessage = "";
            return activePrompt;
        }

        status = (activePlayerNum == 1) ? Status.P2MOVE : Status.P1MOVE;
        lastMessage = "";
        return null;
    }

    private void applyKeyIfPresent(Player activePlayer) {
        Tile partyTile = map.findPlayer(1);
        if (partyTile == null) return;

        if (partyTile.getType() != TileType.KEY || partyTile.isCleared()) return;

        Item keyItem = new Item("DungeonKey", "A simple key found during the escort. Might help later.", "common");
        activePlayer.addItemToInventory(keyItem);
        activePlayer.addScore(5);
        partyTile.setCleared(true);
        lastMessage = "You found a key item and added it to your inventory.";
    }

    private ScenarioPrompt handleResponse(int activePlayerNum, String input) {
        if (activeScenario == null || activePrompt == null || activeScenarioPoint == null) {
            lastMessage = "No active scenario to resolve.";
            status = (activePlayerNum == 1) ? Status.P2MOVE : Status.P1MOVE;
            return null;
        }

        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            lastMessage = "Invalid choice. Enter a number.";
            return activePrompt;
        }

        ScenarioOutcome outcome = activeScenario.resolve(choice);
        if (outcome == null) {
            lastMessage = "That choice isn't valid. Try again.";
            return activePrompt;
        }

        Player activePlayer = (activePlayerNum == 1) ? p1 : p2;

        int playerDamageEff = Math.max(0, outcome.getPlayerDamage());
        int npcDamageEff = Math.max(0, outcome.getNpcDamage());
        StringBuilder itemMsg = new StringBuilder();

        // Passive item effects (active player only)
        if (playerDamageEff + npcDamageEff >= 25 && consumeItem(activePlayer, "SmokeBomb")) {
            playerDamageEff = 0;
            npcDamageEff = 0;
            itemMsg.append("SmokeBomb triggers and negates the worst of the danger.\n");
        } else if (npcDamageEff > 0 && consumeItem(activePlayer, "ShieldToken")) {
            int reduced = Math.min(10, npcDamageEff);
            npcDamageEff -= reduced;
            itemMsg.append("ShieldToken triggers and protects the NPC.\n");
        }

        applyOutcome(activePlayer, outcome, playerDamageEff, npcDamageEff, itemMsg.toString());

        // Mark this player's scenario as cleared at this tile.
        if (activePlayerNum == 1) clearedP1.add(activeScenarioPoint);
        else clearedP2.add(activeScenarioPoint);

        // If both player-scenarios at this tile are cleared, mark tile visually cleared.
        if (clearedP1.contains(activeScenarioPoint) && clearedP2.contains(activeScenarioPoint)) {
            Tile tile = map.getTile(activeScenarioPoint.x, activeScenarioPoint.y);
            if (tile != null) tile.setCleared(true);
        }

        activeScenario = null;
        activePrompt = null;
        activeScenarioPoint = null;

        if (checkWinOrLose()) {
            status = Status.END;
            return null;
        }

        status = (activePlayerNum == 1) ? Status.P2MOVE : Status.P1MOVE;
        return null;
    }

    private void applyOutcome(Player activePlayer, ScenarioOutcome outcome, int playerDamageEff, int npcDamageEff, String itemMessage) {
        StringBuilder msg = new StringBuilder();
        if (outcome.getOutcomeMessage() != null && !outcome.getOutcomeMessage().isBlank()) {
            msg.append(outcome.getOutcomeMessage()).append("\n");
        }
        if (itemMessage != null && !itemMessage.isBlank()) {
            msg.append(itemMessage);
        }

        activePlayer.addScore(outcome.getScoreAwarded());
        if (hasItem(activePlayer, "SmallRelic")) {
            activePlayer.addScore(5);
            msg.append("SmallRelic grants a small blessing.\n");
        }
        activePlayer.addGold(outcome.getGoldAward());

        applyPlayerHealthDelta(activePlayer, -playerDamageEff);
        applyNpcHealthDelta(-npcDamageEff);

        if (playerDamageEff > 0 && consumeItem(activePlayer, "Bandage")) {
            applyPlayerHealthDelta(activePlayer, 5);
            msg.append("Bandage is consumed to patch you up.\n");
        }

        if (activePlayer.getHealth() < 25 && consumeItem(activePlayer, "Medkit")) {
            applyPlayerHealthDelta(activePlayer, 20);
            msg.append("Medkit is consumed to stabilize you.\n");
        }

        if (outcome.getLoot() != null) {
            for (Item item : outcome.getLoot()) {
                if (item != null) activePlayer.addItemToInventory(item);
            }
        }

        lastMessage = msg.toString().trim();
    }

    private void applyPlayerHealthDelta(Player player, int delta) {
        int current = player.getHealth();
        int next = current + delta;
        if (next > player.getMaxHealth()) delta = player.getMaxHealth() - current;
        if (next < 0) delta = -current;
        player.addHealth(delta);
    }

    private void applyNpcHealthDelta(int delta) {
        int next = npcHealth + delta;
        if (next > npcMaxHealth) npcHealth = npcMaxHealth;
        else npcHealth = Math.max(0, next);
    }

    private boolean checkWinOrLose() {
        if (p1.getHealth() <= 0 || p2.getHealth() <= 0) {
            lastMessage = "A player has fallen. The escort failed.";
            return true;
        }
        if (npcHealth <= 0) {
            lastMessage = "The NPC has fallen. The escort failed.";
            return true;
        }

        Tile partyTile = map.findPlayer(1);
        if (partyTile != null && partyTile.getType() == TileType.OBJECTIVE) {
            lastMessage = "You successfully escorted the NPC to safety!";
            return true;
        }

        return false;
    }

    private boolean isDirection(String input) {
        if (input == null) return false;
        return switch (input.toUpperCase()) {
            case "N", "S", "E", "W" -> true;
            default -> false;
        };
    }
}
