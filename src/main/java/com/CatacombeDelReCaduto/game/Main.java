package com.CatacombeDelReCaduto.game;

import com.CatacombeDelReCaduto.game.jsonHandlers.AwsConnectionSettings;
import com.CatacombeDelReCaduto.game.jsonHandlers.BucketManager;
import com.CatacombeDelReCaduto.game.jsonHandlers.FilesManager;
import com.CatacombeDelReCaduto.game.menus.MainMenu;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.io.File;
import java.util.Map;

/**
 * Classe di avvio del gioco.
 */
public class Main {
    public static void main(String[] args){
        try {
            // Crea la cartella di salvataggio dei dati se necessario
            FilesManager.makeSavesDir();

            // Scarica il file di salvataggio se esiste
            try (BucketManager bucket = BucketManager.loadConnection()){
                System.out.println("Connessione con AWS...");
                //sync saves
                if (bucket.syncFile(FilesManager.SAVES_FILE_NAME, FilesManager.SAVES_FILE_PATH)){
                    //sync all files se saves è stato sincronizzato
                    Map<Long, String> games = FilesManager.loadGames();
                    for (var game : games.entrySet()){
                        bucket.syncFile(FilesManager.gameFileName(game.getKey(), game.getValue()),
                                FilesManager.DATA_ROOT + "\\" + FilesManager.gameFileName(game.getKey(), game.getValue()));
                    }
                }
                AwsConnectionSettings.setLoaded(true);
                System.out.println("Connessione con AWS avvenuta con successo");
            } catch (Exception e){
                AwsConnectionSettings.setLoaded(false);
                System.out.println("Impossibile connettersi ad AWS. Il gioco verrà avviato in modalità offline.");
            }

            // Avvia il menu iniziale del gioco
            MainMenu mainMenu = new MainMenu();
            mainMenu.display();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}