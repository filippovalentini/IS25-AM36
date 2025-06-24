package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.SpecialEventType;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.*;

import java.util.*;

/**
 * This class represents a special event card. It extends the EventCard
 */
public class SpecialEvent extends EventCard{
    private final SpecialEventType specialEventType;        //type of special event associated to the card

    /**
     * Constructor for the SpecialEvent class.
     * @param specialEventType
     * @param imageID
     */
    public SpecialEvent(SpecialEventType specialEventType, int imageID){     //constructor
        super(imageID);
        this.specialEventType = specialEventType;
    }

    /**
     * This method applies the special effect of the special event card to the game state.
     * @param gameState
     * @throws InvalidActionException
     */
    @Override
    //if the special event is of the type STARDUST, this method ensures that each player loses as many flight
    //days as the number of exposed connectors on its ship. If the special event is of the type EPIDEMIC, this method
    //ensures that each player loses a crew member from each cabin which is connected to another busy cabin
    public void specialEffect(GameState gameState) throws InvalidActionException{
        List<String> nicknames = gameState.getNicknames(); //get the list of nicknames of the players in the game
        Collections.reverse(nicknames); //reverse the list of nicknames to ensure that the players are affected in the correct order

        if(specialEventType == SpecialEventType.STARDUST){
            int exposedConnectors; //variable to store the number of exposed connectors for each player
            for(String nickname : nicknames){ // for each player in the game
                exposedConnectors = gameState.countExposedConnectors(nickname); //count the number of exposed connectors on the player's ship
                gameState.changePlayerPosition(nickname, -exposedConnectors); //move the player back by the number of exposed connectors

            }
        }
        if(specialEventType == SpecialEventType.EPIDEMIC){ // if the special event is of the type EPIDEMIC
            for(String nickname : nicknames){ // for each player in the game
                gameState.epidemicEffect(nickname); //apply the epidemic effect to the player's ship
            }
        }
        gameState.setGameState(State.CARD_PICKING); //set the game state to CARD_PICKING after the special effect has been applied
    }
}
