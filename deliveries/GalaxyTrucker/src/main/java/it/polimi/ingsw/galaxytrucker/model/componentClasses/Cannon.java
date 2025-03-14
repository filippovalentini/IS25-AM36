package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.List;

public class Cannon extends ConfigurableComponent { //constructor
    public Cannon(boolean isDouble, int imageID, List<Connector> sides) {
        super(isDouble, imageID, sides);
    }
}
