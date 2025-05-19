package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.exceptions.AssembledComponentException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.CannonShot;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.Meteor;
import it.polimi.ingsw.galaxytrucker.network.VirtualView;

import java.util.List;

//this class is used to describe all the main information associated to a player
public class Player {
    private final String nickname;      //unique nickname
    private int credits;        //cosmic credits of the player
    private boolean hasAbandoned;       //true if the player has abandoned the game
    private ShipBoard shipBoard;        //ship board of the player
    private int pickedDeckNumber;         //number of deck picked by the player (0 if no deck has been picked)

    public Player(String nickname, Color color, boolean levelOne) {       //constructor
        this.nickname = nickname;
        this.credits = 0;
        this.pickedDeckNumber = 0;
        this.hasAbandoned = false;
        if(levelOne){
            shipBoard = new LevelOneShipBoard(nickname, color);
        }
        else{
            shipBoard = new LevelTwoShipBoard(nickname, color);
        }
    }
    //METODO DA TOGLIERE!!!!!!!!!!
    public void assembleComponent(Component component, int x, int y){
        shipBoard.assembleComponent(component, x, y);
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
    public int getLostComponents(){ return shipBoard.getLostComponents(); }
    public Color getColor(){
        return shipBoard.getColor();
    }

    //adds a listener to the map of listeners of the player's ship board
    public void addListener(String nickname, VirtualView client) {
        shipBoard.addListener(nickname, client);
    }
    //invoked when the player of the ship board picks a component from the table
    public void pickComponent(Component component) throws PickedComponentException{
        shipBoard.pickComponent(component);
    }
    //invoked when the player wants to reserve the component that it has picked for its ship board
    public Component reserveComponent() throws PickedComponentException{
        return shipBoard.reserveComponent();
    }
    //invoked when the player wants to pick one of the components that it has reserved for its ship board
    public Component pickReservedComponent(int position) throws ReservedComponentException, PickedComponentException{
        return shipBoard.pickReservedComponent(position);
    }
    //invoked when the player wants to release (therefore, place face up) the component that it has picked
    public Component releaseComponent() throws PickedComponentException{
        return shipBoard.releaseComponent();
    }
    //invoked when the player wants to assemble on the ship board the component that it has picked
    public Component assembleComponent(int x, int y) throws AssembledComponentException, PickedComponentException{
        return shipBoard.assembleComponent(x, y);
    }
    //invoked when the player wants to change the orientation of the component that it has picked
    public void rotatePickedComponent() throws PickedComponentException{
        shipBoard.rotatePickedComponent();
    }
    //invoked when a component of the player's ship board must be destroyed
    public void destroyComponent(int x, int y) throws AssembledComponentException{
        shipBoard.destroyComponent(x, y);
    }
    //invoked when the player wants to initialize a cabin of its shipboard with 2 human crew members
    public void addCrew(int x, int y) throws AssembledComponentException, FullCabinException{
        shipBoard.addCrew(x,y);
    }
    //invoked when the player wants to initialize a cabin of its shipboard with an alien
    public void addAlien(boolean isPurple, int x, int y) throws AssembledComponentException, FullCabinException{
        shipBoard.addAlien(isPurple, x,y);
    }
    //invoked when the player wants to initialize a battery container with batteries
    public int addBatteries(int x, int y) throws AssembledComponentException, NoBatteriesException{
        return shipBoard.addBatteries(x,y);
    }
    //invoked when a player wants to pick a deck during the assembling phase
    public void pickDeck(int deckNumber) throws PickedDeckException{
        if(pickedDeckNumber != 0){
            throw new PickedDeckException("Can't pick another deck");
        }
        pickedDeckNumber = deckNumber;
    }
    //invoked when a player wants to release the deck it has picked
    public int releaseDeck() throws PickedDeckException {
        if(pickedDeckNumber == 0){
            throw new PickedDeckException("No picked deck");
        }
        int releasedDeckNumber = pickedDeckNumber;
        pickedDeckNumber = 0;
        return releasedDeckNumber;
    }
    //increases the number of lost components for the number of unused reserved components
    public void loseReservedComponents(){
        shipBoard.loseReservedComponents();
    }
    //determines whether the player's ship board is correctly assembled
    public boolean hasCorrectShipBoard(){
        return shipBoard.isCorrect();
    }
    //determines whether the player's shipBoard has all the cabins and battery containers full
    public boolean hasAllCabinsBatteriesFull(){return shipBoard.hasAllCabinsBatteriesFull();}
    //counts the number of exposed connectors in the player's ship board
    public int countExposedConnectors(){
        return shipBoard.countExposedConnectors();
    }
    //removes a member (human or alien) from each cabin (of the player's ship board) that is directly
    //connected with another busy cabin
    public void epidemicEffect(){
        shipBoard.epidemicEffect();
    }
    //this method is invoked when the player has/wants to remove crew members from its ship board
    public void removeCrewMembers(List<Integer> x, List<Integer> y, List<Integer> crewInEachCabin, int numberCrewToRemove) {
        shipBoard.removeCrewMembers(x, y, crewInEachCabin, numberCrewToRemove);
    }
    //invoked when a meteor/cannon shot hits a player's ship board
    public void meteorAttack(Meteor meteor, int direction, boolean activateShield, boolean activateCannon) {
        shipBoard.meteorAttack(meteor, direction, activateShield, activateCannon);
    }
    //invoked when a cannon shot hits a player's ship board
    public void cannonFireAttack(CannonShot cannonFire, int direction, boolean activateShield){
        shipBoard.cannonFireAttack(cannonFire, direction, activateShield);
    }
    //returns the number of crew members in the player's ship board
    public int getNumberCrew(){
        return shipBoard.getNumberCrew();
    }
    //returns the number of batteries in the player's shipboard
    public int getNumberBatteries() {
        return shipBoard.getNumberBatteries();
    }
    //returns the number of goods on the player's ship board
    public int getNumberGoods() {
        return shipBoard.getNumberGoods();
    }
    //this method removes the numberGoods-most precious goods from the player's ship board
    public void losePreciousGoods(int numberGoods){
        shipBoard.losePreciousGoods(numberGoods);
    }
    //returns the cannon strength of a player, removing the given batteries from its ship board in order to activate double cannons
    public double getCannonStrength(int usedBatteries){
        return shipBoard.getCannonStrength(usedBatteries);
    }
    //returns the engine strength of a player, removing the given batteries from its ship board in order to activate double engines
    public int getEngineStrength(int usedBatteries){
        return shipBoard.getEngineStrength(usedBatteries);
    }
    //returns the overall price for all the goods carried by the player's ship
    public int getGoodsPrice(){
        return shipBoard.getGoodsPrice();
    }
    //invoked when the player has to leave the game
    public void quitGame() {    //invoked when a player must leave the game
        this.hasAbandoned = true;
    }
    //updates the cosmic credits of the player
    public void updateCredits(int update) {     //updates teh cosmic credits pf a player
        this.credits += update;
    }
    //substitutes (in the player's ship) the cargo good at the given coordinates with the good given in input
    public void substituteGoods(int cargo_row, int cargo_col, Color good, int posInCargo) throws FullCargoHoldException, UnsupportedCargoColorException {
        this.shipBoard.substituteGoods(cargo_row, cargo_col, good, posInCargo);
    }
    //adds a set of goods in specific cargo holds of the player's ship board
    public void loadGoods(List<Integer> x, List<Integer> y, List<Color> goods) throws UnsupportedCargoColorException, FullCargoHoldException{
        shipBoard.loadGoods(x, y, goods);
    }

}
