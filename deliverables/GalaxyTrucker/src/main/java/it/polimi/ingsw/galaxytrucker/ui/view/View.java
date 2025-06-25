package it.polimi.ingsw.galaxytrucker.ui.view;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;

import java.rmi.RemoteException;
import java.util.*;

import static it.polimi.ingsw.galaxytrucker.model.enumerations.Color.convertColorIntoEmoji;

/**
 * View class represents the state of the game and the players' ships.
 */
public class View {
    public boolean firstFlight;     //true if the view refers to a first flight game, false otherwise
    private String gameState;   //string describing the phase of the game
    private ViewFlightBoard flightBoard;    //current flight board state
    private final ViewPlayer player;      //state of the player associated to the view
    private Map<String, ViewPlayer> otherPlayers = new HashMap<>();     //other player's state
    private ViewComponent pickedViewComponent;  //component in the player's hand
    private List<Integer> pickedDeck = new ArrayList<>();   //set of cards in the player's hand
    private List<ViewComponent> shownComponents = new ArrayList<>();    //set of components placed face up
    private Integer currentCard;        //current card to solve
    private ViewDice dice;          //dice of the game
    private String hourglassState;      //state of the hourglass
    private int hourglassPosition;      //position of the hourglass on the flight board
    private boolean hourglassRunning;    //true if the hourglass is running
    private String turnPlayer;      //nickname of the player next in turn
    private String damagedPlayer;   //nickname of the player that has to repair its ship

    /**
     * Constructor for the View class.
     * @param nickname
     * @param color
     * @param firstFlight
     */
    public View(String nickname, Color color, boolean firstFlight) {
        this.player = new ViewPlayer(nickname, color, firstFlight);
        this.gameState = "WAITING FOR OTHER PLAYERS....";
        this.firstFlight = firstFlight;
        this.pickedViewComponent = null;
        this.flightBoard = null;
        this.turnPlayer = null;
        this.damagedPlayer = null;
        this.currentCard = null;
        this.dice = new ViewDice();
        this.hourglassState = null;
        this.hourglassPosition = 0;
        this.hourglassRunning = false;
    }

    //returns a map associating the nickname of each current player with its color

    /**
     * Returns a map associating the nickname of each current player with its color.
     * @return Map<String, Color> - a map of player nicknames and their colors
     */
    public Map<String, Color> getCurrentPlayers() {
        Map<String, Color> players = new HashMap<>();
        players.put(player.getNickname(), player.getColor());
        for (ViewPlayer player : otherPlayers.values()) {
            players.put(player.getNickname(), player.getColor());
        }
        return players;
    }

    //returns the nicknames of the other players in the game

    /**
     * Returns the nicknames of the other players in the game.
     * @return List<String> - a list of nicknames of other players
     */
    public List<String> getOtherPlayerNicknames() {
        return new ArrayList<>(otherPlayers.keySet());
    }

    //returns the nickname of the main player

    /**
     * Returns the nickname of the main player associated with the view.
     * @return String - the nickname of the main player
     */
    public String getNickname() {
        return player.getNickname();
    }

    //returns the color of the main player

    /**
     * Returns the color of the main player associated with the view.
     * @return Color - the color of the main player
     */
    public Color getColor() {
        return player.getColor();
    }

    //returns the component picked by the player

    /**
     * Returns the component currently picked by the player.
     * @return ViewComponent - the component picked by the player, or null if none is picked
     */
    public ViewComponent getPickedViewComponent() {
        return pickedViewComponent;
    }

    //determines if the view is related to a first flight game or not

    /**
     * Determines if the view is related to a first flight game.
     * @return boolean - true if it's a first flight game, false otherwise
     */
    public boolean isFirstFlight() {
        return firstFlight;
    }

    //returns a map that for each player color associates the player's position on the flight board; it's
    //invoked by the gui flight board controller for visualization purposes

    /**
     * Returns a map that associates each player's color with their position on the flight board.
     * @return Map<Color, Integer> - a map of player colors and their positions
     */
    public Map<Color,Integer> getColorCellMap(){
        return flightBoard.getColorCellMap();
    }

    //returns a map that for each player's nickname associates the player's color

    /**
     * Returns a map that associates each player's nickname with their color.
     * @return Map<String, Color> - a map of player nicknames and their colors
     */
    public Map<String, Color> getPlayerColorMap(){
        Map<String, Color> map = new HashMap<>();
        for (ViewPlayer player : otherPlayers.values()) {
            map.put(player.getNickname(), player.getColor());
        }
        map.put(this.player.getNickname(), this.player.getColor());
        return map;
    }

    //returns the current position of the hourglass

    /**
     * Returns the current position of the hourglass on the flight board.
     * @return int - the position of the hourglass
     */
    public int getHourglassPosition() {
        return hourglassPosition;
    }

    //determines is the hourglass is running or not

    /**
     * Determines if the hourglass is currently running.
     * @return boolean - true if the hourglass is running, false otherwise
     */
    public boolean isHourglassRunning() {
        return hourglassRunning;
    }

    //determines if the dice have to be thrown or not

    /**
     * Determines if the dice can be thrown.
     * @return boolean - true if the dice can be thrown, false otherwise
     */
    public boolean throwableDice(){
        return dice.areThrowable();
    }

    //returns the dice result (sum of values)

    /**
     * Returns the result of the dice throw, which is the sum of the two dice values.
     * @return int - the sum of the two dice results
     */
    public int diceResult(){
        return dice.getResult1() + dice.getResult2();
    }

    //returns the result of the first dice

    /**
     * Returns the result of the first dice.
     * @return int - the result of the first dice
     */
    public int dice1result(){
        return dice.getResult1();
    }

    //returns the result of the second dice

    /**
     * Returns the result of the second dice.
     * @return int - the result of the second dice
     */
    public int dice2result(){
        return dice.getResult2();
    }

    //returns a string describing the state of the game

    /**
     * Returns the current state of the game.
     * @return String - a string representing the game state
     */
    public String getGameState() {
        return gameState;
    }

    //returns the list of image IDs of the shown components

    /**
     * Returns the list of image IDs of the components currently shown face up.
     * @return List<Integer> - a list of image IDs of the shown components
     */
    public List<Integer> getShownComponents() {
        List<Integer> shownComponentsImageIDs = new ArrayList<>();
        for (ViewComponent component : shownComponents) {
            shownComponentsImageIDs.add(Integer.parseInt(component.getImageID()));
        }
        return shownComponentsImageIDs;
    }

    //returns the list od assembled components of a player

    /**
     * Returns the list of assembled components for a player.
     * @param nickname
     * @return List<List<ViewComponent>> - a list of lists of ViewComponent objects representing the assembled components
     */
    public List<List<ViewComponent>> getAssembledComponents(String nickname) {
        if(nickname.equals(this.player.getNickname())){
            return this.player.getAssembledComponents();
        }
        else{
            return otherPlayers.get(nickname).getAssembledComponents();
        }
    }

    //returns the list of reserved components of a player

    /**
     * Returns the list of reserved components for a player.
     * @param nickname
     * @return List<ViewComponent> - a list of ViewComponent objects representing the reserved components
     */
    public List<ViewComponent> getReservedComponents(String nickname) {
        if(nickname.equals(this.player.getNickname())){
            return this.player.getReservedComponents();
        }
        else{
            return otherPlayers.get(nickname).getReservedComponents();
        }
    }

    //returns the number of lost components of a player

    /**
     * Returns the number of lost components for a player.
     * @param nickname
     * @return int - the number of lost components
     */
    public int getLostComponents(String nickname) {
        if(nickname.equals(this.player.getNickname())){
            return this.player.getLostComponents();
        }
        else{
            return otherPlayers.get(nickname).getLostComponents();
        }
    }

    //returns the number of credits of a player

    /**
     * Returns the number of credits for a player.
     * @param nickname
     * @return int - the number of credits
     */
    public int getCredits(String nickname) {
        if(nickname.equals(this.player.getNickname())){
            return this.player.getCredits();
        }
        else{
            return otherPlayers.get(nickname).getCredits();
        }
    }

    //returns true if the player has abandoned the game

    /**
     * Returns true if the player has abandoned the game, false otherwise.
     * @param nickname
     * @return boolean - true if the player has abandoned, false otherwise
     */
    public boolean hasAbandoned(String nickname) {
        if(nickname.equals(this.player.getNickname())){
            return this.player.hasAbandoned();
        }
        else{
            return otherPlayers.get(nickname).hasAbandoned();
        }
    }

    //returns the id of the current event card

    /**
     * Returns the ID of the current event card to be solved.
     * @return Integer - the ID of the current card, or null if no card is currently active
     */
    public Integer getCurrentCard() {
        return currentCard;
    }

    //returns the nickname of the player in turn to perform an action

    /**
     * Returns the nickname of the player whose turn it is to perform an action.
     * @return String - the nickname of the player in turn, or null if no player is currently in turn
     */
    public String getTurnPlayer() {
        return turnPlayer;
    }

    //visualizes the state of the game and of the ship board of the player associated to the view

    /**
     * Visualizes the state of the game and the ship board of the player associated with the view.
     */
    public void visualizeShip(){
        if(gameState.equals("END GAME")){
            visualizeFinalRanking();
            return;
        }

        System.out.println("╔════════════════════════════╗");
        System.out.println("║       GALAXY TRUCKER       ║");
        if(firstFlight){
            System.out.println("║       (first flight)       ║");
        }else{
            System.out.println("║       (standard game)      ║");
        }
        System.out.println("╚════════════════════════════╝");

        System.out.print("🚀 Game State: " + gameState);
        if(turnPlayer != null && !gameState.equals("SHIP REPAIR")){
            System.out.println(" --------- IT'S " + turnPlayer + "'S TURN");
        }
        if(gameState.equals("SHIP REPAIR")){
            System.out.println("Damaged player: " + damagedPlayer);
        }
        System.out.println();
        if(hourglassState != null){
            System.out.println("⏳ Hourglass state: " + hourglassState + "\n");
        }
        if(currentCard != null && currentCard != 9001 && currentCard != 9002){
            System.out.println("🃏 Card to solve: " + ImageIDToStringConverter.imageIDtoCardDesc(currentCard.toString()) + "\n");
        }
        if(!dice.areThrowable()){
            System.out.println("\uD83C\uDFB2" + " Dice result: " + dice1result() + "   " + dice2result() + "\n");
        }
        System.out.println();
        System.out.println("👨‍🚀 Player: " + player.getNickname() + " " + convertColorIntoEmoji(player.getColor()));
        System.out.println("💰 Credits: " + player.getCredits());
        System.out.println("💣 Lost components: " + player.getLostComponents());

        if (!pickedDeck.isEmpty()) {
            System.out.print("🃏🃏🃏 Picked Deck: ");
            for (Integer card : pickedDeck) {
                System.out.print("[" + card + "] ");
            }
            System.out.println();
        }

        if (!shownComponents.isEmpty()) {
            System.out.print("🪐 Shown Components: ");
            for (ViewComponent comp : shownComponents) {
                System.out.print("[" + comp.toString() + "] ");
            }
            System.out.println();
        }

        if (pickedViewComponent != null) {
            System.out.println("🛠️ Picked Component: [" + pickedViewComponent.toString() + " " + pickedViewComponent.getOrientation().toString().charAt(0) + "]");
        }

        player.visualize();
    }

    //visualizes the state of the ship board of another player

    /**
     * Visualizes the state of the ship board of another player.
     * @param nickname
     */
    public void visualizeShip(String nickname){
        if(gameState.equals("END GAME")){
            visualizeFinalRanking();
            return;
        }

        if(!otherPlayers.containsKey(nickname)){
            System.out.println("Error: invalid nickname");
        }
        else{
            otherPlayers.get(nickname).visualize();
        }
    }

    //visualizes the state of the flight board

    /**
     * Visualizes the state of the flight board.
     */
    public void visualizeFlightBoard(){
        if(gameState.equals("END GAME")){
            visualizeFinalRanking();
            return;
        }

        if(flightBoard == null){
            System.out.println("Error: unstarted game");
        }
        else{
            this.flightBoard.visualize();
        }
    }

    //visualizes the final ranking of the game

    /**
     * Visualizes the final ranking of the game, showing the credits of each player.
     */
    public void visualizeFinalRanking(){
        System.out.println("╔════════════════════════════╗");
        System.out.println("║        GAME OVER !!!       ║");
        System.out.println("║                            ║");
        System.out.println("║    Final cosmic credits:   ║");
        System.out.println("╚════════════════════════════╝");

        System.out.println("👨‍🚀 Player: " + player.getNickname() + "          💰 Credits: " + player.getCredits());
        for(ViewPlayer p: otherPlayers.values()){
            System.out.println("👨‍🚀 Player: " + p.getNickname() + "          💰 Credits: " + p.getCredits());
        }
    }

    /**
     * Returns the final ranking of the game as a list of strings, each representing a player and their credits.
     * @return List<String> - a list of strings representing the final ranking
     */
    public List<String> getFinalRanking(){
        List<String> finalRankingList = new ArrayList<>();
        finalRankingList.add(player.getNickname() + "          💰 Credits: " + player.getCredits());
        for(ViewPlayer p: otherPlayers.values()){
            finalRankingList.add(p.getNickname() + "          💰 Credits: " + p.getCredits());
        }
        return finalRankingList;
    }

    //converts an image id in the XXX format

    /**
     * Converts an image ID to a string in the format "XXX", where X is a digit.
     * @param imageID
     * @return String - the formatted image ID as a string
     */
    public static String convertImageID(int imageID){
        return String.format("%03d", imageID);
    }


    //adds a new player

    /**
     * Adds a new player to the game view.
     * @param nickname
     * @param color
     */
    public void updateNewPlayer(String nickname, Color color) {
        otherPlayers.put(nickname, new ViewPlayer(nickname, color, this.firstFlight));
    }

    //initializes the flight board

    /**
     * Initializes the flight board with the players' states.
     */
    public void updateStartAssembling(){
        gameState = "ASSEMBLING PHASE";
        List<ViewPlayer> players = new ArrayList<>();
        players.add(player);
        players.addAll(otherPlayers.values());
        flightBoard = new ViewFlightBoard(players);
    }

    //changes the picked component of the player associated to the view

    /**
     * Updates the picked component of the player associated with the view.
     * @param imageID
     * @param released
     */
    public void updatePickedComponent(int imageID, boolean released) {
        if(released){
            pickedViewComponent = null;
        }
        else{
            pickedViewComponent = new ViewComponent(convertImageID(imageID));
        }
    }

    //adds a new component to the ones placed face up

    /**
     * Updates the shown components by adding or removing a component based on its image ID.
     * @param imageID
     * @param released
     */
    public void updateShownComponent(int imageID, boolean released) {
        if(released){
            shownComponents.add(new ViewComponent(convertImageID(imageID)));
        }
        else{
            for(ViewComponent comp : shownComponents){
                if(comp.getImageID().equals(convertImageID(imageID))){
                    shownComponents.remove(comp);
                    break;
                }
            }
        }
    }

    //updates the reserved components of a player

    /**
     * Updates the reserved components of a player based on their nickname and the image ID of the component.
     * @param nickname
     * @param imageID
     * @param released
     */
    public void updateReservedComponent(String nickname, int imageID, boolean released){
        if(nickname.equals(this.player.getNickname())){
            player.updateReservedComponent(imageID, released);
        }
        else{
            otherPlayers.get(nickname).updateReservedComponent(imageID, released);
        }
    }

    //rotates the picked component of the player associated to the view

    /**
     * Updates the orientation of the picked component by rotating it left.
     */
    public void updateRotatePickedComponent() {
        pickedViewComponent.rotateLeft();
    }

    //assembles a component on the ship board of a player

    /**
     * Updates the assembled component on the ship board of a player.
     * @param nickname
     * @param imageID
     * @param orientation
     * @param x
     * @param y
     */
    public void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y){
        if(nickname.equals(this.player.getNickname())){
            player.updateAssembledComponent(imageID, orientation, x, y);
        }
        else{
            otherPlayers.get(nickname).updateAssembledComponent(imageID, orientation, x, y);
        }
    }

    //sets the picked cards for the player associated to the view

    /**
     * Updates the picked deck of cards for the player associated with the view.
     * @param deckIDs
     */
    public void updatePickedDeck(List<Integer> deckIDs) {
        pickedDeck = deckIDs;
    }

    //releases the picked cards of the player associated to the view

    /**
     * Updates the released deck by clearing the picked cards of the player associated with the view.
     */
    public void updateReleasedDeck() {
        pickedDeck.clear();
    }

    //sets the position of a player on the flight board

    /**
     * Updates the position of a player on the flight board.
     * @param nickname
     * @param position
     */
    public void updateFinishAssembling(String nickname, int position) {
        if(nickname.equals(this.player.getNickname())){
            this.gameState = "WAIT FOR OTHER PLAYERS...";
            flightBoard.setPosition(player, 0, position);
            player.loseReservedComponents();
        }
        else{
            flightBoard.setPosition(otherPlayers.get(nickname), 0, position);
            otherPlayers.get(nickname).loseReservedComponents();
        }

    }

    //notifies the view that the hourglass has been turned around

    /**
     * Updates the hourglass state to indicate that a new cycle has started.
     */
    public void updateStartNewCycle(){
        this.hourglassState = "Hourglass is running...";
        this.hourglassPosition++;
        this.hourglassRunning = true;
    }

    //notifies the view that the hourglass has finished running

    /**
     * Updates the hourglass state to indicate that the current cycle has finished.
     */
    public void updateFinishedCycle(){
        this.hourglassState = "Hourglass has finished running, you can start the last cycle";
        this.hourglassRunning = false;
    }

    //invoked when the game switches to the ship placement phase

    /**
     * Updates the game state to indicate that the ship placement phase has started.
     */
    public void updateShipPlacement() {
        this.hourglassState = "Hourglass has finished running, place your ship on the flight board!!!";
        this.hourglassRunning = false;
        gameState = "SHIP PLACEMENT";
    }

    //invoked when the game switches to the ship control phase

    /**
     * Updates the game state to indicate that the ship control phase has started.
     */
    public void updateShipControl() {
        gameState = "SHIP CONTROL";
        pickedViewComponent = null;
        hourglassState = null;
        hourglassRunning = false;
        hourglassPosition = 2;
        shownComponents.clear();
        pickedDeck.clear();
        player.updateShipControl();
        for(ViewPlayer player : otherPlayers.values()){
            player.updateShipControl();
        }
    }

    //notifies the view that a player has to repair its ship board before the player in turn can pick a new card

    /**
     * Updates the game state to indicate that a player needs to repair their ship.
     * @param nickname
     */
    public void updateShipRepair(String nickname) {
        gameState = "SHIP REPAIR";
        damagedPlayer = nickname;
    }

    //notifies the view that a component of a player's ship board has been destroyed

    /**
     * Updates the destroyed component on a player's ship board.
     * @param nickname
     * @param x
     * @param y
     */
    public void updateDestroyedComponent(String nickname, int x, int y){
        if(nickname.equals(this.player.getNickname())){
            player.updateDestroyedComponent(x, y);
            player.loseComponent();
        }
        else{
            otherPlayers.get(nickname).updateDestroyedComponent(x, y);
            otherPlayers.get(nickname).loseComponent();
        }
    }

    //notifies the view about the fact that a player has to pick a card in order to continue the game

    /**
     * Updates the game state to indicate that the player needs to pick a card.
     */
    public void updateCardPicking() {
        gameState = "CARD PICKING";
        damagedPlayer = null;
    }

    //notifies the view about the next player whose turn it is to perform an action

    /**
     * Updates the turn player to the next player in line.
     * @param nickname
     */
    public void updateNextTurn(String nickname){
        this.turnPlayer = nickname;
    }

    //notifies the view about the fact that a card has been picked

    /**
     * Updates the game state to indicate that a card has been picked.
     * @param cardID
     */
    public void updateCardSolving(Integer cardID){
        gameState = "CARD SOLVING";
        currentCard = cardID;
    }

    //notifies the view about the fact that a player has quit the game

    /**
     * Updates the game state to reflect that a player has quit the game.
     * @param nickname
     */
    public void updatePlayerQuit(String nickname){
        if(nickname.equals(this.player.getNickname())){
            flightBoard.updatePlayerQuit(player);
            player.updateQuit();
        }
        else{
            flightBoard.updatePlayerQuit(otherPlayers.get(nickname));
            otherPlayers.get(nickname).updateQuit();
        }
    }

    //notifies the view about a change in the number of crew of a cabin

    /**
     * Updates the crew change in a cabin for a player.
     * @param nickname
     * @param x
     * @param y
     * @param change
     */
    public void updateCrewChange(String nickname, int x, int y, int change) {
        if(nickname.equals(this.player.getNickname())){
            player.updateCrewChange(x,y,change);
        }
        else{
            otherPlayers.get(nickname).updateCrewChange(x,y,change);
        }
    }

    //notifies the view about a change in the number of aliens of a cabin

    /**
     * Updates the alien change in a cabin for a player.
     * @param nickname
     * @param x
     * @param y
     * @param isPurple
     * @param added
     */
    public void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) {
        if(nickname.equals(this.player.getNickname())){
            player.updateAlienChange(x,y,isPurple);
        }
        else{
            otherPlayers.get(nickname).updateAlienChange(x,y,isPurple);
        }
    }

    //notifies the view about a change in the number of batteries of a battery hold

    /**
     * Updates the number of batteries in a battery hold for a player.
     * @param nickname
     * @param x
     * @param y
     * @param change
     */
    public void updateBatteries(String nickname, int x, int y, int change) {
        if(nickname.equals(this.player.getNickname())){
            player.updateBatteries(x,y,change);
        }
        else{
            otherPlayers.get(nickname).updateBatteries(x,y,change);
        }
    }

    //notifies the view that a good has been loaded in a cargo hold

    /**
     * Updates the loaded good in a cargo hold for a player.
     * @param nickname
     * @param x
     * @param y
     * @param good
     */
    public void updateLoadedGood(String nickname, int x, int y, Color good) {
        if(nickname.equals(this.player.getNickname())){
            player.updateLoadedGood(x,y,good);
        }
        else{
            otherPlayers.get(nickname).updateLoadedGood(x,y,good);
        }
    }

    //notifies the view that some goods have been removed form a cargo hold

    /**
     * Updates the removed goods from a cargo hold for a player.
     * @param nickname
     * @param x
     * @param y
     * @param good
     * @param numberGoods
     */
    public void updateRemovedGoods(String nickname, int x, int y, Color good, int numberGoods){
        if(nickname.equals(this.player.getNickname())){
            player.updateRemovedGoods(x,y,good,numberGoods);
        }
        else{
            otherPlayers.get(nickname).updateRemovedGoods(x,y,good,numberGoods);
        }
    }

    //notifies the view that a player has gained/lost credits

    /**
     * Updates the credits of a player by adding or subtracting a specified amount.
     * @param nickname
     * @param change
     */
    public void updatePlayerCredits(String nickname, int change) {
        if(nickname.equals(this.player.getNickname())){
            player.updateCredits(change);
        }
        else{
            otherPlayers.get(nickname).updateCredits(change);
        }
    }

    //notifies the view that the position of a player has changed

    /**
     * Updates the position of a player on the flight board.
     * @param nickname
     * @param lap
     * @param cell
     */
    public void updatePlayerPosition(String nickname, int lap, int cell) {
        if(nickname.equals(this.player.getNickname())){
            flightBoard.updatePlayerPosition(player, lap, cell);
        }
        else{
            flightBoard.updatePlayerPosition(otherPlayers.get(nickname), lap, cell);
        }
    }

    //simulates a throw of the dice

    /**
     * Simulates a throw of the dice, updating their values.
     * @throws Exception
     */
    public void updateRollDice() throws Exception{
        this.dice.rollDice();
    }

    //notifies a view that the current dice configuration must be invalidated because it has already been used

    /**
     * Updates the dice to allow a new throw, enabling the player to throw the dice again.
     */
    public void updateThrowableDice(){
        this.dice.enableThrow(true);
    }

    //notifies the view about the fact that the game is finished

    /**
     * Updates the game state to indicate that the game has ended.
     */
    public void updateEndGame(){
        this.gameState = "END GAME";
    }
}
