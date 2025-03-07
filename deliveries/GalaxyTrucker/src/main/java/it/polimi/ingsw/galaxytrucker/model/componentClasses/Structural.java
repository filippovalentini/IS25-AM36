package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.List;

public class Structural extends Component {
    public Structural(String imagePath, List<Connector> sides) {
        super(imagePath, sides);
    }
}
