package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

import java.util.ArrayList;
import java.util.List;

public class Shield extends Component {
    public Shield(int imageID, List<Connector> sides) {    //constructor
        super(imageID, sides);
    }

    @Override
    public boolean protects(Orientation o) {
        if(orientation == o){
            return true;
        }
        if(orientation == Orientation.NORTH && o == Orientation.EAST){
            return true;
        }
        if(orientation == Orientation.EAST && o == Orientation.SOUTH){
            return true;
        }
        if(orientation == Orientation.SOUTH && o == Orientation.WEST){
            return true;
        }
        if(orientation == Orientation.WEST && o == Orientation.NORTH){
            return true;
        }
        return false;
    }
    @Override
    public Component clone(){//return a copy of the component
        Shield retComponent = new Shield(this.imageID, new ArrayList<>(this.sides));
        retComponent.orientation = this.orientation;
        return retComponent;
    }
}
