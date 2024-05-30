package com.CatacombeDelReCaduto.game;

import com.CatacombeDelReCaduto.game.jsonHandlers.BucketManager;
import com.CatacombeDelReCaduto.game.jsonHandlers.FilesManager;
import com.CatacombeDelReCaduto.game.menus.MainMenu;

/**
 * Classe d'avvio del gioco
 */
public class Main {
    public static void main(String[] args){
        try {
            // crea se necessario cartella salvataggio dei dati
            FilesManager.makeSavesDir();

            // sync file di gioco da web
            try (BucketManager bucket = BucketManager.loadConnection()){
                bucket.syncFile(FilesManager.SAVES_FILE_NAME, FilesManager.SAVES_FILE_PATH);
                BucketManager.filesSync = true;
            }catch (Exception e){
                System.out.println("Impossibile connettersi ad aws, il gioco proseguirà in modalità offline");
            }

            // avvio menu iniziale
            MainMenu mainMenu = new MainMenu();
            mainMenu.display();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
