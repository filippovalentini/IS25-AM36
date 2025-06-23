package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.exceptions.FullCargoHoldException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.UnsupportedCargoColorException;

import java.util.*;

/**
 * CargoHold class represents a cargo hold component in the game.
 * It can hold goods of different colors, but not red.
 */
public class CargoHold extends ConfigurableComponent {
    protected List<Color> goods;        //list of goods stored in the cargo hold
    /**
     * Constructor for CargoHold class.
     * @param isDouble true if the cargo hold is double, false otherwise
     * @param imageID the image ID of the cargo hold
     * @param sides the connectors of the cargo hold
     */
    public CargoHold(boolean isDouble, int imageID, List<Connector> sides) {       //constructor
        super(isDouble, imageID, sides);
        goods = new ArrayList<>();
    }

    /**
     * Adds a good to the cargo hold.
     * @param good
     * @throws FullCargoHoldException
     * @throws UnsupportedCargoColorException
     */
    @Override
    public void addGood(Color good) throws FullCargoHoldException, UnsupportedCargoColorException {     //adds one good to the cargo hold (it can't be red)
        if(good==Color.RED){
          throw new UnsupportedCargoColorException("Unsupported Cargo type");
        } else if (!isDouble && goods.size()==3) {
            throw new FullCargoHoldException("The Cargo Hold is full");
        } else if (isDouble && goods.size()==2) {
            throw new FullCargoHoldException("The Cargo Hold is full");
        } else {
            goods.add(good);
        }
    }

    /**
     * Substitutes a good in the cargo hold at a specific position.
     * @param good
     * @param pos
     * @throws FullCargoHoldException
     * @throws UnsupportedCargoColorException
     */
    @Override
    public void substituteGood(Color good, int pos) throws FullCargoHoldException, UnsupportedCargoColorException{
        if(good==Color.RED){
            throw new UnsupportedCargoColorException("Can't add a red good in a normal cargo hold");
        } else {
            if(goods.size()<3 && !isDouble || goods.size()<2 && isDouble){ //if the cargo is not full
                addGood(good);
            }else{ //full cargo (it will substitute)
                goods.set(pos, good);
            }
        }
    }

    /**
     * Calculates the total price of the goods in the cargo hold.
     * @return the total price of the goods
     */
    @Override
    public int goodsPrice(){
        int price = 0;
        for(Color good : goods){ //calculate the price of each good
            if(good==Color.RED){
                price+= 4;
            }
            else if(good==Color.YELLOW){
                price+= 3;
            }
            else if(good==Color.GREEN){
                price+= 2;
            }
            else{
                price+= 1;
            }
        }
        return price;
    }

    /**
     * Returns the list of goods in the cargo hold.
     * @return a copy of the list of goods
     */
    @Override
    public List<Color> getGoods() {
        List<Color> copia = new ArrayList<Color>(this.goods);//return a copy of the listed goods
        return copia;
    }
    /**
     * Returns the number of goods in the cargo hold.
     * @return the number of goods
     */
    @Override
    public int getNumberGoods(){
        return goods.size();
    }

    /**
     * Returns the number of goods of a specific color in the cargo hold.
     * @param color
     * @return the number of goods of the specified color
     */
    @Override
    public int getNumberGoods(Color color){
        int numberGoods = 0;
        for(Color good : goods){
            if(good==color){
                numberGoods++;
            }
        }
        return numberGoods;
    }

    /**
     * Removes a specific number of goods of a specific color from the cargo hold.
     * @param color
     * @param numberGoods
     */
    @Override
    public void removeSpecificGoods(Color color, int numberGoods){
        for(int i=0, deleted=0; i<goods.size() && deleted<numberGoods; i++){
            if(goods.get(i).equals(color)){
                goods.remove(i); //the list is shifted
                i--; //decrement due to shifted list
                deleted++;
            }
        }
    }

    /**
     * Clones the cargo hold component.
     * @return a new CargoHold component with the same properties
     */
    @Override
    public Component clone(){//return a copy of the component
        CargoHold retComponent = new CargoHold(isDouble,this.imageID, new ArrayList<>(this.sides)); //create a new component with the same properties
        retComponent.orientation = this.orientation; //copy the orientation
        retComponent.goods = this.goods; //copy the goods
        return retComponent;
    }

    /**
     * Checks if the cargo hold is full of goods.
     * @return true if the cargo hold is full, false otherwise
     */
    @Override
    public boolean isFullOfGoods() {
        if(isDouble){
            return goods.size()==2;
        }
        else {
            return goods.size()==3;
        }
    }

}
