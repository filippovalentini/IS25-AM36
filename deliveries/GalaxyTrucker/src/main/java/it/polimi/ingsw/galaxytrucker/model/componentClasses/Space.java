package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.List;

public class Space extends Component {
    public Space(int imageID, List<Connector> sides) {        //constructor
        super(imageID, sides);
    }
}
