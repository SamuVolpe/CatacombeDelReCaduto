package com.CatacombeDelReCaduto.game.entities;

import com.CatacombeDelReCaduto.game.items.*;
import com.CatacombeDelReCaduto.game.jsonHandlers.PlayerSave;
import com.CatacombeDelReCaduto.game.menus.BattleMenu;
import com.CatacombeDelReCaduto.game.rooms.*;

import java.util.Map;

public class Player extends Entity{
    // data creazione del personaggio
    public final long CREATION_DATE;
    private int score = 0;
    private Weapon weapon;
    private Armor armor;
    private Inventory inventory = new Inventory();
    private Room room = null;
    private String previousRoomDirection = null;

    public Player(long creationDate, String name){
        this(creationDate, name, null, null);
    }

    public Player(long creationDate, String name, Weapon weapon, Armor armor) {
        this(creationDate, name, "avventuriero in cerca di fortuna", 100, 5, 0, weapon, armor);
    }

    public Player(long creationDate, String name, String description, int maxHealth, int attack, int defense, Weapon weapon, Armor armor) {
        super(name, description, maxHealth, attack, defense);
        this.CREATION_DATE = creationDate;
        this.weapon = weapon;
        this.armor = armor;
    }

    public String getSaveFileName() {return getName() + "_" + CREATION_DATE + ".json"; }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        if( inventory != null ){
            this.inventory = inventory;
        }
    }

    public Weapon getWeapon() {
        return weapon;
    }

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

    public Armor getArmor() {
        return armor;
    }

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

    public int getScore() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

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

    public void load(PlayerSave playerSave,Map<String, Item> allItems, Map<String, Room> allRooms){
        // setto dati del player
        setHealth(playerSave.getHealth());
        addScore(playerSave.getScore());
        setRoom(allRooms.get(playerSave.getRoom()));
        if (playerSave.getArmor() != null)
            setArmor((Armor) allItems.get(playerSave.getArmor()).clone());
        if (playerSave.getArmor() != null)
            setWeapon((Weapon) allItems.get(playerSave.getWeapon()).clone());
        for (String itemName : playerSave.getInventory()){
            inventory.addItem(allItems.get(itemName).clone());
        }
    }

    public String getPreviousRoomDirection() {
        return previousRoomDirection;
    }

    public void setPreviousRoomDirection(String previousRoomDirection) {
        this.previousRoomDirection = previousRoomDirection;
    }

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

    @Override
    public String toString() {
        return super.toString() +
                ", Arma : " + weapon +
                ", Armatura : " + armor +
                ", Score : " + score;
    }
}
