package com.CatacombeDelReCaduto.game.prompts;

import java.util.Collections;
import java.util.List;

public class Command {
    private CommandId commandId;
    private List<String> aliases;
    private String description;

    public Command(CommandId commandId, List<String> aliases, String description) {
        this.commandId = commandId;
        this.aliases = Collections.unmodifiableList(aliases);
        this.description = description;
    }

    public CommandId getCommandId() {
        return commandId;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public static String removeNaturalText(String command) {
        command = command.replaceAll(" la ", " ");
        command = command.replaceAll(" il ", " ");
        command = command.replaceAll(" una ", " ");
        command = command.replaceAll(" l' ", " ");
        command = command.replaceAll(" dal ", " ");
        return command;
    }
}
