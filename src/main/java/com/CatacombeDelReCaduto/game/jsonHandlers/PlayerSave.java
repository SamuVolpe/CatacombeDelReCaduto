package com.CatacombeDelReCaduto.game.jsonHandlers;

import java.util.List;

public class PlayerSave {
    private int health;
    private int score;
    private String weapon;
    private String armor;
    private String Room;
    private List<String> inventory;
    private boolean bossRoomOpen;

    public PlayerSave() {
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    public String getArmor() {
        return armor;
    }

    public void setArmor(String armor) {
        this.armor = armor;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public List<String> getInventory() {
        return inventory;
    }

    public void setInventory(List<String> inventory) {
        this.inventory = inventory;
    }

    public boolean isBossRoomOpen() {
        return bossRoomOpen;
    }

    public void setBossRoomOpen(boolean bossRoomOpen) {
        this.bossRoomOpen = bossRoomOpen;
    }
}
