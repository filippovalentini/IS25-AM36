package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.CannonShot;

import java.util.List;

//PIRATES
public class Pirates extends AdvancedEnemies {
    private final List<CannonShot> cannonFire;      //list of cannon shots that can hit the ship of a player

    public Pirates(int prizeCredits, int enemyStrength, List<CannonShot> cannonFire, int lostDays, int imageID) {
        super(prizeCredits, enemyStrength, lostDays, imageID);
        this.cannonFire = cannonFire;
    }
    public List<CannonShot> getCannonFire() {
        return cannonFire;
    }
    @Override
    public void hitShip(GameState gameState, String nickname, int diceResult, boolean activateShield, boolean activateCannon){}
    @Override
    public void defeat(GameState gameState, String nickname, int usedBatteries, boolean loseDays){}
}
