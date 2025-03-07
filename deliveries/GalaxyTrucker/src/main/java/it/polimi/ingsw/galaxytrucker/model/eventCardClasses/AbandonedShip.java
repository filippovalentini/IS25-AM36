package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

//ABANDONED SHIP
public class AbandonedShip extends DayLossCard{
    private final int requiredCrew;      //required crew to land in the ship
    private final int gainedCredits;       //credits that a player can gain by using the card
    private boolean used;       //set to true if a player has already used the card

    public AbandonedShip(int requiredCrew, int gainedCredits, int lostDays, String imagePath) {     //constructor
        super(lostDays, imagePath);
        this.requiredCrew = requiredCrew;
        this.gainedCredits = gainedCredits;
        this.used = false;
    }
    public int getRequiredCrew() {      //returns the required crew to land in the station
        return requiredCrew;
    }
    public int getGainedCredits() {     //returns the credits that a player can gain by using the card
        return gainedCredits;
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