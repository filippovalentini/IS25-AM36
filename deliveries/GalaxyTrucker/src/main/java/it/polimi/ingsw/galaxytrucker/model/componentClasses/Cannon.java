package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.List;

public class Cannon extends ConfigurableComponent {
    public Cannon(boolean isDouble, String imagePath, List<Connector> sides) {
        super(isDouble, imagePath, sides);
    }
}
