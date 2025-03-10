package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;

import java.util.*;
//this class is used to describe all information associated to the shipboard of a player
public class ShipBoard {
    private int lostComponents;     //number of misplaced tiles and of components destroyed during the game
    private List<List<Component>> assembledComponents;      //components assembled on the ship board
    private List<Component> reservedComponents;     //components reserved during the assembling phase
    private Component pickedComponent;      //component which has been picked by a player and brought to its ship board
    private final Color color;      //color associated to the ship board (and to the player that owns it)

    public ShipBoard(Color color) {     //constructor
        this.color = color;
        this.lostComponents = 0;
        this.assembledComponents = new ArrayList<>();
        for (int i = 0; i < 5; i++) {       //at the beginning of the assembling phase, all assembled components are set to null
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

    //invoked when the owner of the ship board picks a component from the table
    public void pickComponent(Component component) throws PickedComponentException {
        if(pickedComponent!=null){
            throw new PickedComponentException("Already one component");
        }
        else {
            this.pickedComponent = component;
        }
    }
    //the picked component is released, therefore returned so that it can be shown to the other players
    public Component releaseComponent(){
        if(pickedComponent==null){
            throw new PickedComponentException("No picked component");
        }
        else {
            Component c = pickedComponent;
            pickedComponent = null;
            return c;
        }
    }
    //the picked component is added to the reserved components for the ship board
    public void reserveComponent() throws PickedComponentException, ReservedComponentException {
        if(pickedComponent==null){
            throw new PickedComponentException("No picked component");
        }
        else {
            if(reservedComponents.size() == 2){
                throw new ReservedComponentException("Too many reserved components");
            }
            else {
                reservedComponents.add(pickedComponent);
                pickedComponent = null;
            }
        }
    }
    //invoked when a player picks a specific component among the ones reserved for its ship board
    public void pickReservedComponent(int position) throws ReservedComponentException {
        if(position < 0 || position >= reservedComponents.size()){
            throw new ReservedComponentException("Invalid reserved component position");
        }
        else {
            pickedComponent = reservedComponents.get(position);
            reservedComponents.remove(position);
        }
    }
    //assembles the component picked by a player in the specified cell (x,y) of its ship board
    public void assembleComponent(int x, int y) throws AssembledComponentException {
        if(assembledComponents.get(x).get(y) != null){
            throw new AssembledComponentException("Already assembled component");
        }
        assembledComponents.get(x).set(y, pickedComponent);
        pickedComponent = null;
    }
    //removes an assembled component from the ship board
    public void destroyComponent(int x, int y){
        assembledComponents.get(x).set(y, null);
        lostComponents++;
    }
}
