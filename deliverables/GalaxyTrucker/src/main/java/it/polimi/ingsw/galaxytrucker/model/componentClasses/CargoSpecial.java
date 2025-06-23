package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.exceptions.FullCargoHoldException;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.exceptions.UnsupportedCargoColorException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a special cargo hold.
 */
public class CargoSpecial extends CargoHold {
    /**
     * Constructor for a special cargo hold.
     * @param isDouble
     * @param imageID
     * @param sides
     */
    public CargoSpecial(boolean isDouble, int imageID, List<Connector> sides) {
        super(isDouble, imageID, sides);
    }

    /**
     * Adds a good to the cargo hold.
     * @param good
     * @throws FullCargoHoldException
     */
    @Override
    public void addGood(Color good) throws FullCargoHoldException {     //adds one good to the cargo hold (it can also be red)
        if (!isDouble && this.goods.size() == 1) {
            throw new FullCargoHoldException("The Cargo Hold is full."); //if the cargo hold is single and already has a good, it cannot add another one
        } else if (isDouble && this.goods.size() == 2) {
            throw new FullCargoHoldException("The Cargo Hold is full."); //if the cargo hold is double and already has two goods, it cannot add another one
        } else {
            goods.add(good); //add the good to the cargo hold
            //numberGoods++;
        }
    }

    /**
     * Substitutes a good in the cargo hold at the specified position.
     * @param good
     * @param pos
     * @throws FullCargoHoldException
     */
    @Override
    public void substituteGood(Color good, int pos) throws FullCargoHoldException{
        if(goods.size()<2 && isDouble || goods.size()<1 && !isDouble){ //if the cargo hold is not full, it can add the good
            addGood(good);
        }else{ //full cargo (it will substitute)
            goods.set(pos, good);
        }
    }

    /**
     * Returns a copy of the goods in the cargo hold.
     * @return a list of goods
     */
    @Override
    public List<Color> getGoods() { //return a copy of the listed goods
        return new ArrayList<>(this.goods);
    }

    /**
     * Checks if the cargo hold is full of goods.
     * @return true if the cargo hold is full of goods, false otherwise
     */
    @Override
    public boolean isFullOfGoods() {
        if(isDouble){
            return goods.size()==2;
        }
        else {
            return goods.size()==1;
        }
    }
}
