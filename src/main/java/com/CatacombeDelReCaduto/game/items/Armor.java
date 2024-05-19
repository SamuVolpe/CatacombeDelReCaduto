package com.CatacombeDelReCaduto.game.items;

public class Armor extends Item {
    private int defense;

    public Armor(String name, String description, int weight, int defense) {
        super(name, description, weight);
        this.defense = defense;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }
}
