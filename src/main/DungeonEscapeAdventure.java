package main;

import java.awt.*;
import java.util.HashMap;
import main.scenarios.*;

public class DungeonEscapeAdventure implements MiniAdventure{

    private static final int ROWS = 8;
    private static final int COLS = 8;

    public enum status {
        INIT,
        P1MOVE,
        P2MOVE,
        P1RESPONSE,
        P2RESPONSE,
        END,
    }

    private status currentStatus;

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
        currentStatus = status.INIT;
        key1Collected = false;
        key2Collected = false;
        complete = false;
        setup();
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

        return;
    }

    @Override
    public String currentState() {
        return "";
    }

    @Override
    public ScenarioPrompt processInput(String input) {
        return null;
    }

    public ScenarioPrompt processMoveTile(String input) {
        return null;
    }
}
