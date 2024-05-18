package com.CatacombeDelReCaduto.game.jsonHandlers;

public class RoomSave {
    private String[] items;
    private String[] enemies;

    public RoomSave() {
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    public String[] getEnemies() {
        return enemies;
    }

    public void setEnemies(String[] enemies) {
        this.enemies = enemies;
    }
}
