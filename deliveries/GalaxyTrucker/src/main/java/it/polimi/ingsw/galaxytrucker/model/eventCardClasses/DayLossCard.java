package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

//DAY LOSS CARD
public abstract class DayLossCard extends EventCard{
    protected final int lostDays;     //flight days lost by a player if the card is exploited
    //or if the card has an effect on him
    public DayLossCard(int lostDays, String imagePath) {      //constructor
        super(imagePath);
        this.lostDays = lostDays;
    }
    public int getLostDays(){       //returns the lost days
        return lostDays;
    }       //returns the lost days
}
