package com.CatacombeDelReCaduto.game;

/**
 * Eccezione personalizzata per gestire la vittoria del giocatore.
 */
public class WinException extends Exception {

    /**
     * Costruttore predefinito.
     */
    public WinException() {
        super();
    }

    /**
     * Costruttore che accetta un messaggio di errore.
     *
     * @param message il messaggio di errore da visualizzare
     */
    public WinException(String message) {
        super(message);
    }
}