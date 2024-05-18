package com.CatacombeDelReCaduto.game;

import com.CatacombeDelReCaduto.game.entities.*;
import com.CatacombeDelReCaduto.game.items.*;
import com.CatacombeDelReCaduto.game.jsonUtility.*;
import com.CatacombeDelReCaduto.game.prompts.CmdHandler;
import com.CatacombeDelReCaduto.game.prompts.Command;
import com.CatacombeDelReCaduto.game.prompts.CommandId;
import com.CatacombeDelReCaduto.game.rooms.Room;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Logger;

public class Game {
    public static final Logger logger =  Logger.getLogger(Game.class.getName());

    // comandi di gioco
    public static final List<Command> COMMANDS = List.of(
            new Command(CommandId.EXIT_GAME, List.of("esci", "esci partita"), "Inizia una nuova partita")
            ,new Command(CommandId.MOVE, List.of("v", "vai"), "Spostati in un'altra stanza", 1));
    // mappa per il parse dei comandi
    private TreeMap<String, Command> commandMap = null;

    private Player player = null;

    // non mutable (da prendere clonati)
    private List<Item> items = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private List<Npc> npcs = new ArrayList<>();

    // mutable da inserire resto info dal salvataggio, prima stanza e' quella iniziale
    private List<Room> rooms = new ArrayList<>();

    public Game() {
        // DATI BASE
        // carica dati comandi ?? forse
        initCommandMap();

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
        // loop che continua a chiedere comandi e a parsarli finche` non esce
        Command command = null;
            do {
                // prendo input
                String userCommand = CmdHandler.getInput();

                // parse command
                command = Command.parse(userCommand, commandMap);

                // gestisce comando richiamando metodo corretto
                handleCommand(command);

            } while (command == null || command.getId() != CommandId.EXIT_GAME);
    }

    public void startNew() {
        // inizia nuovo gioco da 0

        // chiedi nome giocatore
        System.out.println("Benvenuto avventuriero! come ti chiami?");

        // todo controllo non inserisca un nome strano in quando dovra' essere parte del fileName
        String name = CmdHandler.getInput();

        logger.info(name);

        // crea record in file giochi

        // crea player
        player = new Player(name, "giocatore", 30, 5, 5, null, null);
        player.setRoom(rooms.getFirst());

        // setup stanze
        setupNewGameRooms();
    }

    public void load(){
        // carica gioco esistete da json
    }

    private void initCommandMap() {
        commandMap = new TreeMap<>();

        // inserisco nella mappa tutti gli alias del comando come chiave
        for (var command : COMMANDS)
            for (var alias : command.getAliases())
                commandMap.put(alias, command);
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

            // todo Print out the items
//            for (Item item : items) {
//                System.out.println("Name: " + item.getName());
//                System.out.println("Description: " + item.getDescription());
//                System.out.println("Weight: " + item.getWeight());
//                if (item instanceof Food) {
//                    Food food = (Food) item;
//                    System.out.println("Health Recovery Amount: " + food.getHealthRecoveryAmount());
//                } else if (item instanceof Weapon) {
//                    Weapon weapon = (Weapon) item;
//                    System.out.println("Damage: " + weapon.getDamage());
//                }
//                System.out.println();
//            }
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    // carica dati nemici
    private void loadEnemies(){
        enemies.add(new Enemy("scheletro", "un non morto invocato dal lich", 40, 10, 10));
        enemies.add(new Enemy("spettro", "spettro che vaga", 40, 10, 10));
    }

    // carica dati stanze
    private void loadRooms() {
        // stanze todo manca roba esaminabile
        Room uno = new Room("Iniziale", "L'aria e' fredda...");
        Room due = new Room("Seconda", "L'aria e' calda...");

        // stanze adiacenti
        uno.setNorth(due);
        due.setSouth(uno);

        // carica in lista
        rooms.add(uno);
        rooms.add(due);
    }

    // riempie stanze con la roba per un nuovo gioco
    private void setupNewGameRooms(){
        ArrayList<Item> itemsToAdd = new ArrayList<>();
        ArrayList<Enemy> enemiesToAdd = new ArrayList<>();

        // trova stanza a cui aggiungere roba
        Room uno = rooms.stream().filter(x -> x.getName().equalsIgnoreCase("iniziale")).findFirst().get();

        // clear liste di supporto
        itemsToAdd.clear();
        enemiesToAdd.clear();

        // aggiungo oggetti
        itemsToAdd.add(items.stream().filter(x -> x.getName().equalsIgnoreCase("pane")).findFirst().get().clone());
        // aggiungo mostri
        enemiesToAdd.add(enemies.stream().filter(x -> x.getName().equalsIgnoreCase("scheletro")).findFirst().get().clone());

        // aggiungo a stanza
        uno.setItems(itemsToAdd);
        uno.setEnemies(enemiesToAdd);
    }

    // region COMMAND HANDLER

    private void handleCommand(Command command){
        if (command == null || command.getId() == CommandId.EXIT_GAME)
            return;

        switch (command.getId()) {
            case CommandId.MOVE -> commandMove(command.getArgs()[0]);
            default -> throw new RuntimeException("Command not implemented");
        }
    }

    private void commandMove(String arg){

        Room nextRoom = null;

        // gestisco spostamento, se voglio fare sinonimi di arg es. 'nord' e 'n' da gestirlo con alias in command
        switch (arg)
        {
            case "nord" :
                nextRoom = player.getRoom().getNorth();
                break;
            case "sud" :
                nextRoom = player.getRoom().getSouth();
                break;
            default:
                System.out.println("Direzione inesistente");
                return;
        }

        if (nextRoom != null){
            player.setRoom(nextRoom);
            System.out.println(player.getRoom().getDescription());
        }
        else {
            System.out.println("Non c'e` nessuna stanza in questa direzione, provane un'altra");
        }
    }

    // endregion
}
