package com.CatacombeDelReCaduto.game.rooms;

import com.CatacombeDelReCaduto.game.entities.*;
import com.CatacombeDelReCaduto.game.items.*;
import com.CatacombeDelReCaduto.game.jsonHandlers.RoomSave;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Room {
    private String name;
    private String description;
    // nord,sud,est,ovest
    private Room[] nearRooms = null; //nord sud est ovest
    private Npc npc = null;
    private List<Item> items = null;
    private Map<String, Enemy> enemies = null;
    private Map<String, String> examinables = null; //todo ancora da implementare, key nome oggetto esaminabile, value dialogo di risposta

    public Room() {
    }

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String printNearRooms() {
        String ret = "";
        if (nearRooms == null) {
            return "Non ci sono stanze vicine";
        }
        if (nearRooms[0] == null) {
            ret += "Nord: nessuna stanza in questa direzione\n";
        } else {
            ret += "Nord: " + nearRooms[0].name + "\n";
        }
        if (nearRooms[2] == null) {
            ret += "Est: nessuna stanza in questa direzione\n";
        } else {
            ret += "Est: " + nearRooms[2].name + "\n";
        }
        if (nearRooms[1] == null) {
            ret += "Sud: nessuna stanza in questa direzione\n";
        } else {
            ret += "Sud: " + nearRooms[1].name + "\n";
        }
        if (nearRooms[3] == null) {
            ret += "Ovest: nessuna stanza in questa direzione";
        } else {
            ret += "Ovest: " + nearRooms[3].name;
        }

        return ret;
    }

    public String printExaminables() {
        String ret = "";
        if (!examinables.isEmpty()) {
            for (Map.Entry<String, String> entry : examinables.entrySet()) {
                ret += entry.getKey() + ", ";
            }
            ret = ret.substring(0, ret.length() - 2);
        } else {
            ret = "Non c'è niente da esaminare nella stanza";
        }
        return ret;
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

    public Map<String, Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(Map<String, Enemy> enemies) {
        this.enemies = enemies;
    }

    public Map<String, String> getExaminables() {
        return examinables;
    }

    public void setExaminables(Map<String, String> examinables) {
        this.examinables = examinables;
    }

    public RoomSave save(){
        RoomSave roomSave = new RoomSave();
        // dati da salvare
        roomSave.setItems(items.stream().map(Item::getName).collect(Collectors.toList()));
        roomSave.setEnemies(new ArrayList<>(enemies.keySet()));
        return roomSave;
    }

    public void load(RoomSave roomSave){
        // carica dati in istanza
    }
}
