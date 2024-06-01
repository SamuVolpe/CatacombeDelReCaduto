package com.CatacombeDelReCaduto.game.jsonHandlers;

import java.util.List;

/**
 * Classe di utilita` per gestire il salvataggio di una stanza
 */
public class RoomSave {
    private List<String> items;
    private List<String> enemies;
    private boolean visited;

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

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
