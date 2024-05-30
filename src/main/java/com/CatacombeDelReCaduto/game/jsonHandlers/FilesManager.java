package com.CatacombeDelReCaduto.game.jsonHandlers;

import com.CatacombeDelReCaduto.game.entities.Enemy;
import com.CatacombeDelReCaduto.game.entities.Player;
import com.CatacombeDelReCaduto.game.items.*;
import com.CatacombeDelReCaduto.game.rooms.Room;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Classe per la gestione del caricamento/ salvataggio sui files
 */
public class FilesManager {
    // folders
    public static final String DATA_ROOT = "data";
    public static final String PLAYER_ROOT = DATA_ROOT + "\\player";
    public static final String GAME_ROOT = DATA_ROOT + "\\game";

    // files
    public static final String SAVES_FILE_NAME = "saves.json";
    public static final String ITEMS_FILE_PATH = GAME_ROOT + "\\items.json";
    public static final String ENEMIES_FILE_PATH = GAME_ROOT + "\\enemies.json";
    public static final String ROOMS_FILE_PATH = GAME_ROOT + "\\rooms.json";
    public static final String SAVES_FILE_PATH = PLAYER_ROOT + "\\" + SAVES_FILE_NAME;

    public static String gameFileName(long id, String name){
        return name + "_" + id + ".json";
    }

    public static void makeSavesDir(){
        // crea cartella di salvataggio se non esiste
        File directory = new File(PLAYER_ROOT);
        if (!directory.exists()) {
            boolean maked = directory.mkdir();
            if (!maked)
                throw new RuntimeException("Impossibile creare la cartella per il salvataggio dei dati");
        }
    }

    /**
     * Carica dati partite
     * @return map - key : data partita, value : nome giocatore
     */
    public static Map<Long, String> loadGames(){
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(SAVES_FILE_PATH);

        if (file.exists()) {
            // Leggi il file JSON e deserializza nella mappa
            try {
                return mapper.readValue(file, new TypeReference<Map<Long, String>>() {
                });
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            return new TreeMap<>();
        }
    }

    /**
     * Salva giocatore nel file delle partite
     * @param player giocatore da salvare
     */
    public static void saveNewGame (Player player){
        ObjectMapper mapper = new ObjectMapper();
        Map<Long, String> map = loadGames();

        // Aggiungi un nuovo elemento alla mappa
        if (player != null)
            map.put(player.CREATION_DATE, player.getName());

        // Scrivi la mappa aggiornata nel file json
        try {
            mapper.writeValue(new File(SAVES_FILE_PATH), map);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Salva la partita nel file
     * @param saveFileName nome file in cui salvare (json)
     * @param save oggetto con i dati da salvare
     */
    public static void saveGame (String saveFileName, Save save) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(PLAYER_ROOT + "\\" + saveFileName), save);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Carica partita dal file
     * @param loadFileName nome file da cui caricare i dati (json)
     * @return oggetto con i dati caricati
     */
    public static Save loadGame(String loadFileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(new File(PLAYER_ROOT + "\\" + loadFileName), Save.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Carica dati oggetti
     * @return map - key : identificativo oggetto, value : oggetto
     */
    public static Map<String, Item> loadItems(){
        Map<String, Item> result = new TreeMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            // leggi file json e converti in jsonNode
            JsonNode rootNode = mapper.readTree(new File(ITEMS_FILE_PATH));

            // ogni figlio del nodo principale e' un item, converto
            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String name = field.getKey();
                JsonNode itemNode = field.getValue();

                // Estrai i dati dell'item
                String description = itemNode.get("description").asText();
                int weight = itemNode.get("weight").asInt();

                Item item = switch (itemNode.get("type").asText()){
                    case "food" -> new Food(name, description, weight, itemNode.get("healthRecoveryAmount").asInt());
                    case "weapon" -> new Weapon(name, description, weight, itemNode.get("damage").asInt());
                    case "armor" -> new Armor(name, description, weight, itemNode.get("defense").asInt());
                    default -> new Item(name, description, weight);
                };

                // aggiungo a mappa
                result.put(name, item);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Errore nel caricamento degli oggetti di gioco, il file : '" + ITEMS_FILE_PATH + "' potrebbe essere stato compromesso");
        }

        return result;
    }

    /**
     * Carica dati nemici
     * @param items tutti gli oggetti di gioco
     * @return map - key : identificativo nemico, value : nemico
     */
    public static Map<String, Enemy> loadEnemies(Map<String, Item> items){
        // Path del file JSON
        final String FILE_PATH = ENEMIES_FILE_PATH;

        Map<String, Enemy> result = new TreeMap<>();

        // Crea un ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Leggi il file JSON e converti in JsonNode
            JsonNode rootNode = mapper.readTree(new File(FILE_PATH));

            // Itera attraverso i nodi figli
            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                JsonNode enemyNode = field.getValue();

                // Estrarre i dati dell'enemy
                String name = enemyNode.get("name").asText();
                String description = enemyNode.get("description").asText();
                int maxHealth = enemyNode.get("maxHealth").asInt();
                int attack = enemyNode.get("attack").asInt();
                int defense = enemyNode.get("defense").asInt();

                // aggiungo a lista drop gli items
                JsonNode dropNode = enemyNode.get("drop");
                List<Item> drop = new ArrayList<>();
                if (dropNode != null && dropNode.isArray()) {
                    for (JsonNode itemNode : dropNode) {
                        drop.add(items.get(itemNode.asText()));
                    }
                }

                // aggiungo a mappa
                result.put(field.getKey(), new Enemy(name, description, maxHealth, attack, defense, drop));
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }

    /**
     * Carica dati stanze senza oggetti e nemici
     * @return map - key : identificativo stanza, value : stanza
     */
    public static Map<String, Room> loadRooms(){
        return loadRooms(null, null);
    }

    /**
     * Carica dati stanze
     * @param items tutti gli oggetti di gioco
     * @param enemies tutti i nemici di gioco
     * @return map - key : identificativo stanza, value : stanza
     */
    public static Map<String, Room> loadRooms(Map<String, Item> items, Map<String, Enemy> enemies)
    {
        Map<String, Room> result = new TreeMap<>();
        // mappa di supporto salvataggio nearRooms
        Map<String, String[]> nearRooms = new TreeMap<>();

        // Crea un ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Leggi il file JSON e converti in JsonNode
            JsonNode rootNode = mapper.readTree(new File(ROOMS_FILE_PATH));

            // Itera attraverso i nodi figli
            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String name = field.getKey();
                JsonNode roomNode = field.getValue();

                // Estrae i dati
                String description = roomNode.get("description").asText();
                int dangerLevel = roomNode.get("dangerLevel").asInt();

                Room room = new Room(name, description, dangerLevel);

                // carica stanze vicine
                JsonNode roomsNode = roomNode.get("nearRooms");
                String[] rooms = new String[4];
                int i = 0;
                for (JsonNode nearRoomNode : roomsNode) {
                    rooms[i] = nearRoomNode.asText();
                    i++;
                }
                nearRooms.put(field.getKey(), rooms);

                // carica esaminabili se necessario
                JsonNode exNode = roomNode.get("examinables");
                if (exNode != null){
                    Map<String, String> examinables = new TreeMap<>();
                    exNode.fields().forEachRemaining(entry -> {
                        examinables.put(entry.getKey(), entry.getValue().toString());
                    });

                    room.setExaminables(examinables);
                }

                // carica oggetti se necessario
                if (items != null) {
                    JsonNode itemsNode = roomNode.get("items");
                    List<Item> inRoomItems = new ArrayList<>();
                    if (itemsNode != null && itemsNode.isArray()) {
                        for (JsonNode itemNode : itemsNode) {
                            inRoomItems.add(items.get(itemNode.asText()));
                        }
                    }

                    room.setItems(inRoomItems);
                }

                // carica nemici se necessario
                if (enemies != null) {
                    JsonNode enemiesNode = roomNode.get("enemies");
                    Map<String, Enemy> inRoomEnemies = new TreeMap<>();
                    if (enemiesNode != null && enemiesNode.isArray()) {
                        for (JsonNode enemyNode : enemiesNode) {
                            inRoomEnemies.put(enemyNode.asText(), enemies.get(enemyNode.asText()).clone());
                        }
                    }

                    room.setEnemies(inRoomEnemies);
                }

                // aggiungo a mappa
                result.put(name, room);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Errore nel caricamento delle stanze di gioco, il file : '" + ROOMS_FILE_PATH + "' potrebbe essere stato compromesso");
        }

        // carica stanze adiacenti converto da Nome a Room
        for (var entry : nearRooms.entrySet()){
            String[] roomsKey = nearRooms.get(entry.getKey());
            // carica in array di supporto le room convertite
            Room[] rooms = new Room[roomsKey.length];
            for (int i=0; i < roomsKey.length; i++)
                if (roomsKey[i] != null)
                    rooms[i] = result.get(roomsKey[i]);

            result.get(entry.getKey()).setNearRooms(rooms);
        }

        return result;
    }
}
