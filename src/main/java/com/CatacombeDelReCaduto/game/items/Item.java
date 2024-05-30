package com.CatacombeDelReCaduto.game.items;

/**
 * Rappresenta un oggetto del gioco
 */
public class Item {

    private String name;
    private String description;
    private int weight;

    /**
     * Costruttore per inizializzare l'oggetto con nome, descrizione e peso.
     *
     * @param name Il nome dell'oggetto.
     * @param description La descrizione dell'oggetto.
     * @param weight Il peso dell'oggetto.
     */
    public Item(String name, String description, int weight) {
        this.description = description; // Imposta la descrizione
        this.name = name; // Imposta il nome
        this.weight = weight; // Imposta il peso
    }

    /**
     * Restituisce la descrizione dell'oggetto.
     *
     * @return La descrizione dell'oggetto.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Imposta una nuova descrizione per l'oggetto.
     *
     * @param description La nuova descrizione dell'oggetto.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Restituisce il peso dell'oggetto.
     *
     * @return Il peso dell'oggetto.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Imposta un nuovo peso per l'oggetto.
     *
     * @param weight Il nuovo peso dell'oggetto.
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Restituisce il nome dell'oggetto.
     *
     * @return Il nome dell'oggetto.
     */
    public String getName() {
        return name;
    }

    /**
     * Imposta un nuovo nome per l'oggetto.
     *
     * @param name Il nuovo nome dell'oggetto.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Override del metodo toString per fornire una rappresentazione stringa dell'oggetto.
     *
     * @return Una stringa che rappresenta l'oggetto.
     */
    @Override
    public String toString() {
        return "Nome=" + name + "\nDescrizione=" + description  + "\nPeso=" + weight;
    }
}

