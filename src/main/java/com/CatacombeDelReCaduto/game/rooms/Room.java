package com.CatacombeDelReCaduto.game.rooms;

import com.CatacombeDelReCaduto.game.entities.*;
import com.CatacombeDelReCaduto.game.items.*;
import java.util.List;
import java.util.Map;

public class Room {
    private String name;
    private String description;
    // nord,sud,est,ovest
    private Room[] nearRooms = null;
    private Npc npc = null;
    private List<Item> items = null;
    private List<Enemy> enemies = null;
    private Map<String, String> examinables = null; //todo ancora da implementare, key nome oggetto esaminabile, value dialogo di risposta

    public Room() {
    }

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

    public Room[] getNearRooms() {
        return nearRooms;
    }

    public void setNearRooms(Room[] nearRooms) {
        this.nearRooms = nearRooms;
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

    public Map<String, String> getExaminables() {
        return examinables;
    }

    public void setExaminables(Map<String, String> examinables) {
        this.examinables = examinables;
    }
}
