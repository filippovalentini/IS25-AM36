package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.CannonShot;

import java.util.List;

//PIRATES
public class Pirates extends AdvancedEnemies {
    private final List<CannonShot> cannonFire;      //list of cannon shots that can hit the ship of a player

    public Pirates(int prizeCredits, int enemyStrength, List<CannonShot> cannonFire, int lostDays, String imagePath) {
        super(prizeCredits, enemyStrength, lostDays, imagePath);
        this.cannonFire = cannonFire;
    }
    public List<CannonShot> getCannonFire() {
        return cannonFire;
    }
    @Override
    public void solve(GameState gameState){}       //implements the effect of the card
}
