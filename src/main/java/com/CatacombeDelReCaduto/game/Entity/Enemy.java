import java.util.ArrayList;

public class Enemy extends Entity{
    private ArrayList<Item> itemsDrop;

    public Enemy() {
        itemsDrop=new ArrayList<Item>();
    }

    public Enemy(int currentlyLifePoints, int attack, int defense, int maxLifePoints, String name, String description) {
        super(currentlyLifePoints, attack, defense, maxLifePoints, name, description);
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
