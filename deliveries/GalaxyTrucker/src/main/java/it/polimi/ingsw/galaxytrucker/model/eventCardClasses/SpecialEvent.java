package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.SpecialEventType;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

//SPECIAL EVENT
public class SpecialEvent extends EventCard{
    private final SpecialEventType specialEventType;        //type of special event associated to the card

    public SpecialEvent(SpecialEventType specialEventType, int imageID){     //constructor
        super(imageID);
        this.specialEventType = specialEventType;
    }
    public SpecialEventType getSpecialEventType(){      //returns the type of special event
        return specialEventType;
    }
    @Override
    public void specialEffect(GameState gameState) throws InvalidActionException{}
}
