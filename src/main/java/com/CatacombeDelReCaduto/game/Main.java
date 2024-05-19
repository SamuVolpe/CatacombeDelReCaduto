package com.CatacombeDelReCaduto.game;

import com.CatacombeDelReCaduto.game.menus.MainMenu;

import java.util.logging.Logger;

/**
 * Classe d'avvio del gioco
 */
public class Main {
    private static final Logger logger =  Logger.getLogger(Main.class.getName());

    public static void main(String[] args){
        // qua magari fare un pre check della connessione con il cloud

        // avvio menu iniziale
        MainMenu mainMenu = new MainMenu();
        mainMenu.display();
    }
}
