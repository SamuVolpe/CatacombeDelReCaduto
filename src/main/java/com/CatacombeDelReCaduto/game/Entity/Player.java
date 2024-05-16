package com.CatacombeDelReCaduto.game.Entity;
public class Player extends Entity{
    private Inventory inventory;
    private Weapon weapon;
    private Armor armor;
    private int score;

    public Player(Inventory inventory, Weapon weapon, Armor armor, int score) {
        this.inventory = inventory;
        this.weapon = weapon;
        this.armor = armor;
        this.score = score;
    }

    public Player(int defense, String description, String name, Room room, int maxLifePoints, int attack, int currentlyLifePoints, Inventory inventory, Weapon weapon, Armor armor, int score) {
        super(defense, description, name, room, maxLifePoints, attack, currentlyLifePoints);
        this.inventory = inventory;
        this.weapon = weapon;
        this.armor = armor;
        this.score = score;
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

    public void setScore(int score) {
        this.score += score;
    }

}
