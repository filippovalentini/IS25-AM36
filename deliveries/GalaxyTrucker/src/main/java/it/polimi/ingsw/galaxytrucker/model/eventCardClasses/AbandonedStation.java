package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;

import java.util.List;

//ABANDONED STATION
class AbandonedStation extends DayLossCard{
    private final List<Color> stationGoods;     //goods that a player can gain by using the card
    private final int requiredCrew;     //required crew to land in the station
    private boolean used;       //set to true if a player has already used the card

    public AbandonedStation(List<Color> stationGoods, int requiredCrew, int lostDays) { //constructor
        super(lostDays);
        this.stationGoods = stationGoods;
        this.requiredCrew = requiredCrew;
        this.used = false;
    }
    public List<Color> getStationGoods() {      //returns the goods that a player can gain in the station
        return stationGoods;
    }
    public int getRequiredCrew() {      //returns the required crew to land in the station
        return requiredCrew;
    }
    public boolean isUsed() {       //determines if the card has been used or not
        return used;
    }
    public void setUsed() {     //invoked when a player decides to use the card
        used = true;
    }
    @Override
    public void solve(){}       //implements the effect of the card
}
