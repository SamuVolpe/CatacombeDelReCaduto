package com.CatacombeDelReCaduto.game.menus;

import com.CatacombeDelReCaduto.game.prompts.Command;
import com.CatacombeDelReCaduto.game.prompts.InputReader;

import java.util.List;
import java.util.TreeMap;

/**
 * Estendere questa classe per creare un menu di gioco che e` gestito da comandi
 */
public abstract class CommandMenu extends Menu {
    // comandi del menu, vengono visualizzati nel menu in ordine
    protected List<Command> commands;
    // mappa per la gestione dei comandi
    private TreeMap<String, Command> commandMap;

    /**
     * Costruttore che inizializza i comandi del menu
     * @param commands comandi ordinati per la visualizzazione
     */
    public CommandMenu(List<Command> commands) {
        // chiamo costruttore Menu dando gli item da mostrare
        super(commands.stream().map(Command::getDescription).toList());

        // init comandi
        this.commands = commands;
        initCommandMap();
    }

    /**
     * Display del menu
     */
    public void display() {
        boolean handled = false;
        do {
            // stampo menu
            print();

            // prendo input
            String userCommand = InputReader.getInput();
            // traduco input in command
            Command command = handleInput(userCommand);

            // gestisco comando
            if (command != null)
                handled = commandsHandler(command);

        } while (!handled);
    }

    /**
     * Gestisce l'input dell'utente, traducendolo in un comando.
     * @param userCommand L'input inserito dall'utente
     * @return Il comando associato all'input, o null se non è stato riconosciuto un comando
     */
    protected Command handleInput(String userCommand) {
        Command command = null;

        // verifico se l'utente ha inputato il numero del menu
        int choice = userChoice(userCommand);

        // se comando non trovato parso il linguaggio
        if (choice < 1)
            command = Command.parse(userCommand, commandMap);
            // se comando trovato get command
        else {
            command = commands.get(choice - 1);
            // controllo che il comando preso da menu non necessiti di un'argomento
            if (command.getArgsNum() > 0){
                System.out.println("Comando non selezionabile dal menu, scrivere il comando completo");
                command = null;
            }
        }
        return command;
    }

    /**
     * Implementare switch che chiama il metodo corretto in base al comando
     * @param command comando dato dall'utente
     * @return true per uscire dal menu
     */
    protected abstract boolean commandsHandler(Command command);

    /**
     * Inizializza mappa per gestione dei comandi
     */
    private void initCommandMap() {
        commandMap = new TreeMap<>();

        // inserisco nella mappa tutti gli alias del comando come chiave
        for (var command : commands)
            for (var alias : command.getAliases())
                commandMap.put(alias, command);
    }
}
