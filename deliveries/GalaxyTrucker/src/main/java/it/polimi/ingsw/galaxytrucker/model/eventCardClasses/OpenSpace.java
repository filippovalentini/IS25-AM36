package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoBatteriesException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.*;


//OPEN SPACE
public class OpenSpace extends EventCard {

    public OpenSpace(int imageID) {
        super(imageID);
    }

    @Override
    public void fly(GameState gameState, String nickname, int usedBatteries) throws InvalidActionException, NoBatteriesException {
        Player player = gameState.getPlayersPlay().get(nickname);
        if(player.hasAbandoned()){
            gameState.changeTurn();
            return;
        }
        int maxBatteries = player.getShipBoard().getNumberBatteries();
        if(maxBatteries < usedBatteries) {
            throw new InvalidActionException("Too few batteries");
        }

    }
}