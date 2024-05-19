package com.CatacombeDelReCaduto.game.jsonHandlers;

import com.CatacombeDelReCaduto.game.Game;
import com.CatacombeDelReCaduto.game.entities.Enemy;
import com.CatacombeDelReCaduto.game.items.*;
import com.CatacombeDelReCaduto.game.rooms.Room;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

// classe che carica i dati del gioco
public class GameLoader {
    public static final Logger logger =  Logger.getLogger(GameLoader.class.getName());

    public static Map<String, Item> loadItems(){
        // Path del file JSON
        final String FILE_PATH = FilesPath.ITEMS_FILE_PATH;

        Map<String, Item> result = new TreeMap<>();

        // Crea un ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Leggi il file JSON e converti in JsonNode
            JsonNode rootNode = mapper.readTree(new File(FILE_PATH));

            // Itera attraverso i nodi figli
            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                JsonNode itemNode = field.getValue();

                // Estrai i dati dell'item
                String name = itemNode.get("name").asText();
                String description = itemNode.get("description").asText();
                int weight = itemNode.get("weight").asInt();

                Item item = switch (itemNode.get("type").asText()){
                    case "food" -> new Food(name, description, weight, itemNode.get("healthRecoveryAmount").asInt());
                    case "weapon" -> new Weapon(name, description, weight, itemNode.get("damage").asInt());
                    case "armor" -> new Armor(name, description, weight, itemNode.get("defense").asInt());
                    default -> new Item(name, description, weight);
                };

                // aggiungo a mappa
                result.put(field.getKey(), item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Map<String, Enemy> loadEnemies(Map<String, Item> items){
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    // non carica oggetti e nemici
    public static Map<String, Room> loadRooms(){
        return loadRooms(null, null);
    }

    // carica tutto
    public static Map<String, Room> loadRooms(Map<String, Item> items, Map<String, Enemy> enemies)
    {
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
                JsonNode roomNode = field.getValue();

                // Estrae i dati
                String name = roomNode.get("name").asText();
                String description = roomNode.get("description").asText();

                Room room = new Room(name, description);

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
                    List<Enemy> inRoomEnemies = new ArrayList<>();
                    try {
                        if (enemiesNode != null && enemiesNode.isArray()) {
                            for (JsonNode enemyNode : enemiesNode) {
                                inRoomEnemies.add(enemies.get(enemyNode.asText()).clone());
                            }
                        }
                    } catch (NoSuchElementException ex) {
                        logger.severe("Error in room file, enemy not found " + ex.getMessage());
                    }

                    room.setEnemies(inRoomEnemies);
                }

                // aggiungo a mappa
                result.put(field.getKey(), room);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
