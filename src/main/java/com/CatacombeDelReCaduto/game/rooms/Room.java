package com.CatacombeDelReCaduto.game.rooms;

import com.CatacombeDelReCaduto.game.entities.*;
import com.CatacombeDelReCaduto.game.items.*;
import com.CatacombeDelReCaduto.game.jsonHandlers.RoomSave;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Stanza di gioco
 */
public class Room {
    private String name;
    private String description;
    // 1 = 10% di prob che nemico attacchi
    private int dangerLevel;
    // nord,sud,est,ovest
    private Room[] nearRooms = null; //nord sud est ovest
    private Npc npc = null;
    private List<Item> items = null;
    private Map<String, Enemy> enemies = null;
    private Map<String, String> examinables = null; //todo ancora da implementare, key nome oggetto esaminabile, value dialogo di risposta

    public Room(String name, String description, int dangerLevel) {
        this.name = name;
        this.description = description;
        setDangerLevel(dangerLevel);
    }

    private void setDangerLevel(int dangerLevel){
        if (dangerLevel > 10)
            this.dangerLevel = 10;
        else
            this.dangerLevel = dangerLevel;
    }

    public int getDangerLevel() {
        return dangerLevel;
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

    public boolean isEnemyEngaging(){
        if (enemies == null || enemies.isEmpty())
            return false;

        // calcolo con probabilita' che il nemico attacchi
        Random random = new Random();
        return random.nextInt(10) < dangerLevel;
    }

    public RoomSave save(){
        RoomSave roomSave = new RoomSave();
        // dati da salvare
        roomSave.setItems(items.stream().map(Item::getName).collect(Collectors.toList()));
        roomSave.setEnemies(new ArrayList<>(enemies.keySet()));
        return roomSave;
    }

    public void load(RoomSave roomSave, Map<String, Item> allItems, Map<String, Enemy> allEnemies){
        // setto dati stanza
        items = new ArrayList<>();
        for (String itemKey : roomSave.getItems())
            items.add(allItems.get(itemKey).clone());

        enemies = new TreeMap<>();
        for (String enemyKey : roomSave.getEnemies())
            enemies.put(enemyKey, allEnemies.get(enemyKey).clone());
    }
}
