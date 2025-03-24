package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoBatteriesException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.CannonShot;

import java.util.List;

//PIRATES
public class Pirates extends AdvancedEnemies {
    private final List<CannonShot> cannonFire;      //list of cannon shots that can hit the ship of a player
    private int currentCannonFire;
    private boolean defeated;  //the prize can be claimed only if not already defeated

    public Pirates(int prizeCredits, int enemyStrength, List<CannonShot> cannonFire, int lostDays, int imageID) {
        super(prizeCredits, enemyStrength, lostDays, imageID);
        this.cannonFire = cannonFire;
        this.currentCannonFire = 0;
    }

    public boolean isDefeated() {return this.defeated;}

    public List<CannonShot> getCannonFire() {
        return cannonFire;
    }
    @Override
    public void hitShip(GameState gameState, String nickname, int diceResult, boolean activateShield, boolean activateCannon){
        //activateCannon is ignored
        if((activateShield) && gameState.getNumberBatteries(nickname) == 0){
            throw new InvalidActionException("Too few batteries");
        }
        Orientation orientation = cannonFire.get(currentCannonFire).getOrientation();
        int direction = (orientation.isVertical() ? diceResult-4 : diceResult-5);
        gameState.cannonFireAttack(nickname, cannonFire.get(currentCannonFire), direction, activateShield);
        if(currentCannonFire == cannonFire.size() - 1){
            if(gameState.isLastInTurn(nickname)) {
                gameState.setGameState(State.CARD_PICKING);
            }
            currentCannonFire=0;
            gameState.nextTurn();
        }
        else{
            currentCannonFire++;
        }
    }
    @Override
    public void defeat(GameState gameState, String nickname, int usedBatteries, boolean loseDays){
        if(this.defeated){
            throw new InvalidActionException("Pirates already defeated");
        }
        if(gameState.getNumberBatteries(nickname) < usedBatteries) {
            throw new NoBatteriesException("Too few batteries");
        }
        double cannonStrength = gameState.getCannonStrength(nickname, usedBatteries);
        if(cannonStrength>=this.enemyStrength){
            if(!this.defeated){
                gameState.updatePlayerCredits(nickname, this.prizeCredits);
                gameState.changePlayerPosition(nickname, this.getLostDays());
            }
            this.defeated = true;
        }
        if(gameState.isLastInTurn(nickname)) {
            gameState.setGameState(State.CARD_PICKING);
        }
        gameState.nextTurn();
    }
}
