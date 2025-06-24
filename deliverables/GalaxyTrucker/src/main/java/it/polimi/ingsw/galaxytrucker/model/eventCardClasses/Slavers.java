package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoBatteriesException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoCrewException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

import java.util.List;

/**
 * Slavers is an event card that represents a group of slavers attacking a player's ship.
 */
public class Slavers extends AdvancedEnemies{
    private final int crewLoss;     //number of crew members that a ship board can lose if the card has effect on the corresponding player
    private boolean crewLossPhase;      //true if we are in the phase where the current player must lose crew members

    /**
     * Constructor for Slavers event card.
     * @param prizeCredits
     * @param enemyStrength
     * @param crewLoss
     * @param lostDays
     * @param imageID
     */
    public Slavers(int prizeCredits, int enemyStrength, int crewLoss, int lostDays, int imageID) {
        super(prizeCredits, enemyStrength, lostDays, imageID);
        this.crewLoss = crewLoss;
        this.crewLossPhase = false;
    }

    @Override
    /**
     * If a player leaves the game during the crew loss phase, the card resolution switches to the fight phase for the next player in turn
     * @param gameState the current game state
     * @param nickname the nickname of the player who left the game
     */
    public void manageGameQuit(GameState gameState, String nickname){
        if(nickname.equals(gameState.getTurnPlayer())){ // if the player who left is the current player
            if(crewLossPhase){// if we are in the crew loss phase
                crewLossPhase = false;
            }
            if(gameState.isLastInTurn(nickname)){ // if the player who left is the last in turn
                gameState.setGameState(State.CARD_PICKING);
            }
        }
    }

    /**
     * This method is called when the player decides which crew members to remove from the ship because the slavers have defeated him.
     * @param gameState
     * @param nickname
     * @param x
     * @param y
     * @param crewInEachCabin
     * @throws InvalidActionException
     * @throws NoCrewException
     */
    @Override
    //the player decides which crew members to remove from the ship because the slavers have defeated him
    public void landing(GameState gameState, String nickname, List<Integer> x, List<Integer> y, List<Integer> crewInEachCabin) throws InvalidActionException, NoCrewException {
        if (isDefeated() || !crewLossPhase) { // if the player has already defeated the slavers or we are not in the crew loss phase
            throw new InvalidActionException("Invalid action");
        }
        gameState.removeCrewMembers(nickname, x, y, crewInEachCabin, this.crewLoss); // remove the crew members from the ship
        if(gameState.isLastInTurn(nickname)) { // if the player who just lost crew members is the last in turn
            gameState.setGameState(State.CARD_PICKING);
        }
        crewLossPhase = false; // end the crew loss phase
        gameState.nextTurn();
    }

    /**
     * This method is called when the player decides to fight the slavers.
     * @param gameState
     * @param nickname
     * @param usedBatteries
     * @param loseDays
     * @throws InvalidActionException
     */
    @Override
    public void defeat(GameState gameState, String nickname, int usedBatteries, boolean loseDays) throws InvalidActionException{
        if(isDefeated() || crewLossPhase){ // if the player has already defeated the slavers or we are in the crew loss phase
            throw new InvalidActionException("Invalid action");
        }
        if(gameState.getNumberBatteries(nickname) < usedBatteries) { // if the player does not have enough batteries
            throw new NoBatteriesException("Too few batteries");
        }
        double cannonStrength = gameState.getCannonStrength(nickname, usedBatteries); // calculate the cannon strength of the player's ship
        if(cannonStrength>this.enemyStrength){      //defeated slavers
            if(loseDays){       //the player gains credits and loses flight days if he wants
                gameState.updatePlayerCredits(nickname, this.prizeCredits);
                gameState.changePlayerPosition(nickname, - this.getLostDays());
            }
            this.defeated = true;
            gameState.setGameState(State.CARD_PICKING);
            gameState.updateTurns();
        }
        else if(cannonStrength== this.enemyStrength){       //draw; nothing happens to the player in turn but the slavers are not defeated
            if(gameState.isLastInTurn(nickname)) {
                gameState.setGameState(State.CARD_PICKING);
            }
            gameState.nextTurn();
        }
        else{
            //if cannonStrength<this.enemyStrength, the slavers have defeated the player; nothing happens, but
            //the player is forced to lose crew members, otherwise the game can't go on
            if (gameState.getCrewCount(nickname)< this.crewLoss) {
                if(gameState.isLastInTurn(nickname)) {
                    gameState.setGameState(State.CARD_PICKING);
                }
                gameState.quitGame(nickname, false);
                throw new NoCrewException("You do not have enough crew members: quitting game...");
            }
            else{
                crewLossPhase = true;
            }
        }
    }
}
