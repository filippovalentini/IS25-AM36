package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.SpecialEventType;

//SPECIAL EVENT
class SpecialEvent extends EventCard{
    private final SpecialEventType specialEventType;        //type of special event associated to the card

    public SpecialEvent(SpecialEventType specialEventType){     //constructor
        this.specialEventType = specialEventType;
    }
    public SpecialEventType getSpecialEventType(){      //returns the type of special event
        return specialEventType;
    }
    @Override
    public void solve(){}       //implements the effect of the card
}
