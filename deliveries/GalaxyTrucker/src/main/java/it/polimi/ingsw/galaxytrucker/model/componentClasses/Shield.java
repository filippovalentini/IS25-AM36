package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.List;

public class Shield extends Component {
    public Shield(String imagePath, List<Connector> sides) {    //constructor
        super(imagePath, sides);
    }
}
