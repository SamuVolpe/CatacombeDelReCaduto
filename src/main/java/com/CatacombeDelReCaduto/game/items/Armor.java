package com.CatacombeDelReCaduto.game.items;

public class Armor extends Item {
    private int defense;

    public Armor(String description, String name, int weight, int defense) {
        super(description, name, weight);
        this.defense = defense;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }
}
