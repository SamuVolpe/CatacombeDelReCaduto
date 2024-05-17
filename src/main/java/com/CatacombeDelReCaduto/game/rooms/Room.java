package com.CatacombeDelReCaduto.game.rooms;

import com.CatacombeDelReCaduto.game.entities.*;
import com.CatacombeDelReCaduto.game.items.*;
import java.util.List;

public class Room {
    private String name;
    private String description;
    private Room nord = null;
    private Room sud = null;
    private Room est = null;
    private Room ovest = null;
    private Npc npc = null;
    private List<Item> items = null;
    private List<Enemy> enemies = null;
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

    public Room getNord() {
        return nord;
    }

    public void setNord(Room nord) {
        this.nord = nord;
    }

    public Room getSud() {
        return sud;
    }

    public void setSud(Room sud) {
        this.sud = sud;
    }

    public Room getEst() {
        return est;
    }

    public void setEst(Room est) {
        this.est = est;
    }

    public Room getOvest() {
        return ovest;
    }

    public void setOvest(Room ovest) {
        this.ovest = ovest;
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
