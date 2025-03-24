package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoBatteriesException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

import java.util.List;

//SMUGGLERS
public class Smugglers extends DayLossCard{
    private final List<Color> prizeGoods;       //goods that a player gains by defeating the smugglers
    private final int goodLoss;     //goods that a player loses if defeated by the smugglers
    private final int enemyStrength;        //strength required to defeat the smugglers
    private boolean defeated;       //set to true if a player has defeated the smugglers

    public Smugglers(List<Color> prizeGoods, int goodLoss, int enemyStrength, int lostDays, int imageID) {  //constructor
        super(lostDays, imageID);
        this.prizeGoods = prizeGoods;
        this.goodLoss = goodLoss;
        this.enemyStrength = enemyStrength;
        this.defeated = false;
    }

    public boolean isDefeated() {   //determines if a player has defeated the smugglers or not
        return defeated;
    }

    public void setDefeated() {      //invoked when a player defeats the smugglers
        defeated = true;
    }
    @Override
    public void defeat(GameState gameState, String nickname, int usedBatteries, boolean loseDays) throws InvalidActionException{
        if(this.defeated){
            throw new InvalidActionException("Pirates already defeated");
        }
        if(gameState.getNumberBatteries(nickname) < usedBatteries) {
            throw new NoBatteriesException("Too few batteries");
        }
        double cannonStrength = gameState.getCannonStrength(nickname, usedBatteries);
        if(cannonStrength>=this.enemyStrength){
            if(!this.defeated){
                //gameState.substitutePlayerGood(...);
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
