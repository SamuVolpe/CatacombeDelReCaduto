package com.CatacombeDelReCaduto.game.menus;

import com.CatacombeDelReCaduto.game.entities.*;
import com.CatacombeDelReCaduto.game.rooms.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BattleMenuTest {
    ByteArrayOutputStream outContent;
    private BattleMenu battleMenu;
    private Player player;
    private Enemy enemy;
    Random random;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        player = new Player(0, "test");
        enemy = new Enemy("scheletro", "", 20, 2, 2);
        battleMenu = new BattleMenu(player, enemy);

        random = mock(Random.class);
        battleMenu.setRandom(random);
    }

    @Test
    void attackDodge(){
        when(random.nextInt(100)).thenReturn(1);
        battleMenu.attack(player, enemy);

        assertEquals(enemy.getHealth(), enemy.getMaxHealth());
    }

    @Test
    void attackLand(){
        when(random.nextInt(100)).thenReturn(99);
        battleMenu.attack(player, enemy);

        assertTrue(enemy.getHealth() < enemy.getMaxHealth());
    }

    @Test
    void commandEscapeSuccess() {
        // mock dangerLevel
        Room room = mock(Room.class);
        when(room.getDangerLevel()).thenReturn(0);
        player.setRoom(room);

        when(random.nextInt(10)).thenReturn(10);
        boolean escaped = battleMenu.commandEscape();

        assertTrue(escaped);
    }

    @Test
    void commandEscapeFail() {
        // mock dangerLevel
        Room room = mock(Room.class);
        when(room.getDangerLevel()).thenReturn(10);
        player.setRoom(room);

        when(random.nextInt(10)).thenReturn(0);
        boolean escaped = battleMenu.commandEscape();

        assertFalse(escaped);
    }
}