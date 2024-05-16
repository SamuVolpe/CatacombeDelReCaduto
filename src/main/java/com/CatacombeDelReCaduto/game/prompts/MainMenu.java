package com.CatacombeDelReCaduto.game.prompts;

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
        System.out.println("Benvenuto nelle Catacombe!\n");
        Command command = null;

        try (Scanner scanner = new Scanner(System.in)) {
            String userCommand = "";
            do {
                // stampo menu
                print();

                // prendo input
                if (scanner.hasNextLine())
                    userCommand = scanner.nextLine();

                // verifico se l'utente ha inputato il numero del menu
                try {
                    int choice = Integer.parseInt(userCommand.trim());
                    if (choice > 0 && choice <= COMMANDS.size())
                        command = COMMANDS.get(choice);
                } catch (NumberFormatException e) {
                    command = null;
                }

                // se comando non trovato parso il linguaggio
                if (command == null)
                    command = parse(userCommand);

            }while (command == null);
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }

        switch (command.getCommandId()) {
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
    }

    private void commandLoadGame() {
        // guarda partite salvate
        // mostra menu per decidere quale partita caricare
        // carica la partita scelta
        logger.info("load");
    }

    private void commandDeleteGame() {
        // mostra menu partite
        // elimina partita scelta
        logger.info("delete");
    }

    private void commandExit() {
        // esce dal gioco
        logger.info("exit");
    }

    // endregion
}
