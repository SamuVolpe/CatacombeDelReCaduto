package com.CatacombeDelReCaduto.game.menus;

import com.CatacombeDelReCaduto.game.Game;
import com.CatacombeDelReCaduto.game.jsonHandlers.Save;
import com.CatacombeDelReCaduto.game.prompts.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * Mostra menu di gestione del gioco
 */
public class MainMenu extends CommandMenu {
    // l'ordine dei comandi e' importante per la stampa del menu
    private static final List<Command> COMMANDS = List.of(
            new Command(CommandId.NEW_GAME, List.of("inizia", "inizia partita"), "Inizia una nuova partita")
            ,new Command(CommandId.LOAD_GAME, List.of("carica", "carica partita"), "Carica una partita")
            ,new Command(CommandId.DELETE_GAME, List.of("elimina", "elimina partita"), "Elimina una partita")
            ,new Command(CommandId.EXIT_GAME, List.of("esci", "esci gioco"), "Esci dal gioco"));

    private final Logger logger =  Logger.getLogger(this.getClass().getName());

    public MainMenu() { super(COMMANDS); }

    public void display(){
        super.display();
    }

    // region comandi MainMenu

    protected boolean commandsHandler(Command command){
        return switch (command.getId()) {
            case CommandId.NEW_GAME -> commandStartNewGame();
            case CommandId.LOAD_GAME -> commandLoadGame();
            case CommandId.DELETE_GAME -> commandDeleteGame();
            case CommandId.EXIT_GAME -> true;
            default -> throw new IllegalArgumentException("Command not implemented");
        };
    }

    private boolean commandStartNewGame() {
        // carica dati nuovo gioco
        Game game = new Game();
        // avvia gioco
        game.run();

        return true;
    }

    private boolean commandLoadGame() {
        // mostra menu per decidere quale partita caricare
        LoadMenu loadMenu = new LoadMenu();
        var chosedGame = loadMenu.display();

        // nessuna partita scelta
        if (chosedGame == null)
            return false;

        // carica dati gioco
        Game game = new Game(chosedGame.getKey(), chosedGame.getValue());
        // avvia gioco
        game.run();

        return true;
    }

    private boolean commandDeleteGame() {
        // mostra menu elimina parita e elimina partita
        DeleteMenu deleteMenu = new DeleteMenu();
        deleteMenu.display();
        return false;
    }

    // endregion
}
