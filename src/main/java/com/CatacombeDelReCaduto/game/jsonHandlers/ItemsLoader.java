package com.CatacombeDelReCaduto.game.jsonHandlers;

import com.CatacombeDelReCaduto.game.items.*;

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

// classe che gestisce il iniziale degli items da file
public class ItemsLoader {
    private static final String JSON_PATH = "data\\game\\items.json";
    public final Logger logger =  Logger.getLogger(this.getClass().getName());

    // carica dati oggetti da file
    public Map<String, Item> loadItems()
    {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Item.class, new ItemsDeserializer());
        mapper.registerModule(module);

        try {
            // Assuming the JSON file is named "items.json"
            File jsonFile = new File(JSON_PATH);

            // Read the JSON file into a List<Item>
            return mapper.readValue(
                    jsonFile,
                    new TypeReference<Map<String, Item>>() {}
            );
        } catch (IOException ex) {
            //logger.severe("Error with the loading of the items in the json file" + e.getMessage());
            //System.out.println("Il file contenente gli oggetti di gioco e` stato compromesso, reinstallare il gioco");
            ex.printStackTrace();
            return null;
        }
    }
}

// deserializer degli items
class ItemsDeserializer extends StdDeserializer<Item> {

    public ItemsDeserializer() {
        this(null);
    }

    public ItemsDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Item deserialize(JsonParser jp, DeserializationContext context) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String type = node.get("type").asText();
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();

        return switch (type) {
            case "food" -> mapper.treeToValue(node, Food.class);
            case "weapon" -> mapper.treeToValue(node, Weapon.class);
            case "armor" -> mapper.treeToValue(node, Armor.class);
            default -> mapper.treeToValue(node, Item.class);
        };
    }
}
