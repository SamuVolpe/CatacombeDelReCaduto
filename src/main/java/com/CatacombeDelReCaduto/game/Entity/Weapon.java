package com.CatacombeDelReCaduto.game.Entity;

public class Weapon extends Item {
    private int damage;

    public Weapon(String description, String name, int weight, int damage) {
        super(description, name, weight);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
