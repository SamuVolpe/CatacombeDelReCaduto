package com.CatacombeDelReCaduto.game.menus;

import com.CatacombeDelReCaduto.game.jsonHandlers.BucketManager;
import com.CatacombeDelReCaduto.game.jsonHandlers.FilesManager;
import com.CatacombeDelReCaduto.game.prompts.InputReader;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Menu per eliminare una partita esistente
 */
public class DeleteMenu extends Menu {
    private Map<Long, String> games = new TreeMap<>();

    public DeleteMenu(){
        // carica giochi da file
        games = FilesManager.loadGames();
        // inizializza lista da visualizzare nel menu
        initMenuItems(new ArrayList<>(games.values()));
    }

    /**
     * Mostra menu e gestisce l'eliminazione della partita scelta
     */
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
        System.out.println("Partita eliminata");
    }

    /**
     * Elimina la partita
     * @param gameId id partita
     * @param playerName nome giocatore
     */
    private void deleteGame(Long gameId, String playerName){
        // elimina gioco da mappa
        games.remove(gameId);

        // init file
        File file = new File(FilesManager.SAVES_FILE_PATH);
        ObjectMapper mapper = new ObjectMapper();

        // scrivo la mappa aggiornata nel file json
        try {
            mapper.writeValue(file, games);
        } catch (IOException e) {
            System.out.println("Errore nell'eliminazione della partita, il file : '" + FilesManager.SAVES_FILE_PATH + "' potrebbe essere stato compromesso");
            e.printStackTrace();
            return;
        }

        // elimina il file di gioco se esiste
        file = new File(FilesManager.DATA_ROOT + "\\" + FilesManager.gameFileName(gameId, playerName));
        if (file.exists()) {
            boolean isDeleted = file.delete();
            if (!isDeleted) {
                System.out.println("Impossibile eliminare il file : " + file.getPath());
                return;
            }
        }

        // web
        try (BucketManager bucket = BucketManager.loadExistConnection()){
            // elimina file di gioco
            bucket.deleteFile(FilesManager.gameFileName(gameId, playerName), FilesManager.DATA_ROOT + "\\" + FilesManager.gameFileName(gameId, playerName));
            // aggiorna file salvataggi
            bucket.uploadFile(FilesManager.SAVES_FILE_NAME, FilesManager.SAVES_FILE_PATH);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
