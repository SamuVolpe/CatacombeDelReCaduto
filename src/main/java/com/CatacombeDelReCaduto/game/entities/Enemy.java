package com.CatacombeDelReCaduto.game.entities;

import com.CatacombeDelReCaduto.game.items.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che estende un'entità, rappresenta un nemico e implementa la classe Cloneable per fare una deep copy del singolo oggetto.
 * Aggiunge la lista di oggetti che può rilasciare.
 */
public class Enemy extends Entity implements Cloneable{
    private List<Item> drop = new ArrayList<Item>();

    /**
     * Costruttore vuoto che riprende il costruttore della classe entità.
     * Imposta nome, descrizione, punti vita massimi, attacco, difesa.
     *
     * @param name nome del nemico.
     * @param description descrizione del nemico.
     * @param maxHealth punti vita massimi del nemico.
     * @param attack attacco del nemico.
     * @param defense difesa del nemico.
     * */
    public Enemy(String name, String description, int maxHealth, int attack, int defense) {
        super(name, description, maxHealth, attack, defense);
    }

    /**
     * Costruttore che passa una lista di oggetti riprendendo il costruttore della classe entità.
     *
     * @param drop lista degli oggetti che rilascerebbe in caso di morte.
     * */
    public Enemy(String name, String description, int maxHealth, int attack, int defense, List<Item> drop) {
        super(name, description, maxHealth, attack, defense);
        this.drop = drop;
    }

    /**
     * Ritorna la lista degli oggetti che rilascerebbe il nemico.
     *
     * @return la lista di oggetti.
     * */
    public List<Item> getDrop() {
        return drop;
    }

    /**
     * Override del metodo clone per fare una deep copy del nemico.
     *
     * @return Un clone del nemico.
     */
    @Override
    public Enemy clone() {
        try {
            Enemy clone = (Enemy) super.clone();

            // clona lista di items
            clone.drop = new ArrayList<>();
            for (Item item : this.drop) {
                clone.drop.add((Item) item);
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
