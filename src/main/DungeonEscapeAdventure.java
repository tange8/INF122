package main;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import main.scenarios.*;

public class DungeonEscapeAdventure implements MiniAdventure{

    private static final int ROWS = 8;
    private static final int COLS = 8;

    private Status currentStatus;

    private Scenario currentScenario;

    private AdventureMap map;
    private HashMap<Point, Scenario> scenarios;

    private Player p1, p2;

    private boolean key1Collected;
    private boolean key2Collected;

    private boolean complete;

    private String name = "Dungeon Escape Adventure";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void initialize(Player P1, Player P2) {
        p1 = P1;
        p2 = P2;
        currentStatus = Status.INIT;
        key1Collected = false;
        key2Collected = false;
        complete = false;
        setup();
    }

    public boolean isComplete() {
        return complete;
    }

    public Status getCurrentStatus() {
        return currentStatus;
    }

    // Map
    //     0  1  2  3  4  5  6  7
    //  0 [#][#][#][#][#][#][#][#]
    //  1 [#][1][2][#][#][.][K][#]
    //  2 [#][.][H][#][.][A][#][#]
    //  3 [#][#][.][G][.][.][H][#]
    //  4 [#][.][H][.][#][#][T][#]
    //  5 [#][K][#][A][.][T][#][#]
    //  6 [#][.][#][.][#][.][O][#]
    //  7 [#][#][#][#][#][#][#][#]
    @Override
    public void setup() {
        map = new AdventureMap(ROWS, COLS);
        scenarios = new HashMap<>();

        // Border walls
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (r == 0 || r == ROWS-1 || c == 0 || c == COLS-1)
                    map.setTileType(r, c, TileType.WALL);
            }
        }

        // Interior maze walls
        int[][] walls = {
                {1,3},{1,4},{2,3},{2,6},
                {3,1},{4,4},{4,5},{5,2},
                {5,6},{6,2},{6,4}
        };
        for (int[] w : walls) map.setTileType(w[0], w[1], TileType.WALL);

        // Keys
        map.setTileType(5, 1, TileType.KEY);
        map.setTileType(1, 5, TileType.KEY);

        // Exit
        map.setTileType(6, 6, TileType.OBJECTIVE);

        // Hazards
        int[][] hazards = {{2,2},{3,6},{4,2}};
        for (int[] h : hazards) {
            map.setTileType(h[0], h[1], TileType.SCENARIO);
            scenarios.put(new Point(h[0], h[1]), new HazardScenario());
        }

        int[][] ambushes = {{2,5},{5,3}};
        for (int[] a : ambushes) {
            map.setTileType(a[0], a[1], TileType.SCENARIO);
            scenarios.put(new Point(a[0], a[1]), new AmbushScenario());
        }

        map.setTileType(3, 3, TileType.SCENARIO);
        scenarios.put(new Point(3, 3), new GoddessStatueScenario());

        map.setTileType(4, 6, TileType.SCENARIO);
        scenarios.put(new Point(4, 6), new TreasureScenario());
        map.setTileType(5, 5, TileType.SCENARIO);
        scenarios.put(new Point(5, 5), new TreasureScenario());

        map.placePlayer(1, 1, 1);
        map.placePlayer(2, 1, 2);

        return;
    }

    public String currentState() {
        if (p1 == null || p2 == null) return "";

        int keysFound = (key1Collected ? 1 : 0) + (key2Collected ? 1 : 0);

        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(map.render());
        sb.append(AdventureMap.legend());
        sb.append(String.format(
                "  %-12s (P1): HP=%3d  Gold=%3d  Score=%3d%n",
                p1.getName(), p1.getHealth(), p1.getGold(), p1.getScore()));
        sb.append(String.format(
                "  %-12s (P2): HP=%3d  Gold=%3d  Score=%3d%n",
                p2.getName(), p2.getHealth(), p2.getGold(), p2.getScore()));
        sb.append(String.format(
                "  Keys collected: %d / 2%n", keysFound));
        return sb.toString();
    }


    @Override
    public ScenarioPrompt processInput(String input) {
        switch (currentStatus) {
            case INIT:
                return handleInit();

            case P1MOVE:
                return handleMove(input, p1, 1);

            case P1RESPONSE:
                return handleResponse(input, p1, Status.P2MOVE);

            case P2MOVE:
                return handleMove(input, p2, 2);

            case P2RESPONSE:
                return handleResponse(input, p2, Status.P1MOVE);

            case END:
                return endPrompt();

            default:
                return null;
        }
    }


    private ScenarioPrompt handleInit() {
        currentStatus = Status.P1MOVE;
        String intro =
                "You and your ally awaken in a dungeon. The exit is sealed.\n" +
                        "Find BOTH keys hidden in the maze, then reach the exit.\n" +
                        "Watch out for hazards, ambushes, and other surprises along the way!\n";
        return movePrompt("P1's First Move", intro, p1);
    }

    private ScenarioPrompt handleMove(String input, Player player, int playerNum) {
        // --- Attempt movement ---
        Tile tile;
        try {
            tile = map.movePlayer(playerNum, input.trim().toUpperCase());
        } catch (IllegalArgumentException e) {

            String problem = e.getMessage().equalsIgnoreCase("wall")
                    ? "There's a wall in that direction. Choose another path."
                    : "Invalid direction. Use N, S, E, or W.";
            return movePrompt("Invalid Move", problem + "\n", player);
        }


        switch (tile.getType()) {
            case KEY:
                return handleKeyTile(tile, player, playerNum);

            case SCENARIO:
                return handleScenarioTile(tile, player, playerNum);

            case OBJECTIVE:
                return handleObjectiveTile(player, playerNum);

            default:

                advanceTurn(playerNum);
                return nextTurnPrompt(playerNum, "");
        }
    }

    private ScenarioPrompt handleKeyTile(Tile tile, Player player, int playerNum) {

        if (!key1Collected) {
            key1Collected = true;
        } else {
            key2Collected = true;
        }
        tile.setType(TileType.EMPTY);
        tile.setCleared(true);

        int keysFound = (key1Collected ? 1 : 0) + (key2Collected ? 1 : 0);
        String msg = String.format(
                "%s found a key! (%d/2 keys collected)%n",
                player.getName(), keysFound);

        advanceTurn(playerNum);
        return nextTurnPrompt(playerNum, msg);
    }

    private ScenarioPrompt handleScenarioTile(Tile tile, Player player, int playerNum) {
        if (tile.isCleared()) {

            advanceTurn(playerNum);
            return nextTurnPrompt(playerNum, "");
        }


        currentScenario = scenarios.get(new Point(tile.getRow(), tile.getCol()));
        if (currentScenario == null) {

            advanceTurn(playerNum);
            return nextTurnPrompt(playerNum, "");
        }

        ScenarioPrompt prompt = currentScenario.execute(player);


        currentStatus = (playerNum == 1) ? Status.P1RESPONSE : Status.P2RESPONSE;
        return prompt;
    }

    private ScenarioPrompt handleObjectiveTile(Player player, int playerNum) {
        if (key1Collected && key2Collected){

            currentStatus = Status.END;
            complete = true;
            p1.reset();
            p2.reset();
            return endPrompt();
        }


        int keysFound = (key1Collected ? 1 : 0) + (key2Collected ? 1 : 0);
        String msg = String.format(
                "The dungeon door is sealed. You need %d more key(s) to escape!%n",
                2 - keysFound);

        advanceTurn(playerNum);
        return nextTurnPrompt(playerNum, msg);
    }

    private ScenarioPrompt handleResponse(String input, Player player, Status nextStatus) {

        int choice;
        try {
            choice = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return retryResponsePrompt("Please enter the number of your choice.");
        }


        if (choice < 1 || choice > currentScenario.getOptions().size()) {
            return retryResponsePrompt(String.format(
                    "Please choose between 1 and %d.", currentScenario.getOptions().size()));
        }


        ScenarioOutcome outcome = currentScenario.resolve(choice);
        if (outcome == null) {
            return retryResponsePrompt("Something went wrong. Try a different choice.");
        }

        applyOutcome(player, outcome);

        Tile tile = map.findPlayer(player == p1 ? 1 : 2);
        if (tile != null) tile.setCleared(true);

        currentStatus = nextStatus;
        currentScenario = null;

        int nextPlayerNum = (nextStatus == Status.P2MOVE) ? 2 : 1;
        String resultMsg = outcome.getOutcomeMessage() + "\n" ;
        return movePrompt("P" + nextPlayerNum + "'s Turn", resultMsg, nextPlayerNum == 1 ? p1 : p2);
    }


    private void applyOutcome(Player player, ScenarioOutcome outcome) {
        if (outcome.getPlayerDamage() > 0) {
            player.addHealth(-outcome.getPlayerDamage());
        }
        if (outcome.getGoldAward() != 0) {
            player.addGold(outcome.getGoldAward());
        }
        if (outcome.getScoreAwarded() > 0) {
            player.addScore(outcome.getScoreAwarded());
        }

        if (outcome.getLoot() != null) {
            for (Item item : outcome.getLoot()) {
                player.addItemToInventory(item);
            }
        }
    }

    private void advanceTurn(int playerNum) {
        currentStatus = (playerNum == 1) ? Status.P2MOVE : Status.P1MOVE;
    }

    private ScenarioPrompt nextTurnPrompt(int completedPlayerNum, String contextMessage) {
        int nextNum = (completedPlayerNum == 1) ? 2 : 1;
        Player nextPlayer = (nextNum == 1) ? p1 : p2;
        String body = contextMessage.isEmpty()
                ? currentState()
                : contextMessage;
        return movePrompt("P" + nextNum + "'s Turn", body, nextPlayer);
    }

    private static final ArrayList<String> MOVE_OPTIONS =
            new ArrayList<>(Arrays.<String>asList("N - Move North", "S - Move South", "E - Move East", "W - Move West"));

    private ScenarioPrompt movePrompt(String title, String description, Player player) {
        return new ScenarioPrompt(
                title + " [" + player.getName() + "]",
                description,
                MOVE_OPTIONS);
    }


    private ScenarioPrompt retryResponsePrompt(String errorMessage) {
        return new ScenarioPrompt(
                currentScenario.getName(),
                errorMessage + "\n" + currentScenario.getDescription(),
                currentScenario.getOptions());
    }

    private ScenarioPrompt endPrompt() {
        int totalScore = p1.getScore() + p2.getScore();
        String summary = String.format(
                "=== DUNGEON ESCAPED! ===%n%n" +
                        "%s and %s have broken free from the dungeon!%n%n" +
                        "  %s — HP: %d  Gold: %d  Score: %d%n" +
                        "  %s — HP: %d  Gold: %d  Score: %d%n%n" +
                        "  Combined Score: %d%n",
                p1.getName(), p2.getName(),
                p1.getName(), p1.getHealth(), p1.getGold(), p1.getScore(),
                p2.getName(), p2.getHealth(), p2.getGold(), p2.getScore(),
                totalScore);
        return new ScenarioPrompt("VICTORY", summary, new ArrayList<>());
    }
}
