package com.CatacombeDelReCaduto.game.rooms;

import com.CatacombeDelReCaduto.game.entities.Enemy;
import com.CatacombeDelReCaduto.game.items.Item;
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
    // put con clone
    private Map<String, Enemy> enemies = null;
    private Map<String, String> examinables = null;
    // indica se il giocatore ha gia` visitato la stanza
    private boolean visited;

    /**
     * Costruttore della classe Room.
     *
     * @param name Nome della stanza
     * @param description Descrizione della stanza
     * @param dangerLevel Livello di pericolo della stanza
     */
    public Room(String name, String description, int dangerLevel) {
        this (name, description, dangerLevel, false);
    }

    /**
     * Costruttore della classe Room.
     *
     * @param name Nome della stanza
     * @param description Descrizione della stanza
     * @param dangerLevel Livello di pericolo della stanza
     * @param visited Indica se la stanza è stata già visitata
     */
    public Room(String name, String description, int dangerLevel, boolean visited) {
        this.name = name;
        this.description = description;
        setDangerLevel(dangerLevel);
        this.visited = visited;
    }

    /**
     * Stampa le stanze adiacenti.
     * @return Una stringa contenente le informazioni sulle stanze adiacenti
     */
    public String printNearRooms() {
        if (nearRooms == null) {
            return "Non ci sono stanze vicine";
        }

        String ret = "";

        for (int i = 0; i < nearRooms.length; i++){
            String roomName = "???";

             ret += switch (i){
                case 0 -> "Nord: ";
                case 1 -> "Sud: ";
                case 2 -> "Est: ";
                case 3 -> "Ovest: ";
                default -> throw new IllegalArgumentException("nearRooms length can be max 4");
            };

            if (nearRooms[i] == null)
                ret += "nessuna stanza in questa direzione";
            else{
                // se stanza visitata visualizzo nome stanza altrimenti no
                if (nearRooms[i].isVisited())
                    roomName = nearRooms[i].name;
                ret += roomName;
            }

            ret += "\n";
        }

        return ret;
    }

    /**
     * Restituisce una stringa contenente gli oggetti esaminabili presenti nella stanza.
     * Se non ci sono oggetti esaminabili, restituisce un messaggio appropriato.
     * @return una stringa contenente gli oggetti esaminabili
     */
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
        roomSave.setVisited(visited);
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
            items.add(allItems.get(itemKey));

        enemies = new TreeMap<>();
        for (String enemyKey : roomSave.getEnemies())
            enemies.put(enemyKey, allEnemies.get(enemyKey).clone());

        visited = roomSave.isVisited();
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

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getDangerLevel() {
        return dangerLevel;
    }

    /**
     * Imposta il livello di pericolo della stanza.
     * Se il livello di pericolo specificato è superiore a 10, viene impostato a 10.
     * @param dangerLevel Il livello di pericolo da impostare
     */
    public void setDangerLevel(int dangerLevel){
        if (dangerLevel > 10)
            this.dangerLevel = 10;
        else
            this.dangerLevel = dangerLevel;
    }
}
