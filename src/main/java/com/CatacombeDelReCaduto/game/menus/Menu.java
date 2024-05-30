package com.CatacombeDelReCaduto.game.menus;

import java.util.List;

/**
 * Estendere questa classe per creare un menu di gioco
 */
public abstract class Menu {
    // oggetti da visualizzare nel menu nell'ordine corretto
    protected List<String> menuItems;

    /**
     * Costruttore di default, se utilizzato poi e` necessario inizializzare i menuItems
     */
    protected Menu(){
    }

    /**
     * Costruttore che inizializza i menuItems
     * @param menuItems items del menu
     */
    protected Menu(List<String> menuItems) {
        initMenuItems(menuItems);
    }

    /**
     * Inizializza la lista contenente gli items del menu
     * @param menuItems items del menu
     */
    protected void initMenuItems(List<String> menuItems){
        this.menuItems = menuItems;
    }

    /**
     * Riporta numero selezionato nella lista degli oggetti del menu
     * @param userCommand input utente
     * @return -1 se numero selezionato non valido
     */
    protected int userChoice(String userCommand) {
        // verifico se l'utente ha inputato il numero del menu
        try {
            int choice = Integer.parseInt(userCommand.trim());
            if (choice > 0 && choice <= menuItems.size())
                return choice;
        } catch (NumberFormatException e) {
        }
        return -1;
    }

    /**
     * Stampa il menu
     */
    protected void print() {
        String output = "";

        int i = 0;
        for (var menuItem : menuItems) {
            i++;
            output += "("+i+") " + menuItem + "\n";
        }

        System.out.print(output);
    }
}
