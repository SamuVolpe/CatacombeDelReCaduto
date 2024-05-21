package com.CatacombeDelReCaduto.game.items;

public class Armor extends Item {
    private final int defense;

    public Armor(String name, String description, int weight, int defense) {
        super(name, description, weight);
        this.defense = defense;
    }

    public int getDefense() {
        return defense;
    }

    @Override
    public String toString() {
        return super.toString() + ", protezione=" + defense;
    }

    @Override
    public Armor clone() {
        Armor clone = (Armor) super.clone();
        return clone;
    }
}
