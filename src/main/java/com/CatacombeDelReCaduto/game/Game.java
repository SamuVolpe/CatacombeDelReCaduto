package com.CatacombeDelReCaduto.game;

import com.CatacombeDelReCaduto.game.entities.*;
import com.CatacombeDelReCaduto.game.items.*;
import com.CatacombeDelReCaduto.game.jsonUtility.*;
import com.CatacombeDelReCaduto.game.rooms.Room;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Game {
    public static final Logger logger =  Logger.getLogger(Game.class.getName());
    private Player player = null;

    // non mutable (da prendere clonati)
    private List<Item> items = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private List<Npc> npcs = new ArrayList<>();

    // mutable da inserire resto info dal salvataggio
    private List<Room> rooms = new ArrayList<>();

    public Game() {
        // DATI BASE
        // carica dati comandi ?? forse

        // carica dati oggetti
        loadItems();
        // carica dati mostri
        loadEnemies();
        // npc todo
        // rooms
        loadRooms();
    }

    // metodo dove verra' eseguito il gioco
    public void run(){
    }

    public void startNew() {
        // inizia nuovo gioco da 0

        // chiedi nome giocatore
        System.out.println("Benvenuto avventuriero! come ti chiami?");

        // todo controllo non inserisca un nome strano in quando dovra' essere parte del fileName
        String name = null;
        try (Scanner scanner = new Scanner(System.in)) {
            name = scanner.nextLine();
        }
        logger.info(name);

        // crea record in file giochi

        // crea player

        // setup stanze
        setupNewGameRooms();
    }

    public void load(){
        // carica gioco esistete da json
    }

    private void save(){
        // salva gioco su file
    }

    // carica dati oggetti da file
    private void loadItems()
    {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Item.class, new ItemsDeserializer());
        mapper.registerModule(module);

        try {
            // Assuming the JSON file is named "items.json"
            File jsonFile = new File(ItemsDeserializer.JSON_PATH);

            // Read the JSON file into a List<Item>
            items = mapper.readValue(
                    jsonFile,
                    new TypeReference<List<Item>>() {}
            );

            // Print out the items
            for (Item item : items) {
                System.out.println("Name: " + item.getName());
                System.out.println("Description: " + item.getDescription());
                System.out.println("Weight: " + item.getWeight());
                if (item instanceof Food) {
                    Food food = (Food) item;
                    System.out.println("Health Recovery Amount: " + food.getHealthRecoveryAmount());
                } else if (item instanceof Weapon) {
                    Weapon weapon = (Weapon) item;
                    System.out.println("Damage: " + weapon.getDamage());
                }
                System.out.println();
            }
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    // carica dati nemici da file
    private void loadEnemies(){
        //enemies.add()
    }

    // carica dati stanze da file
    private void loadRooms() {
        rooms.add(new Room("Iniziale", "L'aria e' fredda..."));

        // stanze adiacenti
    }

    // riempie stanze con la roba per un nuovo gioco
    private void setupNewGameRooms()
    {}
}
