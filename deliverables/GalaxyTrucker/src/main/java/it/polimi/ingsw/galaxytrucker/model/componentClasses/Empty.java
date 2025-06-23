package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.List;

//this class doesn't represent a real component, but is used to represent free positions on a ship board
public class Empty extends Component {
    public Empty(int imageID, List<Connector> sides) {
        super(imageID, sides);
    }
}
