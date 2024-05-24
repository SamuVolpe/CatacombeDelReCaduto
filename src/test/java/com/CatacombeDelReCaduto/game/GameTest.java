package com.CatacombeDelReCaduto.game;

import com.CatacombeDelReCaduto.game.entities.Player;
import com.CatacombeDelReCaduto.game.items.Armor;
import com.CatacombeDelReCaduto.game.items.Inventory;
import com.CatacombeDelReCaduto.game.items.Item;
import com.CatacombeDelReCaduto.game.items.Weapon;
import com.CatacombeDelReCaduto.game.rooms.Room;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {
    ByteArrayOutputStream outContent;
    Game game;
/*
    @BeforeEach
    void beforeEach() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
//        Room room = new Room("Stanza", "Stanza di prova", 2);
//        Room room2 = new Room("Stanza2", "Stanza di prova2", 2);
//        room.setNearRooms(new Room[]{room2});
        Player player = new Player(0, "Nome");
//        player.setRoom(game.rooms.get("Stanza1"));
        game = new Game();
        game.setPlayer(player);
//        Item mela = new Item("Mela", "Mela prelibata", 1);
//        Weapon coltello = new Weapon("Coltello", "Coltello affilato", 1, 5);
//        Armor elmo = new Armor("Elmo", "Elmo rigido", 2, 5);
//        List<Item> list = new ArrayList<>();
//        list.add(mela);
//        list.add(coltello);
//        list.add(elmo);
//        room.setItems(list);
    }

  /*  @Test
    void commandMoveTest() {
        Room room = new Room("Stanza", "Stanza di prova", 2);
        Room room2 = new Room("Stanza2", "Stanza di prova2", 2);
        room.setNearRooms(new Room[]{room2});
        Player player = new Player(21232, "Giocatore");
        player.setRoom(room);
        Game game = new Game();
        game.setPlayer() = player;
        game.commandMove("norda");
        outContent.reset();
        game.commandMove("norda");

        assertEquals("dsdadas", outContent);
    }
/*
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

    @Test
    void commandTakeNotPresentTest() {
        game.commandTake("oggettoNonPresente");
        assertEquals("Impossibile raccogliere l'oggetto: l'oggetto non e' presente nella stanza", outContent);
    }
*/
    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Player player = mock(Player.class);
        Room room = mock(Room.class);
        Item mela = new Item("Mela", "Mela prelibata", 1);
        Weapon coltello = new Weapon("Coltello", "Coltello affilato", 1, 5);
        Armor elmo = new Armor("Elmo", "Elmo rigido", 2, 5);
        List<Item> list = new ArrayList<Item>();
        list.add(mela);
        list.add(coltello);
        list.add(elmo);
        when(player.getRoom()).thenReturn(room);
        when(room.getItems()).thenReturn(list);
        game = new Game();
        game.setPlayer(player);
    }

    @Test
    void commandTakeNotPresentTest() {
        game.commandTake("oggettoNonPresente");
        assertTrue(outContent.toString().contains("Impossibile raccogliere l'oggetto: l'oggetto non e' presente nella stanza"));
    }
}