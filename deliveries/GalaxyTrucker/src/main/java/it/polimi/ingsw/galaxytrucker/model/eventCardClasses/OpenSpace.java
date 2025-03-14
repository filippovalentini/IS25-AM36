package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.ConfigurableComponent;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Engine;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.Player;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.ShipBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//OPEN SPACE
public class OpenSpace extends EventCard {

    public OpenSpace(int imageID) {
        super(imageID);
    }

    @Override
    public void fly(GameState gameState, String nickname, int usedBatteries) throws InvalidActionException{}
}