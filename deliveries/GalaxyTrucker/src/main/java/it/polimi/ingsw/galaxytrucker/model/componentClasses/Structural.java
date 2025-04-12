package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.ArrayList;
import java.util.List;

public class Structural extends Component {
    public Structural(int imageID, List<Connector> sides) {        //constructor
        super(imageID, sides);
    }

    @Override
    public Component clone() {//return a copy of the component
        Structural retComponent = new Structural(this.imageID, new ArrayList<>(this.sides));
        retComponent.orientation = this.orientation;
        return retComponent;
    }
}