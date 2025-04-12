package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

import java.util.ArrayList;
import java.util.List;

public class Engine extends ConfigurableComponent {
    public Engine(boolean isDouble, int imageID, List<Connector> sides) {      //constructor
        super(isDouble, imageID, sides);
    }
    @Override
    public boolean isWellOriented(){        //the third side of engine components must be south-oriented (therefore, the side in position zero must be north-oriented)
        return orientation == Orientation.NORTH;
    }
    @Override
    public boolean hasDoubleEngines() {
        return isDouble();
    }
    @Override
    public boolean hasSingleEngine() {
        return !isDouble();
    }
    @Override
    public Component clone(){//return a copy of the component
        Engine retComponent = new Engine(isDouble,this.imageID, new ArrayList<>(this.sides));
        retComponent.orientation = this.orientation;
        return retComponent;
    }
}
