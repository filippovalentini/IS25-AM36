package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.*;
import it.polimi.ingsw.galaxytrucker.model.enumerations.*;

import java.util.*;

/**
 * This class represents a level two ship board, which is a specific type of ship board
 */
public class LevelTwoShipBoard extends ShipBoard{
    /**
     * Constructor for a level two ship board.
     * @param nickname
     * @param color
     */
    public LevelTwoShipBoard(String nickname, Color color) {
        super(nickname, color);
        imageID = 2;

        //adds "Space" components in the positions of the ship board where normal components cannot be assembled
        assembledComponents.getFirst().set(0, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.getFirst().set(1, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.getFirst().set(3, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.getFirst().set(5, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.getFirst().set(6, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(1).set(0, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(1).set(6, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(4).set(3, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));


    }
}
