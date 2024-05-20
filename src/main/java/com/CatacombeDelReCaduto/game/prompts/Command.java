package com.CatacombeDelReCaduto.game.prompts;

import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * Rappresenta un comando di gioco
 */
public class Command {
    private final CommandId id;
    private final List<String> aliases;
    private final String description;
    private final int argsNum;
    // i parametri, sono mutabili in base a quanto inserito dall'utente
    private String[] args = null;

    public Command(CommandId id, List<String> aliases, String description) {
        this(id, aliases, description, 0);
    }

    public Command(CommandId id, List<String> aliases, String description, int argsNum) {
        this.id = id;
        this.aliases = Collections.unmodifiableList(aliases);
        this.description = description;
        this.argsNum = argsNum;
    }

    public CommandId getId() {
        return id;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public int getArgsNum() {
        return argsNum;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        if (args.length != argsNum)
            throw new IllegalArgumentException("args length doesn't match with argsNum");

        this.args = args;
    }

    /**
     * Rimuove articoli significativi dal comando
     * @param command comando inputato dall'utente
     * @return comando senza articoli
     */
    public static String removeNaturalText(String command) {
        command = command.replaceAll(" la ", " ");
        command = command.replaceAll(" il ", " ");
        command = command.replaceAll(" una ", " ");
        command = command.replaceAll(" l' ", " ");
        command = command.replaceAll(" dal ", " ");
        command = command.replaceAll(" dalla ", " ");
        return command;
    }

    /**
     * Fa il parse dello userCommand in un command
     * @param userCommand comando inputato dall'utente
     * @param commandMap mappa di supporto contenente i comandi
     * @return comando tradotto con args compilato, null se il comando non e` valido
     */
    public static Command parse(String userCommand, TreeMap<String, Command> commandMap) {
        // rimuovi testo
        userCommand = Command.removeNaturalText(userCommand.toLowerCase().trim());

        if (!userCommand.isEmpty())
            // controlla che ci sia un comando uguale nella mappa partendo dal maggiore
            for (var entry : commandMap.descendingMap().entrySet()) {
                Command command = entry.getValue();
                if (userCommand.startsWith(entry.getKey())) {
                    if (command.getArgsNum() == 0 && entry.getKey().equals(userCommand)) {
                        return command;
                    }
                    // se un parametro trovo il parametro e lo setto nel comando
                    else if (command.getArgsNum() == 1) {
                        String arg = userCommand.substring(entry.getKey().length()).trim();
                        command.setArgs(new String[] {arg});
                        return command;
                    }
                }
            }

        System.out.println("Comando non riconosciuto");
        return null;
    }
}
