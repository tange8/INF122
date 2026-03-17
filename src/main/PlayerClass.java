package main;

public class PlayerClass {
    public enum Type {
        WARRIOR,
        MAGE,
        ROGUE
    }

    private Type type;

    public PlayerClass(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String toString() {
        return type.toString().toLowerCase();
    }
}
