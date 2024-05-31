package com.CatacombeDelReCaduto.game.menus;

import com.CatacombeDelReCaduto.game.jsonHandlers.*;

import com.CatacombeDelReCaduto.game.prompts.InputReader;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Menu per il caricamento di una partita
 */
public class LoadMenu extends Menu {
    private Map<Long, String> games = new TreeMap<>();

    /**
     * Costruttore della classe LoadMenu.
     * Carica le partite esistenti e inizializza la lista di opzioni del menu.
     */
    public LoadMenu(){
        // carica giochi da file
        games = FilesManager.loadGames();
        // inizializza lista da visualizzare nel menu
        initMenuItems(new ArrayList<>(games.values()));
    }

    /**
     * Mostra menu partite caricabili
     * @return partita scelta - map - key : gameId - value : nome giocatore
     */
    public Map.Entry<Long, String> display() {
        if (games.isEmpty()){
            System.out.println("Nessuna partita esistente");
            return null;
        }

        int choice = -1;

        do {
            // stampo menu
            print();

            // prendo input
            System.out.println("Digita un numero del menu, 'esci' per tornare al menu principale (le partite sono mostrate in ordine di creazione)");
            String userCommand = InputReader.getInput();

            if (userCommand.equalsIgnoreCase("esci"))
                return null;

            // verifico se l'utente ha inputato il numero del menu
            choice = userChoice(userCommand);

        } while (choice < 1);

        // trova gioco scelto e lo carica nella classe
        Map.Entry<Long, String> game = new ArrayList<>(games.entrySet()).get(choice - 1);
        return game;
    }
}
