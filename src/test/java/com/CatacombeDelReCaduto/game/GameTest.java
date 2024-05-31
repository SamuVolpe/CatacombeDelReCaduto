package com.CatacombeDelReCaduto.game;

import com.CatacombeDelReCaduto.game.entities.Player;
import com.CatacombeDelReCaduto.game.items.*;
import com.CatacombeDelReCaduto.game.rooms.Room;
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
    Inventory inventory;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        player = mock(Player.class);
        room = mock(Room.class);
        Food mela = new Food("Mela", "Mela prelibata", 1, 5);
        Weapon coltello = new Weapon("Coltello", "Coltello affilato", 1, 5);
        Armor elmo = new Armor("Elmo", "Elmo rigido", 200, 5);
        Weapon spada = new Weapon("Spada", "Spada di pietra", 2, 5);
        List<Item> list = new ArrayList<Item>();
        inventory = new Inventory();
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

    @Test
    void commandThrowMedallion() {
        Item item = new Item("medaglione del re", "", 0);
        inventory.addItem(item);

        game.commandThrow("medaglione del re");
        assertNotNull(inventory.getItem("medaglione del re"));
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
        assertTrue(outContent.toString().contains("Impossibile equipaggiare l'arma: è già equipaggiata un'arma"));
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

    @Test
    void commandLookNoNearRooms() {
        Room noNearRooms = new Room("Stanza", "No stanze vicine", 3);
        when(room.printNearRooms()).thenReturn(noNearRooms.printNearRooms());
        game.commandLook("Stanze");
        assertTrue(outContent.toString().contains("Non ci sono stanze vicine"));
    }

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
        assertTrue(outContent.toString().contains("Nome"));
    }

    @Test
    void commandDetailItemInInventory() {
        game.commandDetail("Spada");
        //verifico che sia chiamato il toString di Item
        assertTrue(outContent.toString().contains("Nome"));
    }

    //test di Examine
    @Test
    void commandExamineNotExaminable() {
        game.commandExamine("NonEsaminabile");
        assertTrue(outContent.toString().contains("Non esaminabile"));
    }

    @Test
    void commandExamineExaminable() {
        Map<String, String> examinables = new TreeMap<>();
        examinables.put("Muro", "Muro rovinato");
        when(room.getExaminables()).thenReturn(examinables);
        game.commandExamine("Muro rovinato");
        assertTrue(outContent.toString().contains("Non esaminabile"));
    }

    //test di Use
    @Test
    void commandUseNotPresent() {
        Player player = new Player(0, "test");
        game.setPlayer(player);
        assertFalse(game.commandUse("OggettoNonPresente"));
        assertTrue(outContent.toString().contains("Impossibile utilizzare l'oggetto: non è presente nell'inventario"));
    }

    @Test
    void commandUseNotUsable() {
        Player player = new Player(0, "test");
        player.setInventory(inventory);
        game.setPlayer(player);
        assertFalse(game.commandUse("Spada"));
        assertTrue(outContent.toString().contains("Impossibile utilizzare l'oggetto"));
    }

    @Test
    void commandUseFoodSuccess() {
        Player player = new Player(0, "test");
        player.setInventory(inventory);
        game.setPlayer(player);
        assertTrue(game.commandUse("Mela"));
        assertTrue(outContent.toString().contains("Vita dopo aver mangiato"));
    }

    @Test
    void commandUseMedallionRoomIncorrect() {
        Player player = new Player(0, "test");
        Item item = new Item("medaglione del re", "", 0);
        inventory.addItem(item);
        player.setInventory(inventory);
        Room current = new Room("current", "", 0);
        player.setRoom(current);
        game.setPlayer(player);

        boolean used = game.commandUse("medaglione del re");
        assertFalse(used);
        assertTrue(outContent.toString().contains("Questo oggetto non è utilizzabile qui"));
    }

    @Test
    void commandUseMedallionAlreadyUsed() {
        Player player = new Player(0, "test");
        Item item = new Item("medaglione del re", "", 0);
        inventory.addItem(item);
        player.setInventory(inventory);
        Room current = new Room("altare", "", 0);
        player.setRoom(current);
        player.setBossRoomOpen(true);
        game.setPlayer(player);

        boolean used = game.commandUse("medaglione del re");
        assertFalse(used);
        assertTrue(outContent.toString().contains("La porta è già aperta"));
    }

    @Test
    void commandUseMedallionSuccess() {
        Player player = new Player(0, "test");
        Item item = new Item("medaglione del re", "", 0);
        inventory.addItem(item);
        player.setInventory(inventory);
        Room current = new Room("altare", "", 0);
        player.setRoom(current);
        game.setPlayer(player);

        boolean used = game.commandUse("medaglione del re");
        assertTrue(used);
        assertTrue(player.isBossRoomOpen());
    }

    @Test
    void commandMoveSuccess() {
        // prepara stanza in cui spostarsi/stanza di partenza e giocatore
        Player player = new Player(0, "test");
        Room current = new Room("current", "", 0);
        Room moveInto = new Room("moveInto", "", 0);
        current.setNearRooms(new Room[] {moveInto, null, null, null});
        player.setRoom(current);
        game.setPlayer(player);

        game.commandMove("nord");
        assertSame(moveInto, game.getPlayer().getRoom());
        assertEquals("sud", game.getPlayer().getPreviousRoomDirection());
    }

    @Test
    void commandMoveNoRoom() {
        // prepara stanza in cui spostarsi/stanza di partenza e giocatore
        Player player = new Player(0, "test");
        Room current = new Room("current", "", 0);
        current.setNearRooms(new Room[] {null, null, null, null});
        player.setRoom(current);
        player.setPreviousRoomDirection("ovest");
        game.setPlayer(player);

        game.commandMove("nord");
        assertSame(current, game.getPlayer().getRoom());
        assertEquals("ovest", game.getPlayer().getPreviousRoomDirection());
    }

    @Test
    void commandMoveFinalRoomFail() {
        // prepara stanza in cui spostarsi/stanza di partenza e giocatore
        Player player = new Player(0, "test");
        Room current = new Room("current", "", 0);
        Room moveInto = new Room("Sala del tesoro", "", 0);
        current.setNearRooms(new Room[] {moveInto, null, null, null});
        player.setRoom(current);
        player.setPreviousRoomDirection("ovest");
        player.setBossRoomOpen(false);
        game.setPlayer(player);

        game.commandMove("nord");
        assertSame(current, game.getPlayer().getRoom());
        assertEquals("ovest", game.getPlayer().getPreviousRoomDirection());
    }

    @Test
    void commandMoveFinalRoomSuccess() {
        // prepara stanza in cui spostarsi/stanza di partenza e giocatore
        Player player = new Player(0, "test");
        Room current = new Room("current", "", 0);
        Room moveInto = new Room("Sala del tesoro", "", 0);
        current.setNearRooms(new Room[] {moveInto, null, null, null});
        player.setRoom(current);
        player.setBossRoomOpen(true);
        game.setPlayer(player);

        game.commandMove("nord");
        assertSame(moveInto, game.getPlayer().getRoom());
        assertEquals("sud", game.getPlayer().getPreviousRoomDirection());
    }
}