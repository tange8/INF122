package main;

import java.util.ArrayList;

public class Inventory {
    private ArrayList<Item> inventory;

    public Inventory() {
        this.inventory = new ArrayList<Item>();
    }

    public void addItem(Item item) {
        inventory.add(item);
    }
    public void removeItem(Item item) {
        inventory.remove(item);
    }
    public ArrayList<Item> getInventory() {
        return inventory;
    }
}
