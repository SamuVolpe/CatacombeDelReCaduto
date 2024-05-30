package com.CatacombeDelReCaduto.game.entities;

import com.CatacombeDelReCaduto.game.items.*;
import com.CatacombeDelReCaduto.game.jsonHandlers.FilesManager;
import com.CatacombeDelReCaduto.game.jsonHandlers.PlayerSave;
import com.CatacombeDelReCaduto.game.rooms.Room;

import java.util.Map;

/**
 * Classe che estende un'entità, rappresenta il giocatore.
 * Aggiunge la data di creazione, il punteggio della sua partita, l'arma equipaggiata, l'armatura equipaggiata, l'inventario degli oggetti, la stanza dove è presente, la stanza precedente.
 */
public class Player extends Entity{
    // data creazione del giocatore
    public final long CREATION_DATE;
    private int score = 0;
    private Weapon weapon;
    private Armor armor;
    private Inventory inventory = new Inventory();
    private Room room = null;
    private String previousRoomDirection = null;

    /**
     * Costruttore che inzializza la data di creazione e il nome.
     *
     * @param creationDate data di creazione del giocatore.
     * @param name nome del giocatore.
     * */
    public Player(long creationDate, String name){
        this(creationDate, name, null, null);
    }

    /**
     * Costruttore che inzializza la data di creazione, il nome, l'arma e l'armatura del giocatore.
     *
     * @param creationDate data di creazione del giocatore.
     * @param name nome del giocatore.
     * @param weapon arma del giocatore equipaggiata.
     * @param armor armatura del giocatore equipaggiata.
     * */
    public Player(long creationDate, String name, Weapon weapon, Armor armor) {
        this(creationDate, name, "avventuriero in cerca di fortuna", 100, 5, 0, weapon, armor);
    }

    /**
     * Costruttore che inzializza la data di creazione, il nome, la descrizione, i punti di vita massima, l'attacco, la difesa, l'arma e l'armatura del giocatore.
     * Utilizza anche il costruttore della classe padre entity.
     *
     * @param creationDate data di creazione del giocatore.
     * @param name nome del giocatore.
     * @param description descrizione del giocatore.
     * @param maxHealth punti di vita massima del giocatore.
     * @param attack attacco del giocatore.
     * @param defense difesa del giocatore.
     * @param weapon arma del giocatore equipaggiata.
     * @param armor armatura del giocatore equipaggiata.
     * */
    public Player(long creationDate, String name, String description, int maxHealth, int attack, int defense, Weapon weapon, Armor armor) {
        super(name, description, maxHealth, attack, defense);
        this.CREATION_DATE = creationDate;
        this.weapon = weapon;
        this.armor = armor;
    }

    /**
     * Metodo che imposta il nome della partita.
     *
     * @return nome della partita.
     * */
    public String getSaveFileName() {return FilesManager.gameFileName(CREATION_DATE, getName());}

    /**
     * Metodo che ritorna l'inventario degli oggetti del giocatore.
     *
     * @return l'inventario.
     * */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Metodo che imposta l'inventario del giocatore, un inventario passato come parametro.
     *
     * @param inventory inventario esterno.
     * */
    public void setInventory(Inventory inventory) {
        if( inventory != null ){
            this.inventory = inventory;
        }
    }

    /**
     * Metodo che ritorna l'arma equipaggiata dal giocatore.
     *
     * @return l'arma equipaggiata dal giocatore.
     * */
    public Weapon getWeapon() {
        return weapon;
    }

    /**
     * Metodo che imposta l'arma da equipaggiare al giocatore.
     *
     * @param weapon arma che si vuole equipaggiare.
     * */
    public void setWeapon(Weapon weapon) {
        // tolgo attacco arma vecchia
        if (this.weapon != null)
            addAttack(-this.weapon.getDamage());
        // set new armor
        this.weapon = weapon;
        // aggiungo attacco arma nuova
        if (this.weapon != null)
            addAttack(this.weapon.getDamage());
    }

    /**
     * Metodo che ritorna l'armarmatura equipaggiata dal giocatore.
     *
     * @return l'armatura equipaggiata dal giocatore.
     * */
    public Armor getArmor() {
        return armor;
    }

    /**
     * Metodo che imposta l'armatura da equipaggiare al giocatore.
     *
     * @param armor arma che si vuole equipaggiare.
     * */
    public void setArmor(Armor armor) {
        // tolgo difesa armatura vecchia
        if (this.armor != null)
            addDefense(-this.armor.getDefense());
        // set new armor
        this.armor = armor;
        // aggiungo difesa armatura nuova
        if (this.armor != null)
            addDefense(this.armor.getDefense());
    }

    /**
     * Metodo che ritorna il punteggio del giocatore.
     *
     * @return il punteggio dal giocatore.
     * */
    public int getScore() {
        return score;
    }

    /**
     * Metodo che aggiunge punteggio al giocatore.
     *
     * @param score il punteggio che si vuole aggiungere al giocatore.
     * */
    public void addScore(int score) {
        this.score += score;
        // notifica di aggiornamento dello score
        System.out.println("Score +" + score + ", nuovo score : " + this.score);
    }

    /**
     * Metodo che ritorna la stanza dov'è presente il giocatore.
     *
     * @return la stanza dov'è il giocatore.
     * */
    public Room getRoom() {
        return room;
    }

    /**
     * Metodo per impostare la stanza dove posizionare il giocatore.
     *
     * @param room stanza che si vuole impostare al giocatore.
     * */
    public void setRoom(Room room) {
        // se non visitata segna visitata
        if (!room.isVisited()) {
            room.setVisited(true);
            // se non prima stanza aggiungi score
            if (this.room != null)
                addScore(2);
        }
        this.room = room;
    }

    /**
     * Metodo che per salvare i parametri del giocatore.
     *
     * @return playerSave oggetto con all'interno i parametri salvati del giocatore.
     * */
    public PlayerSave save(){
        PlayerSave playerSave = new PlayerSave();
        // dati da salvare
        playerSave.setHealth(getHealth());
        playerSave.setScore(getScore());
        playerSave.setRoom(getRoom().getName());
        if (getArmor() != null)
            playerSave.setArmor(getArmor().getName());
        if (getWeapon() != null)
            playerSave.setWeapon(getWeapon().getName());
        playerSave.setInventory(getInventory().getItemsNames());

        return playerSave;
    }

    /**
     * Metodo che per caricare i parametri del giocatore della partita salvata.
     *
     * @param playerSave oggetto con all'interno i parametri salvati del giocatore.
     * @param allItems oggetto di tipo mappa per reimpostare gli oggetti dopo averli caricati.
     * @param allRooms oggetto di tipo mappa per reimpostare le stanze visitate salvate dopo averle caricate.
     * */
    public void load(PlayerSave playerSave,Map<String, Item> allItems, Map<String, Room> allRooms){
        // setto dati del player
        setHealth(playerSave.getHealth());
        score = playerSave.getScore();
        setRoom(allRooms.get(playerSave.getRoom()));
        if (playerSave.getArmor() != null)
            setArmor((Armor) allItems.get(playerSave.getArmor()));
        if (playerSave.getArmor() != null)
            setWeapon((Weapon) allItems.get(playerSave.getWeapon()));
        for (String itemName : playerSave.getInventory()){
            inventory.addItem(allItems.get(itemName));
        }
    }

    /**
     * Metodo per riprendere l'ultima stanza visitata dal giocatore.
     *
     * @return oggetto di tipo stanza.
     * */
    public String getPreviousRoomDirection() {
        return previousRoomDirection;
    }

    /**
     * Metodo per impostare l'ultima stanza visitata dal giocatore.
     *
     * @param previousRoomDirection il nome dell'ultima stanza visitata dal giocatore.
     * */
    public void setPreviousRoomDirection(String previousRoomDirection) {
        this.previousRoomDirection = previousRoomDirection;
    }

    /**
     * Metodo per utilizzare un oggetto dell'inventario.
     *
     * @param itemName il nome dell'oggetto che utilizza il gicatore.
     * @return boolean se andato a buon fine l'utilizzo.
     * */
    public boolean use(String itemName){
        Item toUse = inventory.removeItem(itemName);
        if (toUse == null) {
            System.out.println("Impossibile utilizzare l'oggetto: non è presente nell'inventario");
            return false;
        } else {
            if (toUse instanceof Food) {
                setHealth(getHealth() + ((Food) toUse).getHealthRecoveryAmount());
                System.out.println("Vita dopo aver mangiato " + toUse.getName() + ": " + getHealth());
            } else {
                System.out.println("Impossibile utilizzare l'oggetto: non è cibo");
                inventory.addItem(toUse);
                return false;
            }
        }
        return true;
    }

    /**
     * Override del metodo toString per fornire una rappresentazione stringa del giocatore.
     *
     * @return Una stringa che rappresenta il gicatore, utilizzando il metodo della classe padre entity.
     */
    @Override
    public String toString() {
        String weaponName = "nessuna";
        String armorName = "nessuna";
        if (weapon != null)
            weaponName = weapon.getName();
        if (armor != null)
            armorName = armor.getName();

        return super.toString() +
                "\nArma=" + weaponName +
                ", Armatura=" + armorName +
                "\nScore=" + score;
    }
}
