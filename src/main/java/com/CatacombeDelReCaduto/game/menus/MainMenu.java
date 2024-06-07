package com.CatacombeDelReCaduto.game.menus;

import com.CatacombeDelReCaduto.game.Game;
import com.CatacombeDelReCaduto.game.jsonHandlers.BucketManager;
import com.CatacombeDelReCaduto.game.jsonHandlers.FilesManager;
import com.CatacombeDelReCaduto.game.prompts.*;

import java.util.List;

/**
 * Mostra menu di gestione delle partite
 */
public class MainMenu extends CommandMenu {

    public MainMenu() {
        // l'ordine dei comandi e' importante per la stampa del menu
        super(List.of(
            new Command(CommandId.NEW_GAME, List.of("inizia", "inizia partita"), "Inizia una nuova partita")
            ,new Command(CommandId.LOAD_GAME, List.of("carica", "carica partita"), "Carica una partita")
            ,new Command(CommandId.DELETE_GAME, List.of("elimina", "elimina partita"), "Elimina una partita")
            ,new Command(CommandId.EXIT_GAME, List.of("esci", "esci gioco"), "Esci dal gioco")
        ));
    }

    // region comandi MainMenu
    
    protected boolean commandsHandler(Command command){
        return switch (command.getId()) {
            case NEW_GAME -> commandStartNewGame();
            case LOAD_GAME -> commandLoadGame();
            case DELETE_GAME -> commandDeleteGame();
            case EXIT_GAME -> true;
            default -> throw new IllegalArgumentException("Command not implemented");
        };
    }

    /**
     * Avvia una nuova partita.
     * @return true poiché è necessario terminare l'esecuzione del menu dopo l'esecuzione della partita
     */
    private boolean commandStartNewGame() {
        // carica dati nuovo gioco
        Game game = new Game();
        // avvia gioco
        game.run();

        return true;
    }

    /**
     * Carica una partita esistente.
     * @return true se il caricamento è andato a buon fine, false altrimenti
     */
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

    /**
     * Elimina una partita esistente.
     * @return false poiché non è necessario terminare l'esecuzione del menu dopo l'eliminazione di una partita
     */
    private boolean commandDeleteGame() {
        // mostra menu elimina parita e elimina partita
        DeleteMenu deleteMenu = new DeleteMenu();
        deleteMenu.display();
        return false;
    }

    // endregion
}
