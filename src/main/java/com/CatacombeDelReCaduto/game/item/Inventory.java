package com.CatacombeDelReCaduto.game.item;

import java.util.ArrayList;
import java.util.HashMap;

public class Inventory {
    static public final int MAX_WEIGHT = 100;
    private int currentWeight;
    private HashMap<String, ArrayList<Item>> inventory;

    public Inventory() {
    }

    public Inventory(HashMap<String, ArrayList<Item>> inventory) {
        this.inventory = inventory;
    }

    public boolean addItem(Item item) {
        //controllo se supero il peso massimo
        if (currentWeight + item.getWeight() > MAX_WEIGHT) {
            return false;
        }
        ArrayList<Item> list = inventory.get(item.getName());
        if (list == null) {
            list = new ArrayList<Item>();
        }
        list.add(item);
        inventory.put(item.getName(), list);
        return true;
    }

    public void removeItem(String itemName) {
        ArrayList<Item> list = inventory.get(itemName);
        if (list.size() == 1) {
            inventory.remove(itemName);
        } else {
            list.removeFirst();
        }
    }
}
