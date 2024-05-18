package com.CatacombeDelReCaduto.game.prompts;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Logger;

public class CmdHandler {
    private static final Logger logger =  Logger.getLogger(CmdHandler.class.getName());

    public static void sendOutput(String msg){
        System.out.println(msg);
    }

    public static String getInput(){
        try {
            Scanner scanner = new Scanner(System.in);
            return scanner.nextLine();
        }
        catch (NoSuchElementException ex){
            throw ex;
        }
    }
}
