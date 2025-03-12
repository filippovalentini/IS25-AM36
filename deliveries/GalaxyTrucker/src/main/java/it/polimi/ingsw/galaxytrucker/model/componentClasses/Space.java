package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.List;

public class Space extends Component {
    public Space(String imagePath, List<Connector> sides) {        //constructor
        super(imagePath, sides);
    }
}
