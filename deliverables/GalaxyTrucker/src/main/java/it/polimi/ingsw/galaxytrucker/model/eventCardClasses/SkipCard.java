package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.Player;

/**
 * SkipCard is an abstract class representing a card that allows a player to skip their turn
 */
public abstract class SkipCard extends DayLossCard{
    /**
     * Constructor for SkipCard
     * @param lostDays
     * @param imageId
     */
    public SkipCard(int lostDays, int imageId){
        super(lostDays, imageId);
    }

    /**
     * Method invoked when a player doesn't want to land on the station
     * @param gameState
     * @param nickname
     * @throws InvalidActionException
     */
    public void skip(GameState gameState, String nickname) throws InvalidActionException {
        if(gameState.isLastInTurn(nickname)) {
            gameState.setGameState(State.CARD_PICKING);
        }
        gameState.nextTurn();
    }
}
