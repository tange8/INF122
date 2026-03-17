package main;

import java.awt.*;

public class AdventureMap {
    private Tile[][] grid;
    private int rows, cols;
    private Point p1Pos, p2Pos;

    public AdventureMap(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;

        grid = new Tile[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Tile(TileType.EMPTY, r, c);
            }
        }
    }

    public boolean inBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public Tile getTile(int row, int col) {
        if (!inBounds(row, col)) return null;
        return grid[row][col];
    }
    public void setTileType(int row, int col, TileType type) {
        if (!inBounds(row, col)) {grid[row][col].setType(type);};
    }

    public Tile placePlayer(int playerNum, int row, int col) {
        if (!inBounds(row, col)) throw new IllegalArgumentException("row or col out of bounds");
        Tile dest = grid[row][col];
        if (dest.getType() == TileType.WALL) throw new IllegalArgumentException("wall");

        clearPlayer(playerNum);

        Point newPos = new Point(row, col);
        if (playerNum == 1) {
            dest.setHasP1(true);
            p1Pos = newPos;
        }
        else if (playerNum == 2) {
            dest.setHasP2(true);
            p2Pos = newPos;
        }

        return dest;

    }

    public void clearPlayer(int playerNum) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (playerNum == 1) grid[r][c].setHasP1(false);
                else if (playerNum == 2) grid[r][c].setHasP2(false);
            }
        }
    }

    public Tile findPlayer(int playerNum) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Tile tile = grid[r][c];
                if (playerNum == 1 && tile.hasP1()) return tile;
                if (playerNum == 2 && tile.hasP2()) return tile;
            }
        }
        return null;
    }

    public Tile movePlayer(int playerNum, String direction) {
        Tile current = findPlayer(playerNum);
        if (current == null) return null;
        int r = current.getRow();
        int c = current.getCol();

        switch (direction.toUpperCase()) {
            case "N" -> r--;
            case "S" -> r++;
            case "E" -> c++;
            case "W" -> c--;
            default -> {throw new IllegalArgumentException("invalid direction");}
        }

        return placePlayer(playerNum, r, c);
    }

    public String render() {
        StringBuilder sb = new StringBuilder();

        // Column header
        sb.append("    ");
        for (int c = 0; c < cols; c++) sb.append(String.format(" %-2d", c));
        sb.append("\n");

        for (int r = 0; r < rows; r++) {
            sb.append(String.format("%3d ", r));
            for (int c = 0; c < cols; c++) {
                sb.append("[").append(grid[r][c].toChar()).append("]");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String legend() {
        return "  Legend: [1]=P1  [2]=P2  [B]=Both  [R]=Relic  [?]=Scenario\n"
                + "       [O]=Objective  [K]=Key\n";
    }
}
