public class Player extends Entity{
    private Inventory inventory;
    private Weapon weapon;
    private Armor armor;
    private int score;
    private Room room;

    public Player(Inventory inventory, Weapon weapon, Armor armor, int score, Room room) {
        this.inventory = inventory;
        this.weapon = weapon;
        this.armor = armor;
        this.score = score;
        this.room = room;
    }

    public Player(int currentlyLifePoints, int attack, int defense, int maxLifePoints, String name, String description, Inventory inventory, Weapon weapon, Armor armor, int score, Room room) {
        super(currentlyLifePoints, attack, defense, maxLifePoints, name, description);
        this.inventory = inventory;
        this.weapon = weapon;
        this.armor = armor;
        this.score = score;
        this.room = room;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        if( inventory != null ){
            this.inventory = inventory;
        }
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        if( weapon != null ){
            this.weapon = weapon;
        }
    }

    public Armor getArmor() {
        return armor;
    }

    public void setArmor(Armor armor) {
        if( armor != null ){
            this.armor = armor;
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score += score;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        if( room != null ){
            this.room = room;
        }
    }
}
