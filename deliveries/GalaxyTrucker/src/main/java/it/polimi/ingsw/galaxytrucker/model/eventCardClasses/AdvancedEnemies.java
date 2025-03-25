package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

//ADVANCED ENEMIES
public abstract class AdvancedEnemies extends DayLossCard{
    protected final int prizeCredits;       //cosmic credits gained if the enemy is defeated
    protected final int enemyStrength;      //strength of the enemy
    protected boolean defeated;     //true if the enemy has been defeated

    public AdvancedEnemies(int prizeCredits, int enemyStrength, int lostDays, int imageID) {   //constructor
        super(lostDays, imageID);
        this.prizeCredits = prizeCredits;
        this.enemyStrength = enemyStrength;
        this.defeated = false;
    }


    public boolean isDefeated() {
        return defeated;
    }

    public void setDefeated() {     //invoked when a player defeats the enemy
        defeated = true;
    }
}
