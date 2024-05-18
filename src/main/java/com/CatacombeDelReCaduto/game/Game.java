package com.CatacombeDelReCaduto.game;

import com.CatacombeDelReCaduto.game.entities.*;
import com.CatacombeDelReCaduto.game.items.*;
import com.CatacombeDelReCaduto.game.jsonHandlers.*;
import com.CatacombeDelReCaduto.game.prompts.InputReader;
import com.CatacombeDelReCaduto.game.prompts.Command;
import com.CatacombeDelReCaduto.game.prompts.CommandId;
import com.CatacombeDelReCaduto.game.rooms.Room;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class Game {
    public static final Logger logger =  Logger.getLogger(Game.class.getName());

    // data file di salvataggio
    long creationDate;

    // comandi di gioco
    public static final List<Command> COMMANDS = List.of(
            new Command(CommandId.EXIT_GAME, List.of("esci", "esci partita"), "Inizia una nuova partita")
            ,new Command(CommandId.HELP, List.of("aiuto", "comandi", "lista comandi"), "Mostra tutti i comandi")
            ,new Command(CommandId.MOVE, List.of("v", "vai"), "Spostati in un'altra stanza", 1));
    // mappa per il parse dei comandi
    private TreeMap<String, Command> commandMap = null;

    private Player player = null;

    // non mutable (da prendere clonati)
    // key -> identificativo puo' essere diverso dal nome
    private Map<String, Item> items = new TreeMap<>();
    private Map<String, Enemy> enemies = new TreeMap<>();

    // da vedere
    private List<Npc> npcs = new ArrayList<>();

    // mutable da inserire resto info dal salvataggio, prima stanza e' quella iniziale
    private Map<String, Room> rooms = new TreeMap<>();

    public Game() {
        // carica dati base di gioco

        // carica dati comandi
        initCommandMap();

        items = GameLoader.loadItems();
        enemies = GameLoader.loadEnemies(items);
        // carica dati oggetti
        //ItemsLoader itemsLoader = new ItemsLoader();
        //items = itemsLoader.loadItems();
        // carica dati mostri
        //EnemiesLoader enemiesLoader = new EnemiesLoader();
        //enemies = enemiesLoader.loadEnemies(items);

        // npc todo
        // rooms
    }

    // metodo dove verra' eseguito il gioco
    public void run() {
        System.out.println(player.getRoom().getDescription());

        // loop che continua a chiedere comandi e a parsarli finche` non esce
        Command command = null;

        do {
            // prendo input
            String userCommand = InputReader.getInput();

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
        String name = InputReader.getInput();

        logger.info(name);

        // crea player
        player = new Player(name, "giocatore", 30, 5, 5, null, null);
        creationDate = System.currentTimeMillis();

        // setup stanze
        rooms = GameLoader.loadRooms(items, enemies);
        player.setRoom(rooms.values().stream().findFirst().get());

        // salvataggio iniziale (file save)
        //save();

        // crea record in file giochi
        saveNewGame();
    }

    public void load() {
        // carica gioco esistete da json

        // path todo da ritoccare in base a menu di load
        String filePath = "datas\\" + player.getName() + "_" + creationDate + ".json";

        try {
            ObjectMapper mapper = new ObjectMapper();
            Save save = mapper.readValue(new File(filePath), Save.class);
            logger.info(save.getPlayer().getHealth() + "");
        }
        catch (IOException ex) { logger.severe(ex.getMessage());}
    }

    private void saveNewGame (){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        Map<Long, String> map = new HashMap<>();

        // Controlla se il file esiste
        File file = new File("data\\saves.json");
        if (file.exists()) {
            // Leggi il file JSON e deserializza nella mappa
            try {
                map = mapper.readValue(file, new TypeReference<Map<Long, String>>() {});
                logger.info("File letto con successo");
            } catch (IOException ex) {
                logger.severe(ex.getMessage());
            }
        }

        // Aggiungi un nuovo elemento alla mappa
        map.put(creationDate, player.getName());

        // Scrivi la mappa aggiornata nel file JSON
        try {
            mapper.writeValue(file, map);
            logger.info("Mappa aggiornata e scritta nel file con successo.");
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
        }
    }

    private void save() {
        // path todo da ritoccare in base a menu di load
        String filePath = "datas\\" + player.getName() + "_" + creationDate + ".json";

        // salva gioco su file

        PlayerSave playerSave = new PlayerSave();
        playerSave.setHealth(10);
        playerSave.setInventory(new String[] {"spada", "cuscino"});

        RoomSave roomSave = new RoomSave();
        roomSave.setItems(new String[] {"spada", "cuscino"});
        roomSave.setEnemies(new String[] {"mostro", "scheletro"});

        RoomSave roomSave2 = new RoomSave();
        roomSave2.setItems(new String[] {"spada", "cuscino"});
        roomSave2.setEnemies(new String[] {"mostro", "scheletro"});

        Save save = new Save();
        save.setPlayer(playerSave);
        //save.setRooms(new RoomSave[] {roomSave, roomSave2});

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(filePath), save);
        }
        catch (IOException _) {}
    }

    private void initCommandMap() {
        commandMap = new TreeMap<>();

        // inserisco nella mappa tutti gli alias del comando come chiave
        for (var command : COMMANDS)
            for (var alias : command.getAliases())
                commandMap.put(alias, command);
    }

    // carica dati stanze
    private void loadRooms() {
        // stanze todo manca roba esaminabile
        Room uno = new Room("Iniziale", "L'aria e' fredda...");
        Room due = new Room("Seconda", "L'aria e' calda...");

        // stanze adiacenti
        //uno.setNorth(due);
        //due.setSouth(uno);

        // oggetti in stanza
        // mostri in stanza
        // esaminabili in stanza

        // carica in lista
//        rooms.add(uno);
//        rooms.add(due);
    }

    // riempie stanze con la roba per un nuovo gioco
    private void setupNewGameRooms(){
//        ArrayList<Item> itemsToAdd = new ArrayList<>();
//        ArrayList<Enemy> enemiesToAdd = new ArrayList<>();
//
//        // trova stanza a cui aggiungere roba
//        Room uno = rooms.stream().filter(x -> x.getName().equalsIgnoreCase("iniziale")).findFirst().get();
//
//        // clear liste di supporto
//        itemsToAdd.clear();
//        enemiesToAdd.clear();
//
//        // aggiungo oggetti
//        //itemsToAdd.add(items.stream().filter(x -> x.getName().equalsIgnoreCase("pane")).findFirst().get().clone());
//        // aggiungo mostri
//        //enemiesToAdd.add(enemies.stream().filter(x -> x.getName().equalsIgnoreCase("scheletro")).findFirst().get().clone());
//
//        // aggiungo a stanza
//        uno.setItems(itemsToAdd);
//        uno.setEnemies(enemiesToAdd);
    }

    // region COMMAND HANDLER

    private void handleCommand(Command command){
        if (command == null || command.getId() == CommandId.EXIT_GAME)
            return;

        // aggiungi comandi su switch
        switch (command.getId()) {
            case CommandId.HELP -> commandHelp();
            case CommandId.MOVE -> commandMove(command.getArgs()[0]);
            default -> throw new RuntimeException("Command not implemented");
        }
    }

    private void commandHelp() {
        String output = "Lista di tutti i comandi possibili\n\n";
        // todo in caso aggiungi name a command per roba tipo >> v <direzione> - descrizione
        for (var command : COMMANDS)
            output += command.getAliases().getFirst() + " - " + command.getDescription() + "\n";

        System.out.print(output);
    }
    private void commandMove(String arg){

        Room nextRoom = null;

        // gestisco spostamento, se voglio fare sinonimi di arg es. 'nord' e 'n' da gestirlo con alias in command
        switch (arg)
        {
            case "nord" :
                nextRoom = player.getRoom().getNearRooms()[0];
                break;
            case "sud" :
                nextRoom = player.getRoom().getNearRooms()[1];
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
