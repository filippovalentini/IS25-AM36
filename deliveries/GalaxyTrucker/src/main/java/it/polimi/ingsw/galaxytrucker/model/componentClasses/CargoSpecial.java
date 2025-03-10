package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.exceptions.FullCargoHoldException;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;

import java.util.List;

public class CargoSpecial extends CargoHold {

    public CargoSpecial(boolean isDouble, String imagePath, List<Connector> sides) {
        super(isDouble, imagePath, sides);
    }

    @Override
    public void addGood(Color good) throws FullCargoHoldException {     //adds one good to the cargo hold (it can also be red)
        if (!isDouble && numberGoods == 1) {
            throw new FullCargoHoldException("The Cargo Hold is full.");
        } else if (isDouble && numberGoods == 2) {
            throw new FullCargoHoldException("The Cargo Hold is full.");
        } else {
            goods.add(good);
            numberGoods++;
        }
    }
}
