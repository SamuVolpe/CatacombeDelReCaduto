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
     * Rimuove un oggetto dall'inventario.
     *
     * @param itemName il nome dell'oggetto da rimuovere
     * @return l'oggetto rimosso, o null se l'oggetto non è presente
     */
    public Item removeItem(String itemName) {
        ArrayList<Item> list = inventory.get(itemName);
        if (list != null) {
            Item it = list.getFirst();
            currentWeight = currentWeight - it.getWeight();
            if (list.size() == 1) {
                inventory.remove(itemName);
            } else {
                list.removeFirst();
            }
            return it;
        } else {
            return null;
        }
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
            ret = ret + key + "(peso=" + value.getFirst().getWeight() + ")(x" + value.size() + ") ";
        }
        return ret;
    }
}

