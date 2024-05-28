package com.CatacombeDelReCaduto.game.jsonHandlers;

// contiene path dei file di json
public class FilesPath {
    // folders
    public static String DATA_ROOT = "data";
    public static String PLAYER_ROOT = DATA_ROOT + "\\player";
    public static String GAME_ROOT = DATA_ROOT + "\\game";

    // files
    public static String SAVES_FILE_NAME = "saves.json";
    public static String ITEMS_FILE_PATH = GAME_ROOT + "\\items.json";
    public static String ENEMIES_FILE_PATH = GAME_ROOT + "\\enemies.json";
    public static String ROOMS_FILE_PATH = GAME_ROOT + "\\rooms.json";
    public static String SAVES_FILE_PATH = PLAYER_ROOT + "\\" + SAVES_FILE_NAME;
}
