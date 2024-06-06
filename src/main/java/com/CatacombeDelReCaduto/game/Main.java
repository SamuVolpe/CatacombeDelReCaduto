package com.CatacombeDelReCaduto.game;

import com.CatacombeDelReCaduto.game.jsonHandlers.AwsConnectionSettings;
import com.CatacombeDelReCaduto.game.jsonHandlers.BucketManager;
import com.CatacombeDelReCaduto.game.jsonHandlers.FilesManager;
import com.CatacombeDelReCaduto.game.menus.MainMenu;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.io.File;

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
                bucket.downloadFile(FilesManager.SAVES_FILE_NAME, FilesManager.SAVES_FILE_PATH);
                AwsConnectionSettings.setLoaded(true);
            }
            catch (NoSuchKeyException e) {
                // Se il file di configurazione non è trovato, elimina il file di salvataggio se esiste
                File savesFile = new File(FilesManager.SAVES_FILE_PATH);
                if (savesFile.exists())
                    savesFile.delete();
                AwsConnectionSettings.setLoaded(true);
            }
            catch (Exception e){
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