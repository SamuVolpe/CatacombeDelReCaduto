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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe per la gestione del caricamento/ salvataggio sui files
 */
public class FilesManager {
    public static final Logger logger =  Logger.getLogger(FilesManager.class.getName());

    /**
     * Salva la partita nel file
     * @param saveFileName nome file in cui salvare (json)
     * @param save oggetto con i dati da salvare
     */
    public static void saveGame (String saveFileName, Save save) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(FilesPath.PLAYER_ROOT + "\\" + saveFileName), save);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
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
        map.put(player.CREATION_DATE, player.getName());

        // Scrivi la mappa aggiornata nel file json
        try {
            mapper.writeValue(new File(FilesPath.SAVES_FILE_PATH), map);
            logger.info("saves updated");
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
            return mapper.readValue(new File(FilesPath.PLAYER_ROOT + "\\" + loadFileName), Save.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Carica dati partite
     * @return map - key : data partita, value : nome giocatore
     */
    public static Map<Long, String> loadGames(){
        logger.info("load games");

        ObjectMapper mapper = new ObjectMapper();
        File file = new File(FilesPath.SAVES_FILE_PATH);

        if (file.exists()) {
            // Leggi il file JSON e deserializza nella mappa
            try {
                return mapper.readValue(file, new TypeReference<Map<Long, String>>() {
                });
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "error on games loading", ex);
                throw new RuntimeException("Errore nel caricamento delle partite, il file : '" + FilesPath.SAVES_FILE_PATH + "' potrebbe essere stato compromesso");
            }
        }
        else {
            logger.info("file doesn't exist");
            return new TreeMap<>();
        }
    }

    /**
     * Carica dati oggetti
     * @return map - key : identificativo oggetto, value : oggetto
     */
    public static Map<String, Item> loadItems(){
        logger.info("load items");
        Map<String, Item> result = new TreeMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            // leggi file json e converti in jsonNode
            JsonNode rootNode = mapper.readTree(new File(FilesPath.ITEMS_FILE_PATH));

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
            logger.log(Level.SEVERE, "error on items loading", ex);
            throw new RuntimeException("Errore nel caricamento degli oggetti di gioco, il file : '" + FilesPath.ITEMS_FILE_PATH + "' potrebbe essere stato compromesso");
        }

        return result;
    }

    /**
     * Carica dati nemici
     * @param items tutti gli oggetti di gioco
     * @return map - key : identificativo nemico, value : nemico
     */
    public static Map<String, Enemy> loadEnemies(Map<String, Item> items){
        logger.info("load enemies");
        // Path del file JSON
        final String FILE_PATH = FilesPath.ENEMIES_FILE_PATH;

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
                try {
                    if (dropNode != null && dropNode.isArray()) {
                        for (JsonNode itemNode : dropNode) {
                            drop.add(items.get(itemNode.asText()).clone());
                        }
                    }
                }
                catch (NoSuchElementException ex) {
                    logger.severe("Error in enemies file, item not found " + ex.getMessage());
                }

                // aggiungo a mappa
                result.put(field.getKey(), new Enemy(name, description, maxHealth, attack, defense, drop));
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "error on enemies loading", ex);
            throw new RuntimeException("Errore nel caricamento dei nemici di gioco, il file : '" + FilesPath.ENEMIES_FILE_PATH + "' potrebbe essere stato compromesso");
        }

        return result;
    }

    /**
     * Carica dati stanze senza oggetti e nemici
     * @return map - key : identificativo stanza, value : stanza
     */
    public static Map<String, Room> loadRooms(){
        logger.info("load base rooms data");
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
        logger.info("load rooms");
        Map<String, Room> result = new TreeMap<>();
        // mappa di supporto salvataggio nearRooms
        Map<String, String[]> nearRooms = new TreeMap<>();

        // Crea un ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        try {

            // Leggi il file JSON e converti in JsonNode
            JsonNode rootNode = mapper.readTree(new File(FilesPath.ROOMS_FILE_PATH));

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
                    try {
                        if (itemsNode != null && itemsNode.isArray()) {
                            for (JsonNode itemNode : itemsNode) {
                                inRoomItems.add(items.get(itemNode.asText()).clone());
                            }
                        }
                    } catch (NoSuchElementException ex) {
                        logger.severe("Error in room file, item not found " + ex.getMessage());
                    }

                    room.setItems(inRoomItems);
                }

                // carica nemici se necessario
                if (enemies != null) {
                    JsonNode enemiesNode = roomNode.get("enemies");
                    Map<String, Enemy> inRoomEnemies = new TreeMap<>();
                    try {
                        if (enemiesNode != null && enemiesNode.isArray()) {
                            for (JsonNode enemyNode : enemiesNode) {
                                inRoomEnemies.put(enemyNode.asText(), enemies.get(enemyNode.asText()).clone());
                            }
                        }
                    } catch (NoSuchElementException ex) {
                        logger.severe("Error in room file, enemy not found " + ex.getMessage());
                    }

                    room.setEnemies(inRoomEnemies);
                }

                // aggiungo a mappa
                result.put(name, room);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "error on rooms loading", ex);
            throw new RuntimeException("Errore nel caricamento delle stanze di gioco, il file : '" + FilesPath.ROOMS_FILE_PATH + "' potrebbe essere stato compromesso");
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
