package com.CatacombeDelReCaduto.game.jsonHandlers;

import java.util.List;

public class RoomSave {
    private List<String> items;
    private List<String> enemies;

    public RoomSave() {
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public List<String> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<String> enemies) {
        this.enemies = enemies;
    }
}
