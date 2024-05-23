package com.CatacombeDelReCaduto.game;

import com.CatacombeDelReCaduto.game.entities.Player;
import com.CatacombeDelReCaduto.game.rooms.Room;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    ByteArrayOutputStream outContent;

    @BeforeEach
    void beforeEach() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void commandMoveTest() {
        Room room = new Room("Stanza", "Stanza di prova", 2);
        Room room2 = new Room("Stanza2", "Stanza di prova2", 2);
        room.setNearRooms(new Room[]{room2});
        Player player = new Player(21232, "Giocatore");
        player.setRoom(room);
        Game game = new Game();
        game.player = player;
        game.commandMove("norda");
        outContent.reset();
        game.commandMove("norda");

        assertEquals("dsdadas", outContent);
    }

}