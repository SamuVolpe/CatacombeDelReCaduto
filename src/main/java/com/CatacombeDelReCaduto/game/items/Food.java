package com.CatacombeDelReCaduto.game.items;

public class Food extends Item{
    private int healthRecoveryAmount;

    public Food(String name, String description, int weight, int healthRecoveryAmount) {
        super(name, description, weight);
        this.healthRecoveryAmount = healthRecoveryAmount;
    }

    public int getHealthRecoveryAmount() {
        return healthRecoveryAmount;
    }

    public void setHealthRecoveryAmount(int healthRecoveryAmount) {
        this.healthRecoveryAmount = healthRecoveryAmount;
    }

    @Override
    public String toString() {
        return super.toString() + ", recupero vita=" + healthRecoveryAmount;
    }
}
