package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.*;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.CannonShot;

import java.util.List;

/**
 * Pirates represents the pirates event card in the game.
 */
public class Pirates extends AdvancedEnemies {
    private final List<CannonShot> cannonFire;      //list of cannon shots that can hit the ship of a player
    private int currentCannonFire;          //position of the curren cannon shot that hits a ship
    private boolean defeated;  //the prize can be claimed only if not already defeated
    private boolean cannonFirePhase;        //true if we are in the phase where cannon shots have to hit a ship

    /**
     * Constructor for the Pirates event card.
     * @param prizeCredits
     * @param enemyStrength
     * @param cannonFire
     * @param lostDays
     * @param imageID
     */
    public Pirates(int prizeCredits, int enemyStrength, List<CannonShot> cannonFire, int lostDays, int imageID) {
        super(prizeCredits, enemyStrength, lostDays, imageID);
        this.cannonFire = cannonFire;
        this.currentCannonFire = 0;
        this.cannonFirePhase = false;
    }

    /**
     * Checks if the player has been defeated by the pirates.
     * @return true if the player has been defeated, false otherwise
     */
    public boolean isDefeated() {return this.defeated;}

    @Override
    /**
     * Method invoked if a player leaves the game during the cannon fire, the card resolution switches to the fight phase for the next player in turn
     * @param gameState the current game state
     * @param nickname the nickname of the player who left the game
     */
    public void manageGameQuit(GameState gameState, String nickname){
        if(nickname.equals(gameState.getTurnPlayer())){ //if the player who left the game is the current player in turn
            if(cannonFirePhase){ //if the current player is in the cannon fire phase, it is set to false
                cannonFirePhase = false;
            }
            if(gameState.isLastInTurn(nickname)){ //if the current player in turn is the last one, the game state is set to CARD_PICKING
                gameState.setGameState(State.CARD_PICKING);
            }
        }
    }

    /**
     * Method invoked when a ship has to be hit by a cannon shot.
     * @param gameState
     * @param nickname
     * @param diceResult
     * @param activateShield
     * @param activateCannon
     * @throws InvalidActionException
     * @throws NoBatteriesException
     */
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
        if(currentCannonFire == cannonFire.size() - 1) {//if no more cannon shots have to hit the ship, the player's turn is finished
            currentCannonFire = 0;
            cannonFirePhase = false;
            if (gameState.isLastInTurn(nickname)) {
                gameState.checkDamages();
            }
            if (gameState.getCrewCount(nickname) == 0) { //if the player has lost all his crew members, he is forced to quit the game
                gameState.quitGame(nickname, false);
                throw new NoCrewException("You have lost all your crew: quitting game...");
            }
            else {
                gameState.nextTurn();
            }
        }
        else{       //otherwise the cannon shot counter is incremented and the current player in turn will have to invoke this method again
            currentCannonFire++;
        }
    }
    /**
     * Method invoked when a player wants to defeat the pirates.
     * @param gameState the current game state
     * @param nickname the nickname of the player who wants to defeat the pirates
     * @param usedBatteries the number of batteries used to fire the cannon
     * @param loseDays true if the player wants to lose flight days, false otherwise
     * @throws InvalidActionException if the action is invalid
     * @throws NoBatteriesException if the player doesn't have enough batteries
     */
    @Override
    public void defeat(GameState gameState, String nickname, int usedBatteries, boolean loseDays) throws InvalidActionException, NoBatteriesException {
        if(isDefeated() || cannonFirePhase){ //if the pirates have already been defeated or if the current player is in the cannon fire phase, the action is invalid
            throw new InvalidActionException("Invalid action");
        }
        if(gameState.getNumberBatteries(nickname) < usedBatteries) { //if the player doesn't have enough batteries, the action is invalid
            throw new NoBatteriesException("Too few batteries");
        }
        double cannonStrength = gameState.getCannonStrength(nickname, usedBatteries);
        if(cannonStrength>this.enemyStrength){      //defeated pirates
            if(loseDays){       //the player gains credits and loses flight days if he wants
                gameState.updatePlayerCredits(nickname, this.prizeCredits);
                gameState.changePlayerPosition(nickname, - this.getLostDays());
            }
            this.defeated = true;
            gameState.checkDamages();
            gameState.updateTurns();
        }
        else if(cannonStrength== this.enemyStrength){       //draw; nothing happens to the player in turn but the pirates are not defeated
            if(gameState.isLastInTurn(nickname)) {
                gameState.checkDamages();
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
