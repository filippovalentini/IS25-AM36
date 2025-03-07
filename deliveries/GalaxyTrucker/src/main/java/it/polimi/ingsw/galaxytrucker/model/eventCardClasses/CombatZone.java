package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

//COMBAT ZONE
public class CombatZone extends EventCard{
    private final boolean levelOne;     //discriminates between level I and level II card

    public CombatZone(boolean levelOne, String imagePath) {//constructor
        super(imagePath);
        this.levelOne = levelOne;
    }
    public boolean isLevelOne(){        //returns the card level
        return levelOne;
    }
    @Override
    public void solve(){}       //implements the effect of the card
}
