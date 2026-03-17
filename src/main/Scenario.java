package main;

import java.util.ArrayList;

public abstract class Scenario {
    protected String name;
    protected String description;
    protected ArrayList<Item> loot;
    protected Player activePlayer;
    protected ArrayList<String> options;

    public Scenario(String name, String description) {
        this.name = name;
        this.description = description;
        this.loot = new ArrayList<Item>();
        this.activePlayer = null;
        this.options = new ArrayList<String>();
    }

    public void addItem(Item item) {
        loot.add(item);
    }

    public void addOption(String option) {
        options.add(option);
    }

    public void setPlayer(Player player) {
        activePlayer = player;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public ArrayList<String> getOptions(){
        return options;
    }

    public abstract ScenarioPrompt execute(Player player);
    public abstract ScenarioOutcome resolve(int choice);

}
