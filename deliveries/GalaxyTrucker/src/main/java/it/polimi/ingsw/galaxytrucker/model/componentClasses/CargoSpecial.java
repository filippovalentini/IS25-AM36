package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.List;

public class CargoSpecial extends CargoHold{
    public CargoSpecial(boolean isDouble, String imagePath, List<Connector> sides) {
        super(isDouble, imagePath, sides);
    }
}
