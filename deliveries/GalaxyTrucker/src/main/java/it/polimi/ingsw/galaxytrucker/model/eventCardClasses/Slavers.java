package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

//SLAVERS
class Slavers extends AdvancedEnemies{
    private final int crewLoss;

    public Slavers(int prizeCredits, int enemyStrength, int crewLoss, int lostDays){
        super(prizeCredits, enemyStrength, lostDays);
        this.crewLoss = crewLoss;
    }
    public int getCrewLoss() {
        return crewLoss;
    }
    @Override
    public void solve(){}       //implements the effect of the card
}
