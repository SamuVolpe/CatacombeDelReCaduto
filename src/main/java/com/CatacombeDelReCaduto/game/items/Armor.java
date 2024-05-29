package com.CatacombeDelReCaduto.game.items;

/**
 * La classe Armor rappresenta un'armatura nel gioco.
 * Estende la classe Item e aggiunge l'attributo defense.
 */
public class Armor extends Item {
    /**
     * Livello di protezione offerto dall'armatura.
     */
    private final int defense;

    /**
     * Costruttore per inizializzare l'armatura con nome, descrizione, peso e livello di protezione.
     *
     * @param name Il nome dell'armatura.
     * @param description La descrizione dell'armatura.
     * @param weight Il peso dell'armatura.
     * @param defense Il livello di protezione offerto dall'armatura.
     */
    public Armor(String name, String description, int weight, int defense) {
        super(name, description, weight); // Inizializza gli attributi della classe base
        this.defense = defense; // Imposta il livello di protezione
    }

    /**
     * Restituisce il livello di protezione offerto dall'armatura.
     *
     * @return Il livello di protezione.
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Override del metodo toString per fornire una rappresentazione stringa dell'armatura.
     *
     * @return Una stringa che rappresenta l'armatura.
     */
    @Override
    public String toString() {
        return super.toString() + ", protezione=" + defense;
    }
}

