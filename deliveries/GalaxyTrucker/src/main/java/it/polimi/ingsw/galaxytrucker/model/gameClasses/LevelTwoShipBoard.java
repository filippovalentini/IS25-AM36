package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.*;
import it.polimi.ingsw.galaxytrucker.model.enumerations.*;

import java.util.*;

public class LevelTwoShipBoard extends ShipBoard{
    public LevelTwoShipBoard(Color color) {
        super(color);
        imageID = 2;
        for (int i = 0; i < 5; i++) {       //at the beginning of the assembling phase, all assembled components are set to null
            List<Component> row = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                row.add(new Empty(0, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
            }
            assembledComponents.add(row);
        }
        //adds "Space" components in the positions of the ship board where normal components cannot be assembled
        assembledComponents.getFirst().set(0, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.getFirst().set(1, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.getFirst().set(3, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.getFirst().set(5, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.getFirst().set(6, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(1).set(0, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(1).set(6, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        assembledComponents.get(4).set(3, new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        if(color == Color.BLUE){
            assembledComponents.get(2).set(3, new Cabin(318, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))));
        }
        else if(color == Color.GREEN){
            assembledComponents.get(2).set(3, new Cabin(319, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))));
        }
        else if(color == Color.RED){
            assembledComponents.get(2).set(3, new Cabin(320, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))));
        }
        else{
            assembledComponents.get(2).set(3, new Cabin(321, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))));

        }

    }
}
