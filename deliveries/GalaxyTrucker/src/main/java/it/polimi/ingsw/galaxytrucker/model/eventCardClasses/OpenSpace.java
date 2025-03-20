package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.*;


//OPEN SPACE
public class OpenSpace extends EventCard {

    public OpenSpace(int imageID) {
        super(imageID);
    }

    @Override
    public void fly(GameState gameState, String nickname, int usedBatteries) throws InvalidActionException, NoBatteriesException {
        if(gameState.getNumberBatteries(nickname) < usedBatteries) {
            throw new NoBatteriesException("Too few batteries");
        }
        float engineStrength = gameState.getEngineStrength(nickname, usedBatteries);
        gameState.changePlayerPosition(nickname, (int) Math.abs(engineStrength));
        if(gameState.isLastInTurn(nickname)) {
            gameState.setGameState(State.CARD_PICKING);
        }
        gameState.nextTurn();
    }
}