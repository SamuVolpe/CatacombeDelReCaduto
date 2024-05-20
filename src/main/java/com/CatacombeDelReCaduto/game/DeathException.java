package com.CatacombeDelReCaduto.game;

public class DeathException extends Exception {
    private static final String DEFAULT_MESSAGE = "Sei morto.";
    private String message;

    public DeathException(String message) {
        super(message);
        this.message = message;
    }

    public DeathException(){
        super(DEFAULT_MESSAGE);
    }

    public String getMessage() {
        return message;
    }
    public String getDefaultMessage() {return DEFAULT_MESSAGE; }
}
