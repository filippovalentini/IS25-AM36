package it.polimi.ingsw.galaxytrucker.ui.view;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

import java.util.ArrayList;
import java.util.List;

public class ViewComponent {
    private String imageID;
    private List<Connector> sides;
    private Orientation orientation;
    private int batteries;
    private int crew;
    private boolean purpleAlien;
    private boolean brownAlien;
    private List<Color> goods;

    public ViewComponent(String imageID) {
        this.imageID = imageID;
        this.orientation = Orientation.NORTH;
        this.batteries = 0;
        this.crew = 0;
        this.purpleAlien = false;
        this.brownAlien = false;
        this.goods = new ArrayList<>();
    }

    public String getImageID() {
        return imageID;
    }
    public Orientation getOrientation() {
        return orientation;
    }
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
    public void rotateLeft(){
        if(orientation == Orientation.NORTH) {
            orientation = Orientation.WEST;
        }
        else if(orientation == Orientation.WEST) {
            orientation = Orientation.SOUTH;
        }
        else if(orientation == Orientation.SOUTH) {
            orientation = Orientation.EAST;
        }
        else {
            orientation = Orientation.NORTH;
        }
    }
    public void updateBatteries(int change){
        batteries += change;
    }
    public int getBatteries() {
        return batteries;
    }
    public void updateCrew(int change){
        crew += change;
    }
    public int getCrew() {
        return crew;
    }
    public void updateAlien(boolean isPurple){
        if(isPurple){
            purpleAlien = !purpleAlien;
        }
        else{
            brownAlien = !brownAlien;
        }
    }
    public boolean isPurpleAlien() {
        return purpleAlien;
    }
    public boolean isBrownAlien() {
        return brownAlien;
    }
    public void loadGood(Color good){
        goods.add(good);
    }
    public void removeGood(Color good){
        for(Color g : goods){
            if(g == good){
                goods.remove(g);
                break;
            }
        }
    }
    public List<Color> getGoods() {
        return new ArrayList<>(goods);
    }
    public int getNumberGoods() {
        return goods.size();
    }

    @Override
    public String toString(){
        return ImageIDToStringConverter.imageIDtoEID(this.imageID);
    }
}
