package it.polimi.ingsw.galaxytrucker.view;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

import java.util.*;

public class View {
    public boolean firstFlight;     //true if the view refers to a first flight game, false otherwise
    private String gameState;   //string describing the phase of the game
    private String turnPlayer;      //nickname of the player next in turn
    private ViewFlightBoard flightBoard;    //current flight board state
    private ViewPlayer player;      //state of the player associated to the view
    private Map<String, ViewPlayer> otherPlayers = new HashMap<>();     //other player's state
    private ViewComponent pickedViewComponent;  //component in the player's hand
    private List<Integer> pickedDeck = new ArrayList<>();   //set of cards in the player's hand
    private List<ViewComponent> shownComponents = new ArrayList<>();    //set of components placed face up
    private Integer currentCard;        //current card to solve

    public View(String nickname, Color color, boolean firstFlight) {
        this.player = new ViewPlayer(nickname, color, firstFlight);
        this.gameState = "WAITING FOR OTHER PLAYERS....";
        this.firstFlight = firstFlight;
        this.pickedViewComponent = null;
        this.flightBoard = null;
        this.turnPlayer = null;
        this.currentCard = null;
    }

    public static void main(String[] args){
        View view = new View("fil", Color.BLUE, true);
        view.updateBatteries("fil", 2, 3, 1);
        view.visualizeShip();
    }

    //visualizes the state of the game and of the ship board of the player associated to the view
    public void visualizeShip(){
        System.out.println("╔════════════════════════════╗");
        System.out.println("║       GALAXY TRUCKER       ║");
        if(firstFlight){
            System.out.println("║       (first flight)       ║");
        }else{
            System.out.println("║       (standard game)      ║");
        }
        System.out.println("╚════════════════════════════╝");

        System.out.print("🚀 Game State: " + gameState);
        if(turnPlayer != null){
            System.out.println(" --------- IT'S " + turnPlayer + "'S TURN");
        }
        if(currentCard != null){
            System.out.println("🃏 Card to solve: " + currentCard + "\n");
        }
        System.out.println();
        System.out.println("👨‍🚀 Player: " + player.getNickname() + " " + convertColor(player.getColor()));
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
                System.out.print("[" + comp.getImageID() + "] ");
            }
            System.out.println();
        }

        if (pickedViewComponent != null) {
            System.out.println("🛠️ Picked Component: [" + pickedViewComponent.getImageID() + " " + pickedViewComponent.getOrientation().toString().charAt(0) + "]");
        }

        player.visualize();
    }

    //visualizes the state of the ship board of another player
    public void visualizeShip(String nickname){
        if(!otherPlayers.containsKey(nickname)){
            System.out.println("Error: invalid nickname");
        }
        else{
            otherPlayers.get(nickname).visualize();
        }
    }

    //visualizes the state of the flight board
    public void visualizeFlightBoard(){
        if(flightBoard == null){
            System.out.println("Error: unstarted game");
        }
        else{
            this.flightBoard.visualize();
        }
    }

    //converts an image id in the XXX format
    public static String convertImageID(int imageID){
        return String.format("%03d", imageID);
    }

    //converts a color in an emoji
    public static String convertColor(Color color) {
        return switch (color) {
            case Color.RED -> "🟥";
            case Color.GREEN -> "🟩";
            case Color.BLUE -> "🟦";
            case Color.YELLOW -> "🟨";
        };
    }

    //adds a new player
    public void updateNewPlayer(String nickname, Color color) {
        otherPlayers.put(nickname, new ViewPlayer(nickname, color, this.firstFlight));
    }

    //initializes the flight board
    public void updateStartAssembling(){
        gameState = "ASSEMBLING PHASE";
        List<ViewPlayer> players = new ArrayList<>();
        players.add(player);
        players.addAll(otherPlayers.values());
        flightBoard = new ViewFlightBoard(players);
    }

    //changes the picked component of the player associated to the view
    public void updatePickedComponent(int imageID, boolean released) {
        if(released){
            pickedViewComponent = null;
        }
        else{
            pickedViewComponent = new ViewComponent(convertImageID(imageID));
        }
    }

    //adds a new component to the ones placed face up
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
    public void updateReservedComponent(String nickname, int imageID, boolean released){
        if(nickname.equals(this.player.getNickname())){
            player.updateReservedComponent(imageID, released);
        }
        else{
            otherPlayers.get(nickname).updateReservedComponent(imageID, released);
        }
    }

    //rotates the picked component of the player associated to the view
    public void updateRotatePickedComponent() {
        pickedViewComponent.rotateLeft();
    }

    //assembles a component on the ship board of a player
    public void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y){
        if(nickname.equals(this.player.getNickname())){
            player.updateAssembledComponent(imageID, orientation, x, y);
        }
        else{
            otherPlayers.get(nickname).updateAssembledComponent(imageID, orientation, x, y);
        }
    }

    //sets the picked cards for the player associated to the view
    public void updatePickedDeck(List<Integer> deckIDs) {
        pickedDeck = deckIDs;
    }

    //releases the picked cards of the player associated to the view
    public void updateReleasedDeck() {
        pickedDeck.clear();
    }

    //sets the position of a player on the flight board
    public void updateFinishAssembling(String nickname, int position) {
        if(nickname.equals(this.player.getNickname())){
            flightBoard.setPosition(player, 0, position);
            player.loseReservedComponents();
        }
        else{
            flightBoard.setPosition(otherPlayers.get(nickname), 0, position);
            otherPlayers.get(nickname).loseReservedComponents();
        }

    }

    //invoked when the game switches to the ship control phase
    public void updateShipControl() {
        gameState = "SHIP CONTROL";
        pickedViewComponent = null;
        shownComponents.clear();
        pickedDeck.clear();
        player.updateShipControl();
    }

    //notifies the view that a component of a player's ship board has been destroyed
    public void updateDestroyedComponent(String nickname, int x, int y){
        if(nickname.equals(this.player.getNickname())){
            player.updateDestroyedComponent(nickname, x, y);
            player.loseComponent();
        }
        else{
            otherPlayers.get(nickname).updateDestroyedComponent(nickname, x, y);
            otherPlayers.get(nickname).loseComponent();
        }
    }

    //notifies the view about the fact that a player has to pick a card in order to continue the game
    public void updateCardPicking() {
        gameState = "CARD PICKING";
        currentCard = null;
    }

    //notifies the view about the next player whose turn it is to perform an action
    public void updateNextTurn(String nickname){
        this.turnPlayer = nickname;
    }

    //notifies the view about the fact that a card has been picked
    public void updateCardSolving(Integer cardID){
        gameState = "CARD SOLVING";
        currentCard = cardID;
    }

    //notifies the view about the fact that a player has quit the game
    public void updatePlayerQuit(String nickname){
        if(nickname.equals(this.player.getNickname())){
            flightBoard.updatePlayerQuit(player);
        }
        else{
            flightBoard.updatePlayerQuit(otherPlayers.get(nickname));
        }
    }

    //notifies the view about a change in the number of crew of a cabin
    public void updateCrewChange(String nickname, int x, int y, int change) {
        if(nickname.equals(this.player.getNickname())){
            player.updateCrewChange(x,y,change);
        }
        else{
            otherPlayers.get(nickname).updateCrewChange(x,y,change);
        }
    }

    //notifies the view about a change in the number of aliens of a cabin
    public void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) {
        if(nickname.equals(this.player.getNickname())){
            player.updateAlienChange(x,y,isPurple);
        }
        else{
            otherPlayers.get(nickname).updateAlienChange(x,y,isPurple);
        }
    }

    //notifies the view about a change in the number of batteries of a battery hold
    public void updateBatteries(String nickname, int x, int y, int change) {
        if(nickname.equals(this.player.getNickname())){
            player.updateBatteries(x,y,change);
        }
        else{
            otherPlayers.get(nickname).updateBatteries(x,y,change);
        }
    }
}
