package com.CatacombeDelReCaduto.game.menus;

import com.CatacombeDelReCaduto.game.jsonHandlers.*;

import com.CatacombeDelReCaduto.game.prompts.InputReader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

public class LoadMenu extends Menu {
    private final Logger logger =  Logger.getLogger(this.getClass().getName());
    private Map<Long, String> games = new TreeMap<>();

    public LoadMenu(){
        // carica giochi da file
        loadGames();
        // inizializza lista da visualizzare nel menu
        initMenuItems(new ArrayList<>(games.values()));
    }

    public Save display() {
        if (games.isEmpty()){
            System.out.println("Nessuna partita esistente");
            return null;
        }

        int choice = -1;

        do {
            // stampo menu
            print();

            // prendo input
            String userCommand = InputReader.getInput();

            // verifico se l'utente ha inputato il numero del menu
            choice = userChoice(userCommand);
            if (choice < 1)
                System.out.println("Scegliere un numero dal menu");

        } while (choice < 1);

        // trova gioco scelto e lo carica nella classe
        Map.Entry<Long, String> game = new ArrayList<>(games.entrySet()).get(choice - 1);
        return loadGame(game.getKey(), game.getValue());
    }

    private void loadGames(){
        // Controlla se il file esiste
        File file = new File(FilesPath.SAVES_FILE_PATH);
        if (!file.exists())
            return;

        ObjectMapper mapper = new ObjectMapper();

        // Leggi il file JSON e deserializza nella mappa
        try {
            games = mapper.readValue(file, new TypeReference<Map<Long, String>>() {});
            logger.info("saves file readed");
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
        }
    }

    private Save loadGame(Long gameId, String playerName){
        // carica gioco dal file
        return new Save();
    }
}
