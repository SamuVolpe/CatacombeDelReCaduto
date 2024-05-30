package com.CatacombeDelReCaduto.game.items;

/**
 * La classe Weapon rappresenta un'arma nel gioco.
 * Estende la classe Item e aggiunge il danno.
 */
public class Weapon extends Item {
    private int damage;

    /**
     * Costruttore per inizializzare l'arma con nome, descrizione, peso e danno.
     *
     * @param name Il nome dell'arma.
     * @param description La descrizione dell'arma.
     * @param weight Il peso dell'arma.
     * @param damage Il danno inflitto dall'arma.
     */
    public Weapon(String name, String description, int weight, int damage) {
        super(name, description, weight); // Inizializza gli attributi della classe base
        this.damage = damage; // Imposta il danno
    }

    /**
     * Restituisce il danno inflitto dall'arma.
     *
     * @return Il danno inflitto dall'arma.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Imposta un nuovo valore di danno per l'arma.
     *
     * @param damage Il nuovo valore di danno.
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Override del metodo toString per fornire una rappresentazione stringa dell'arma.
     *
     * @return Una stringa che rappresenta l'arma.
     */
    @Override
    public String toString() {
        return super.toString() + ", Danno=" + damage;
    }
}

