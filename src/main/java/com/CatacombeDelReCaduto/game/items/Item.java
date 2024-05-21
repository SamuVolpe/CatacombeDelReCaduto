package com.CatacombeDelReCaduto.game.items;

public class Item implements Cloneable {
    private String name;
    private String description;
    private int weight;

    public Item(String name, String description, int weight) {
        this.description = description;
        this.name = name;
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "nome=" + name + ", descrizione=" + description  + ", peso=" + weight;
    }

    @Override
    public Item clone() {
        try {
            Item clone = (Item) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
