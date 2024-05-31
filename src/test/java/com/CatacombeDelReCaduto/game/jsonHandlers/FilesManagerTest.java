package com.CatacombeDelReCaduto.game.jsonHandlers;

import com.CatacombeDelReCaduto.game.entities.Enemy;
import com.CatacombeDelReCaduto.game.items.Food;
import com.CatacombeDelReCaduto.game.items.Item;
import com.CatacombeDelReCaduto.game.rooms.Room;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class FilesManagerTest {
    ObjectMapper mapper;
    Map<String, Item> items;
    Map<String, Enemy> enemies;

    @BeforeEach
    void setUp(){
        mapper = new ObjectMapper();
        items = new TreeMap<>();
        enemies = new TreeMap<>();

        Food item = new Food("mela", "", 1, 5);
        items.put(item.getName(), item);
        Enemy enemy = new Enemy("scheletro", "", 20, 2, 2);
        enemies.put(enemy.getName(), enemy);
    }

    @Test
    void loadRooms() throws IOException {
        String json = "{\"Entrata\":{\"description\":\"descrizione entrata\",\"dangerLevel\":0,\"nearRooms\":[null,\"Stanza buia\",null,null],\"examinables\":{\"muro\":\"descrizione muro\",\"stendardo\":\"descrizione stendardo\"},\"items\":[],\"enemies\":[]}" +
                ",\"Stanza buia\":{\"description\":\"descrizione stanza buia\",\"dangerLevel\":5,\"nearRooms\":[\"Entrata\",null,null,null],\"examinables\":{},\"items\":[\"mela\"],\"enemies\":[\"scheletro\",\"scheletro\"]}}\n";
        JsonNode rootNode = mapper.readTree(json);
        Map<String, Room> rooms = FilesManager.loadRooms(rootNode, null, null);
        assertEquals("Entrata", rooms.get("Entrata").getName());
        assertEquals("Stanza buia", rooms.get("Stanza buia").getName());
    }

    @Test
    void loadRoomsNoItemsEnemies() throws IOException {
        String json = "{\"Entrata\":{\"description\":\"descrizione entrata\",\"dangerLevel\":0,\"nearRooms\":[null,\"Stanza buia\",null,null],\"examinables\":{\"muro\":\"descrizione muro\",\"stendardo\":\"descrizione stendardo\"},\"items\":[],\"enemies\":[]}" +
                ",\"Stanza buia\":{\"description\":\"descrizione stanza buia\",\"dangerLevel\":5,\"nearRooms\":[\"Entrata\",null,null,null],\"examinables\":{},\"items\":[\"mela\"],\"enemies\":[\"scheletro\",\"scheletro\"]}}\n";
        JsonNode rootNode = mapper.readTree(json);
        Map<String, Room> rooms = FilesManager.loadRooms(rootNode, null, null);
        assertNull(rooms.get("Stanza buia").getItems());
        assertNull(rooms.get("Stanza buia").getEnemies());
    }

    @Test
    void loadRoomsItemsEnemies() throws IOException {
        String json = "{\"Entrata\":{\"description\":\"descrizione entrata\",\"dangerLevel\":0,\"nearRooms\":[null,\"Stanza buia\",null,null],\"examinables\":{\"muro\":\"descrizione muro\",\"stendardo\":\"descrizione stendardo\"},\"items\":[],\"enemies\":[]}" +
                ",\"Stanza buia\":{\"description\":\"descrizione stanza buia\",\"dangerLevel\":5,\"nearRooms\":[\"Entrata\",null,null,null],\"examinables\":{},\"items\":[\"mela\"],\"enemies\":[\"scheletro\",\"scheletro\"]}}\n";
        JsonNode rootNode = mapper.readTree(json);
        Map<String, Room> rooms = FilesManager.loadRooms(rootNode, items, enemies);
        assertEquals(1, rooms.get("Stanza buia").getItems().size());
        assertEquals(2, rooms.get("Stanza buia").getEnemies().size());
    }
}