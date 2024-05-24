package com.CatacombeDelReCaduto.game;

import com.CatacombeDelReCaduto.game.entities.Player;
import com.CatacombeDelReCaduto.game.items.Inventory;
import com.CatacombeDelReCaduto.game.items.Item;
import com.CatacombeDelReCaduto.game.rooms.Room;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {
    ByteArrayOutputStream outContent;

    @BeforeEach
    void beforeEach() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }
/*
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
    } */

    @Test
    void commandMoveTestMock() {
   //     Player player22 = mock(Player.class);
     //   List<Item> items = mock(List.class);
       Item item1 = mock(Item.class);
   //     Item item2 = mock(Item.class);
        Inventory inventory = mock(Inventory.class);
        when(inventory.removeItem("Item1")).thenReturn(item1);
        Game game = mock(Game.class);
        game.commandThrow("Item1");

        assertEquals("Oggetto buttato", outContent);
    }

}