package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.exceptions.AssembledComponentException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.Meteor;

import java.util.List;

//this class is used to describe all the main information associated to a player
public class Player {
    private final String nickname;      //unique nickname
    private int credits;        //cosmic credits of the player
    private boolean hasAbandoned;       //true if the player has abandoned the game
    private ShipBoard shipBoard;        //ship board of the player

    public Player(String nickname, Color color, boolean levelOne) {       //constructor
        this.nickname = nickname;
        this.credits = 0;
        this.hasAbandoned = false;
        if(levelOne){
            shipBoard = new LevelOneShipBoard(color);
        }
        else{
            shipBoard = new LevelTwoShipBoard(color);
        }
    }
    public String getNickname() { //return a copy of the nickname
        return nickname;
    }
    public int getCredits() {
        return credits;
    }
    public boolean hasAbandoned() {
        return hasAbandoned;
    }
    public ShipBoard getShipBoard() {
        return shipBoard;
    }
    public Color getColor(){
        return shipBoard.getColor();
    }
    public void removeShipBoardCrew(List<Integer> x, List<Integer> y, List<Integer> eachCabinCrew, int numberCrewToRemove) {
        shipBoard.removeCrewMembers(x, y, eachCabinCrew, numberCrewToRemove);
    }

    //invoked when the player of the ship board picks a component from the table
    public void pickComponent(Component component) throws PickedComponentException{
        shipBoard.pickComponent(component);
    }
    //invoked when the player wants to reserve the component that it has picked for its ship board
    public void reserveComponent() throws PickedComponentException{
        shipBoard.reserveComponent();
    }
    //invoked when the player wants to pick one of the components that it has reserved for its ship board
    public void pickReservedComponent(int position) throws ReservedComponentException, PickedComponentException{
        shipBoard.pickReservedComponent(position);
    }
    //invoked when the player wants to release (therefore, place face up) the component that it has picked
    public Component releaseComponent() throws PickedComponentException{
        return shipBoard.releaseComponent();
    }
    //invoked when the player wants to assemble on the ship board the component that it has picked
    public void assembleComponent(int x, int y) throws AssembledComponentException, PickedComponentException{
        shipBoard.assembleComponent(x, y);
    }
    //invoked when the player wants to change the orientation of the component that it has picked
    public void rotatePickedComponent() throws PickedComponentException{
        shipBoard.rotatePickedComponent();
    }
    //invoked when a component of the player's ship board must be destroyed
    public void destroyComponent(int x, int y) throws AssembledComponentException{
        shipBoard.destroyComponent(x, y);
    }
    //determines whether the player's ship board is correctly assembled
    public boolean hasCorrectShipBoard(){
        return shipBoard.isCorrect();
    }
    //counts the number of exposed connectors in the player's ship board
    public int countExposedConnectors(){
        return shipBoard.countExposedConnectors();
    }
    //removes a member (human or alien) from each cabin (of the player's ship board) that is directly
    //connected with another busy cabin
    public void epidemicEffect(){
        shipBoard.epidemicEffect();
    }
    //invoked when a meteor/cannon shot hits a player's ship board
    public void meteorAttack(Meteor meteor, int direction, boolean activateShield, boolean activateCannon) {
        shipBoard.meteorAttack(meteor, direction, activateShield, activateCannon);
    }
    //returns the number of crew members in the player's ship board
    public int getNumberCrew(){
        return shipBoard.getNumberCrew();
    }
    //returns the number of batteries in the player's shipboard
    public int getNumberBatteries() {
        return shipBoard.getNumberBatteries();
    }
    //returns the cannon strength of a player, removing the given batteries from its ship board in order to activate double cannons
    public float getCannonStrength(int usedBatteries){
        return shipBoard.getCannonStrength(usedBatteries);
    }
    //returns the engine strength of a player, removing the given batteries from its ship board in order to activate double engines
    public float getEngineStrength(int usedBatteries){
        return shipBoard.getEngineStrength(usedBatteries);
    }

    public void quitGame() {    //invoked when a player must leave the game
        this.hasAbandoned = true;
    }
    public void updateCredits(int update) {     //updates teh cosmic credits pf a player
        this.credits += update;
    }

    public void substituteShipboardCargoGood(int cargo_row, int cargo_col, Color good, int pos){
        this.shipBoard.substituteCargoGoodGivenGood(cargo_row, cargo_col, good, pos);
    }

}
