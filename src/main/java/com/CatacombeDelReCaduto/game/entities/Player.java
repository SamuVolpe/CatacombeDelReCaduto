package com.CatacombeDelReCaduto.game.entities;

import com.CatacombeDelReCaduto.game.items.*;
import com.CatacombeDelReCaduto.game.rooms.*;

public class Player extends Entity{
    private int score = 0;
    private Weapon weapon;
    private Armor armor;
    private Inventory inventory = new Inventory();
    private Room room = null;

    public Player(String name, String description, int maxHealth, int attack, int defense, Weapon weapon, Armor armor) {
        super(name, description, maxHealth, attack, defense);
        this.weapon = weapon;
        this.armor = armor;
    }

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
        if( weapon != null ){
            this.weapon = weapon;
        }
    }

    public Armor getArmor() {
        return armor;
    }

    public void setArmor(Armor armor) {
        if( armor != null ){
            this.armor = armor;
        }
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

    @Override
    public String toString() {
        return super.toString() +
                " score=" + score +
                ", weapon=" + weapon +
                ", armor=" + armor;
    }
}
