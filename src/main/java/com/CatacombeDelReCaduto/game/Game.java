package com.CatacombeDelReCaduto.game;

import com.CatacombeDelReCaduto.game.entities.*;
import com.CatacombeDelReCaduto.game.items.*;
import com.CatacombeDelReCaduto.game.jsonHandlers.*;
import com.CatacombeDelReCaduto.game.menus.BattleMenu;
import com.CatacombeDelReCaduto.game.prompts.InputReader;
import com.CatacombeDelReCaduto.game.prompts.Command;
import com.CatacombeDelReCaduto.game.prompts.CommandId;
import com.CatacombeDelReCaduto.game.rooms.Room;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Game {
    // comandi di gioco
    private final List<Command> commands = List.of(
            new Command(CommandId.EXIT_GAME, List.of("esci", "esci partita"), "esci - Esci dalla partita")
            ,new Command(CommandId.HELP, List.of("help", "aiuto", "comandi", "lista comandi"), "help - Mostra tutti i comandi")
            ,new Command(CommandId.SAVE, List.of("s", "salva", "salva partita", "save"), "salva - Salva la partita")
            ,new Command(CommandId.MOVE, List.of("vai"), "vai <direzione> - Spostati in un'altra stanza", 1)
            ,new Command(CommandId.TAKE, List.of("p", "prendi"), "p <oggetto> - Prendi oggetto", 1)
            ,new Command(CommandId.USE, List.of("u", "usa"), "u <oggetto> - Usa oggetto", 1)
            ,new Command(CommandId.THROW, List.of("b", "butta"), "b <oggetto> - Butta oggetto", 1)
            ,new Command(CommandId.EQUIP, List.of("e", "equipaggia"), "e <oggetto> - Equipaggia oggetto", 1)
            ,new Command(CommandId.UNEQUIP, List.of("d", "disequipaggia"), "d <oggetto> - Togli oggetto dall'equipaggiamento", 1)
            ,new Command(CommandId.EXAMINE, List.of("esamina"), "esamina <elemento> - Esamina un elemento nella stanza o la stanza stessa", 1)
            ,new Command(CommandId.VIEW, List.of("v","visualizza"), "v <'inventario'/'stato'> - Visualizza l'inventario o lo stato del giocatore", 1)
            ,new Command(CommandId.BACK, List.of("back"), "back - Torna alla stanza precedente", 0)
            ,new Command(CommandId.LOOK, List.of("g","guarda"), "g <'stanze'/'oggetti'/'esaminabili'> - Guarda le stanze nei dintorni/gli oggetti presenti nella stanza/gli esaminabili presenti nella stanza", 1)
            ,new Command(CommandId.DETAIL, List.of("dettagli"), "dettagli <oggetto> - Vedi dettagli di un oggetto nella stanza o nell'inventario", 1));
    // mappa per il parse dei comandi
    private TreeMap<String, Command> commandMap = null;

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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Inizia il gioco
     */
    public void run() {
        // handle start nuovo gioco
        if (player == null)
            startNew();

        // controllo se il giocatore ha vinto
        if (player.getRoom().getName().equals("Sala del tesoro")){
            System.out.println("Hai già concluso la tua avventura con score=" + player.getScore());
            return;
        }

        System.out.println(player.getRoom().getDescription());

        // loop che continua a chiedere comandi e a parsarli finche` non esce
        Command command = null;

        try {
            do {
                // possibile mostro che ti attacca todo da gestire caso di vittoria (sconfitta del boss finale)
                if (command != null && command.getId() != CommandId.HELP && player.getRoom().isEnemyEngaging())
                    battle(player.getRoom().getEnemies().values().stream().findFirst().get());

                // prendo input
                String userCommand = InputReader.getInput();

                // parse command
                command = Command.parse(userCommand, commandMap);

                // gestisce comando richiamando metodo corretto
                handleCommand(command);

            } while (command == null || command.getId() != CommandId.EXIT_GAME);
        }catch (WinException e){
            // outro
            System.out.println("L'eroe sconfigge il re corrotto, spezzando la maledizione delle catacombe, " +
                    "\ncon la morte del re, la magia oscura inizia a dissolversi." +
                    "\nL'eroe raccoglie tesori e artefatti antichi, quindi si dirige verso l'uscita delle catacombe, ora meno minacciose...");

            System.out.println("Congraturazioni hai vinto! Score=" + player.getScore());
            commandSave();
        }
    }

    /**
     * Inizia nuovo gioco
     */
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
        player.setRoom(rooms.get("Entrata"));

        // salvataggio iniziale (file save)
        commandSave();

        // crea record in file giochi
        FilesManager.saveNewGame(player);

        // carica file saves
        try (BucketManager bucket = BucketManager.loadExistConnection()){
            // aggiorna file salvataggi
            bucket.uploadFile(FilesManager.SAVES_FILE_NAME, FilesManager.SAVES_FILE_PATH);
        }
    }

    /**
     * Carica il gioco
     * @param creationDate id gioco
     * @param playerName nome giocatore
     */
    private void load(Long creationDate, String playerName){
        // carica gioco
        Save save = FilesManager.loadGame(FilesManager.gameFileName(creationDate, playerName));

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
        for (var command : commands)
            for (var alias : command.getAliases())
                commandMap.put(alias, command);
    }

    /**
     * Gestisce il combattimento
     * @param enemy nemico da combattere
     */
    private void battle(Enemy enemy) throws WinException {
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
            System.out.println("Sei morto, il gioco verra` ricaricato dall'ultimo salvataggio");
            // carico gioco da ultimo salvataggio
            load(player.CREATION_DATE, player.getName());
            System.out.println(player.getRoom().getDescription());
        }
        // nemico sconfitto
        else if (!enemy.isAlive()){
            // elimina nemico dalla stanza e sposta drop
            player.getRoom().removeEnemy(enemy);

            System.out.println("Nemico sconfitto");
            // add score
            // miniboss
            if (enemy.getName().equalsIgnoreCase("golem") || enemy.getName().equalsIgnoreCase("chimera"))
                player.addScore(5);
            // boss
            else if (enemy.getName().equalsIgnoreCase("lich")) {
                player.addScore(10);
                // vittoria
                throw new WinException();
            }
            // nemico
            else
                player.addScore(1);
            // show drop
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
            case SAVE -> commandSave();
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

    /**
     * Salva il gioco
     */
    private void commandSave() {
        // salva gioco su file
        try {
            // dati giocatore da salvare
            PlayerSave playerSave = player.save();

            // dati stanze da salvare
            Map<String, RoomSave> roomsSave = new TreeMap<>();
            for (var entry : rooms.entrySet()) {
                roomsSave.put(entry.getKey(), entry.getValue().save());
            }

            // crea Salvataggio
            Save save = new Save();
            save.setPlayer(playerSave);
            save.setRooms(roomsSave);

            // salva sul file
            FilesManager.saveGame(player.getSaveFileName(), save);

            // carica file
            try (BucketManager bucket = BucketManager.loadExistConnection()) {
                // aggiorna file salvataggio
                bucket.uploadFile(player.getSaveFileName(), FilesManager.PLAYER_ROOT + "\\" + player.getSaveFileName());
            }
            System.out.println("Gioco salvato");
        }catch (Exception e){
            System.out.println("Impossibile salvare il gioco");
            e.printStackTrace();
        }
    }

    /**
     * Stampa una lista di tutti i comandi possibili.
     */
    private void commandHelp() {
        String output = "Lista di tutti i comandi possibili\n\n";
        for (var command : commands)
            output += command.getDescription() + "\n";

        System.out.print(output);
    }

    /**
     * Sposta il giocatore nella direzione specificata.
     *
     * @param arg la direzione in cui il giocatore deve muoversi
     */
    void commandMove(String arg) {
        Room nextRoom = null;
        String prevDirection = player.getPreviousRoomDirection();
        switch (arg) {
            case "nord":
                nextRoom = player.getRoom().getNearRooms()[0];
                player.setPreviousRoomDirection("sud");
                break;
            case "sud":
                nextRoom = player.getRoom().getNearRooms()[1];
                player.setPreviousRoomDirection("nord");
                break;
            case "est":
                nextRoom = player.getRoom().getNearRooms()[2];
                player.setPreviousRoomDirection("ovest");
                break;
            case "ovest":
                nextRoom = player.getRoom().getNearRooms()[3];
                player.setPreviousRoomDirection("est");
                break;
            default:
                System.out.println("Direzione inesistente");
                return;
        }
        try {
            if (nextRoom != null) {
                // caso sala del tesoro (stanza accessibile sotto condizioni)
                if (nextRoom.getName().equals("Sala del tesoro")) {
                    if (!player.isBossRoomOpen()) {
                        System.out.println("Non puoi passare, il passaggio sembra bloccato da un meccanismo");
                        return;
                    }
                }

                player.setRoom(nextRoom);
                System.out.println(player.getRoom().getDescription());
            } else {
                System.out.println("Non c'è nessuna stanza in questa direzione, provane un'altra");
            }
        } finally {
            // restore valore se non c'è stato lo spostamento
            if (player.getRoom() != nextRoom)
                player.setPreviousRoomDirection(prevDirection);
        }
    }

    /**
     * Permette al giocatore di raccogliere un oggetto dalla stanza corrente.
     *
     * @param arg il nome dell'oggetto da raccogliere
     */
    void commandTake(String arg) {
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

    /**
     * Permette al giocatore di utilizzare un oggetto specificato.
     *
     * @param arg il nome dell'oggetto da utilizzare
     */
    boolean commandUse(String arg) {
        return player.use(arg);
    }

    /**
     * Permette al giocatore di buttare un oggetto specificato.
     *
     * @param arg il nome dell'oggetto da buttare
     */
    void commandThrow(String arg) {
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

    /**
     * Permette al giocatore di equipaggiare un oggetto specificato.
     *
     * @param arg il nome dell'oggetto da equipaggiare
     */
    void commandEquip(String arg) {
        Inventory inventory = player.getInventory();
        Item toEquip = inventory.removeItem(arg);
        if (toEquip == null) {
            System.out.println("Impossibile equipaggiare l'oggetto: non è presente nell'inventario");
        } else {
            if (toEquip instanceof Weapon) {
                Weapon currentWeapon = player.getWeapon();
                if (currentWeapon != null) {
                    System.out.println("Impossibile equipaggiare l'arma: è già equipaggiata un'arma");
                    inventory.addItem(toEquip);
                } else {
                    player.setWeapon((Weapon) toEquip);
                    System.out.println(toEquip.getName() + " equipaggiato con successo");
                }
            } else if (toEquip instanceof Armor) {
                Armor currentArmor = player.getArmor();
                if (currentArmor != null) {
                    System.out.println("Impossibile equipaggiare l'armatura: è già equipaggiata un'armatura");
                    inventory.addItem(toEquip);
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

    /**
     * Permette al giocatore di rimuovere un oggetto equipaggiato specificato.
     *
     * @param arg il nome dell'oggetto da rimuovere dall'equipaggiamento
     */
    void commandUnequip(String arg) {
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

    /**
     * Permette al giocatore di esaminare un oggetto specificato nella stanza corrente.
     *
     * @param arg il nome dell'oggetto da esaminare
     */
    void commandExamine(String arg) {
        Room currentRoom = player.getRoom();
        String desc = currentRoom.getExaminables().get(arg);
        if (desc == null) {
            System.out.println("Non esaminabile");
        } else {
            System.out.println(desc);
            // gestisce caso speciale (esamina sarcofago)
            if (arg.equalsIgnoreCase("sarcofago")){
                // aggiunge medaglione alla stanza
                currentRoom.getItems().add(items.get("medaglione del re"));
                System.out.print("oggetti nella stanza: ");
                // visualizza oggetti nella stanza
                commandLook("oggetti");
            }
        }
    }

    /**
     * Permette al giocatore di visualizzare lo stato o l'inventario.
     *
     * @param arg il tipo di visualizzazione (stato o inventario)
     */
    void commandView(String arg) {
        if (arg.equalsIgnoreCase("inventario")) {
            System.out.println(player.getInventory());
        } else if (arg.equalsIgnoreCase("stato")) {
            System.out.println(player);
        }
        else
            System.out.println("Comando incorretto : puoi visualizzare 'stato' o 'inventario'");
    }

    /**
     * Permette al giocatore di tornare nella stanza precedente (se possibile).
     */
    void commandBack() {
        if (player.getPreviousRoomDirection() == null) {
            System.out.println("Non e' possibile tornare indietro, non ti sei mai spostato!");
        } else {
            commandMove(player.getPreviousRoomDirection());
        }
    }

    /**
     * Permette al giocatore di guardare gli oggetti, le stanze o gli esaminabili nella stanza corrente.
     *
     * @param arg Cosa si intende guardare (oggetti, stanze o esaminabili)
     */
    void commandLook(String arg) {
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
            System.out.print(player.getRoom().printNearRooms());
        } else if (arg.equalsIgnoreCase("esaminabili")) {
            System.out.println(player.getRoom().printExaminables());
        } else {
            System.out.println("Argomento non valido");
        }
    }

    /**
     * Fornisce i dettagli di un oggetto specificato nell'inventario o nella stanza corrente.
     *
     * @param arg il nome dell'oggetto di cui visualizzare i dettagli
     */
    void commandDetail(String arg) {
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
