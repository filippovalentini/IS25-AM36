package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Space;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.*;

public class LevelOneShipBoard extends ShipBoard {

    public LevelOneShipBoard(Color color) {
        super(color);
        imageID = 1;
        for (int i = 0; i < 5; i++) {       //at the beginning of the assembling phase, all assembled components are set to null
            List<Component> row = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                row.add(null);
            }
            assembledComponents.add(row);
        }
        //adds "Space" components in the positions of the ship board where normal components cannot be assembled
        assembledComponents.get(0).set(0, new Space("no", new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(0).set(1, new Space("no", new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(0).set(3, new Space("no", new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(0).set(4, new Space("no", new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(1).set(0, new Space("no", new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(1).set(4, new Space("no", new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(4).set(2, new Space("no", new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
    }
}
