package com.CatacombeDelReCaduto.game.entities;

/**
 * Rappresenta una entita`
 */
public class Entity {

    private String name, description;

    private int health, maxHealth, attack, defense;

    public Entity(String name, String description, int maxHealth, int attack, int defense) {
        this.defense = defense;
        this.description = description;
        this.name = name;
        this.maxHealth = maxHealth;
        this.attack = attack;
        this.health = maxHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getAttack() {
        return attack;
    }

    public void addAttack(int addattack) {
        if( (attack + addattack) < 0) {
            attack = 0;
        }
        else {
            attack+=addattack;
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health < 0)
            health = 0;
        else if (health > maxHealth)
            health = maxHealth;

        this.health = health;
    }

    public int getDefense() {
        return defense;
    }

    public void addDefense(int addefense) {
        if( (defense + addefense) < 0) {
            defense = 0;
        }
        else {
            defense+=addefense;
        }
        // verifico che la difesa non superi il limite massimo
        if (defense > 100)
            defense = 100;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Nome : " + name
                + "\nInfo : " + description
                + "\nVita : " + health + "/" + maxHealth
                + ", Attacco : " + attack
                + ", Difesa : " + defense;
    }

    public boolean isAlive() { return health > 0; }
}
