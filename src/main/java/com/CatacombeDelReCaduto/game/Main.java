package com.CatacombeDelReCaduto.game;


import java.util.logging.Logger;

/**
 * Classe d'avvio del gioco
 */
public class Main {
    public static void main(String[] args){



        // log da capire come gestirlo nelle classi
        // direi semplice no Handler, static final per ogni classe in cui serve
        Logger logger =  Logger.getLogger(Main.class.getName());
//        FileHandler fileHandler = new FileHandler("status.log");
//        fileHandler.setFormatter(new SimpleFormatter());
//        logger.addHandler(fileHandler);

        logger.info("This is an info message1");
        logger.severe("This is an error message3"); // == ERROR
        logger.fine("Here is a debug message2");
    }
}
