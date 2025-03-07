package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;

import java.util.List;

//SMUGGLERS
public class Smugglers extends DayLossCard{
    private final List<Color> prizeGoods;       //goods that a player gains by defeating the smugglers
    private final int goodLoss;     //goods that a player loses if defeated by the smugglers
    private final int enemyStrength;        //strength required to defeat the smugglers
    private boolean defeated;       //set to true if a player has defeated the smugglers

    public Smugglers(List<Color> prizeGoods, int goodLoss, int enemyStrength, int lostDays, String imagePath) {  //constructor
        super(lostDays, imagePath);
        this.prizeGoods = prizeGoods;
        this.goodLoss = goodLoss;
        this.enemyStrength = enemyStrength;
        this.defeated = false;
    }

    public List<Color> getPrizeGoods() {    //returns the goods that a player gains by defeating the smugglers
        return prizeGoods;
    }
    public int getGoodLoss() {   //returns the goods that a player loses if defeated by the smugglers
        return goodLoss;
    }
    public int getEnemyStrength(){   //returns the strength required to defeat the smugglers
        return enemyStrength;
    }
    public boolean isDefeated() {   //determines if a player has defeated the smugglers or not
        return defeated;
    }
    public void setDefeated() {      //invoked when a player defeats the smugglers
        defeated = true;
    }
    @Override
    public void solve(){}       //implements the effect of the card
}
