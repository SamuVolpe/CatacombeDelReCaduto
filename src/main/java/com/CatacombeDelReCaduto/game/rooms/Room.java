package com.CatacombeDelReCaduto.game.rooms;

import com.CatacombeDelReCaduto.game.entities.*;
import com.CatacombeDelReCaduto.game.items.*;
import java.util.List;
import java.util.Map;

public class Room {
    private String name;
    private String description;
    private Room north = null;
    private Room south = null;
    private Room east = null;
    private Room west = null;
    private Npc npc = null;
    private List<Item> items = null;
    private List<Enemy> enemies = null;
    private Map<String, String> examinables = null; //todo ancora da implementare, key nome oggetto esaminabile, value dialogo di risposta
    // todo da capire come fare per le robe esaminabili

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Room getNorth() {
        return north;
    }

    public void setNorth(Room north) {
        this.north = north;
    }

    public Room getSouth() {
        return south;
    }

    public void setSouth(Room south) {
        this.south = south;
    }

    public Room getEast() {
        return east;
    }

    public void setEast(Room east) {
        this.east = east;
    }

    public Room getWest() {
        return west;
    }

    public void setWest(Room west) {
        this.west = west;
    }

    public Npc getNpc() {
        return npc;
    }

    public void setNpc(Npc npc) {
        this.npc = npc;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }
}
