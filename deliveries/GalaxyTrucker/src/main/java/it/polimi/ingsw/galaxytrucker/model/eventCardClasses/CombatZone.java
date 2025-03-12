package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

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
    public void solve(GameState gameState){}       //implements the effect of the card
}
