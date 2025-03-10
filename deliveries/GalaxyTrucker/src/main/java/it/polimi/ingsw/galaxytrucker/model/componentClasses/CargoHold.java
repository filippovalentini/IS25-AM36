package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.*;

public class CargoHold extends ConfigurableComponent {
    private List<Color> goods;
    private int numberGoods;

    public CargoHold(boolean isDouble, String imagePath, List<Connector> sides) {
        super(isDouble, imagePath, sides);
        goods = new ArrayList<>();
        numberGoods = 0;
    }
    public List<Color> getGoods() {
        return goods;
    }
    public int getNumberGoods() {
        return numberGoods;
    }
    public void addGood(Color good) throws FullCargoHoldException,UnsupportedCargoColorException{
        if(good==RED){
          throw new UnsupportedCargoColorException("Unsupported Cargo type")
        }
        if (isDouble==false && numberGoods==3) {
            throw new FullCargoHoldException("The Cargo Hold is full")
        } else if (isDouble==true && numberGoods==2) {
            throw new FullCargoHoldException("The Cargo Hold is full")
        } else {
            goods.add(good);
            numberGoods++;
        }
    }
}
