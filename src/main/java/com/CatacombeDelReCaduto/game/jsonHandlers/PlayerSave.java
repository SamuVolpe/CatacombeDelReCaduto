package com.CatacombeDelReCaduto.game.jsonHandlers;

public class PlayerSave {
    private int health;
    //altro
    private String[] inventory;

    public PlayerSave() {
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String[] getInventory() {
        return inventory;
    }

    public void setInventory(String[] inventory) {
        this.inventory = inventory;
    }
}
