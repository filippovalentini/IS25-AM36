package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

/**
 * This class represents advanced enemies in the game, which are a type of event card
 */
public abstract class AdvancedEnemies extends DayLossCard{
    protected final int prizeCredits;       //cosmic credits gained if the enemy is defeated
    protected final int enemyStrength;      //strength of the enemy
    protected boolean defeated;     //true if the enemy has been defeated

    /**
     * Constructor for AdvancedEnemies
     * @param prizeCredits
     * @param enemyStrength
     * @param lostDays
     * @param imageID
     */
    public AdvancedEnemies(int prizeCredits, int enemyStrength, int lostDays, int imageID) {   //constructor
        super(lostDays, imageID);
        this.prizeCredits = prizeCredits;
        this.enemyStrength = enemyStrength;
        this.defeated = false;
    }

    /**
     * Returns defeated status of the enemy
     * @return true if the enemy has been defeated, false otherwise
     */
    public boolean isDefeated() {
        return defeated;
    }

}
