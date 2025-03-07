package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;

import java.util.ArrayList;
import java.util.List;

public class CargoHold extends ConfigurableComponent {
    private List<Color> goods;
    private int numberGoods;

    public CargoHold(boolean isDouble) {
        super(isDouble);
        goods = new ArrayList<>();
        numberGoods = 0;
    }
    public List<Color> getGoods() {
        return goods;
    }
    public int getNumberGoods() {
        return numberGoods;
    }
    public void addGood(Color good) {
        goods.add(good);
        numberGoods++;
    }
}
