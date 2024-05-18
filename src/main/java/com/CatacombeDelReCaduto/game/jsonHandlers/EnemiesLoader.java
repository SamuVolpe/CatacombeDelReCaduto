package com.CatacombeDelReCaduto.game.jsonHandlers;

import com.CatacombeDelReCaduto.game.entities.Enemy;
import com.CatacombeDelReCaduto.game.items.Item;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class EnemiesLoader {
    private static final String JSON_PATH = "data\\game\\enemies.json";
    public final Logger logger =  Logger.getLogger(this.getClass().getName());

    // carica dati oggetti da file
    public Map<String, Enemy> loadEnemies(Map<String, Item> items)
    {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Enemy.class, new EnemiesDeserializer(items));
        mapper.registerModule(module);

        try {
            // Assuming the JSON file is named "items.json"
            File jsonFile = new File(JSON_PATH);

            // Read the JSON file into a List<Item>
            return mapper.readValue(
                    jsonFile,
                    new TypeReference<Map<String, Enemy>>() {}
            );
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

// deserializer degli items
class EnemiesDeserializer extends StdDeserializer<Enemy> {
    private Map<String, Item> items = null;

    public EnemiesDeserializer(Map<String, Item> items) {
        this((Class<?>) null);
        this.items = items;
    }

    public EnemiesDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Enemy deserialize(JsonParser jp, DeserializationContext context) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode rootNode = mapper.readTree(jp);

        // Analizza i dati dell'oggetto Enemy
        String name = rootNode.get("name").asText();
        String description = rootNode.get("description").asText();
        int maxHealth = rootNode.get("maxHealth").asInt();
        int attack = rootNode.get("attack").asInt();
        int defense = rootNode.get("defense").asInt();

        // Crea un oggetto Enemy
        Enemy enemy = new Enemy(name, description, maxHealth, attack, defense);

        // Analizza la lista di oggetti drop
        JsonNode dropNode = rootNode.get("drop");
        if (dropNode != null && dropNode.isArray()) {
            for (JsonNode itemNode : dropNode) {
                String itemKey = itemNode.asText();
                // copia item da lista in base alla key
                Item foundItem = items.get(itemKey).clone();
                // Se trovi l'Item, aggiungilo all'Enemy
                enemy.addItem(foundItem);
            }
        }

        return enemy;
    }
}
