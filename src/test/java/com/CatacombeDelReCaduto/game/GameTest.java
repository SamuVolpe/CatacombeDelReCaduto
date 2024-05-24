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
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {
    ByteArrayOutputStream outContent;
    Game game;
    Player player;
    Room room;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        player = mock(Player.class);
        room = mock(Room.class);
        Item mela = new Item("Mela", "Mela prelibata", 1);
        Weapon coltello = new Weapon("Coltello", "Coltello affilato", 1, 5);
        Armor elmo = new Armor("Elmo", "Elmo rigido", 200, 5);
        Weapon spada = new Weapon("Spada", "Spada di pietra", 2, 5);
        List<Item> list = new ArrayList<Item>();
        Inventory inventory = new Inventory();
        inventory.addItem(spada);
        inventory.addItem(mela);
        list.add(mela);
        list.add(coltello);
        list.add(elmo);
        when(player.getRoom()).thenReturn(room);
        when(room.getItems()).thenReturn(list);
        when(player.getInventory()).thenReturn(inventory);
        game = new Game();
        game.setPlayer(player);
    }

    //test di Take
    @Test
    void commandTakeNotPresentTest() {
        game.commandTake("oggettoNonPresente");
        assertTrue(outContent.toString().contains("Impossibile raccogliere l'oggetto: l'oggetto non e' presente nella stanza"));
    }

    @Test
    void commandTakeSuccess() {
        game.commandTake("Coltello");
        assertTrue(outContent.toString().contains("Oggetto raccolto con successo"));
    }

    @Test
    void commandTakeTooHeavy() {
        game.commandTake("Elmo");
        assertTrue(outContent.toString().contains("Impossibile raccogliere l'oggetto: l'oggetto e' troppo pesante (peso=200) per l'inventario, e' necessario alleggerirlo"));
    }

    //test di Throw
    @Test
    void commandThrowNotPresent() {
        game.commandThrow("OggettoNonPresente");
        assertTrue(outContent.toString().contains("Impossibile buttare l'oggetto: non è presente nell'inventario"));
    }

    @Test
    void commandThrowSuccess() {
        game.commandThrow("Spada");
        assertTrue(outContent.toString().contains("Oggetto buttato"));
    }

    //test di Equip
    @Test
    void commandEquipNotPresent() {
        game.commandEquip("OggettoNonPresente");
        assertTrue(outContent.toString().contains("Impossibile equipaggiare l'oggetto: non è presente nell'inventario"));
    }

    @Test
    void commandEquipAlreadyEquipped() {
        Weapon martello = new Weapon("Martello", "Martello pesante", 5, 3);
        when(player.getWeapon()).thenReturn(martello);
        game.commandEquip("Spada");
        assertTrue(outContent.toString().contains("Impossibile equipaggiare l'arma: è già quipaggiata un'arma"));
    }

    @Test
    void commandEquipNotEquippable() {
        game.commandEquip("Mela");
        assertTrue(outContent.toString().contains("Impossibile equipaggiare l'oggetto: non è un oggetto equipaggiabile"));
    }

    @Test
    void commandEquipSuccess() {
        game.commandEquip("Spada");
        assertTrue(outContent.toString().contains("Spada equipaggiato con successo"));
    }

    //test di Unequip
    @Test
    void commandUnequipNotEquipped() {
        game.commandUnequip("coltello");
        assertTrue(outContent.toString().contains("Impossibile rimuovere coltello dall'equipaggiamento: non è equipaggiato"));
    }

    @Test
    void commandUnequipTooHeavy() {
        when(player.getArmor()).thenReturn(new Armor("Corazza", "Corazza pesante", Inventory.MAX_WEIGHT + 1, 10));
        game.commandUnequip("Corazza");
        assertTrue(outContent.toString().contains("Impossibile rimuovere Corazza dall'equipaggiamento: non può essere inserito nell'inventario perchè è troppo pesante, è necessario alleggerire l'inventario"));
    }

    @Test
    void commandUnequipSuccess() {
        when(player.getArmor()).thenReturn(new Armor("Corazza", "Corazza pesante", 1, 10));
        game.commandUnequip("Corazza");
        assertTrue(outContent.toString().contains("Corazza rimosso dall'equipaggiamento"));
    }

    //test di View
    @Test
    void commandViewInvalid() {
        game.commandView("ArgomentoNonValido");
        assertTrue(outContent.toString().contains("Comando incorretto : puoi visualizzare 'stato' o 'inventario'"));
    }

    @Test
    void commandViewInventory() {
        game.commandView("Inventario");
        //verifico che sia chiamato il toString di Inventario
        assertTrue(outContent.toString().contains("Inventario"));
    }

    @Test
    void commandViewState() {
        when(player.toString()).thenReturn("stato");
        game.commandView("Stato");
        assertTrue(outContent.toString().contains("stato"));
    }

    //test di Back
    @Test
    void commandBackNotAllowed() {
        game.commandBack();
        assertTrue(outContent.toString().contains("Non e' possibile tornare indietro, non ti sei mai spostato!"));
    }
/*
    @Test
    void commandBackSuccess() {
        game.commandMove("nord");
        when(room)
        game.commandBack();

    } */

    //test di Look
    @Test
    void commandLookInvalid() {
        game.commandLook("ArgomentoNonValido");
        assertTrue(outContent.toString().contains("Argomento non valido"));
    }

    @Test
    void commandLookNoItems() {
        when(room.getItems()).thenReturn(new ArrayList<Item>());
        game.commandLook("Oggetti");
        assertTrue(outContent.toString().contains("Non ci sono oggetti nella stanza"));
    }

    @Test
    void commandLookWithItems() {
        game.commandLook("Oggetti");
        //verifico che venga stampata la lista di oggetti (tra cui Mela)
        assertTrue(outContent.toString().contains("Mela"));
    }

    @Test
    void commandLookNoExaminables() {
        Room room2 = new Room("Stanza test", "prova", 5);
        when(player.getRoom()).thenReturn(room2);
        room2.setExaminables(new TreeMap<String, String>());
        game.commandLook("Esaminabili");
        assertTrue(outContent.toString().contains("Non c'è niente da esaminare nella stanza"));
    }

/*
    @Test
    void commandLookNoNearRooms() {
        game.commandLook("Stanze");
        assertTrue(outContent.toString().contains("Non ci sono stanze vicine"));
    }
*/

    //test di Detail
    @Test
    void commandDetailNotPresent() {
        game.commandDetail("OggettoNonPresente");
        assertTrue(outContent.toString().contains("L'oggetto non e' presente nell'inventario e neanche nella stanza"));
    }

    @Test
    void commandDetailItemInRoom() {
        game.commandDetail("coltello");
        //verifico che sia chiamato il toString di Item
        assertTrue(outContent.toString().contains("nome"));
    }

    @Test
    void commandDetailItemInInventory() {
        game.commandDetail("Spada");
        //verifico che sia chiamato il toString di Item
        assertTrue(outContent.toString().contains("nome"));
    }

    //TODO Use, Examine, completare Back, completare Look

}