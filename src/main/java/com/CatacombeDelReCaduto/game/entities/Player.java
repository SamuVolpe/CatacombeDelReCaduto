package com.CatacombeDelReCaduto.game.entities;

import com.CatacombeDelReCaduto.game.items.*;
import com.CatacombeDelReCaduto.game.jsonHandlers.PlayerSave;
import com.CatacombeDelReCaduto.game.rooms.*;

public class Player extends Entity{
    // data creazione del personaggio
    public final long CREATION_DATE;
    private int score = 0;
    private Weapon weapon;
    private Armor armor;
    private Inventory inventory = new Inventory();
    private Room room = null;

    public Player(long creationDate, String name, String description, Weapon weapon, Armor armor) {
        this(creationDate, name, description, 100, 5, 0, weapon, armor);
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

    public void load(PlayerSave playerSave){
        // setto dati del player
    }

    @Override
    public String toString() {
        return super.toString() +
                " score=" + score +
                ", weapon=" + weapon +
                ", armor=" + armor;
    }
}
