package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.exceptions.FullCargoHoldException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.UnsupportedCargoColorException;

import java.util.*;

public class CargoHold extends ConfigurableComponent {
    protected List<Color> goods;        //list of goods stored in the cargo hold

    public CargoHold(boolean isDouble, int imageID, List<Connector> sides) {       //constructor
        super(isDouble, imageID, sides);
        goods = new ArrayList<>();
    }

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

    public void substituteGood(Color good, int pos){
        if(good==Color.RED){
            throw new UnsupportedCargoColorException("Unsupported Cargo type");
        } else {
            if(goods.size()<3 && !isDouble || goods.size()<2 && isDouble){
                addGood(good);
            }else{ //full cargo (it will substitute)
                goods.set(pos, good);
            }
        }
    }

    @Override
    public int goodsPrice(){
        int price = 0;
        for(Color good : goods){
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
    @Override
    public List<Color> getGoods() {
        List<Color> copia = new ArrayList<Color>(this.goods);//return a copy of the listed goods
        return copia;
    }
    @Override
    public int getNumberGoods(){
        return goods.size();
    }
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
    @Override
    public void removeSpecificGoods(Color color, int numberGoods){
        for(int i=0, deleted=0; i<goods.size() && deleted<numberGoods; i++){
            if(goods.get(i).equals(color)){
                goods.remove(i); //the list is shifted
                i--; //decrement due to shifted list
            }
        }
    }

    @Override
    public Component clone(){//return a copy of the component
        CargoHold retComponent = new CargoHold(isDouble,this.imageID, new ArrayList<>(this.sides));
        retComponent.orientation = this.orientation;
        retComponent.goods = this.goods;
        return retComponent;
    }
}
