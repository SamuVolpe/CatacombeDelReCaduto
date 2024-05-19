package com.CatacombeDelReCaduto.game.menus;

import java.util.List;

/**
 * Estendere questa classe per creare un menu di gioco
 */
public abstract class Menu {
    // oggetti da visualizzare nel menu nell'ordine corretto
    protected List<String> menuItems;

    public Menu(){
    }

    public Menu(List<String> menuItems) {
        initMenuItems(menuItems);
    }

    protected void initMenuItems(List<String> menuItems){
        this.menuItems = menuItems;
    }

    // return -1 se scelta non valida
    protected int userChoice(String userCommand) {
        // verifico se l'utente ha inputato il numero del menu
        try {
            int choice = Integer.parseInt(userCommand.trim());
            if (choice > 0 && choice <= menuItems.size())
                return choice;
        } catch (NumberFormatException _) {
        }
        return -1;
    }

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
