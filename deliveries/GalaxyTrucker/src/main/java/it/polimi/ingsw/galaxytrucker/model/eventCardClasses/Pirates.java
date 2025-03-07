package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.shots.CannonShot;

import java.util.List;

//PIRATES
class Pirates extends AdvancedEnemies {
    private final List<CannonShot> cannonFire;

    public Pirates(int prizeCredits, int enemyStrength, List<CannonShot> cannonFire, int lostDays) {
        super(prizeCredits, enemyStrength, lostDays);
        this.cannonFire = cannonFire;
    }
    public List<CannonShot> getCannonFire() {
        return cannonFire;
    }
    @Override
    public void solve(){}       //implements the effect of the card
}
