package com.CatacombeDelReCaduto.game.Entity;
public class Entity {

    private int currentlyLifePoints, attack, defense, maxLifePoints;

    private Room room;

    private String name, description;

    public Entity() {

    }

    public Entity(int defense, String description, String name, Room room, int maxLifePoints, int attack, int currentlyLifePoints) {
        this.defense = defense;
        this.description = description;
        this.name = name;
        this.room = room;
        this.maxLifePoints = maxLifePoints;
        this.attack = attack;
        this.currentlyLifePoints = currentlyLifePoints;
    }

    public int getMaxLifePoints() {
        return maxLifePoints;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int addattack) {
        if( (attack + addattack) < 0) {
            attack = 0;
        }
        else {
            attack+=addattack;
        }
    }

    public int getCurrentlyLifePoints() {
        return currentlyLifePoints;
    }

    public void setCurrentlyLifePoints(int lifePoints) {
        if( (currentlyLifePoints + lifePoints) > maxLifePoints){
            this.currentlyLifePoints = maxLifePoints;
        }
        else if( (currentlyLifePoints + lifePoints) < 0){
            this.currentlyLifePoints = 0;
        }
        else{
            this.currentlyLifePoints += lifePoints;
        }
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int addefense) {
        if( (defense + addefense) < 0) {
            defense = 0;
        }
        else {
            defense+=addefense;
        }
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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        if( room != null ){
            this.room = room;
        }
    }
}
