package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.SpecialEventType;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.*;

import java.util.*;

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
    //if the special event is of the type STARDUST, this method ensures that each player loses as many flight
    //days as the number of exposed connectors on its ship. If the special event is of the type EPIDEMIC, this method
    //ensures that each player loses a crew member from each cabin which is connected to another busy cabin
    public void specialEffect(GameState gameState) throws InvalidActionException{
        List<String> nicknames = gameState.getNicknames();
        Collections.reverse(nicknames);

        if(specialEventType == SpecialEventType.STARDUST){
            int exposedConnectors;
            for(String nickname : nicknames){
                exposedConnectors = gameState.countExposedConnectors(nickname);
                gameState.changePlayerPosition(nickname, -exposedConnectors);

            }
        }
        if(specialEventType == SpecialEventType.EPIDEMIC){
            for(String nickname : nicknames){
                gameState.epidemicEffect(nickname);
            }
        }
        gameState.setGameState(State.CARD_PICKING);
    }
}
