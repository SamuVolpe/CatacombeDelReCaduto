package com.CatacombeDelReCaduto.game.jsonUtility;

import com.CatacombeDelReCaduto.game.items.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ItemsDeserializer extends StdDeserializer<Item> {
    public static final String JSON_PATH = "datas\\items.json";

    public ItemsDeserializer() {
        this(null);
    }

    public ItemsDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Item deserialize(JsonParser jp, DeserializationContext context)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String type = node.get("type").asText();
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        Item item;

        switch (type) {
            case "food":
                item = mapper.treeToValue(node, Food.class);
                break;
            case "weapon":
                item = mapper.treeToValue(node, Weapon.class);
                break;
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
        return item;
    }
}