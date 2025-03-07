package it.polimi.ingsw.galaxytrucker.model.game;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;

import java.util.*;

public class ShipBoard {
    private int lostComponents;
    private List<List<Component>> assembledComponents;
    private List<Component> reservedComponents;
    private Component pickedComponent;
    private final Color color;

    public ShipBoard(Color color) {
        this.color = color;
        this.lostComponents = 0;
        this.assembledComponents = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<Component> row = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                row.add(null);
            }
            assembledComponents.add(row);
        }
        this.reservedComponents = new ArrayList<>();
        this.pickedComponent = null;
    }
    public List<List<Component>> getAssembledComponents() {
        return assembledComponents;
    }
    public Component getAssembledComponent(int x, int y) {
        return assembledComponents.get(x).get(y);
    }
    public List<Component> getReservedComponents() {
        return reservedComponents;
    }
    public Component getPickedComponent() {
        return pickedComponent;
    }
    public Color getColor() {
        return color;
    }
    public int getLostComponents() {
        return lostComponents;
    }
    public void pickComponent(Component component){
        this.pickedComponent = component;
    }
    public Component releaseComponent(){
        Component c = pickedComponent;
        pickedComponent = null;
        return c;
    }
    public void pickReservedComponent(int position){
        pickedComponent = reservedComponents.get(position);
        reservedComponents.remove(position);
    }
    public void assembleComponent(int x, int y){
        assembledComponents.get(x).set(y, pickedComponent);
        pickedComponent = null;
    }
    public void destroyComponent(int x, int y){
        assembledComponents.get(x).set(y, null);
        lostComponents++;
    }
}
