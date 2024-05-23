package com.CatacombeDelReCaduto.game;

import com.CatacombeDelReCaduto.game.entities.*;
import com.CatacombeDelReCaduto.game.items.*;
import com.CatacombeDelReCaduto.game.jsonHandlers.*;
import com.CatacombeDelReCaduto.game.menus.BattleMenu;
import com.CatacombeDelReCaduto.game.prompts.InputReader;
import com.CatacombeDelReCaduto.game.prompts.Command;
import com.CatacombeDelReCaduto.game.prompts.CommandId;
import com.CatacombeDelReCaduto.game.rooms.Room;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Game {

    // data file di salvataggio
    private long creationDate;

    // comandi di gioco
    public static final List<Command> COMMANDS = List.of(
            new Command(CommandId.EXIT_GAME, List.of("esci", "esci partita"), "esci - Esci dalla partita")
            ,new Command(CommandId.HELP, List.of("help", "aiuto", "comandi", "lista comandi"), "aiuto - Mostra tutti i comandi")
            ,new Command(CommandId.SAVE, List.of("s", "salva", "salva partita"), "salva - Salva la partita")
            ,new Command(CommandId.MOVE, List.of("vai"), "vai <direzione> - Spostati in un'altra stanza", 1)
            ,new Command(CommandId.TAKE, List.of("p", "prendi"), "p <oggetto> - Prendi oggetto", 1)
            ,new Command(CommandId.USE, List.of("u", "usa"), "u <oggetto> - Usa oggetto", 1)
            ,new Command(CommandId.THROW, List.of("b", "butta"), "b <oggetto> - Butta oggetto", 1)
            ,new Command(CommandId.EQUIP, List.of("e", "equipaggia"), "e <oggetto> - Equipaggia oggetto", 1)
            ,new Command(CommandId.UNEQUIP, List.of("d", "disequipaggia"), "d <oggetto> - Togli oggetto dall'equipaggiamento", 1)
            ,new Command(CommandId.EXAMINE, List.of("esamina"), "esamina <elemento> - Esamina un elemento nella stanza o la stanza stessa", 1)
            ,new Command(CommandId.VIEW, List.of("v","visualizza"), "v <'inventario'/'stato'> - Visualizza l'inventario o lo stato del giocatore", 1)
            ,new Command(CommandId.BACK, List.of("back"), "back - Torna alla stanza precedente", 0)
            ,new Command(CommandId.LOOK, List.of("guarda"), "guarda <'stanze'/'oggetti'/'esaminabili'> - Guarda le stanze nei dintorni/gli oggetti presenti nella stanza/gli esaminabili presenti nella stanza", 1)
            ,new Command(CommandId.DETAIL, List.of("dettagli"), "dettagli <oggetto> - Vedi dettagli di un oggetto nella stanza o nell'inventario", 1));
    // mappa per il parse dei comandi
    private TreeMap<String, Command> commandMap = null;

    private final Logger logger =  Logger.getLogger(this.getClass().getName());
    private Player player = null;

    // non mutable (da prendere clonati)
    // tutti gli oggetti e i nemici del gioco
    private Map<String, Item> items = new TreeMap<>();
    private Map<String, Enemy> enemies = new TreeMap<>();

    // mutable da inserire resto info dal salvataggio
    private Map<String, Room> rooms = new TreeMap<>();

    /**
     * Costruttore per una nuova partita
     */
    public Game() {
        this(null, null);
    }

    /**
     * Costruttore per una partita esistente
     * @param creationDate id partita
     * @param playerName nome giocatore
     */
    public Game(Long creationDate, String playerName) {
        // carica dati base di gioco

        // carica dati comandi
        initCommandMap();
        // carica oggetti
        items = FilesManager.loadItems();
        // carica nemici
        enemies = FilesManager.loadEnemies(items);

        if (creationDate == null)
            // carica stanze con dati d'inizio
            rooms = FilesManager.loadRooms(items, enemies);
        else{
            // carica stanze con dati basici
            rooms = FilesManager.loadRooms();
            // carica salvataggio
            load(creationDate, playerName);
        }
    }

    /**
     * Inizia il gioco
     */
    public void run() {
        // handle start nuovo gioco
        if (player == null)
            startNew();

        System.out.println(player.getRoom().getDescription());

        // loop che continua a chiedere comandi e a parsarli finche` non esce
        Command command = null;

        do {
            // possibile mostro che ti attacca todo da gestire caso di vittoria (sconfitta del boss finale)
            if (player.getRoom().isEnemyEngaging())
                battle(player.getRoom().getEnemies().values().stream().findFirst().get());

            // prendo input
            String userCommand = InputReader.getInput();

            // parse command
            command = Command.parse(userCommand, commandMap);

            // gestisce comando richiamando metodo corretto
            handleCommand(command);

        } while (command == null || command.getId() != CommandId.EXIT_GAME);
    }

    private void startNew() {
        // inizia nuovo gioco da 0

        // chiedi nome giocatore
        System.out.println("Benvenuto avventuriero! come ti chiami?");

        String name = null;
        // pattern di controllo del nome
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]{1,10}");
        // chiedo nome finche` non e` valido
        do{
            if (name != null)
                System.out.println("Nome non valido, il nome deve contenere solo lettere e/o numeri ed essere lungo da 1 a 10 caratteri");

            name = InputReader.getInput();
        }while (!pattern.matcher(name).matches());

        // crea player
        player = new Player(System.currentTimeMillis(), name, null, null);

        // setup stanza d'inizio
        player.setRoom(rooms.get("inizio"));

        // crea cartella di salvataggio se non esiste
        File directory = new File(FilesPath.PLAYER_ROOT);
        if (!directory.exists()) {
            boolean maked = directory.mkdir();
            if (!maked)
                throw new RuntimeException("Impossibile creare la cartella per il salvataggio dei dati");
        }

        // salvataggio iniziale (file save)
        save();

        // crea record in file giochi
        FilesManager.saveNewGame(player);
    }

    /**
     * Salva il gioco
     */
    private void save() {
        // salva gioco su file

        // dati giocatore da salvare
        PlayerSave playerSave = player.save();

        // dati stanze da salvare
        Map<String, RoomSave> roomsSave = new TreeMap<>();
        for (var entry : rooms.entrySet()){
            roomsSave.put(entry.getKey(), entry.getValue().save());
        }

        // crea Salvataggio
        Save save = new Save();
        save.setPlayer(playerSave);
        save.setRooms(roomsSave);

        // salva sul file
        FilesManager.saveGame(player.getSaveFileName(), save);
        System.out.println("Gioco salvato");
    }

    /**
     * Carica il gioco
     * @param creationDate id gioco
     * @param playerName nome giocatore
     */
    private void load(Long creationDate, String playerName){
        // carica gioco
        Save save = FilesManager.loadGame(playerName + "_" + creationDate + ".json");

        // carica dati stanze
        for (var entry : rooms.entrySet()){
            entry.getValue().load(save.getRooms().get(entry.getKey()), items, enemies);
        }

        // carica dati giocatore
        Player player = new Player(creationDate, playerName);
        player.load(save.getPlayer(), items ,rooms);
        // set player
        this.player = player;

        System.out.println("Gioco caricato");
    }

    /**
     * Inizializza mappa di supporto comandi
     */
    private void initCommandMap() {
        commandMap = new TreeMap<>();

        // inserisco nella mappa tutti gli alias del comando come chiave
        for (var command : COMMANDS)
            for (var alias : command.getAliases())
                commandMap.put(alias, command);
    }

    private void battle(Enemy enemy){
        System.out.println("Sei stato attaccato!");

        // menu combattimento
        BattleMenu battle = new BattleMenu(player, enemy);
        boolean ended = battle.battle();

        // player fuggito reset del nemico
        if (!ended) {
            enemy.setHealth(enemy.getMaxHealth());
            System.out.println("Sei scappato con successo");
            return;
        }

        // player sconfitto
        if (!player.isAlive()){
            System.out.println("Sei morto, ricarica da ultimo salvataggio..");
            // carico gioco da ultimo salvataggio
            this.load(player.CREATION_DATE, player.getName());
        }
        // nemico sconfitto
        else if (!enemy.isAlive()){
            // elimina nemico dalla stanza
            player.getRoom().removeEnemy(enemy);

            System.out.println("Nemico sconfitto");
            if (!enemy.getDrop().isEmpty()) {
                System.out.println("Il nemico ha droppato i seguenti oggetti : "
                        + enemy.getDrop().stream().map(Item::getName).collect(Collectors.joining(", "))
                        + "\nRicordati di raccoglierli se ti possono servire");
            }
        }
    }

    /**
     * Gestisce i comandi del gioco
     * @param command comando utilizzato
     */
    private void handleCommand(Command command){
        if (command == null || command.getId() == CommandId.EXIT_GAME)
            return;

        // aggiungi comandi su switch
        switch (command.getId()) {
            case HELP -> commandHelp();
            case SAVE -> save();
            case MOVE -> commandMove(command.getArgs()[0]);
            case TAKE -> commandTake(command.getArgs()[0]);
            case USE -> commandUse(command.getArgs()[0]);
            case THROW -> commandThrow(command.getArgs()[0]);
            case EQUIP -> commandEquip(command.getArgs()[0]);
            case UNEQUIP -> commandUnequip(command.getArgs()[0]);
            case EXAMINE -> commandExamine(command.getArgs()[0]);
            case VIEW -> commandView(command.getArgs()[0]);
            case BACK -> commandBack();
            case LOOK -> commandLook(command.getArgs()[0]);
            case DETAIL -> commandDetail(command.getArgs()[0]);
            default -> throw new IllegalArgumentException("Command not implemented");
        }
    }

    // region commands

    private void commandHelp() {
        String output = "Lista di tutti i comandi possibili\n\n";
        for (var command : COMMANDS)
            output += command.getDescription() + "\n";

        System.out.print(output);
    }

    private void commandMove(String arg){

        Room nextRoom = null;
        String prevDir = player.getPreviousRoomDirection();
        // gestisco spostamento
        switch (arg)
        {
            case "nord" :
                nextRoom = player.getRoom().getNearRooms()[0];
                player.setPreviousRoomDirection("sud");
                break;
            case "sud" :
                nextRoom = player.getRoom().getNearRooms()[1];
                player.setPreviousRoomDirection("nord");
                break;
            case "est" :
                nextRoom = player.getRoom().getNearRooms()[2];
                player.setPreviousRoomDirection("ovest");
                break;
            case "ovest" :
                nextRoom = player.getRoom().getNearRooms()[3];
                player.setPreviousRoomDirection("est");
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
            player.setPreviousRoomDirection(prevDir);
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
            System.out.println("Oggetto raccolto con successo");
        } else {
            System.out.println("Impossibile raccogliere l'oggetto: l'oggetto e' troppo pesante (peso=" + toTake.getWeight() + ") per l'inventario, e' necessario alleggerirlo");
        }
    }

    private void commandUse(String arg) {
        player.use(arg);
    }

    private void commandThrow(String arg) {
        Room currentRoom = player.getRoom();
        List<Item> currentRoomItems = currentRoom.getItems();
        Inventory inventory = player.getInventory();
        Item toThrow = inventory.removeItem(arg);
        if (toThrow == null) {
            System.out.println("Impossibile buttare l'oggetto: non è presente nell'inventario");
        } else {
            currentRoomItems.add(toThrow);
            System.out.println("Oggetto buttato");
        }
    }

    private void commandEquip(String arg) {
        Inventory inventory = player.getInventory();
        Item toEquip = inventory.removeItem(arg);
        if (toEquip == null) {
            System.out.println("Impossibile equipaggiare l'oggetto: non è presente nell'inventario");
        } else {
            if (toEquip instanceof Weapon) {
                Weapon currentWeapon = player.getWeapon();
                if (currentWeapon != null) {
                    System.out.println("Impossibile equipaggiare l'arma: è già quipaggiata un'arma");
                } else {
                    player.setWeapon((Weapon) toEquip);
                    System.out.println(toEquip.getName() + " equipaggiato con successo");
                }
            } else if (toEquip instanceof Armor) {
                Armor currentArmor = player.getArmor();
                if (currentArmor != null) {
                    System.out.println("Impossibile equipaggiare l'armatura: è già quipaggiata un'armatura");
                } else {
                    player.setArmor((Armor) toEquip);
                    System.out.println(toEquip.getName() + " equipaggiato con successo");
                }
            } else {
                System.out.println("Impossibile equipaggiare l'oggetto: non è un oggetto equipaggiabile");
                inventory.addItem(toEquip);
            }
        }
    }

    private void commandUnequip(String arg) {
        Weapon currentWeapon = player.getWeapon();
        Armor currentArmor = player.getArmor();
        Inventory inventory = player.getInventory();
        if (currentArmor != null && currentArmor.getName().equalsIgnoreCase(arg)) {
            boolean flag = inventory.addItem(currentArmor);
            if (flag) {
                player.setArmor(null);
                System.out.println(arg + " rimosso dall'equipaggiamento");
            } else {
                System.out.println("Impossibile rimuovere " + arg + " dall'equipaggiamento: non può essere inserito nell'inventario perchè è troppo pesante, è necessario alleggerire l'inventario");
            }
        } else if (currentWeapon != null && currentWeapon.getName().equalsIgnoreCase(arg)) {
            boolean flag = inventory.addItem(currentWeapon);
            if (flag) {
                player.setWeapon(null);
                System.out.println(arg + " rimosso dall'equipaggiamento");
            } else {
                System.out.println("Impossibile rimuovere " + arg + " dall'equipaggiamento: non può essere inserito nell'inventario perchè è troppo pesante, è necessario alleggerire l'inventario");
            }
        } else {
            System.out.println("Impossibile rimuovere " + arg + " dall'equipaggiamento: non è equipaggiato");
        }
    }

    private void commandExamine(String arg) {
        Room currentRoom = player.getRoom();
        String desc = currentRoom.getExaminables().get(arg);
        if (desc == null) {
            System.out.println("Non esaminabile");
        } else {
            System.out.println(desc);
        }
    }

    private void commandView(String arg) {
        if (arg.equalsIgnoreCase("inventario")) {
            System.out.println(player.getInventory());
        } else if (arg.equalsIgnoreCase("stato")) {
            System.out.println(player);
        }
        else
            System.out.println("Comando incorretto : puoi visualizzare 'stato' o 'inventario");
    }

    private void commandBack() {
        if (player.getPreviousRoomDirection() == null) {
            System.out.println("Non e' possibile tornare indietro, non ti sei mai spostato!");
        } else {
            commandMove(player.getPreviousRoomDirection());
        }
    }

    private void commandLook(String arg) {
        if (arg.equalsIgnoreCase("oggetti")) {
            List<Item> items = player.getRoom().getItems();
            String out = "";
            for (Item it : items) {
                out += it.getName() + ", ";
            }
            if (!items.isEmpty()) {
                System.out.println(out.substring(0, out.length()-2));
            } else {
                System.out.println("Non ci sono oggetti nella stanza");
            }
        } else if (arg.equalsIgnoreCase("stanze")) {
            System.out.println(player.getRoom().printNearRooms());
        } else if (arg.equalsIgnoreCase("esaminabili")) {
            System.out.println(player.getRoom().printExaminables());
        } else {
            System.out.println("Argomento non valido");
        }
    }

    private void commandDetail(String arg) {
        Inventory inventory = player.getInventory();
        Item item = inventory.removeItem(arg);
        if (item != null) {
            System.out.println(item);
            inventory.addItem(item);
        } else {
            List<Item> roomItems = player.getRoom().getItems();
            item = null;
            for (Item it : roomItems) {
                if (it.getName().equalsIgnoreCase(arg)) {
                    item = it;
                }
            }
            if (item == null) {
                System.out.println("L'oggetto non e' presente nell'inventario e neanche nella stanza");
            } else {
                System.out.println(item);
            }
        }
    }

    // endregion
}
