package com.CatacombeDelReCaduto.game.prompts;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Classe che gestisce la lettura di input da console
 */
public class InputReader {
    /**
     * Legge input da console
     * @return input utente
     * @throws NoSuchElementException se nessun input trovato
     * @throws IllegalStateException se la console e` stata chiusa
     */
    public static String getInput() throws  NoSuchElementException, IllegalStateException{
        System.out.print("> ");

        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
