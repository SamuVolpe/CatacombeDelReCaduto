package com.CatacombeDelReCaduto.game.menus;

import com.CatacombeDelReCaduto.game.DeathException;
import com.CatacombeDelReCaduto.game.Game;
import com.CatacombeDelReCaduto.game.entities.Enemy;
import com.CatacombeDelReCaduto.game.entities.Player;
import com.CatacombeDelReCaduto.game.jsonHandlers.Save;
import com.CatacombeDelReCaduto.game.prompts.Command;
import com.CatacombeDelReCaduto.game.prompts.CommandId;

import java.util.List;
import java.util.logging.Logger;

public class BattleMenu extends CommandMenu {
    // l'ordine dei comandi e' importante per la stampa del menu todo potrei aggiungere comandi di visualizza status nemico, player
    private static final List<Command> COMMANDS = List.of(
            new Command(CommandId.ATTACK, List.of("attacca"), "Attacca")
            ,new Command(CommandId.USE, List.of("usa", "mangia"), "Usa <cibo>", 1)
            ,new Command(CommandId.VIEW, List.of("visualizza inventario", "inventario"), "Visualizza l'inventario")
            ,new Command(CommandId.ESCAPE, List.of("scappa"), "Scappa"));

    private final Player player;
    private final Enemy enemy;

    public BattleMenu(Player player, Enemy enemy) {
        super(COMMANDS);
        this.player = player;
        this.enemy = enemy;
    }

    // true nemico sconfitto, false fuggito
    public boolean battle() throws DeathException {
        super.display();

        if (!player.isAlive())
            throw new DeathException();

        return !enemy.isAlive();
    }

    @Override
    protected void print(){
        super.print();

        String playerStatus = String.format("vita %s : %d/%d", player.getName(), player.getHealth(), player.getMaxHealth());
        String enemyStatus = String.format("vita %s : %d/%d", enemy.getName(), enemy.getHealth(), enemy.getMaxHealth());

        System.out.println(playerStatus);
        System.out.println(enemyStatus);
    }

    // region comandi MainMenu

    protected boolean commandsHandler(Command command){
        return switch (command.getId()) {
            case CommandId.ATTACK -> commandAttack();
            case CommandId.USE -> commandUse(command.getArgs()[0]);
            case CommandId.VIEW -> commandView();
            case CommandId.ESCAPE -> true;
            default -> throw new IllegalArgumentException("Command not implemented");
        };
    }

    private boolean commandAttack() {
        // attacca nemico, torna true se player o mostro sconfitto
        return true;
    }

    private boolean commandUse(String arg) {
        // usa oggetto se un cibo valido presente nell'inventario
        return true;
    }

    private boolean commandView() {
        // print inventory
        System.out.println(player.getInventory().toString());
        return false;
    }

    // endregion
}
