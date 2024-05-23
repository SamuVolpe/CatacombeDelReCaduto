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
    private List<Item> items = null;
    private Map<String, Enemy> enemies = null;
    private Map<String, String> examinables = null;

    public Room(String name, String description, int dangerLevel) {
        this.name = name;
        this.description = description;
        setDangerLevel(dangerLevel);
    }

    public int getDangerLevel() {
        return dangerLevel;
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

    /**
     * Calcolo con probabilita` in base al dangerLevel della stanza che il nemico attacchi
     * @return true - se il nemico attacca
     */
    public boolean isEnemyEngaging(){
        if (enemies == null || enemies.isEmpty())
            return false;

        // calcolo con probabilita' che il nemico attacchi
        Random random = new Random();
        return random.nextInt(10) < dangerLevel;
    }

    /**
     * Rimuove nemico dalla stanza, aggiungendo il drop agli oggetti della stanza
     * @param enemy nemico da rimuovere
     */
    public void removeEnemy(Enemy enemy){
        // rimuove nemico dalla stanza
        boolean removed = enemies.values().remove(enemy);
        if (!removed)
            throw new IllegalArgumentException("Can't remove " + enemy + " from the room");

        // aggiunge gli oggetti alla stanza
        if (!enemy.getDrop().isEmpty())
            items.addAll(enemy.getDrop());
    }

    /**
     * Salva i dati della stanza nell'oggetto apposito
     * @return oggetto contenente i dati da salvare
     */
    public RoomSave save(){
        RoomSave roomSave = new RoomSave();
        // dati da salvare
        roomSave.setItems(items.stream().map(Item::getName).collect(Collectors.toList()));
        roomSave.setEnemies(new ArrayList<>(enemies.keySet()));
        return roomSave;
    }

    /**
     * Carica i dati della stanza dall'oggetto apposito
     * @param roomSave oggetto da cui caricare i dati
     * @param allItems lista di tutti gli items di gioco
     * @param allEnemies lista di tutti i nemici di gioco
     */
    public void load(RoomSave roomSave, Map<String, Item> allItems, Map<String, Enemy> allEnemies){
        // setto dati stanza
        items = new ArrayList<>();
        for (String itemKey : roomSave.getItems())
            items.add(allItems.get(itemKey).clone());

        enemies = new TreeMap<>();
        for (String enemyKey : roomSave.getEnemies())
            enemies.put(enemyKey, allEnemies.get(enemyKey).clone());
    }

    private void setDangerLevel(int dangerLevel){
        if (dangerLevel > 10)
            this.dangerLevel = 10;
        else
            this.dangerLevel = dangerLevel;
    }
}
