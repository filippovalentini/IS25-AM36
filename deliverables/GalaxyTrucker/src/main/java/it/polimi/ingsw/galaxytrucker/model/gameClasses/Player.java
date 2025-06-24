package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.exceptions.AssembledComponentException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.CannonShot;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.Meteor;
import it.polimi.ingsw.galaxytrucker.network.VirtualView;

import java.util.List;

/**
 * This class is used to describe all the main information associated to a player
 */
public class Player {
    private final String nickname;      //unique nickname
    private int credits;        //cosmic credits of the player
    private boolean hasAbandoned;       //true if the player has abandoned the game
    private ShipBoard shipBoard;        //ship board of the player
    private int pickedDeckNumber;         //number of deck picked by the player (0 if no deck has been picked)

    /**
     * Constructor for the Player class.
     * @param nickname
     * @param color
     * @param levelOne
     */
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

    /**
     * Assembles a component on the player's ship board at the specified coordinates.
     * @param component
     * @param x
     * @param y
     */
    public void assembleComponent(Component component, int x, int y){
        shipBoard.assembleComponent(component, x, y);
    }

    /**
     * Returns the nickname of the player.
     * @return nickname
     */
    public String getNickname() { //return a copy of the nickname
        return nickname;
    }

    /**
     * Returns the number of credits of the player.
     * @return credits
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Returns true if the player has abandoned the game, false otherwise.
     * @return true if the player has abandoned, false otherwise
     */
    public boolean hasAbandoned() {
        return hasAbandoned;
    }

    /**
     * Returns the ship board of the player.
     * @return shipBoard
     */
    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    /**
     * Returns the number of lost components in the player's ship board.
     * @return lost components count
     */
    public int getLostComponents(){ return shipBoard.getLostComponents(); }

    /**
     * Returns the color of the player's ship board.
     * @return color of the ship board
     */
    public Color getColor(){
        return shipBoard.getColor();
    }

    /**
     * Returns the number of reserved components in the player's ship board.
     * @return number of reserved components
     */
    public int getNumberReservedComponents() {
        return shipBoard.getNumberReservedComponents();
    }

    /**
     * Adds a listener to the map of listeners of the player's ship board
     * @param nickname
     * @param client
     */
    public void addListener(String nickname, VirtualView client) {
        shipBoard.addListener(nickname, client);
    }

    /**
     * Method  invoked when the player of the ship board picks a component from the table
     * @param component
     * @throws PickedComponentException
     */
    public void pickComponent(Component component) throws PickedComponentException{
        shipBoard.pickComponent(component);
    }

    /**
     * Methods invoked when the player wants to reserve the component that it has picked for its ship board
     * @return the reserved component
     * @throws PickedComponentException
     */
    public Component reserveComponent() throws PickedComponentException{
        return shipBoard.reserveComponent();
    }

    /**
     * Method invoked when the player wants to pick one of the components that it has reserved for its ship board
     * @param position
     * @return the picked reserved component
     * @throws ReservedComponentException
     * @throws PickedComponentException
     */
    public Component pickReservedComponent(int position) throws ReservedComponentException, PickedComponentException{
        return shipBoard.pickReservedComponent(position);
    }

    /**
     * Method invoked when the player wants to release (therefore, place face up) the component that it has picked
     * @return the released component
     * @throws PickedComponentException
     */
    public Component releaseComponent() throws PickedComponentException{
        return shipBoard.releaseComponent();
    }

    /**
     * Method invoked when the player wants to assemble on the ship board the component that it has picked
     * @param x
     * @param y
     * @return
     * @throws AssembledComponentException
     * @throws PickedComponentException
     */
    public Component assembleComponent(int x, int y) throws AssembledComponentException, PickedComponentException{
        return shipBoard.assembleComponent(x, y);
    }

    /**
     * Method invoked when the player wants to change the orientation of the component that it has picked
     * @throws PickedComponentException
     */
    public void rotatePickedComponent() throws PickedComponentException{
        shipBoard.rotatePickedComponent();
    }

    /**
     * Method invoked when a component of the player's ship board must be destroyed
     * @param x
     * @param y
     * @throws AssembledComponentException
     */
    public void destroyComponent(int x, int y) throws AssembledComponentException{
        shipBoard.destroyComponent(x, y);
    }

    /**
     * Method invoked when the player wants to initialize a cabin of its shipboard with 2 human crew members
     * @param x
     * @param y
     * @throws AssembledComponentException
     * @throws FullCabinException
     */
    public void addCrew(int x, int y) throws AssembledComponentException, FullCabinException{
        shipBoard.addCrew(x,y);
    }

    /**
     * Method invoked when the player wants to initialize a cabin of its shipboard with an alien
     * @param isPurple
     * @param x
     * @param y
     * @throws AssembledComponentException
     * @throws FullCabinException
     */
    public void addAlien(boolean isPurple, int x, int y) throws AssembledComponentException, FullCabinException{
        shipBoard.addAlien(isPurple, x,y);
    }

    /**
     * Method invoked when the player wants to initialize a battery container with batteries
     * @param x
     * @param y
     * @return
     * @throws AssembledComponentException
     * @throws NoBatteriesException
     */
    public int addBatteries(int x, int y) throws AssembledComponentException, NoBatteriesException{
        return shipBoard.addBatteries(x,y);
    }

    /**
     * Method invoked when a player wants to pick a deck during the assembling phase
     * @param deckNumber
     * @throws PickedDeckException
     */
    public void pickDeck(int deckNumber) throws PickedDeckException{
        if(pickedDeckNumber != 0){
            throw new PickedDeckException("Can't pick another deck");
        }
        pickedDeckNumber = deckNumber;
    }

    /**
     * Method invoked when a player wants to release the deck it has picked
     * @return
     * @throws PickedDeckException
     */
    public int releaseDeck() throws PickedDeckException {
        if(pickedDeckNumber == 0){
            throw new PickedDeckException("No picked deck");
        }
        int releasedDeckNumber = pickedDeckNumber;
        pickedDeckNumber = 0;
        return releasedDeckNumber;
    }

    /**
     * Method invoked when the player wants to lose the reserved components
     */
    public void loseReservedComponents(){
        shipBoard.loseReservedComponents();
    }

    /**
     * Method that determines whether the player's ship board is correctly assembled
     * @return true if the ship board is correctly assembled, false otherwise
     */
    public boolean hasCorrectShipBoard(){
        return shipBoard.isCorrect();
    }

    /**
     * Methods that determines whether the player's shipBoard has all the cabins and battery containers full
     * @return true if all cabins and battery containers are full, false otherwise
     */
    public boolean hasAllCabinsBatteriesFull(){
        return shipBoard.hasAllCabinsBatteriesFull();
    }

    /**
     * Method that counts the number of exposed connectors in the player's ship board
     * @return the number of exposed connectors
     */
    public int countExposedConnectors(){
        return shipBoard.countExposedConnectors();
    }

    /**
     * Method that removes a member (human or alien) from each cabin (of the player's ship board) that is directly connected with another busy cabin
     */
    public void epidemicEffect(){
        shipBoard.epidemicEffect();
    }

    /**
     * Method invoked when the player has/wants to remove crew members from its ship board
     * @param x
     * @param y
     * @param crewInEachCabin
     * @param numberCrewToRemove
     */
    public void removeCrewMembers(List<Integer> x, List<Integer> y, List<Integer> crewInEachCabin, int numberCrewToRemove) {
        shipBoard.removeCrewMembers(x, y, crewInEachCabin, numberCrewToRemove);
    }

    /**
     * Method invoked when a meteor/cannon shot hits a player's ship board
     * @param meteor
     * @param direction
     * @param activateShield
     * @param activateCannon
     */
    public void meteorAttack(Meteor meteor, int direction, boolean activateShield, boolean activateCannon) {
        shipBoard.meteorAttack(meteor, direction, activateShield, activateCannon);
    }

    /**
     * Method invoked when a cannon shot hits a player's ship board
     * @param cannonFire
     * @param direction
     * @param activateShield
     */
    public void cannonFireAttack(CannonShot cannonFire, int direction, boolean activateShield){
        shipBoard.cannonFireAttack(cannonFire, direction, activateShield);
    }

    /**
     * Method that returns the number of crew members in the player's ship board
     * @return the number of crew members
     */
    public int getNumberCrew(){
        return shipBoard.getNumberCrew();
    }

    /**
     * Method that returns the number of batteries in the player's shipboard
     * @return the number of batteries
     */
    public int getNumberBatteries() {
        return shipBoard.getNumberBatteries();
    }

    /**
     * Method that returns the number of goods on the player's ship board
     * @return the number of goods
     */
    public int getNumberGoods() {
        return shipBoard.getNumberGoods();
    }

    /**
     * Method that removes the numberGoods-most precious goods from the player's ship board
     * @param numberGoods
     *
     */
    public void losePreciousGoods(int numberGoods){
        shipBoard.losePreciousGoods(numberGoods);
    }

    /**
     * Method that returns the cannon strength of a player, removing the given batteries from its ship board in order to activate double cannons
     * @param usedBatteries
     * @return the cannon strength
     */
    public double getCannonStrength(int usedBatteries){
        return shipBoard.getCannonStrength(usedBatteries);
    }

    /**
     * Method that returns the engine strength of a player, removing the given batteries from its ship board in order to activate double engines
     * @param usedBatteries
     * @return the engine strength
     */
    public int getEngineStrength(int usedBatteries){
        return shipBoard.getEngineStrength(usedBatteries);
    }

    /**
     * Method that returns the overall price for all the goods carried by the player's ship
     * @return the overall price of the goods
     */
    public int getGoodsPrice(){
        return shipBoard.getGoodsPrice();
    }

    /**
     * Method invoked when the player has to leave the game
     */
    public void quitGame() {    //invoked when a player must leave the game
        this.hasAbandoned = true;
    }

    /**
     * Method that updates the cosmic credits of the player
     * @param update
     */
    public void updateCredits(int update) {     //updates teh cosmic credits pf a player
        this.credits += update;
    }

    /**
     * Method that substitutes (in the player's ship) the cargo good at the given coordinates with the good given in input
     * @param cargo_row
     * @param cargo_col
     * @param good
     * @param posInCargo
     * @throws FullCargoHoldException
     * @throws UnsupportedCargoColorException
     */
    public void substituteGoods(int cargo_row, int cargo_col, Color good, int posInCargo) throws FullCargoHoldException, UnsupportedCargoColorException {
        this.shipBoard.substituteGoods(cargo_row, cargo_col, good, posInCargo);
    }

    /**
     * Method that adds a set of goods in specific cargo holds of the player's ship board
     * @param x
     * @param y
     * @param goods
     * @throws UnsupportedCargoColorException
     * @throws FullCargoHoldException
     */
    public void loadGoods(List<Integer> x, List<Integer> y, List<Color> goods) throws UnsupportedCargoColorException, FullCargoHoldException{
        shipBoard.loadGoods(x, y, goods);
    }

}
