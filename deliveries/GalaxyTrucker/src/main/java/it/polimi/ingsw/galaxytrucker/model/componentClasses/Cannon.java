package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

import java.util.List;

public class Cannon extends ConfigurableComponent { //constructor
    public Cannon(boolean isDouble, int imageID, List<Connector> sides) {
        super(isDouble, imageID, sides);
    }

    @Override
    public boolean hasDoubleCannons() { return isDouble();}
    @Override
    public boolean hasSingleCannon() { return !isDouble();}
    @Override
    public boolean pointsForward(){
        return this.orientation == Orientation.NORTH;
    }
}
