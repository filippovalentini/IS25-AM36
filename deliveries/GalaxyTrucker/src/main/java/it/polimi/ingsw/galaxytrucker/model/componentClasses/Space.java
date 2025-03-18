package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.List;

//this class doesn't represent a real component, but is used to represent positions on a ship board where other
//components can't be assembled
public class Space extends Component {
    public Space(int imageID, List<Connector> sides) {        //constructor
        super(imageID, sides);
    }
}
