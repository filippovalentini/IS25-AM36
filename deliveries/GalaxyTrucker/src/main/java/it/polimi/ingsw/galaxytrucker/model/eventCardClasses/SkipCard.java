package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.Player;

public abstract class SkipCard extends DayLossCard{
    public SkipCard(int lostDays, int imageId){
        super(lostDays, imageId);
    }

    //invoked when a player doesn't want to land on the station
    public void skip(GameState gameState, String nickname){
        if(gameState.isLastInTurn(nickname)) {
            gameState.setGameState(State.CARD_PICKING);
        }
        gameState.nextTurn();
    }
}
