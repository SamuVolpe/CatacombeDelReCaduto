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
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class Game {
    public static final Logger logger =  Logger.getLogger(Game.class.getName());

    // data file di salvataggio
    private long creationDate;

    // comandi di gioco
    public static final List<Command> COMMANDS = List.of(
            new Command(CommandId.EXIT_GAME, List.of("esci", "esci partita"), "esci - Inizia una nuova partita")
            ,new Command(CommandId.HELP, List.of("aiuto", "comandi", "lista comandi"), "aiuto - Mostra tutti i comandi")
            ,new Command(CommandId.MOVE, List.of("v", "vai"), "v <direzione> - Spostati in un'altra stanza", 1)
            ,new Command(CommandId.TAKE, List.of("p", "prendi"), "p <oggetto> - Prendi oggetto", 1)
            ,new Command(CommandId.USE, List.of("u", "usa"), "u <oggetto> - Usa oggetto", 1)
            ,new Command(CommandId.THROW, List.of("b", "butta"), "b <oggetto> - Butta oggetto", 1)
            ,new Command(CommandId.EQUIP, List.of("e", "equipaggia"), "e <oggetto> - Equipaggia oggetto", 1)
            ,new Command(CommandId.UNEQUIP, List.of("d", "disequipaggia"), "d <oggetto> - Togli oggetto dall'equipaggiamento", 1)
            ,new Command(CommandId.EXAMINE, List.of("e", "esamina"), "e <oggetto> - Esamina oggetto", 1));
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

        // crea player
        player = new Player(name, "giocatore", 30, 5, 5, null, null);
        creationDate = System.currentTimeMillis();

        // setup stanze
        rooms = GameLoader.loadRooms(items, enemies);
        player.setRoom(rooms.get("inizio"));

        // crea cartella di salvataggio se non esiste
        File directory = new File("data\\player");
        if (!directory.exists()) {
            boolean maked = directory.mkdir();
            if (!maked)
                throw new RuntimeException("Impossibile creare la cartella per il salvataggio dei dati, verificare i permessi del programma");
        }

        // salvataggio iniziale (file save)
        save();

        // crea record in file giochi
        saveNewGame();
    }

    public void load() {
        // carica gioco esistete da json

        // path todo da ritoccare in base a menu di load
        String filePath = "data\\" + player.getName() + "_" + creationDate + ".json";

        try {
            ObjectMapper mapper = new ObjectMapper();
            Save save = mapper.readValue(new File(filePath), Save.class);
            logger.info(save.getPlayer().getHealth() + "");
        }
        catch (IOException ex) { logger.severe(ex.getMessage());}
    }

    private void saveNewGame (){
        ObjectMapper mapper = new ObjectMapper();
        Map<Long, String> map = new HashMap<>();

        // Controlla se il file esiste
        File file = new File(FilesPath.SAVES_FILE_PATH);
        if (file.exists()) {
            // Leggi il file JSON e deserializza nella mappa
            try {
                map = mapper.readValue(file, new TypeReference<Map<Long, String>>() {});
                logger.info("saves file readed");
            } catch (IOException ex) {
                logger.severe(ex.getMessage());
            }
        }

        // Aggiungi un nuovo elemento alla mappa
        map.put(creationDate, player.getName());

        // Scrivi la mappa aggiornata nel file JSON
        try {
            mapper.writeValue(file, map);
            logger.info("saves updated");
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
        }
    }

    private void save() {
        // path todo da ritoccare in base a menu di load
        String filePath = FilesPath.PLAYER_ROOT + "\\" + player.getName() + "_" + creationDate + ".json";

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

    // region COMMAND HANDLER

    private void handleCommand(Command command){
        if (command == null || command.getId() == CommandId.EXIT_GAME)
            return;

        // aggiungi comandi su switch
        switch (command.getId()) {
            case CommandId.HELP -> commandHelp();
            case CommandId.MOVE -> commandMove(command.getArgs()[0]);
            case CommandId.TAKE -> commandTake(command.getArgs()[0]);
            case CommandId.USE -> commandUse(command.getArgs()[0]);
            case CommandId.THROW -> commandThrow(command.getArgs()[0]);
            case CommandId.EQUIP -> commandEquip(command.getArgs()[0]);
            case CommandId.UNEQUIP -> commandUnequip(command.getArgs()[0]);
            case CommandId.EXAMINE -> commandExamine(command.getArgs()[0]);
            default -> throw new RuntimeException("Command not implemented");
        }
    }

    private void commandHelp() {
        String output = "Lista di tutti i comandi possibili\n\n";
        // todo in caso aggiungi name a command per roba tipo >> v <direzione> - descrizione
        for (var command : COMMANDS)
            output += command.getDescription() + "\n";

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

    private void commandTake(String arg) {
        //se in player.getroom.items c'è l'item da prendere faccio un inventory.addItem e un Room.removeItem (se non è troppo pesante)
        Room currentRoom = player.getRoom();
        List<Item> currentRoomItems = currentRoom.getItems();
        Item toTake;
        try {
            toTake = currentRoomItems.stream().filter(x -> x.getName().equalsIgnoreCase(arg)).findFirst().get();
        } catch (NullPointerException | NoSuchElementException a) {
            System.out.println("Impossibile raccogliere l'oggetto: l'oggetto non e' presente nella stanza");
            return;
        }
        Inventory inventory = player.getInventory();
        boolean flag = inventory.addItem(toTake);
        if (flag) {
            currentRoomItems.remove(toTake);
        } else {
            System.out.println("Impossibile raccogliere l'oggetto: l'oggetto e' troppo pesante per l'inventario, e' necessario alleggerirlo");
        }
    }

    private void commandUse(String arg) {
    }

    private void commandThrow(String arg) {
    }

    private void commandEquip(String arg) {
    }

    private void commandUnequip(String arg) {
    }

    private void commandExamine(String arg) {
    }

    // endregion
}
