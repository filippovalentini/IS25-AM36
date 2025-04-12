package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.exceptions.FullCargoHoldException;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.exceptions.UnsupportedCargoColorException;

import java.util.ArrayList;
import java.util.List;

public class CargoSpecial extends CargoHold {

    public CargoSpecial(boolean isDouble, int imageID, List<Connector> sides) {
        super(isDouble, imageID, sides);
    }

    @Override
    public void addGood(Color good) throws FullCargoHoldException {     //adds one good to the cargo hold (it can also be red)
        if (!isDouble && this.goods.size() == 1) {
            throw new FullCargoHoldException("The Cargo Hold is full.");
        } else if (isDouble && this.goods.size() == 2) {
            throw new FullCargoHoldException("The Cargo Hold is full.");
        } else {
            goods.add(good);
            //numberGoods++;
        }
    }

    @Override
    public void substituteGood(Color good, int pos){
        if(goods.size()<2 && isDouble || goods.size()<1 && !isDouble){
            addGood(good);
        }else{ //full cargo (it will substitute)
            goods.set(pos, good);
        }
    }

    @Override
    public List<Color> getGoods() { //return a copy of the listed goods
        return new ArrayList<>(this.goods);
    }
}
