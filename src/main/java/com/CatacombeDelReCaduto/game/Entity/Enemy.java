package com.CatacombeDelReCaduto.game.Entity;
import java.util.ArrayList;

public class Enemy extends Entity{
    private ArrayList<Item> itemsDrop;

    public Enemy() {
        itemsDrop=new ArrayList<Item>();
    }

    public Enemy(int defense, String description, String name, Room room, int maxLifePoints, int attack, int currentlyLifePoints) {
        super(defense, description, name, room, maxLifePoints, attack, currentlyLifePoints);
        itemsDrop=new ArrayList<Item>();
    }

    public boolean addItem(Item item){
        if( item != null ){
            return itemsDrop.add(item);
        }
        return false;
    }

    public boolean removeItem(Item item){
        return itemsDrop.remove(item);
    }

}
