package com.CatacombeDelReCaduto.game.menus;

import com.CatacombeDelReCaduto.game.Game;
import com.CatacombeDelReCaduto.game.jsonHandlers.Save;
import com.CatacombeDelReCaduto.game.prompts.*;

import java.util.List;
import java.util.logging.Logger;

public class MainMenu extends CommandMenu {
    // l'ordine dei comandi e' importante per la stampa del menu
    public static final List<Command> COMMANDS = List.of(
            new Command(CommandId.NEW_GAME, List.of("inizia", "inizia partita"), "Inizia una nuova partita")
            ,new Command(CommandId.LOAD_GAME, List.of("carica", "carica partita"), "Carica una partita")
            ,new Command(CommandId.DELETE_GAME, List.of("elimina", "elimina partita"), "Elimina una partita")
            ,new Command(CommandId.EXIT_GAME, List.of("esci", "esci gioco"), "Esci dal gioco"));

    private final Logger logger =  Logger.getLogger(this.getClass().getName());

    public MainMenu() { super(COMMANDS); }

    // region comandi MainMenu

    protected boolean commandsHandler(Command command){
        return switch (command.getId()) {
            case CommandId.NEW_GAME -> commandStartNewGame();
            case CommandId.LOAD_GAME -> commandLoadGame();
            case CommandId.DELETE_GAME -> commandDeleteGame();
            case CommandId.EXIT_GAME -> true;
            default -> throw new RuntimeException("Command not implemented");
        };
    }

    private boolean commandStartNewGame() {
        // inizia una nuova partita
        logger.info("start");

        // carica dati gioco
        Game game = new Game();
        // carica dati nuovo gioco
        game.startNew();
        // avvia gioco
        game.run();

        return true;
    }

    private boolean commandLoadGame() {
        logger.info("load");
        // guarda partite salvate
        // mostra menu per decidere quale partita caricare
        // carica la partita scelta
        // avvia partita

        LoadMenu loadMenu = new LoadMenu();
        Save loadedGameData = loadMenu.display();

        if (loadedGameData == null)
            return false;

        // handle save into game con un metodo di game

        return true;
    }

    private boolean commandDeleteGame() {
        logger.info("delete");
        // mostra menu partite
        // elimina partita scelta
        // rimostra menu iniziale??

        DeleteMenu deleteMenu = new DeleteMenu();
        deleteMenu.display();
        return false;
    }

    // endregion
}
