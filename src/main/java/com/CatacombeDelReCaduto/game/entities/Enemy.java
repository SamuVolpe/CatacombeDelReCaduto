package com.CatacombeDelReCaduto.game.entities;

import com.CatacombeDelReCaduto.game.items.Item;

import java.util.ArrayList;
import java.util.List;

public class Enemy extends Entity implements Cloneable{
    private List<Item> drop = new ArrayList<Item>();

    public Enemy(String name, String description, int maxHealth, int attack, int defense) {
        super(name, description, maxHealth, attack, defense);
    }

    public Enemy(String name, String description, int maxHealth, int attack, int defense, List<Item> drop) {
        super(name, description, maxHealth, attack, defense);
        this.drop = drop;
    }

    public boolean addItem(Item item){
        if( item != null ){
            return drop.add(item);
        }
        return false;
    }

    public boolean removeItem(Item item){
        return drop.remove(item);
    }

    @Override
    public Enemy clone() {
        try {
            Enemy clone = (Enemy) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
