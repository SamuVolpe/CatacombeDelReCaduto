package com.CatacombeDelReCaduto.game.items;

/**
 * La classe Food rappresenta un cibo nel gioco.
 * Estende la classe Item e aggiunge la quantità di vita recuperata.
 */
public class Food extends Item {
    private int healthRecoveryAmount;

    /**
     * Costruttore per inizializzare il cibo con nome, descrizione, peso e quantità di vita recuperata.
     *
     * @param name Il nome del cibo.
     * @param description La descrizione del cibo.
     * @param weight Il peso del cibo.
     * @param healthRecoveryAmount La quantità di vita recuperabile mangiando il cibo.
     */
    public Food(String name, String description, int weight, int healthRecoveryAmount) {
        super(name, description, weight); // Inizializza gli attributi della classe base
        this.healthRecoveryAmount = healthRecoveryAmount; // Imposta la quantità di vita recuperata
    }

    /**
     * Restituisce la quantità di vita recuperabile mangiando il cibo.
     *
     * @return La quantità di vita recuperabile mangiando il cibo.
     */
    public int getHealthRecoveryAmount() {
        return healthRecoveryAmount;
    }

    /**
     * Imposta una nuova quantità di vita recuperabile mangiando il cibo.
     *
     * @param healthRecoveryAmount La quantità di vita recuperabile mangiando il cibo.
     */
    public void setHealthRecoveryAmount(int healthRecoveryAmount) {
        this.healthRecoveryAmount = healthRecoveryAmount;
    }

    /**
     * Override del metodo toString per fornire una rappresentazione stringa del cibo.
     *
     * @return Una stringa che rappresenta il cibo.
     */
    @Override
    public String toString() {
        return super.toString() + ", recupero vita=" + healthRecoveryAmount;
    }
}
