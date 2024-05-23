package com.CatacombeDelReCaduto.game.items;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
    private static Inventory inventory;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
    }

    @Test
    void addOneLightItemTest() {
        Item it = new Item("Mela", "Mela pregiata", 1);
        assertTrue(inventory.addItem(it));
    }

    @Test
    void addTwoLightItemTest() {
        Item it = new Item("Mela", "Mela pregiata", 1);
        Item it2 = new Item("Mela", "Mela pregiata", 1);
        assertTrue(inventory.addItem(it) && inventory.addItem(it2));
    }

    @Test
    void addTooHeavyItemTest() {
        Item it = new Item("Mela", "Mela pregiata", 1000);
        assertFalse(inventory.addItem(it));
    }


    @Test
    void removeNotPresentItem() {
        Item it = new Item("Mela", "Mela pregiata", 1);
        inventory.addItem(it);
        assertNull(inventory.removeItem("Fragola"));
    }

    @Test
    void removeFromVoidInventory() {
        assertNull(inventory.removeItem("Fragola"));
    }

    @Test
    void removePresentItem() {
        Item it = new Item("Mela", "Mela pregiata", 1);
        inventory.addItem(it);
        assertEquals(it, inventory.removeItem("Mela"));
    }

    @Test
    void removeMultiplePresentItem() {
        Item it = new Item("Mela", "Mela pregiata", 1);
        Item it2 = new Item("Mela", "Mela pregiata", 1);
        inventory.addItem(it);
        inventory.addItem(it2);
        assertEquals(it, inventory.removeItem("Mela"));
    }
}