package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.network.VirtualView;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.*;
import it.polimi.ingsw.galaxytrucker.model.enumerations.*;
import it.polimi.ingsw.galaxytrucker.model.eventCardClasses.*;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.*;

import java.util.*;

//this class describes the entire status of the game, the controller will invoke its methods in order to modify the
//model according to specific actions performed by the players on the view
public class GameState {
    private final boolean firstFlight;      //true if the game has been set as "learning flight", false if it is a standard game
    private final int numPlayers;     //number of players
    private Map<String,Position> playersPos;    //maps (in position order) each player to its position on the flight board
    private Map<String,Player> playersPlay;     //maps each player to its information
    private String turnPlayer;       //player that must perform an action in order to solve a card
    private List<Deck> decks;       //decks used during the assembling phase (only for standard game)
    private Deck gameDeck;      //main deck used during the game
    private EventCard currentCard;          //current card picked from the deck during the game
    private List<Component> hiddenComponents;       //components turned face down during the assembling phase
    private List<Component> shownComponents;        //components turned face up during the assembling phase
    private State state;            //current state of the game
    final Map<String, VirtualView> clients = new HashMap<>();     //list of all clients

    public GameState(boolean firstFlight, int numPlayers) {     //constructor, creates the deck(s) of cards and instantiates the components
        this.firstFlight = firstFlight;
        this.playersPos = new LinkedHashMap<>();
        this.playersPlay = new HashMap<>();
        this.turnPlayer = "";
        this.numPlayers = numPlayers;
        this.gameDeck = null;
        this.state = State.WAITING_FOR_PLAYERS;
    }



     //[method for testing]
    public void assembleComponent(String nickname, Component component, int x, int y){
        playersPlay.get(nickname).assembleComponent(component, x, y);
    }
    //[method for testing] set a custom deck
    public void setGameDeck(Deck deck){
        gameDeck = deck;
    }
    //[method for testing]
    public EventCard getCurrentCard(){
        return this.currentCard;
    }

    //
    // GETTERS AND SETTERS
    // these methods don't belong to the model-controller interface, but are needed by the methods of
    // the EventCardClasses package in order to modify the model due to the effect of a card
    //

    public void setGameState(State state) {
        this.state = state;
    }
    public State getGameState() {
        return this.state;
    }
    public Map<String,Position> getPlayersPos() { //return a copy of the player postion map
        if(this.playersPos == null) { return null;}
        return new LinkedHashMap<>(playersPos);
    }
    public String getTurnPlayer() {
        return turnPlayer;
    }
    public Map<String,Player> getPlayersPlay() { // return a copy of the player nickanme map
        if(this.playersPlay == null) { return null;}
        return new HashMap<>(playersPlay);
    }
    public int getCurrentPlayers(){return playersPlay.size();}
    public List<Component> getShownComponent() { //return a copy of the shown components
        return new ArrayList<Component>(shownComponents);
    }
    //returns the list of player nicknames (in position order)
    public List<String> getNicknames(){
        return new ArrayList<>(playersPos.keySet());
    }
    //determines whether a player has abandoned the game
    public boolean hasAbandoned(String nickname){
        return playersPlay.get(nickname).hasAbandoned();
    }
    //return the ship board color of a player
    public Color getColor(String nickname){
        return playersPlay.get(nickname).getColor();
    }
    //returns a list of image IDs of the cards contained in the deck
    public List<Integer> convertDeck(Deck deck){
        List<Integer> IDs = new ArrayList<>();
        for(EventCard card : deck.getCards()){
            IDs.add(card.getImageID());
        }
        return IDs;
    }
    //returns teh number of exposed connectors on a player's ship board
    public int countExposedConnectors(String nickname){
        return playersPlay.get(nickname).countExposedConnectors();

    }
    //returns the number of crew members in a player's ship board
    public int getCrewCount(String nickname) {
        return playersPlay.get(nickname).getNumberCrew();
    }
    //returns the number of batteries on a player's ship board
    public int getNumberBatteries(String nickname){
        return playersPlay.get(nickname).getNumberBatteries();
    }
    //returns the number of goods on a player's ship board
    public int getNumberGoods(String nickname){
        return playersPlay.get(nickname).getNumberGoods();
    }
    //this method removes the numberGoods-most precious goods from a player's ship board
    public void losePreciousGoods(String nickname, int numberGoods){
        playersPlay.get(nickname).losePreciousGoods(numberGoods);
    }
    //this method returns the nickname of the player with fewer crew members
    public String getCrewMinPlayer() {
        int numberCrew;
        int crewMin=1000;
        String crewMinPlayer = "";
        for(String nickname : getNicknames()){
            numberCrew = getCrewCount(nickname);
            if(numberCrew < crewMin && !hasAbandoned(nickname)){
                crewMin = numberCrew;
                crewMinPlayer = nickname;
            }
            else if(numberCrew == crewMin && !hasAbandoned(nickname) && playersPos.get(nickname).higherThan(playersPos.get(crewMinPlayer))){
                crewMin = numberCrew;
                crewMinPlayer = nickname;
            }
        }
        return crewMinPlayer;
    }
    //returns the cannon strength of a player, removing the given batteries from its ship board in order to activate double cannons
    public double getCannonStrength(String nickname, int usedBatteries){
        return playersPlay.get(nickname).getCannonStrength(usedBatteries);
    }
    //returns the engine strength of a player, removing the given batteries from its ship board in order to activate double engines
    public int getEngineStrength(String nickname, int usedBatteries){
        return playersPlay.get(nickname).getEngineStrength(usedBatteries);
    }
    //substitutes (or adds) a good in a specific container of a player's cargo hold
    public void substituteGoods(String nickname, int cargo_row, int cargo_col, Color good, int pos){
        playersPlay.get(nickname).substituteGoods(cargo_row, cargo_col, good, pos);
    }

    //
     //STARTING PHASE
    //

    //this method instantiates all the components (tiles) of the game, which will be placed face down (hidden)
    public void createComponents(boolean firstFlight) {
        hiddenComponents = new ArrayList<>();
        shownComponents = new ArrayList<>();

        hiddenComponents.add(new Battery(true, 201, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.SINGLE, Connector.DOUBLE, Connector.SMOOTH))));
        hiddenComponents.add(new Battery(true, 202, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.DOUBLE, Connector.SMOOTH, Connector.SMOOTH))));
        hiddenComponents.add(new Battery(true, 203, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.DOUBLE, Connector.SINGLE, Connector.SMOOTH))));
        hiddenComponents.add(new Battery(true, 204, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SINGLE, Connector.DOUBLE, Connector.SINGLE))));
        hiddenComponents.add(new Battery(true, 205, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.SMOOTH, Connector.SMOOTH, Connector.SINGLE))));
        hiddenComponents.add(new Battery(true, 206, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.SINGLE, Connector.SINGLE, Connector.SINGLE))));
        hiddenComponents.add(new Battery(true, 207, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.DOUBLE, Connector.DOUBLE, Connector.DOUBLE))));
        hiddenComponents.add(new Battery(true, 208, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new Battery(true, 209, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new Battery(true, 210, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.UNIVERSAL, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new Battery(true, 211, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new Battery(false, 212, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.SMOOTH, Connector.DOUBLE, Connector.SMOOTH))));
        hiddenComponents.add(new Battery(false, 213, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.SINGLE, Connector.DOUBLE, Connector.SMOOTH))));
        hiddenComponents.add(new Battery(false, 214, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        hiddenComponents.add(new Battery(false, 215, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SINGLE, Connector.SMOOTH, Connector.SMOOTH))));
        hiddenComponents.add(new Battery(false, 216, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.DOUBLE, Connector.SINGLE, Connector.SMOOTH))));
        hiddenComponents.add(new Battery(false, 217, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SINGLE))));

        hiddenComponents.add(new Cabin(301, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL, Connector.SINGLE))));
        hiddenComponents.add(new Cabin(302, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.DOUBLE, Connector.SINGLE, Connector.SINGLE))));
        hiddenComponents.add(new Cabin(303, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.DOUBLE, Connector.DOUBLE, Connector.SINGLE))));
        hiddenComponents.add(new Cabin(304, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SMOOTH, Connector.SMOOTH, Connector.SINGLE))));
        hiddenComponents.add(new Cabin(305, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SINGLE, Connector.SMOOTH, Connector.SINGLE))));
        hiddenComponents.add(new Cabin(306, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SINGLE, Connector.DOUBLE, Connector.SINGLE))));
        hiddenComponents.add(new Cabin(307, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.UNIVERSAL, Connector.DOUBLE))));
        hiddenComponents.add(new Cabin(308, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.DOUBLE, Connector.SMOOTH, Connector.DOUBLE))));
        hiddenComponents.add(new Cabin(309, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SINGLE, Connector.DOUBLE, Connector.DOUBLE))));
        hiddenComponents.add(new Cabin(310, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.SMOOTH, Connector.SMOOTH, Connector.DOUBLE))));
        hiddenComponents.add(new Cabin(311, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new Cabin(312, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.SINGLE, Connector.UNIVERSAL))));
        hiddenComponents.add(new Cabin(313, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.DOUBLE, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new Cabin(314, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.SMOOTH, Connector.SINGLE, Connector.UNIVERSAL))));
        hiddenComponents.add(new Cabin(315, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.SMOOTH, Connector.DOUBLE, Connector.UNIVERSAL))));
        hiddenComponents.add(new Cabin(316, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SMOOTH, Connector.DOUBLE, Connector.UNIVERSAL))));
        hiddenComponents.add(new Cabin(317, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.DOUBLE, Connector.SMOOTH, Connector.UNIVERSAL))));

        hiddenComponents.add(new Cannon(false, 401,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SINGLE, Connector.SMOOTH))));
        hiddenComponents.add(new Cannon(false, 402,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SINGLE, Connector.SMOOTH))));
        hiddenComponents.add(new Cannon(false, 403,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.DOUBLE, Connector.SMOOTH))));
        hiddenComponents.add(new Cannon(false, 404,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.DOUBLE, Connector.SMOOTH))));
        hiddenComponents.add(new Cannon(false, 405,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.SMOOTH, Connector.SMOOTH))));
        hiddenComponents.add(new Cannon(false, 406,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.DOUBLE, Connector.SMOOTH))));
        hiddenComponents.add(new Cannon(false, 407,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.UNIVERSAL, Connector.SMOOTH))));
        hiddenComponents.add(new Cannon(false, 408,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.DOUBLE, Connector.SMOOTH, Connector.SMOOTH))));
        hiddenComponents.add(new Cannon(false, 409,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.DOUBLE, Connector.SINGLE, Connector.SMOOTH))));
        hiddenComponents.add(new Cannon(false, 410,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.DOUBLE, Connector.UNIVERSAL, Connector.SMOOTH))));
        hiddenComponents.add(new Cannon(false, 411,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.UNIVERSAL, Connector.SINGLE, Connector.SMOOTH))));
        hiddenComponents.add(new Cannon(false, 412,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SINGLE))));
        hiddenComponents.add(new Cannon(false, 413,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.DOUBLE, Connector.SINGLE))));
        hiddenComponents.add(new Cannon(false, 414,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL, Connector.SINGLE))));
        hiddenComponents.add(new Cannon(false, 415,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.SINGLE, Connector.SINGLE))));
        hiddenComponents.add(new Cannon(false, 416,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.DOUBLE, Connector.UNIVERSAL, Connector.SINGLE))));
        hiddenComponents.add(new Cannon(false, 417,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.UNIVERSAL, Connector.SMOOTH, Connector.SINGLE))));
        hiddenComponents.add(new Cannon(false, 418,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.DOUBLE))));
        hiddenComponents.add(new Cannon(false, 419,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SINGLE, Connector.DOUBLE))));
        hiddenComponents.add(new Cannon(false, 420,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL, Connector.DOUBLE))));
        hiddenComponents.add(new Cannon(false, 421,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.SMOOTH, Connector.DOUBLE))));
        hiddenComponents.add(new Cannon(false, 422,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.UNIVERSAL, Connector.DOUBLE))));
        hiddenComponents.add(new Cannon(false, 423,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.DOUBLE, Connector.DOUBLE, Connector.DOUBLE))));
        hiddenComponents.add(new Cannon(false, 424,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.DOUBLE, Connector.UNIVERSAL))));
        hiddenComponents.add(new Cannon(false, 425,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.DOUBLE, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new Cannon(true, 426,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SINGLE, Connector.SMOOTH))));
        hiddenComponents.add(new Cannon(true, 427,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.DOUBLE, Connector.SMOOTH))));
        hiddenComponents.add(new Cannon(true, 428,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.UNIVERSAL, Connector.SMOOTH))));
        hiddenComponents.add(new Cannon(true, 429,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.UNIVERSAL, Connector.SMOOTH, Connector.SMOOTH))));
        hiddenComponents.add(new Cannon(true, 430,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.UNIVERSAL, Connector.DOUBLE, Connector.SMOOTH))));
        hiddenComponents.add(new Cannon(true, 431,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.DOUBLE, Connector.SINGLE))));
        hiddenComponents.add(new Cannon(true, 432,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.DOUBLE, Connector.SMOOTH, Connector.SINGLE))));
        hiddenComponents.add(new Cannon(true, 433,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL, Connector.DOUBLE))));
        hiddenComponents.add(new Cannon(true, 434,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.DOUBLE, Connector.SINGLE, Connector.DOUBLE))));
        hiddenComponents.add(new Cannon(true, 435,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new Cannon(true, 436,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SINGLE, Connector.UNIVERSAL))));

        hiddenComponents.add(new CargoHold(true, 501, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SINGLE, Connector.UNIVERSAL, Connector.SMOOTH))));
        hiddenComponents.add(new CargoHold(true, 502, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SINGLE, Connector.UNIVERSAL, Connector.SINGLE))));
        hiddenComponents.add(new CargoHold(true, 503, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.UNIVERSAL, Connector.SMOOTH, Connector.SINGLE))));
        hiddenComponents.add(new CargoHold(true, 504, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new CargoHold(true, 505, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new CargoHold(true, 506, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new CargoHold(true, 507, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.DOUBLE, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new CargoHold(true, 508, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SINGLE, Connector.DOUBLE, Connector.UNIVERSAL))));
        hiddenComponents.add(new CargoHold(true, 509, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new CargoHold(false, 510, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SINGLE))));
        hiddenComponents.add(new CargoHold(false, 511, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.SMOOTH, Connector.SINGLE, Connector.SMOOTH))));
        hiddenComponents.add(new CargoHold(false, 512, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.DOUBLE))));
        hiddenComponents.add(new CargoHold(false, 513, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SMOOTH, Connector.DOUBLE, Connector.SMOOTH))));
        hiddenComponents.add(new CargoHold(false, 514, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.SMOOTH, Connector.DOUBLE, Connector.SINGLE))));
        hiddenComponents.add(new CargoHold(false, 515, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SMOOTH, Connector.SINGLE, Connector.DOUBLE))));
        hiddenComponents.add(new CargoSpecial(false, 601, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.DOUBLE, Connector.SINGLE, Connector.UNIVERSAL))));
        hiddenComponents.add(new CargoSpecial(false, 602, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.UNIVERSAL, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new CargoSpecial(false, 603, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.SINGLE, Connector.SINGLE, Connector.UNIVERSAL))));
        hiddenComponents.add(new CargoSpecial(false, 604, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SINGLE, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new CargoSpecial(false, 605, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.DOUBLE, Connector.DOUBLE, Connector.UNIVERSAL))));
        hiddenComponents.add(new CargoSpecial(false, 606, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new CargoSpecial(true, 607, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SINGLE))));
        hiddenComponents.add(new CargoSpecial(true, 608, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.DOUBLE, Connector.SMOOTH, Connector.SINGLE))));
        hiddenComponents.add(new CargoSpecial(true, 609, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.DOUBLE))));

        hiddenComponents.add(new Engine(false, 701, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.UNIVERSAL, Connector.SMOOTH, Connector.SMOOTH))));
        hiddenComponents.add(new Engine(false, 702, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.UNIVERSAL, Connector.SMOOTH, Connector.SMOOTH))));
        hiddenComponents.add(new Engine(false, 703, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        hiddenComponents.add(new Engine(false, 704, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        hiddenComponents.add(new Engine(false, 705, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.SINGLE, Connector.SMOOTH, Connector.SMOOTH))));
        hiddenComponents.add(new Engine(false, 706, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        hiddenComponents.add(new Engine(false, 707, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        hiddenComponents.add(new Engine(false, 708, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.UNIVERSAL, Connector.SMOOTH, Connector.SMOOTH))));
        hiddenComponents.add(new Engine(false, 709, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.DOUBLE, Connector.SMOOTH, Connector.SMOOTH))));
        hiddenComponents.add(new Engine(false, 710, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.UNIVERSAL, Connector.SMOOTH, Connector.SINGLE))));
        hiddenComponents.add(new Engine(false, 711, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SINGLE, Connector.SMOOTH, Connector.SINGLE))));
        hiddenComponents.add(new Engine(false, 712, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.SMOOTH, Connector.SMOOTH, Connector.SINGLE))));
        hiddenComponents.add(new Engine(false, 713, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.DOUBLE, Connector.SMOOTH, Connector.SINGLE))));
        hiddenComponents.add(new Engine(false, 714, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.SMOOTH, Connector.DOUBLE))));
        hiddenComponents.add(new Engine(false, 715, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.DOUBLE, Connector.SMOOTH, Connector.DOUBLE))));
        hiddenComponents.add(new Engine(false, 716, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SMOOTH, Connector.SMOOTH, Connector.DOUBLE))));
        hiddenComponents.add(new Engine(false, 717, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.SINGLE, Connector.SMOOTH, Connector.DOUBLE))));
        hiddenComponents.add(new Engine(false, 718, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new Engine(false, 719, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new Engine(false, 720, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.DOUBLE, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new Engine(false, 721, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new Engine(true, 722, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        hiddenComponents.add(new Engine(true, 723, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.UNIVERSAL, Connector.SMOOTH, Connector.SMOOTH))));
        hiddenComponents.add(new Engine(true, 724, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
        hiddenComponents.add(new Engine(true, 725, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.SINGLE, Connector.SMOOTH, Connector.SMOOTH))));
        hiddenComponents.add(new Engine(true, 726, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.SINGLE, Connector.SMOOTH, Connector.SINGLE))));
        hiddenComponents.add(new Engine(true, 727, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.DOUBLE, Connector.SMOOTH, Connector.DOUBLE))));
        hiddenComponents.add(new Engine(true, 728, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.SMOOTH, Connector.SMOOTH, Connector.DOUBLE))));
        hiddenComponents.add(new Engine(true, 729, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.UNIVERSAL, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new Engine(true, 730, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL))));

        hiddenComponents.add(new Shield(901, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.UNIVERSAL, Connector.SINGLE))));
        hiddenComponents.add(new Shield(902, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.SMOOTH, Connector.SINGLE, Connector.SINGLE))));
        hiddenComponents.add(new Shield(903, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SINGLE, Connector.DOUBLE, Connector.SINGLE))));
        hiddenComponents.add(new Shield(904, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL, Connector.DOUBLE))));
        hiddenComponents.add(new Shield(905, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.DOUBLE, Connector.DOUBLE, Connector.DOUBLE))));
        hiddenComponents.add(new Shield(906, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.DOUBLE, Connector.SINGLE, Connector.DOUBLE))));
        hiddenComponents.add(new Shield(907, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SINGLE, Connector.UNIVERSAL))));
        hiddenComponents.add(new Shield(908, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.DOUBLE, Connector.DOUBLE, Connector.UNIVERSAL))));

        hiddenComponents.add(new Structural(101, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.SMOOTH, Connector.SINGLE))));
        hiddenComponents.add(new Structural(102, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.UNIVERSAL, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new Structural(103, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.UNIVERSAL, Connector.SINGLE, Connector.UNIVERSAL))));
        hiddenComponents.add(new Structural(104, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.UNIVERSAL, Connector.DOUBLE, Connector.UNIVERSAL))));
        hiddenComponents.add(new Structural(105, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.UNIVERSAL, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new Structural(106, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.SINGLE, Connector.DOUBLE, Connector.UNIVERSAL))));
        hiddenComponents.add(new Structural(107, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.DOUBLE, Connector.SMOOTH, Connector.UNIVERSAL))));
        hiddenComponents.add(new Structural(108, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.DOUBLE, Connector.DOUBLE, Connector.UNIVERSAL))));

        if(!firstFlight){
            hiddenComponents.add(new LifeSupport(false, 801, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.SINGLE, Connector.SMOOTH, Connector.SINGLE))));
            hiddenComponents.add(new LifeSupport(false, 802, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SINGLE, Connector.SMOOTH, Connector.SINGLE))));
            hiddenComponents.add(new LifeSupport(false, 803, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL))));
            hiddenComponents.add(new LifeSupport(false, 804, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SINGLE, Connector.UNIVERSAL))));
            hiddenComponents.add(new LifeSupport(false, 805, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.DOUBLE, Connector.SMOOTH, Connector.UNIVERSAL))));
            hiddenComponents.add(new LifeSupport(false, 806, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL))));
            hiddenComponents.add(new LifeSupport(true, 807, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.DOUBLE, Connector.SMOOTH, Connector.DOUBLE))));
            hiddenComponents.add(new LifeSupport(true, 808, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.DOUBLE, Connector.SMOOTH, Connector.DOUBLE))));
            hiddenComponents.add(new LifeSupport(true, 809, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL))));
            hiddenComponents.add(new LifeSupport(true, 810, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.DOUBLE, Connector.UNIVERSAL))));
            hiddenComponents.add(new LifeSupport(true, 811, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.SMOOTH, Connector.UNIVERSAL))));
            hiddenComponents.add(new LifeSupport(true, 812, new ArrayList<>(Arrays.asList(Connector.DOUBLE, Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL))));
        }
    }
    //if the game is set as "standard", this method instantiates all the adventure cards of the game and creates the 4 decks
    // for the assembling phase; instead, if the game is set as "fist flight", this method creates directly the
    // game deck by selecting 8 level 1 cards
    public void createDecks(boolean firstFlight){

        if(!firstFlight) {  //standard game
            List<EventCard> levelOneCards = new ArrayList<>();
            List<EventCard> levelTwoCards = new ArrayList<>();

            //level 1 cards creation
            levelOneCards.add(new AbandonedShip(2, 3, 1, 1001));
            levelOneCards.add(new AbandonedShip(3, 4, 1, 1002));
            levelOneCards.add(new AbandonedStation(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN)), 5, 1, 2001));
            levelOneCards.add(new AbandonedStation(new ArrayList<>(Arrays.asList(Color.RED, Color.RED)), 6, 1, 2002));
            levelOneCards.add(new CombatZone(true, 3001));
            levelOneCards.add(new MeteorsSwarm(new ArrayList<>(Arrays.asList(new Meteor(true, Orientation.NORTH), new Meteor(false, Orientation.WEST), new Meteor(false, Orientation.EAST))), 5001));
            levelOneCards.add(new MeteorsSwarm(new ArrayList<>(Arrays.asList(new Meteor(false, Orientation.NORTH), new Meteor(false, Orientation.NORTH), new Meteor(false, Orientation.WEST), new Meteor(false, Orientation.EAST), new Meteor(false, Orientation.SOUTH))), 5002));
            levelOneCards.add(new MeteorsSwarm(new ArrayList<>(Arrays.asList(new Meteor(true, Orientation.NORTH), new Meteor(false, Orientation.NORTH), new Meteor(true, Orientation.NORTH))), 5003));
            levelOneCards.add(new OpenSpace(6001));
            levelOneCards.add(new OpenSpace(6002));
            levelOneCards.add(new OpenSpace(6003));
            levelOneCards.add(new OpenSpace(6004));
            levelOneCards.add(new Pirates(4,5, new ArrayList<>(Arrays.asList(new CannonShot(false, Orientation.NORTH), new CannonShot(true, Orientation.NORTH), new CannonShot(false, Orientation.NORTH))),1, 8001));
            levelOneCards.add(new Planets(new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(Color.RED, Color.GREEN, Color.BLUE, Color.BLUE, Color.BLUE)), new ArrayList<>(Arrays.asList(Color.RED, Color.YELLOW, Color.BLUE)), new ArrayList<>(Arrays.asList(Color.RED, Color.BLUE, Color.BLUE, Color.BLUE)), new ArrayList<>(Arrays.asList(Color.RED, Color.GREEN)))),3, 7001));
            levelOneCards.add(new Planets(new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(Color.RED, Color.RED)), new ArrayList<>(Arrays.asList(Color.RED, Color.BLUE, Color.BLUE)), new ArrayList<>(Arrays.asList(Color.YELLOW)))),2, 7002));
            levelOneCards.add(new Planets(new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.BLUE, Color.BLUE)), new ArrayList<>(Arrays.asList(Color.YELLOW, Color.YELLOW)))),3, 7003));
            levelOneCards.add(new Planets(new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(Color.GREEN, Color.GREEN)), new ArrayList<>(Arrays.asList(Color.YELLOW)), new ArrayList<>(Arrays.asList(Color.BLUE, Color.BLUE, Color.BLUE)))),1, 7004));
            levelOneCards.add(new Slavers(5, 6, 3, 1, 8003));
            levelOneCards.add(new Smugglers(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.BLUE)),2,4,1,8005));
            levelOneCards.add(new SpecialEvent(SpecialEventType.STARDUST, 4002));

            Collections.shuffle(levelOneCards);

            //level 2 cards creation
            levelTwoCards.add(new AbandonedShip(4, 6, 1, 1003));
            levelTwoCards.add(new AbandonedShip(5, 6, 1, 1004));
            levelTwoCards.add(new AbandonedStation(new ArrayList<>(Arrays.asList(Color.RED, Color.YELLOW)), 7, 1, 2003));
            levelTwoCards.add(new AbandonedStation(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.YELLOW, Color.GREEN)), 8, 2, 2004));
            levelTwoCards.add(new CombatZone(false, 3002));
            levelTwoCards.add(new SpecialEvent(SpecialEventType.EPIDEMIC, 4001));
            levelTwoCards.add(new MeteorsSwarm(new ArrayList<>(Arrays.asList(new Meteor(false, Orientation.NORTH), new Meteor(false, Orientation.NORTH), new Meteor(true, Orientation.WEST), new Meteor(false, Orientation.WEST), new Meteor(false, Orientation.WEST))), 5004));
            levelTwoCards.add(new MeteorsSwarm(new ArrayList<>(Arrays.asList(new Meteor(true, Orientation.NORTH), new Meteor(true, Orientation.NORTH), new Meteor(false, Orientation.SOUTH), new Meteor(false, Orientation.SOUTH))), 5005));
            levelTwoCards.add(new MeteorsSwarm(new ArrayList<>(Arrays.asList(new Meteor(false, Orientation.NORTH), new Meteor(false, Orientation.NORTH), new Meteor(true, Orientation.EAST), new Meteor(false, Orientation.EAST), new Meteor(false, Orientation.EAST))), 5006));
            levelTwoCards.add(new OpenSpace(6005));
            levelTwoCards.add(new OpenSpace(6006));
            levelTwoCards.add(new OpenSpace(6007));
            levelTwoCards.add(new Pirates(7, 6, new ArrayList<>(Arrays.asList(new CannonShot(true, Orientation.NORTH), new CannonShot(false, Orientation.NORTH), new CannonShot(true, Orientation.NORTH))), 2, 8002));
            levelTwoCards.add(new Planets(new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(Color.RED, Color.RED, Color.RED, Color.YELLOW)), new ArrayList<>(Arrays.asList(Color.RED, Color.RED, Color.GREEN, Color.GREEN)), new ArrayList<>(Arrays.asList(Color.RED, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE)))), 4, 7005));
            levelTwoCards.add(new Planets(new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(Color.RED, Color.RED)), new ArrayList<>(Arrays.asList(Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN)))), 3, 7006));
            levelTwoCards.add(new Planets(new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(Color.RED, Color.YELLOW)), new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.BLUE)), new ArrayList<>(Arrays.asList(Color.GREEN, Color.GREEN)), new ArrayList<>(Arrays.asList(Color.YELLOW)))), 2, 7007));
            levelTwoCards.add(new Planets(new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN)), new ArrayList<>(Arrays.asList(Color.YELLOW, Color.YELLOW)), new ArrayList<>(Arrays.asList(Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE)))), 3, 7008));
            levelTwoCards.add(new Slavers(8, 7, 4, 2, 8004));
            levelTwoCards.add(new Smugglers(new ArrayList<>(Arrays.asList(Color.RED, Color.YELLOW, Color.YELLOW)), 3, 8, 1, 8006));
            levelTwoCards.add(new SpecialEvent(SpecialEventType.STARDUST, 4003));

            Collections.shuffle(levelTwoCards);

            //creation deck 1
            List<EventCard> l1 = new ArrayList<>();
            l1.add(levelOneCards.removeLast());
            l1.add(levelTwoCards.removeLast());
            l1.add(levelTwoCards.removeLast());
            Deck d1 = new Deck(l1);
            //creation deck 2
            List<EventCard> l2 = new ArrayList<>();
            l2.add(levelOneCards.removeLast());
            l2.add(levelTwoCards.removeLast());
            l2.add(levelTwoCards.removeLast());
            Deck d2 = new Deck(l2);
            //creation deck 3
            List<EventCard> l3 = new ArrayList<>();
            l3.add(levelOneCards.removeLast());
            l3.add(levelTwoCards.removeLast());
            l3.add(levelTwoCards.removeLast());
            Deck d3 = new Deck(l3);
            //creation deck 4
            List<EventCard> l4 = new ArrayList<>();
            l4.add(levelOneCards.removeLast());
            l4.add(levelTwoCards.removeLast());
            l4.add(levelTwoCards.removeLast());
            Deck d4 = new Deck(l4);

            decks = new ArrayList<>(Arrays.asList(d1, d2, d3, d4));
        }
        else{   //first flight
            List<EventCard> gameDeckCards = new ArrayList<>();
            //first flight cards creation
            gameDeckCards.add(new AbandonedShip(3, 4, 1, 1002));
            gameDeckCards.add(new AbandonedStation(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN)), 5, 1, 2001));
            gameDeckCards.add(new CombatZone(true, 3001));
            gameDeckCards.add(new MeteorsSwarm(new ArrayList<>(Arrays.asList(new Meteor(true, Orientation.NORTH), new Meteor(false, Orientation.WEST), new Meteor(false, Orientation.EAST))), 5001));
            gameDeckCards.add(new OpenSpace(6001));
            gameDeckCards.add(new Planets(new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(Color.RED, Color.RED)), new ArrayList<>(Arrays.asList(Color.RED, Color.BLUE, Color.BLUE)), new ArrayList<>(Arrays.asList(Color.YELLOW)))),2, 7002));
            gameDeckCards.add(new Smugglers(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.BLUE)),2,4,1,8005));
            gameDeckCards.add(new SpecialEvent(SpecialEventType.STARDUST, 4002));
            //game deck creation
            Deck d = new Deck(gameDeckCards);
            decks = new ArrayList<>();
            decks.add(d);
        }

        currentCard = null;
    }
    //adds a player to the game
    public void addPlayer(VirtualView client, String nickname, Color color) throws UniqueNicknameException, UniquePlayerColorException, InvalidActionException {
        if(state != State.WAITING_FOR_PLAYERS){
            throw new InvalidActionException("Game has already started");
        }
        for(String n : getNicknames()) {
            if(n.equals(nickname)) {
                throw new UniqueNicknameException("Nickname already taken");
            }
            if(color == getColor(n)){
                throw new UniquePlayerColorException("Color already taken");
            }
        }
        playersPlay.put(nickname, new Player(nickname, color, firstFlight));
        playersPos.put(nickname, null);

        clients.put(nickname, client);
        try{
            client.updateWaitingForPlayers(this.firstFlight);
            for(String playerNickname : getNicknames()) {
                if(!playerNickname.equals(nickname)) {
                    clients.get(playerNickname).updateNewPlayer(nickname, color);
                    clients.get(nickname).updateNewPlayer(playerNickname, getColor(playerNickname));
                }
            }
        }
        catch(Exception e){System.out.println("Error during remote method invocation on client");}
        if(numPlayers == getCurrentPlayers()){
            startAssembling();
            for(VirtualView view: clients.values()){
                try{view.updateStartAssembling();}
                catch(Exception e){System.out.println("Error during remote method invocation on client");}
            }
        }
    }
    //invoked when one of the players decides to start the assembling phase
    public void startAssembling() {
        createComponents(firstFlight);
        createDecks(firstFlight);
        state = State.SHIP_BUILDING;
    }

    //
     //ASSEMBLING PHASE
    //

    //invoked when a player wants to pick a component among the one placed face down (assembling phase)
    public void pickHidden(String nickname) throws PickedComponentException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Wait for assembling phase");
        }
        Collections.shuffle(hiddenComponents);
        Component c = hiddenComponents.removeFirst();
        playersPlay.get(nickname).pickComponent(c);

        try{clients.get(nickname).updatePickedComponent(c.getImageID(), false);}
        catch(Exception e){System.out.println("Error during remote method invocation on client");}
    }
    //invoked when a player wants to pick a specific component among the one placed face up (assembling phase)
    public void pickShown(String nickname, int index) throws PickedComponentException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Wait for assembling phase");
        }
        Component c = shownComponents.remove(index);
        playersPlay.get(nickname).pickComponent(c);

        try{clients.get(nickname).updatePickedComponent(c.getImageID(), false);}
        catch(Exception e){System.out.println("Error during remote method invocation on client");}
        for(VirtualView view: clients.values()){
            try{view.updateShownComponent(c.getImageID(), false);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
    }
    //invoked when a player wants to reserve the component that it has picked for its ship board
    public void reserveComponent(String nickname) throws PickedComponentException, ReservedComponentException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Wait for assembling phase");
        }
        if(firstFlight){
            throw new InvalidActionException("Invalid action for first flight game");
        }
        Component c = playersPlay.get(nickname).reserveComponent();

        try{clients.get(nickname).updatePickedComponent(c.getImageID(), true);}
        catch(Exception e){System.out.println("Error during remote method invocation on client");}
        for(VirtualView view: clients.values()){
            try{view.updateReservedComponent(nickname, c.getImageID(), true);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
    }
    //invoked when a player wants to pick one of the components that it has reserved for its ship board
    public void pickReservedComponent(String nickname, int position) throws ReservedComponentException, PickedComponentException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Wait for assembling phase");
        }
        if(firstFlight){
            throw new InvalidActionException("Invalid action for first flight game");
        }
        Component c = playersPlay.get(nickname).pickReservedComponent(position);

        try{clients.get(nickname).updatePickedComponent(c.getImageID(), false);}
        catch(Exception e){System.out.println("Error during remote method invocation on client");}
        for(VirtualView view: clients.values()){
            try{view.updateReservedComponent(nickname, c.getImageID(), false);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
    }
    //invoked when a player wants to release (therefore, place face up) the component that it has picked
    public void putShown(String nickname) throws PickedComponentException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Wait for assembling phase");
        }
        Component c = playersPlay.get(nickname).releaseComponent();
        shownComponents.add(c);

        try{clients.get(nickname).updatePickedComponent(c.getImageID(), true);}
        catch(Exception e){System.out.println("Error during remote method invocation on client");}
        for(VirtualView view: clients.values()){
            try{view.updateShownComponent(c.getImageID(), true);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
    }
    //invoked when a player wants to assemble on the ship board the component that it has picked
    public void assembleComponent(String nickname, int x, int y) throws AssembledComponentException, PickedComponentException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Wait for assembling phase");
        }
        Component c = playersPlay.get(nickname).assembleComponent(x,y);

        try{clients.get(nickname).updatePickedComponent(c.getImageID(), true);}
        catch(Exception e){System.out.println("Error during remote method invocation on client");}
        for(VirtualView view: clients.values()){
            try{view.updateAssembledComponent(nickname, c.getImageID(), c.getOrientation(), x, y);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
    }
    //invoked when a player wants to change the orientation of the component that it has picked
    public void rotatePickedComponent(String nickname) throws InvalidActionException, PickedComponentException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Wait for assembling phase");
        }
        playersPlay.get(nickname).rotatePickedComponent();

        try{clients.get(nickname).updateRotatePickedComponent();}
        catch(Exception e){System.out.println("Error during remote method invocation on client");}
    }
    //invoked when a player wants to pick a deck during the assembling phase to see its content
    public void pickDeck(String nickname, int deckNumber) throws PickedDeckException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Wait for assembling phase");
        }
        if(firstFlight){
            throw new InvalidActionException("Invalid action for first flight game");
        }
        if(deckNumber <= 0 || deckNumber >= decks.size()){
            throw new InvalidActionException("Invalid deck number");
        }
        if(decks.get(deckNumber).isPicked()){
            throw new PickedDeckException("Deck already picked");
        }
        playersPlay.get(nickname).pickDeck(deckNumber);
        decks.get(deckNumber).setPicked();

        List<Integer> deckIDs = convertDeck(decks.get(deckNumber));
        try{clients.get(nickname).updatePickedDeck(deckIDs);}
        catch(Exception e){System.out.println("Error during remote method invocation on client");}
    }
    //invoked when a player wants to release the deck it has picked, during the assembling phase
    public void releaseDeck(String nickname) throws InvalidActionException, PickedDeckException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Wait for assembling phase");
        }
        if(firstFlight){
            throw new InvalidActionException("Invalid action for first flight game");
        }
        int releasedDeckNumber = playersPlay.get(nickname).releaseDeck();
        decks.get(releasedDeckNumber).setNotPicked();

        try{clients.get(nickname).updateReleasedDeck();}
        catch(Exception e){System.out.println("Error during remote method invocation on client");}
    }
    //invoked when a player has finished the assembling phase and has to pick a free position on the flight board
    public void setPosition(String nickname, int initCell) throws InvalidPositionException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Wait for assembling phase");
        }
        if(firstFlight) {
            if(!LevelOnePosition.validStartingCells.contains(initCell)) {
                throw new InvalidPositionException("Invalid starting position");
            }
        }
        else{
            if(!LevelTwoPosition.validStartingCells.contains(initCell)) {
                throw new InvalidPositionException("Invalid starting position");
            }
        }
        for(Position p : playersPos.values()) {
            if(p!= null && p.getCell() == initCell) {
                throw new InvalidPositionException("Starting position already taken");
            }
        }
        if(firstFlight) {
            playersPos.put(nickname, new LevelOnePosition(initCell));
        }
        else{
            playersPos.put(nickname, new LevelTwoPosition(initCell));
            playersPlay.get(nickname).loseReservedComponents();
        }

        for(VirtualView view: clients.values()){
            try{view.updateFinishAssembling(nickname, initCell);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }

        if(!playersPos.containsValue(null)){
            state = State.SHIP_CONTROL;
            for(VirtualView view: clients.values()){
                try{view.updateShipControl();}
                catch(Exception e){System.out.println("Error during remote method invocation on client");}
            }
            checkShipBoards();
        }
    }

    //
     //SHIP CONTROL PHASE
    //

    //invoked when a component of a player's ship board must be destroyed
    public void destroyComponent(String nickname, int x, int y) throws AssembledComponentException, InvalidActionException {
        if(state != State.SHIP_CONTROL){
            throw new InvalidActionException("Wait for ship control phase");
        }
        playersPlay.get(nickname).destroyComponent(x,y);

        for(VirtualView view: clients.values()){
            try{view.updateDestroyedComponent(nickname, x, y);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }

        checkShipBoards();
    }
    //invoked when a player wants to initialize a cabin of its shipboard with 2 human crew members
    public void addCrew(String nickname, int x, int y) throws AssembledComponentException, FullCabinException, InvalidActionException {
        if(state != State.SHIP_CONTROL){
            throw new InvalidActionException("Wait for ship control phase");
        }
        playersPlay.get(nickname).addCrew(x,y);

        for(VirtualView view: clients.values()){
            try{view.updateCrewChange(nickname, x, y, 2);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }

        checkShipBoards();
    }
    //invoked when the player wants to initialize a battery container with batteries
    public void addBatteries(String nickname, int x, int y) throws AssembledComponentException, NoBatteriesException, InvalidActionException {
        if(state != State.SHIP_CONTROL){
            throw new InvalidActionException("Wait for ship control phase");
        }
        int addedBatteries = playersPlay.get(nickname).addBatteries(x,y);

        for(VirtualView view: clients.values()){
            try{view.updateBatteries(nickname, x, y, addedBatteries);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }

        checkShipBoards();
    }
    //invoked when a player wants to initialize a cabin of its shipboard with an alien
    public void addAlien(String nickname, boolean isPurple, int x, int y) throws AssembledComponentException, FullCabinException, InvalidActionException {
        if(state != State.SHIP_CONTROL){
            throw new InvalidActionException("Wait for ship control phase");
        }
        if(firstFlight){
            throw new InvalidActionException("Invalid action for first flight game");
        }
        playersPlay.get(nickname).addAlien(isPurple, x, y);

        for(VirtualView view: clients.values()){
            try{view.updateAlienChange(nickname, x, y, isPurple, true);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }

        checkShipBoards();
    }
    //checks the correctness of all the ships in the game in order to start the flight phase (ships must me
    //well assembled and each cabin must be full of crew)
    public void checkShipBoards(){
        boolean correctShips = true;
        for(Player p : playersPlay.values()) {
            if(!p.hasCorrectShipBoard() || !p.hasAllCabinsBatteriesFull()){
                correctShips = false;
                break;
            }
        }
        if(correctShips){
            state = State.CARD_PICKING;
            if(gameDeck==null){
                createGameDeck();
            }
            for(VirtualView view: clients.values()){
                try{view.updateCardPicking();}
                catch(Exception e){System.out.println("Error during remote method invocation on client");}
            }

            updateTurns();
        }
    }
    //creates the main deck for the game by unifying and shuffling the 4 decks used during the assembling phase;
    //this method is invoked after the assembling phase
    public void createGameDeck() {
        List<EventCard> gameDeckCards = new ArrayList<>();
        for (Deck deck : decks) {
            gameDeckCards.addAll(deck.getCards());
        }
        gameDeck = new Deck(gameDeckCards);
        gameDeck.shuffle();
    }

    //
     //FLIGHT PHASE
    //

    //this method orders playersPos in (decreasing) position order and assigns the leader's nickname to turnPlayer
    public void updateTurns(){
        playersPos = playersPos.entrySet()
                .stream()
                .sorted((p1,p2) ->{
                    Position pp1 = p1.getValue();
                    Position pp2 = p2.getValue();
                    return pp2.higherThan(pp1) ? 1 : -1;
                }).collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), Map::putAll);
        turnPlayer = playersPos.keySet().iterator().next();

        for(VirtualView view: clients.values()){
            try{view.updateNextTurn(this.turnPlayer);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
    }
    //this method updates turnPlayer with the nickname of the next player that has to perform an action
    public void nextTurn(){
        Iterator<String> iterator = playersPos.keySet().iterator();
        boolean found = false;
        //finds the nickname that follows turnPlayer in playerPos
        while (iterator.hasNext()) {
            String current = iterator.next();
            if (found) {
                turnPlayer = current;
                for(VirtualView view: clients.values()){
                    try{view.updateNextTurn(this.turnPlayer);}
                    catch(Exception e){System.out.println("Error during remote method invocation on client");}
                }
                return;
            }
            if (current.equals(turnPlayer)) {
                found = true;
            }
        }
        //if turnPlayer is the last player in position order, turns are updated and the leader nickname is assigned to turnPlayer
        updateTurns();
    }
    //returns true if a player is the last one for the current game turn
    public boolean isLastInTurn(String nickname){
        String lastPlayer = null;

        for (String k : playersPos.keySet()) {
            lastPlayer = k;
        }

        return nickname.equals(lastPlayer);
    }
    //sets turnPlayer to a specific player's nickname
    public void setTurnPlayer(String nickname){
        this.turnPlayer = nickname;
    }
    //this method is invoked when a player wants to leave the game
    public void quitGame(String nickname) throws InvalidActionException {
        if(state != State.CARD_PICKING && state != State.CARD_SOLVING){
            throw new InvalidActionException("Invalid action");
        }
        //if the player quitting is the player in turn, we update the turns
        if(nickname.equals(turnPlayer)){
            nextTurn();
            //this condition must be checked because after the turn update the quitting player could
            //have become the leader
            if(nickname.equals(turnPlayer)){
                nextTurn();
                //if this condition is satisfied it means that the quitting player is the only player left,
                //so the game must end
                if(nickname.equals(turnPlayer)){
                    computeTotalRewards();
                    state = State.END;
                }
            }
        }

        playersPos.remove(nickname);
        playersPlay.get(nickname).quitGame();

        for(VirtualView view: clients.values()){
            try{view.updatePlayerQuit(nickname);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
    }
    //invoked when the leader draws a new card from the deck (during the game), which must be solved
    public void pickNextCard(String nickname) throws InvalidActionException {
        if(state != State.CARD_PICKING){
            throw new InvalidActionException("Can't pick a new card");
        }
        if(!nickname.equals(turnPlayer)){
            throw new InvalidActionException("Only leader can pick cards");
        }
        try{
            currentCard = gameDeck.drawCard();
            state = State.CARD_SOLVING;
            currentCard.specialEffect(this);

            for(VirtualView view: clients.values()){
                try{view.updateCardSolving(currentCard.getImageID());}
                catch(Exception e){System.out.println("Error during remote method invocation on client");}
            }
        }
        catch (EmptyDeckException e) {
            computeTotalRewards();
            state = State.END;
        }
    }
    //removes a member (human or alien) from each cabin (of a player's ship board) that is directly
    //connected with another busy cabin
    public void epidemicEffect(String nickname) throws InvalidActionException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        playersPlay.get(nickname).epidemicEffect();
    }
    //this method is invoked when a player has/wants to remove crew members from its ship board
    public void removeCrewMembers(String nickname, List<Integer> x, List<Integer> y, List<Integer> crewInEachCabin, int numberCrewToRemove) {
        playersPlay.get(nickname).removeCrewMembers(x, y, crewInEachCabin, numberCrewToRemove);
    }
    //updates the cosmic credits of a player
    public void updatePlayerCredits(String nickname, int update) {
        playersPlay.get(nickname).updateCredits(update);

        for(VirtualView view: clients.values()){
            try{view.updatePlayerCredits(nickname, update);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
    }
    //updates the position of a player on the ship board
    public void changePlayerPosition(String nickname, int cells) {
        playersPos.get(nickname).changePosition(cells);

        Position position = playersPos.get(nickname);
        for(VirtualView view: clients.values()){
            try{view.updatePlayerPosition(nickname, position.getLap(), position.getCell());}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
    }
    //invoked when a meteor hits a player's ship board
    public void meteorAttack(String nickname, Meteor meteor, int direction, boolean activateShield, boolean activateCannon) throws InvalidActionException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        playersPlay.get(nickname).meteorAttack(meteor, direction, activateShield, activateCannon);
    }
    //invoked when a cannon shot hits a player's ship board
    public void cannonFireAttack(String nickname, CannonShot cannonFire, int direction, boolean activateShield) throws InvalidActionException{
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        playersPlay.get(nickname).cannonFireAttack(cannonFire, direction, activateShield);
    }

    //ACTIONS THAT A PLAYER CAN PERFORM TO SOLVE A CARD

    //invoked when a player decides to land on a planet in order to gain goods
    public void planetLanding(String nickname, int numberPlanet) throws InvalidActionException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        if(!nickname.equals(turnPlayer)){
            throw new InvalidActionException("Wait for the turn");
        }
        currentCard.planetLanding(this, nickname, numberPlanet);
    }
   //invoked when a player need to switch his goods in Planets effect
    public void switchGoods(String nickname,int cargo_row, int cargo_col, Color good, int pos) throws InvalidActionException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        if(!nickname.equals(turnPlayer)){
            throw new InvalidActionException("Wait for the turn");
        }
        currentCard.switchGoods(this, nickname,cargo_row,cargo_col,good,pos);
    }
    //invoked when a player's ship has to be hit by a meteor/cannon shot; the player can decide whether to
    //activate a shield or a cannon to defend its ship
    public void hit(String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws InvalidActionException, NoBatteriesException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        if(!nickname.equals(turnPlayer)){
            throw new InvalidActionException("Wait for the turn");
        }
        currentCard.hitShip(this, nickname, diceResult, activateShield, activateCannon);
    }
    //invoked when a player decides to land on an abandoned station/ship
    public void landing(String nickname, List<Integer> x, List<Integer> y, List<Integer> z) throws InvalidActionException, NoCrewException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        if(!nickname.equals(turnPlayer)){
            throw new InvalidActionException("Wait for the turn");
        }
        currentCard.landing(this, nickname, x, y, z);
    }
    //invoked when a player wants to defeat an enemy; the player can decide whether to lose flight days
    //to gain credits/goods or not
    public void defeat(String nickname, int usedBatteries, boolean loseDays) throws InvalidActionException, NoBatteriesException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        if(!nickname.equals(turnPlayer)){
            throw new InvalidActionException("Wait for the turn");
        }
        currentCard.defeat(this, nickname, usedBatteries, loseDays);
    }
    //invoked when a player wants to fly across the flight board exploiting its engine strength
    public void fly(String nickname, int usedBatteries) throws InvalidActionException, NoBatteriesException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        if(!nickname.equals(turnPlayer)){
            throw new InvalidActionException("Wait for the turn");
        }
        currentCard.fly(this, nickname, usedBatteries);
    }
    //invoked when a player wants to use batteries to have an advantage while solving a card
    public void useBatteries(String nickname, int usedBatteries) throws InvalidActionException, NoBatteriesException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        if(!nickname.equals(turnPlayer)){
            throw new InvalidActionException("Wait for the turn");
        }
        currentCard.useBatteries(this, nickname, usedBatteries);
    }
    //invoked when a player doesn't want to exploit the benefits of a card and therefore skips the turn
    public void skip(String nickname) throws InvalidActionException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        if(!nickname.equals(turnPlayer)){
            throw new InvalidActionException("Wait for the turn");
        }
        currentCard.skip(this, nickname);
    }

    //
    //GAME OVER
    //


    //invoked when the game is over (all players have abandoned or the adventure card deck is empty) in order
    //to compute the final amount of cosmic credits for each player, including final rewards and penalties
    public void computeTotalRewards(){
        finishOrderReward();
        bestShipReward();
        saleOfGoodsReward();
        lossPenalty();
    }
    //assigns rewards to players based on their final order on the flight board
    public void finishOrderReward(){
        List<Integer> rewards;
        if(firstFlight){
            rewards = new ArrayList<>(Arrays.asList(4,3,2,1));
        }
        else{
            rewards = new ArrayList<>(Arrays.asList(8,6,4,2));
        }
        int i = 0;
        for(String nickname: playersPos.keySet()){
            updatePlayerCredits(nickname, rewards.get(i));
            i++;
        }
    }
    //assigns a reward to the player with the best ship, i.e. the one with the lowest number of exposed connectors
    public void bestShipReward(){
        String bestShipPlayer = "";
        int minExposedConnectors = 1000;
        int exposedConnectorsCount;
        for(String nickname: playersPlay.keySet()){
            exposedConnectorsCount = countExposedConnectors(nickname);
            if(exposedConnectorsCount < minExposedConnectors){
                bestShipPlayer = nickname;
                minExposedConnectors = exposedConnectorsCount;
            }
        }
        if(firstFlight){
            updatePlayerCredits(bestShipPlayer, 2);
        }
        else{
            updatePlayerCredits(bestShipPlayer, 4);
        }
    }
    //assigns a penalty to each player based on the number of components lost during the game
    public void lossPenalty(){
        for(String nickname: playersPlay.keySet()){
            updatePlayerCredits(nickname, -playersPlay.get(nickname).getLostComponents());
        }
    }
    //assigns to each player a reward based on the number and type of goods carried by their ships
    public void saleOfGoodsReward(){
        for(String nickname: playersPlay.keySet()){
            if(hasAbandoned(nickname)){
                updatePlayerCredits(nickname, Math.abs(playersPlay.get(nickname).getGoodsPrice()/2));
            }
            else{
                updatePlayerCredits(nickname, playersPlay.get(nickname).getGoodsPrice());
            }
        }
    }







 }

