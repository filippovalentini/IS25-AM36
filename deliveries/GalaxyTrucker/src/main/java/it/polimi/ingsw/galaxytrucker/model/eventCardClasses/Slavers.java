package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoBatteriesException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoCrewException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

import java.util.List;

//SLAVERS
public class Slavers extends AdvancedEnemies{
    private final int crewLoss;     //number of crew members that a ship board can lose if the card has effect on the corresponding player
    private boolean crewLossPhase;      //true if we are in the phase where the current player must lose crew members

    public Slavers(int prizeCredits, int enemyStrength, int crewLoss, int lostDays, int imageID) {
        super(prizeCredits, enemyStrength, lostDays, imageID);
        this.crewLoss = crewLoss;
        this.crewLossPhase = false;
    }

    @Override
    //if a player leaves the game during the crew loss phase, the card resolution switches to the
    //fight phase for the next player in turn
    public void manageGameQuit(GameState gameState, String nickname){
        if(nickname.equals(gameState.getTurnPlayer())){
            if(crewLossPhase){
                crewLossPhase = false;
            }
            if(gameState.isLastInTurn(nickname)){
                gameState.setGameState(State.CARD_PICKING);
            }
        }
    }

    @Override
    //the player decides which crew members to remove from the ship because the slavers have defeated him
    public void landing(GameState gameState, String nickname, List<Integer> x, List<Integer> y, List<Integer> crewInEachCabin) throws InvalidActionException, NoCrewException {
        if (isDefeated() || !crewLossPhase) {
            throw new InvalidActionException("Invalid action");
        }
        gameState.removeCrewMembers(nickname, x, y, crewInEachCabin, this.crewLoss);
        if(gameState.isLastInTurn(nickname)) {
            gameState.setGameState(State.CARD_PICKING);
        }
        crewLossPhase = false;
        gameState.nextTurn();
    }

    @Override
    public void defeat(GameState gameState, String nickname, int usedBatteries, boolean loseDays) throws InvalidActionException{
        if(isDefeated() || crewLossPhase){
            throw new InvalidActionException("Invalid action");
        }
        if(gameState.getNumberBatteries(nickname) < usedBatteries) {
            throw new NoBatteriesException("Too few batteries");
        }
        double cannonStrength = gameState.getCannonStrength(nickname, usedBatteries);
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
