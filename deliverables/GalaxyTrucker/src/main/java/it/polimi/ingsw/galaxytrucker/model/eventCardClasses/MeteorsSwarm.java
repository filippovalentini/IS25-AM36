package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoBatteriesException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoCrewException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.Meteor;

import java.util.List;

/**
 * Class representing the MeteorsSwarm event card.
 */
public class MeteorsSwarm extends EventCard{
    private final List<Meteor> meteors;     //meteors that compose the swarm
    private int currentMeteor;          //position in list of the next meteor that will hit a player's ship

    /**
     * Constructor for the MeteorsSwarm event card.
     * @param meteors
     * @param imageID
     */
    public MeteorsSwarm(List<Meteor> meteors, int imageID) {       //constructor
        super(imageID);
        this.meteors = meteors;
        this.currentMeteor = 0;
    }

    /**
     * Method to handle the meteor attack on a player's ship.
     * @param gameState
     * @param nickname
     * @param diceResult
     * @param activateShield
     * @param activateCannon
     * @throws InvalidActionException
     * @throws NoBatteriesException
     */
    @Override
    public void hitShip(GameState gameState, String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws InvalidActionException, NoBatteriesException {
        if((activateCannon || activateShield) && gameState.getNumberBatteries(nickname) == 0){ //if the player doesn't have enough batteries it can't invoke this method
            throw new InvalidActionException("Too few batteries");
        }
        Orientation orientation = meteors.get(currentMeteor).getOrientation(); //orientation of the current meteor
        int direction = (orientation.isVertical() ? diceResult-4 : diceResult-5); //direction of the current meteor based on the dice result
        gameState.meteorAttack(nickname, meteors.get(currentMeteor), direction, activateShield, activateCannon); //current meteor hits the ship

        if(currentMeteor == meteors.size() - 1){ //if no more meteors have to hit the ship, the player's turn is finished
            currentMeteor=0;
            if(gameState.isLastInTurn(nickname)) { // if the current player is the last in turn, check damages
                gameState.checkDamages();
            }
            if (gameState.getCrewCount(nickname)==0) { //if the player has no crew left, he quits the game
                gameState.quitGame(nickname, false);
                throw new NoCrewException("You have lost all your crew: quitting game...");
            }else{
                gameState.nextTurn();
            }
        }
        else{
            currentMeteor++;
        }
    }
}
