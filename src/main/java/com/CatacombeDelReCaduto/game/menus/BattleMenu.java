package com.CatacombeDelReCaduto.game.menus;

import com.CatacombeDelReCaduto.game.Game;
import com.CatacombeDelReCaduto.game.entities.Enemy;
import com.CatacombeDelReCaduto.game.entities.Entity;
import com.CatacombeDelReCaduto.game.entities.Player;
import com.CatacombeDelReCaduto.game.jsonHandlers.Save;
import com.CatacombeDelReCaduto.game.prompts.Command;
import com.CatacombeDelReCaduto.game.prompts.CommandId;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class BattleMenu extends CommandMenu {
    // l'ordine dei comandi e' importante per la stampa del menu todo potrei aggiungere comandi di visualizza status nemico, player
    private static final List<Command> COMMANDS = List.of(
            new Command(CommandId.ATTACK, List.of("attacca"), "Attacca")
            ,new Command(CommandId.USE, List.of("u", "usa", "mangia"), "Usa <cibo>", 1)
            ,new Command(CommandId.VIEW, List.of("visualizza inventario", "inventario"), "Visualizza l'inventario")
            ,new Command(CommandId.DETAIL, List.of("dettagli", "dettagli nemico"), "Dettagli sul nemico")
            ,new Command(CommandId.ESCAPE, List.of("scappa"), "Scappa"));

    private final Player player;
    private final Enemy enemy;

    public BattleMenu(Player player, Enemy enemy) {
        super(COMMANDS);
        this.player = player;
        this.enemy = enemy;
    }

    // true combattimento terminato, false fuggito
    public boolean battle(){
        super.display();

        return !enemy.isAlive() || !player.isAlive();
    }

    @Override
    protected void print(){
        super.print();

        String playerHealth = String.format("vita %s : %d/%d", player.getName(), player.getHealth(), player.getMaxHealth());
        String enemyHealth = String.format("vita %s : %d/%d", enemy.getName(), enemy.getHealth(), enemy.getMaxHealth());

        System.out.println();
        System.out.println(playerHealth);
        System.out.println(enemyHealth);
    }

    private void attack(Entity attacker, Entity defender){
        // controllo che sia un attacco tra due entita` vive
        if (attacker.getHealth() <=0 || defender.getHealth() <= 0)
            return;

        String output = "";

        // schivata
        Random random = new Random();
        if (random.nextInt(100) < 8) {
            output = defender.getName() + " ha schivato il colpo";
        }else {
            // scalo attacco sulla difesa
            int damage = attacker.getAttack() - ((attacker.getAttack() * defender.getDefense()) / 100);

            // critico
            if (random.nextInt(100) < 20) {
                damage = (int)(damage * 1.5);
                output = "Colpo critico! ";
            }

            // sottraggo vita
            defender.setHealth(defender.getHealth()-damage);

            // output d'attacco
            output += attacker.getName() + " ha tolto " + damage + " a " + defender.getName();
        }

        System.out.println(output);
    }

    // region comandi MainMenu

    protected boolean commandsHandler(Command command){
        return switch (command.getId()) {
            case ATTACK -> commandAttack();
            case USE -> commandUse(command.getArgs()[0]);
            case DETAIL -> commandDetail();
            case VIEW -> commandView();
            case ESCAPE -> commandEscape();
            default -> throw new IllegalArgumentException("Command not implemented");
        };
    }

    private boolean commandAttack() {
        // player attacca
        attack(player, enemy);

        // enemy attacca
        attack(enemy, player);

        return !player.isAlive() || !enemy.isAlive();
    }

    private boolean commandUse(String arg) {
        // usa cibo
        if (player.use(arg))
            // se cibo valido il nemico attacca
            attack(enemy, player);

        return !player.isAlive() || !enemy.isAlive();
    }

    private boolean commandView() {
        // print inventory
        System.out.println(player.getInventory());
        return false;
    }

    private boolean commandDetail() {
        // print nemico
        System.out.println(enemy);
        return false;
    }

    private boolean commandEscape(){
        Random random = new Random();
        // calcolo prob di fuga in base a stanza
        if (random.nextInt(10) < player.getRoom().getDangerLevel()){
            System.out.println("Fuga fallita!");
            // nemico attacca
            attack(enemy, player);
            return false;
        }
        return true;
    }

    // endregion
}
