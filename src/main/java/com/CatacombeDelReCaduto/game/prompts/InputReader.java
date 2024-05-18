package com.CatacombeDelReCaduto.game.prompts;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Logger;

public class InputReader {
    private static final Logger logger =  Logger.getLogger(InputReader.class.getName());

    public static String getInput(){
        try {
            System.out.print("> ");

            Scanner scanner = new Scanner(System.in);
            return scanner.nextLine();
        }
        catch (NoSuchElementException ex){
            throw ex;
        }
    }
}
