package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.exceptions.FullCargoHoldException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.UnsupportedCargoColorException;

import java.util.*;

import static it.polimi.ingsw.galaxytrucker.model.enumerations.Color.RED;

public class CargoHold extends ConfigurableComponent {
    protected List<Color> goods;        //list of goods stored in the cargo hold
    protected int numberGoods;      //number of goods stored in the cargo hold

    public CargoHold(boolean isDouble, int imageID, List<Connector> sides) {       //constructor
        super(isDouble, imageID, sides);
        goods = new ArrayList<>();
        numberGoods = 0;
    }
    public List<Color> getGoods() { //return a copy of the listed goods
        return new ArrayList<>(this.goods);
    }
    public int getNumberGoods() {
        return numberGoods;
    }

    public void addGood(Color good) throws FullCargoHoldException, UnsupportedCargoColorException {     //adds one good to the cargo hold (it can't be red)
        if(good==Color.RED){
          throw new UnsupportedCargoColorException("Unsupported Cargo type");
        } else if (!isDouble && numberGoods==3) {
            throw new FullCargoHoldException("The Cargo Hold is full");
        } else if (isDouble && numberGoods==2) {
            throw new FullCargoHoldException("The Cargo Hold is full");
        } else {
            goods.add(good);
            numberGoods++;
        }
    }
}
