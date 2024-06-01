package com.CatacombeDelReCaduto.game.jsonHandlers;

import java.util.Map;

/**
 * Classe di utilita` per gestire il salvataggio del gioco
 */
public class Save {
    private PlayerSave player;
    private Map<String, RoomSave> rooms;

    public Save() {
    }

    public PlayerSave getPlayer() {
        return player;
    }

    public void setPlayer(PlayerSave player) {
        this.player = player;
    }

    public Map<String, RoomSave> getRooms() {
        return rooms;
    }

    public void setRooms(Map<String, RoomSave> rooms) {
        this.rooms = rooms;
    }
}
