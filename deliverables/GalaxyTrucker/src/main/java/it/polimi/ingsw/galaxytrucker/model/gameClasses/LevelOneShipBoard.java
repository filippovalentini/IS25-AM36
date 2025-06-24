package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.*;
import it.polimi.ingsw.galaxytrucker.model.enumerations.*;

import java.util.*;

/**
 * This class represents the ship board for level one of the game.
 */
public class LevelOneShipBoard extends ShipBoard {
    /**
     * Constructor for LevelOneShipBoard.
     * @param nickname
     * @param color
     */
    public LevelOneShipBoard(String nickname, Color color) {
        super(nickname, color);
        imageID = 1;

        //adds "Space" components in the positions of the ship board where normal components cannot be assembled
        assembledComponents.getFirst().set(0, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.getFirst().set(1, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.getFirst().set(2, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.getFirst().set(4, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.getFirst().set(5, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.getFirst().set(6, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(1).set(0, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(1).set(1, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(1).set(5, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(1).set(6, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(2).set(0, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(2).set(6, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(3).set(0, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(3).set(6, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(4).set(0, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(4).set(3, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(4).set(6, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));


    }
}
