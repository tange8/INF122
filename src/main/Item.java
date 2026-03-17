package main;

public class Item {
    private String name;
    private String description;
    private String rarity;

    public Item(String name, String description, String rarity) {
        this.name = name;
        this.description = description;
        this.rarity = rarity;
    }

    // getters
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getRarity() {
        return rarity;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setRarity(String rarity) {
        this.rarity = rarity;
    }
}
