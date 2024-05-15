package com.CatacombeDelReCaduto.game.item;

public class Food extends Item{
    private int healthRecoveryAmount;

    public Food(String description, String name, int weight, int healthRecoveryAmount) {
        super(description, name, weight);
        this.healthRecoveryAmount = healthRecoveryAmount;
    }

    public int getHealthRecoveryAmount() {
        return healthRecoveryAmount;
    }

    public void setHealthRecoveryAmount(int healthRecoveryAmount) {
        this.healthRecoveryAmount = healthRecoveryAmount;
    }
}
