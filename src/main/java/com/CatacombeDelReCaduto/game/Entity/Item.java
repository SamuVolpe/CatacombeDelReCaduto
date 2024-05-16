package com.CatacombeDelReCaduto.game.Entity;

public class Item {
    private String name;
    private String description;
    private int weight;

    public Item(String description, String name, int weight) {
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

}
