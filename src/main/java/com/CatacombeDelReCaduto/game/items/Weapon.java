package com.CatacombeDelReCaduto.game.items;

public class Weapon extends Item {
    private int damage;

    public Weapon() {
    }

    public Weapon(String name, String description, int weight, int damage) {
        super(name, description, weight);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
