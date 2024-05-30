package com.CatacombeDelReCaduto.game.entities;

/**
 * Rappresenta una entita`
 */
public class Entity {

    private String name, description;

    private int health, maxHealth, attack, defense;

    /**
     * Costruttore per inizializzare l'entità con nome, descrizione, vita massima, attacco, difesa, e punti vità attuali.
     *
     * @param name Il nome dell'entità.
     * @param description La descrizione dell'entità.
     * @param maxHealth Il punteggio di vita massima che può avere l'entitò, inizialmente le entità avranno la vita piena.
     * @param attack I punti di attacco dell'entità.
     * @param defense I punti di difesa dell'entità.
     */
    public Entity(String name, String description, int maxHealth, int attack, int defense) {
        this.defense = defense;
        this.description = description;
        this.name = name;
        this.maxHealth = maxHealth;
        this.attack = attack;
        this.health = maxHealth;
    }

    /**
     * Restituisce il punteggio di vita massima.
     *
     * @return Il punteggio di vita massima.
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Restituisce l'attacco dell'entitò.
     *
     * @return Quantità di attacco che causerebbe l'entità.
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Modifica punti all'attacco.
     *
     * @param addattack modifica attacco.
     * */
    public void addAttack(int addattack) {
        if( (attack + addattack) < 0) {
            attack = 0;
        }
        else {
            attack+=addattack;
        }
    }

    /**
     * Restituisce i punti vita attuali dell'entità.
     *
     * @return Quantità di punti vita che ha l'entità attualmente.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Modifica i punti vita attuale dell'entità.
     *
     * @param health Imposta i punti vita attuali dell'entità.
     */
    public void setHealth(int health) {
        if (health < 0)
            health = 0;
        else if (health > maxHealth)
            health = maxHealth;

        this.health = health;
    }

    /**
     * Restituisce la difesa dell'entitò.
     *
     * @return punteggio della difesa dell'entità.
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Modifica punti alla difesa.
     *
     * @param addefense modifica difesa.
     * */
    public void addDefense(int addefense) {
        if( (defense + addefense) < 0) {
            defense = 0;
        }
        else {
            defense+=addefense;
        }
        // verifico che la difesa non superi il limite massimo
        if (defense > 100)
            defense = 100;
    }

    /**
     * Restituisce il nome dell'entitò.
     *
     * @return Il nome dell'entità.
     */
    public String getName() {
        return name;
    }

    /**
     * Modifica il nome dell'entità.
     *
     * @param name Imposta il nome dell'entità.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Restituisce la descrizione dell'entitò.
     *
     * @return La descrizione dell'entità.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Modifica la descrizione dell'entità.
     *
     * @param description Imposta la descrizione dell'entità.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Override del metodo toString per fornire una rappresentazione stringa dell'entità.
     *
     * @return Una stringa che rappresenta l'entità.
     */
    @Override
    public String toString() {
        return "Nome=" + name
                + "\nInfo=" + description
                + "\nVita=" + health + "/" + maxHealth
                + ", Attacco=" + attack
                + ", Difesa=" + defense;
    }

    /**
     * Metodo che controlla se l'entità è ancora viva.
     *
     * @return boolean true se viva, false se morto.
     */
    public boolean isAlive() { return health > 0; }
}
