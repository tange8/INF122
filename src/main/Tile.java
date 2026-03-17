package main;

public class Tile {
    private TileType type;
    private int row;
    private int col;
    private boolean cleared;
    private boolean hasP1;
    private boolean hasP2;

    public Tile(TileType type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
        this.cleared = false;
        this.hasP1 = false;
        this.hasP2 = false;
    }
    public TileType getType() {
        return type;
    }
    public void setType(TileType type) {
        this.type = type;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public boolean isCleared() {
        return cleared;
    }
    public void setCleared(boolean cleared) {
        this.cleared = cleared;
    }
    public boolean hasP1() {
        return hasP1;
    }
    public void setHasP1(boolean hasP1) {
        this.hasP1 = hasP1;
    }
    public boolean hasP2() {
        return hasP2;
    }
    public void setHasP2(boolean hasP2) {
        this.hasP2 = hasP2;
    }

    public char toChar() {
        if (hasP1 && hasP2) return 'B';
        if (hasP1) return '1';
        if (hasP2) return '2';

        if (cleared) return '.';

        return switch (type) {
            case WALL -> '#';
            case SCENARIO -> '?';
            case OBJECTIVE -> 'o';
            case KEY -> 'K';
            default -> '.';
        };
    }


}
