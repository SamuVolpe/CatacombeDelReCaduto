package com.CatacombeDelReCaduto.game.item;

public class Armor extends Item {
    private int defence;

    public Armor(String description, String name, int weight, int defence) {
        super(description, name, weight);
        this.defence = defence;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }
}
