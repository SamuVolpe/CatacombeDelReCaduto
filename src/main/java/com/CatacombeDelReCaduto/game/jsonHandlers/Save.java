package com.CatacombeDelReCaduto.game.jsonHandlers;

import java.util.Map;

// gestisce json salvataggio
public class Save {
    // da salvare come sono messe le stanze (oggetti dentro..)
    // progressi con npc e storia
    // dati giocatore

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
