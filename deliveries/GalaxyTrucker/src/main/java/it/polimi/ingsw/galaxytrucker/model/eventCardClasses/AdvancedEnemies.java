package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

//ADVANCED ENEMIES
public abstract class AdvancedEnemies extends DayLossCard{
    protected final int prizeCredits;
    protected final int enemyStrength;
    protected boolean defeated;

    public AdvancedEnemies(int prizeCredits, int enemyStrength, int lostDays, String imagePath) {
        super(lostDays, imagePath);
        this.prizeCredits = prizeCredits;
        this.enemyStrength = enemyStrength;
        this.defeated = false;
    }

    public int getPrizeCredits() {
        return prizeCredits;
    }
    public int getEnemyStrength() {
        return enemyStrength;
    }
    public boolean isDefeated() {
        return defeated;
    }
    public void setDefeated() {
        defeated = true;
    }
}
