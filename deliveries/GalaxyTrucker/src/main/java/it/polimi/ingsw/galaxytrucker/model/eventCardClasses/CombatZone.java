package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.Player;

//COMBAT ZONE
public class CombatZone extends EventCard{
    private final boolean levelOne;     //discriminates between level I and level II card

    public CombatZone(boolean levelOne, int imageID) {     //constructor
        super(imageID);
        this.levelOne = levelOne;
    }
    public boolean isLevelOne(){        //returns the card level
        return levelOne;
    }

    @Override
    //the player with fewer crew members loses 3 flight days
    public void specialEffect(GameState gameState) throws InvalidActionException{
        String nickname = gameState.getCrewMinPlayer();
        gameState.changePlayerPosition(nickname, -3);
    }
    @Override
    public void useBatteries(GameState gameState, String nickname, int usedBatteries) throws InvalidActionException{}
    @Override
    public void hitShip(GameState gameState, String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws InvalidActionException{}

}
