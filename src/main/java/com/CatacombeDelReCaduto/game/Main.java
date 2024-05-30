package com.CatacombeDelReCaduto.game;

import com.CatacombeDelReCaduto.game.jsonHandlers.BucketManager;
import com.CatacombeDelReCaduto.game.jsonHandlers.FilesManager;
import com.CatacombeDelReCaduto.game.menus.MainMenu;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.io.File;

/**
 * Classe d'avvio del gioco
 */
public class Main {
    public static void main(String[] args){
        try {
            // crea se necessario cartella salvataggio dei dati
            FilesManager.makeSavesDir();

            // scarica file salvataggi se esiste
            try (BucketManager bucket = BucketManager.loadConnection()){
                bucket.downloadFile(FilesManager.SAVES_FILE_NAME, FilesManager.SAVES_FILE_PATH);
            }
            catch (NoSuchKeyException e) {
                // file di configurazione non trovato elimina file saves se esiste
                File savesFile = new File(FilesManager.SAVES_FILE_PATH);
                if (savesFile.exists())
                    savesFile.delete();
            }
            catch (Exception e){
                System.out.println("Impossibile connettersi ad aws, controllare il file di configurazione o la rete");
                throw e;
            }

            // avvio menu iniziale
            MainMenu mainMenu = new MainMenu();
            mainMenu.display();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
