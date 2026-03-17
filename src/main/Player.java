package main;

import java.util.ArrayList;

public class Player {
    private String name;
    private Inventory inventory;
    private PlayerClass playerClass;
    private int gold;
    private int score;
    private int health;
    private int maxHealth;


    public Player(String name, PlayerClass playerClass) {
        this.name = name;
        this.playerClass = playerClass;
        this.inventory = new Inventory();
        this.gold = 0;
        this.score = 0;
        this.health = 100;
        this.maxHealth = 100;
    }

    // getters
    public String getName() {
        return name;
    }
    public PlayerClass getPlayerClass() {
        return playerClass;
    }
    public ArrayList<Item> getInventoryList() {
        return inventory.getInventory();
    }
    public int getGold() {
        return gold;
    }
    public int getScore() {
        return score;
    }
    public int getHealth() {
        return health;
    }
    public int getMaxHealth() {
        return maxHealth;
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }
    public void setPlayerClass(PlayerClass playerClass) {
        this.playerClass = playerClass;
    }

    public void addItemToInventory(Item item) {
        inventory.addItem(item);
    }
    public void removeItemFromInventory(Item item) {
        inventory.removeItem(item);
    }

    public void addGold(int gold) {
        this.gold += gold;
    }

    public void addScore(int score) {
        this.score += score;
    }
    public void addHealth(int health) {
        this.health += health;
    }


}
