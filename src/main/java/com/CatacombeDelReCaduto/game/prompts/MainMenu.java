package com.CatacombeDelReCaduto.game.prompts;

import com.CatacombeDelReCaduto.game.Game;

import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Logger;

public class MainMenu {
    // l'ordine dei comandi e' importante per la stampa del menu
    public static final List<Command> COMMANDS = List.of(
            new Command(CommandId.NEW_GAME, List.of("inizia", "inizia partita"), "Inizia una nuova partita")
            ,new Command(CommandId.LOAD_GAME, List.of("carica", "carica partita"), "Carica una partita"));

    private final Logger logger =  Logger.getLogger(this.getClass().getName());
    private TreeMap<String, Command> commandMap = null;

    public MainMenu() {
        initCommandMap();
    }

    public void run() {
        Command command = null;

        String userCommand = "";
        do {
            // stampo menu
            print();

            // prendo input
            userCommand = CmdHandler.getInput();

            // verifico se l'utente ha inputato il numero del menu
            try {
                int choice = Integer.parseInt(userCommand.trim());
                if (choice > 0 && choice <= COMMANDS.size())
                    command = COMMANDS.get(choice - 1);
            } catch (NumberFormatException _) {
            }

            // se comando non trovato parso il linguaggio
            if (command == null)
                command = Command.parse(userCommand, commandMap);

        } while (command == null);

        switch (command.getId()) {
            case CommandId.NEW_GAME -> commandStartNewGame();
            case CommandId.LOAD_GAME -> commandLoadGame();
            case CommandId.DELETE_GAME -> commandDeleteGame();
            case CommandId.EXIT_GAME -> commandExit();
            default -> throw new RuntimeException("Command not implemented");
        }
    }

    private void initCommandMap() {
        commandMap = new TreeMap<>();

        // inserisco nella mappa tutti gli alias del comando come chiave
        for (var command : COMMANDS)
            for (var alias : command.getAliases())
                commandMap.put(alias, command);
    }

    private void print() {
        String menu = "";

        int i = 0;
        for (var command : COMMANDS) {
            i++;
            menu += "("+i+") " + command.getDescription() + "\n";
        }

        System.out.print(menu);
    }

    // traduce il comando dato in un Command
    private Command parse(String userCommand) {
        String command = Command.removeNaturalText(userCommand.toLowerCase().trim());

        if (!command.isEmpty())
            for (var entry : commandMap.entrySet()) {
                if (command.equals(entry.getKey()))
                    return entry.getValue();
            }

        System.out.println("Comando non riconosciuto");
        return null;
    }

    // region comandi MainMenu

    private void commandStartNewGame() {
        // inizia una nuova partita
        logger.info("start");

        // carica dati gioco
        Game game = new Game();
        // carica dati nuovo gioco
        game.startNew();
        // avvia gioco
        game.run();
    }

    private void commandLoadGame() {
        logger.info("load");
        // guarda partite salvate
        // mostra menu per decidere quale partita caricare
        // carica la partita scelta
        // avvia partita
    }

    private void commandDeleteGame() {
        logger.info("delete");
        // mostra menu partite
        // elimina partita scelta
        // rimostra menu iniziale??
    }

    private void commandExit() {
        // esce dal gioco
        logger.info("exit");
    }

    // endregion
}
