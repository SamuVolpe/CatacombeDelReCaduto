package com.CatacombeDelReCaduto.game.prompts;

import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class Command {
    private CommandId id;
    private List<String> aliases;
    private String description;
    private int paramsNum = 0;
    private String[] args = null;

    public Command(CommandId id, List<String> aliases, String description) {
        this.id = id;
        this.aliases = Collections.unmodifiableList(aliases);
        this.description = description;
    }

    public Command(CommandId id, List<String> aliases, String description, int paramsNum) {
        this(id, aliases, description);
        this.paramsNum = paramsNum;
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

    public int getParamsNum() {
        return paramsNum;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public static String removeNaturalText(String command) {
        command = command.replaceAll(" la ", " ");
        command = command.replaceAll(" il ", " ");
        command = command.replaceAll(" una ", " ");
        command = command.replaceAll(" l' ", " ");
        command = command.replaceAll(" dal ", " ");
        command = command.replaceAll(" dalla ", " ");
        return command;
    }

    // traduce il comando dato in un Command
    public static Command parse(String userCommand, TreeMap<String, Command> commandMap) {
        // rimuovi testo
        userCommand = Command.removeNaturalText(userCommand.toLowerCase().trim());

        if (!userCommand.isEmpty())
            // controlla che ci sia un comando uguale nella mappa partendo dal maggiore
            for (var entry : commandMap.descendingMap().entrySet()) {
                Command command = entry.getValue();
                if (userCommand.startsWith(entry.getKey())) {
                    if (command.getParamsNum() == 0 && entry.getKey().equals(userCommand)) {
                        return command;
                    }
                    // se un parametro trovo il parametro e lo setto nel comando
                    else if (command.getParamsNum() == 1) {
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
