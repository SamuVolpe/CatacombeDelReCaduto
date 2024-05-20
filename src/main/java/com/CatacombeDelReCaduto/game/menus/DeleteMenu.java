package com.CatacombeDelReCaduto.game.menus;

import com.CatacombeDelReCaduto.game.jsonHandlers.FilesPath;
import com.CatacombeDelReCaduto.game.jsonHandlers.FilesManager;
import com.CatacombeDelReCaduto.game.prompts.InputReader;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

public class DeleteMenu extends Menu {
    private final Logger logger =  Logger.getLogger(this.getClass().getName());
    private Map<Long, String> games = new TreeMap<>();

    public DeleteMenu(){
        // carica giochi da file
        games = FilesManager.loadGames();
        // inizializza lista da visualizzare nel menu
        initMenuItems(new ArrayList<>(games.values()));
    }

    public void display() {
        if (games.isEmpty()){
            System.out.println("Nessuna partita esistente");
            return;
        }

        int choice = -1;

        do {
            // stampo menu
            print();

            // prendo input
            System.out.println("Digita un numero del menu, 'esci' per tornare al menu principale (le partite sono mostrate in ordine di creazione)");
            String userCommand = InputReader.getInput();

            if (userCommand.equalsIgnoreCase("esci"))
                return;

            // verifico se l'utente ha inputato il numero del menu
            choice = userChoice(userCommand);

        } while (choice < 1);

        // trova gioco scelto e lo carica nella classe
        Map.Entry<Long, String> game = new ArrayList<>(games.entrySet()).get(choice - 1);
        deleteGame(game.getKey(), game.getValue());
    }

    private void deleteGame(Long gameId, String playerName){
        // elimina gioco da mappa
        games.remove(gameId);

        // init file
        File file = new File(FilesPath.SAVES_FILE_PATH);
        ObjectMapper mapper = new ObjectMapper();

        // Scrivi la mappa aggiornata nel file JSON
        try {
            mapper.writeValue(file, games);
            logger.info("saves updated");
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
        }

        // elimina il file di gioco
        file = new File(FilesPath.PLAYER_ROOT + "\\" + playerName + "_" + gameId + ".json");
        boolean isDeleted = file.delete();
        if (!isDeleted)
            logger.severe("impossibile eliminare il file : " + file);
    }
}
