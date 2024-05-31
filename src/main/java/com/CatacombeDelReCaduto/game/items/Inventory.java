package com.CatacombeDelReCaduto.game.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * La classe Inventory rappresenta un inventario che può contenere vari oggetti,
 * con un limite massimo di peso totale.
 */
public class Inventory {
    static public final int MAX_WEIGHT = 50;
    private int currentWeight;
    private HashMap<String, ArrayList<Item>> inventory;

    /**
     * Costruttore di default per la classe Inventory.
     * Crea l'inventario vuoto.
     */
    public Inventory() {
        currentWeight = 0;
        inventory = new HashMap<String, ArrayList<Item>>();
    }

    /**
     * Costruttore alternativo per la classe Inventory.
     *
     * @param inventory una HashMap che rappresenta l'inventario iniziale
     */
    public Inventory(HashMap<String, ArrayList<Item>> inventory) {
        this.inventory = inventory;
    }

    /**
     * Aggiunge un oggetto all'inventario.
     *
     * @param item l'oggetto da aggiungere
     * @return true se l'oggetto è stato aggiunto con successo, false se il peso massimo viene superato
     */
    public boolean addItem(Item item) {
        if (currentWeight + item.getWeight() > MAX_WEIGHT) {
            return false;
        }
        currentWeight = currentWeight + item.getWeight();
        ArrayList<Item> list = inventory.get(item.getName());
        if (list == null) {
            list = new ArrayList<Item>();
        }
        list.add(item);
        inventory.put(item.getName(), list);
        return true;
    }

    /**
     * Prende oggetto dall'inventario
     *
     * @param itemName il nome dell'oggetto da prendere
     * @return l'oggetto, o null se l'oggetto non è presente
     */
    public Item getItem(String itemName){
        // prende lista nell'inventario contenente quel tipo di oggetti
        ArrayList<Item> list = inventory.get(itemName);
        if (list != null) {
            // se esiste ritorna il primo oggetto
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * Rimuove un oggetto dall'inventario.
     *
     * @param itemName il nome dell'oggetto da rimuovere
     * @return l'oggetto rimosso, o null se l'oggetto non è presente
     */
    public Item removeItem(String itemName) {
        // prende oggetto
        Item item = getItem(itemName);
        // se oggetto esiste
        if (item != null){
            // toglie peso da inventario
            currentWeight = currentWeight - item.getWeight();
            if (inventory.get(item.getName()).size() == 1)
                // rimuove tutta la lista se l'oggetto è singolo
                inventory.remove(item.getName());
            else
                // rimuove solo l'oggetto se ce ne sono più di uno
                inventory.get(item.getName()).remove(0);
        }
        return item;
    }

    /**
     * Torna i nomi di tutti gli oggetti presenti nell'inventario.
     *
     * @return una lista dei nomi di tutti gli oggetti
     */
    public List<String> getItemsNames() {
        List<String> result = new ArrayList<>();
        for (var itemName : inventory.keySet()) {
            for (var item : inventory.get(itemName))
                result.add(itemName);
        }
        return result;
    }

    /**
     * Restituisce una rappresentazione in formato stringa dell'inventario.
     *
     * @return una stringa che rappresenta l'inventario, compreso il peso totale e la lista degli oggetti
     */
    @Override
    public String toString() {
        String ret = "Inventario: peso totale=" + currentWeight + "/" + MAX_WEIGHT + ", lista oggetti=";
        for (Map.Entry<String, ArrayList<Item>> entry : inventory.entrySet()) {
            String key = entry.getKey();
            ArrayList<Item> value = entry.getValue();
            ret = ret + key + "(peso=" + value.get(0).getWeight() + ")(x" + value.size() + ") ";
        }
        return ret;
    }
}

