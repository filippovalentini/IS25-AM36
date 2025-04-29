package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.*;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.CannonShot;

import java.util.List;

//PIRATES
public class Pirates extends AdvancedEnemies {
    private final List<CannonShot> cannonFire;      //list of cannon shots that can hit the ship of a player
    private int currentCannonFire;          //position of the curren cannon shot that hits a ship
    private boolean defeated;  //the prize can be claimed only if not already defeated
    private boolean cannonFirePhase;        //true if we are in the phase where cannon shots have to hit a ship

    public Pirates(int prizeCredits, int enemyStrength, List<CannonShot> cannonFire, int lostDays, int imageID) {
        super(prizeCredits, enemyStrength, lostDays, imageID);
        this.cannonFire = cannonFire;
        this.currentCannonFire = 0;
        this.cannonFirePhase = false;
    }

    public boolean isDefeated() {return this.defeated;}

    public List<CannonShot> getCannonFire() {
        return cannonFire;
    }
    @Override
    public void hitShip(GameState gameState, String nickname, int diceResult, boolean activateShield, boolean activateCannon /*ignored*/) throws InvalidActionException, NoBatteriesException {
        if(isDefeated() || !cannonFirePhase){  //if the enemy has been defeated or hasn't defeated the current player, a player can't invoke this method
            throw new InvalidActionException("Invalid action");
        }
        if((activateShield) && gameState.getNumberBatteries(nickname) == 0){    //if the player doesn't have enough batteries it can't invoke this method
            throw new NoBatteriesException("Too few batteries");
        }
        Orientation orientation = cannonFire.get(currentCannonFire).getOrientation();
        int direction = (orientation.isVertical() ? diceResult-4 : diceResult-5);
        gameState.cannonFireAttack(nickname, cannonFire.get(currentCannonFire), direction, activateShield); //current cannon shot hits the ship
        if(currentCannonFire == cannonFire.size() - 1) {     //if no more cannon shots have to hit the ship, the player's turn is finished
            if (gameState.getCrewCount(nickname) == 0) {
                System.out.println("You lost all your crew member.");
                gameState.quitGame(nickname);
            } else {
                if (gameState.isLastInTurn(nickname)) {
                    gameState.setGameState(State.CARD_PICKING);
                }
                currentCannonFire = 0;
                cannonFirePhase = false;
                gameState.nextTurn();
            }
        }
        else{       //otherwise the cannon shot counter is incremented and the current player in turn will have to invoke this method again
            currentCannonFire++;
        }
    }
    @Override
    public void defeat(GameState gameState, String nickname, int usedBatteries, boolean loseDays) throws InvalidActionException, NoBatteriesException {
        if(isDefeated() || cannonFirePhase){
            throw new InvalidActionException("Invalid action");
        }
        if(gameState.getNumberBatteries(nickname) < usedBatteries) {
            throw new NoBatteriesException("Too few batteries");
        }
        double cannonStrength = gameState.getCannonStrength(nickname, usedBatteries);
        if(cannonStrength>this.enemyStrength){      //defeated pirates
            if(loseDays){       //the player gains credits and loses flight days if he wants
                gameState.updatePlayerCredits(nickname, this.prizeCredits);
                gameState.changePlayerPosition(nickname, - this.getLostDays());
            }
            this.defeated = true;
            gameState.setGameState(State.CARD_PICKING);
            gameState.updateTurns();
        }
        else if(cannonStrength== this.enemyStrength){       //draw; nothing happens to the player in turn but the pirates are not defeated
            if(gameState.isLastInTurn(nickname)) {
                gameState.setGameState(State.CARD_PICKING);
            }
            gameState.nextTurn();
        }
        else{
            //if cannonStrength<this.enemyStrength, the pirates have defeated the player; nothing happens, but
            //the player is forced to throw the dice and receive a cannon shot, otherwise the game can't go on
            cannonFirePhase = true;
        }

    }
}
