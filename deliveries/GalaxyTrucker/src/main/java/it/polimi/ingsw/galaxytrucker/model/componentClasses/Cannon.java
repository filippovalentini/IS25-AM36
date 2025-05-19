package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

import java.util.ArrayList;
import java.util.List;

public class Cannon extends ConfigurableComponent { //constructor
    public Cannon(boolean isDouble, int imageID, List<Connector> sides) {
        super(isDouble, imageID, sides);
    }

    @Override
    public boolean hasDoubleCannons() { return isDouble();}
    public List<Connector> getSides() { return sides; } //getter for sides
    @Override
    public boolean hasSingleCannon() { return !isDouble();}
    @Override
    public boolean pointsForward(){
        return this.orientation == Orientation.NORTH;
    }
    @Override
    public Component clone(){ //return a copy of the component
        Cannon retComponent = new Cannon(isDouble,this.imageID, new ArrayList<>(this.sides));
        retComponent.orientation = this.orientation;
        return retComponent;
    }
}
