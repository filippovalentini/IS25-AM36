package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

/**
 * This class is used to represent adventure cards that imply a loss of positions in the flight board for players that decide to use the card or that are subject of its effect
 */
public abstract class DayLossCard extends EventCard{
    protected final int lostDays;     //flight days lost by a player if the card is exploited
    //or if the card has an effect on him

    /**
     * Constructor for DayLossCard
     * @param lostDays
     * @param imageID
     */
    public DayLossCard(int lostDays, int imageID) {      //constructor
        super(imageID);
        this.lostDays = lostDays;
    }

    /**
     * Returns the number of flight days lost by a player if the card is exploited
     * @return lostDays
     */
    public int getLostDays(){       //returns the lost days
        return lostDays;
    }       //returns the lost days
}
