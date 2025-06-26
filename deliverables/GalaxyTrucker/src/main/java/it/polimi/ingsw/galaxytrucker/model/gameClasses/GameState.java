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
    private final Map<String, VirtualView> clients = new HashMap<>(); //list of observers (clients of the game)
    private Hourglass hourglass;        //hourglass used during the assembling phase
    private Runnable endGameManagement;   //implements a callback procedure to remove the controller after the end of the game

    public GameState(boolean firstFlight, int numPlayers) {     //constructor, creates the deck(s) of cards and instantiates the components
        this.firstFlight = firstFlight;
        this.playersPos = new LinkedHashMap<>();
        this.playersPlay = new HashMap<>();
        this.turnPlayer = "";
        this.numPlayers = numPlayers;
        this.gameDeck = null;
        this.hourglass = new Hourglass(120, this);
        this.state = State.WAITING_FOR_PLAYERS;
    }

    //
     // 1) EXTRA METHODS NEEDED FOR TESTING
    //

    //[method for testing] assembles a custom component in the specified position
    public void assembleComponent(String nickname, Component component, int x, int y){
        playersPlay.get(nickname).assembleComponent(component, x, y);
    }
    //[method for testing] set the game deck with a custom one
    public void setGameDeck(Deck deck){
        gameDeck = deck;
    }
    //[method for testing] set the current picked card with a custom one
    public void pickGivenCard(EventCard eventCardIn) throws InvalidActionException{
        if(state != State.CARD_PICKING){
            throw new InvalidActionException("Can't pick a new card");
        }
        try{
            currentCard = eventCardIn;
            setGameState(State.CARD_SOLVING);
            currentCard.specialEffect(this);
        }
        catch (EmptyDeckException e) {
            computeTotalRewards();
            setGameState(State.END);
        }
    }

    //
    // 2) GETTERS (some of them also modify the model)
    //

    //returns the state of the game
    public State getGameState() {
        return this.state;
    }
    //returns a copy of the nickname-position mapping
    public Map<String,Position> getPlayersPos() { //return a copy of the player postion map
        if(this.playersPos == null) { return null;}
        return new LinkedHashMap<>(playersPos);
    }
    //returns the nickname of the current player in turn
    public String getTurnPlayer() {
        return turnPlayer;
    }
    //returns true if a player is the last one for the current game turn
    public boolean isLastInTurn(String nickname){
        String lastPlayer = null;

        for (String k : playersPos.keySet()) {
            lastPlayer = k;
        }

        return nickname.equals(lastPlayer);
    }
    //returns a copy of the nickname-player mapping
    public Map<String,Player> getPlayersPlay() { // return a copy of the player nickanme map
        if(this.playersPlay == null) { return null;}
        return new HashMap<>(playersPlay);
    }
    //returns the number of current active players in the game
    public int getCurrentPlayers(){return playersPlay.size();}
    //returns the list of player nicknames (in position order)
    public List<String> getNicknames(){
        return new ArrayList<>(playersPos.keySet());
    }
    //determines whether a player has abandoned the game
    public boolean hasAbandoned(String nickname){
        return playersPlay.get(nickname).hasAbandoned();
    }
    //determines whether ha player is correctly positioned on the flight board
    public boolean isPositioned(String nickname){
        return playersPos.get(nickname) != null;
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
    //returns the cannon strength of a player, removing the given batteries from its ship board in order
    // to activate double cannons
    public double getCannonStrength(String nickname, int usedBatteries){
        return playersPlay.get(nickname).getCannonStrength(usedBatteries);
    }
    //returns the engine strength of a player, removing the given batteries from its ship board in order
    // to activate double engines
    public int getEngineStrength(String nickname, int usedBatteries){
        return playersPlay.get(nickname).getEngineStrength(usedBatteries);
    }
    //returns the cell numbers of the busy (taken by the players) cells on the flight board
    public List<Integer> getBusyCells(){
        List<Integer> busyCells = new ArrayList<>();
        for(Position p : playersPos.values()){
            busyCells.add(p.getCell());
        }
        return busyCells;
    }


    //
     // 3) SETTERS
    //



     //STARTING PHASE


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
            gameDeckCards.add(new Smugglers(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.BLUE)),2,4,1,8005));
            gameDeckCards.add(new AbandonedStation(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN)), 5, 1, 2001));
            gameDeckCards.add(new CombatZone(true, 3001));
            gameDeckCards.add(new MeteorsSwarm(new ArrayList<>(Arrays.asList(new Meteor(true, Orientation.NORTH), new Meteor(false, Orientation.WEST), new Meteor(false, Orientation.EAST))), 5001));
            gameDeckCards.add(new OpenSpace(6001));
            gameDeckCards.add(new Planets(new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(Color.RED, Color.RED)), new ArrayList<>(Arrays.asList(Color.RED, Color.BLUE, Color.BLUE)), new ArrayList<>(Arrays.asList(Color.YELLOW)))),2, 7002));
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
        //nickname and color of the player must be unique
        for(String n : getNicknames()) {
            if(n.equals(nickname)) {
                throw new UniqueNicknameException("Nickname already taken");
            }
            if(color == getColor(n)){
                throw new UniquePlayerColorException("Color already taken");
            }
        }
        //the new player is added to the game and its position on the flight board is set to null
        playersPlay.put(nickname, new Player(nickname, color, firstFlight));
        playersPos.put(nickname, null);

        //the listeners stored in the game state and in the ship board of the current players are updated
        for(String n: clients.keySet()){
            playersPlay.get(nickname).addListener(n, clients.get(n));
        }
        for(Player p: playersPlay.values()){
            p.addListener(nickname, client);
        }
        clients.put(nickname, client);
        //listeners are updated about the fact that a new player has entered the game
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
        }
    }
    //invoked when one of the players decides to start the assembling phase
    public void startAssembling() {
        createComponents(firstFlight);
        createDecks(firstFlight);
        if(!firstFlight){
            startNewCycle();
        }
        setGameState(State.SHIP_BUILDING);
    }


     //ASSEMBLING PHASE


    //invoked when a player wants to pick a component among the one placed face down (assembling phase)
    public void pickHidden(String nickname) throws PickedComponentException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Wait for assembling phase");
        }
        if(isPositioned(nickname)){
            throw new InvalidActionException("You have already finished assembling");
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
        if(isPositioned(nickname)){
            throw new InvalidActionException("You have already finished assembling");
        }
        if(index < 0 || index >= hiddenComponents.size()){
            throw new InvalidActionException("Impossible to pick the selected component");
        }

        Component c = shownComponents.get(index);
        playersPlay.get(nickname).pickComponent(c);
        shownComponents.remove(index);

        for(VirtualView view: clients.values()){
            try{view.updateShownComponent(c.getImageID(), false);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
        try{clients.get(nickname).updatePickedComponent(c.getImageID(), false);}
        catch(Exception e){System.out.println("Error during remote method invocation on client");}
    }
    //invoked when a player wants to reserve the component that it has picked for its ship board
    public void reserveComponent(String nickname) throws PickedComponentException, ReservedComponentException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Wait for assembling phase");
        }
        if(firstFlight){
            throw new InvalidActionException("Invalid action for first flight game");
        }
        if(isPositioned(nickname)){
            throw new InvalidActionException("You have already finished assembling");
        }

        Component c = playersPlay.get(nickname).reserveComponent();

        for(VirtualView view: clients.values()){
            try{view.updateReservedComponent(nickname, c.getImageID(), true);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
        try{clients.get(nickname).updatePickedComponent(c.getImageID(), true);}
        catch(Exception e){System.out.println("Error during remote method invocation on client");}
    }
    //invoked when a player wants to pick one of the components that it has reserved for its ship board
    public void pickReservedComponent(String nickname, int position) throws ReservedComponentException, PickedComponentException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Wait for assembling phase");
        }
        if(firstFlight){
            throw new InvalidActionException("Invalid action for first flight game");
        }
        if(isPositioned(nickname)){
            throw new InvalidActionException("You have already finished assembling");
        }
        if(position < 0 || position >= playersPlay.get(nickname).getNumberReservedComponents()){
            throw new InvalidActionException("No reserved component in this position");
        }

        Component c = playersPlay.get(nickname).pickReservedComponent(position);

        for(VirtualView view: clients.values()){
            try{view.updateReservedComponent(nickname, c.getImageID(), false);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
        try{clients.get(nickname).updatePickedComponent(c.getImageID(), false);}
        catch(Exception e){System.out.println("Error during remote method invocation on client");}
    }
    /**
     * Handles the release of a picked component by a player during the ship building phase.
     * The component is placed face up and made visible to all players.
     *
     * @param nickname the nickname of the player releasing the component
     * @throws PickedComponentException if the player has no component to release
     * @throws InvalidActionException if the game is not in ship building phase or if the player has already finished assembling
     */
    public void putShown(String nickname) throws PickedComponentException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Wait for assembling phase");
        }
        if(isPositioned(nickname)){
            throw new InvalidActionException("You have already finished assembling");
        }

        Component c = playersPlay.get(nickname).releaseComponent();
        c.setOrientation();
        shownComponents.add(c);

        for(VirtualView view: clients.values()){
            try{view.updateShownComponent(c.getImageID(), true);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
        try{clients.get(nickname).updatePickedComponent(c.getImageID(), true);}
        catch(Exception e){System.out.println("Error during remote method invocation on client");}
    }

    /**
     * Assembles a picked component onto the player's ship board at the specified coordinates.
     * Updates all clients with the assembled component information.
     *
     * @param nickname the nickname of the player assembling the component
     * @param x the x-coordinate where to place the component
     * @param y the y-coordinate where to place the component
     * @throws AssembledComponentException if there's an issue with component assembly
     * @throws PickedComponentException if the player has no component to assemble
     * @throws InvalidActionException if the game is not in ship building phase or if the player has already finished assembling
     */
    public void assembleComponent(String nickname, int x, int y) throws AssembledComponentException, PickedComponentException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Wait for assembling phase");
        }
        if(isPositioned(nickname)){
            throw new InvalidActionException("You have already finished assembling");
        }

        Component c = playersPlay.get(nickname).assembleComponent(x,y);

        for(VirtualView view: clients.values()){
            try{view.updateAssembledComponent(nickname, c.getImageID(), c.getOrientation(), x, y);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
        try{clients.get(nickname).updatePickedComponent(c.getImageID(), true);}
        catch(Exception e){System.out.println("Error during remote method invocation on client");}
    }

    /**
     * Rotates the currently picked component of a player by changing its orientation.
     *
     * @param nickname the nickname of the player rotating the component
     * @throws InvalidActionException if the game is not in ship building phase or if the player has already finished assembling
     * @throws PickedComponentException if the player has no component to rotate
     */
    public void rotatePickedComponent(String nickname) throws InvalidActionException, PickedComponentException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Wait for assembling phase");
        }
        if(isPositioned(nickname)){
            throw new InvalidActionException("You have already finished assembling");
        }

        playersPlay.get(nickname).rotatePickedComponent();

        try{clients.get(nickname).updateRotatePickedComponent();}
        catch(Exception e){System.out.println("Error during remote method invocation on client");}
    }

    /**
     * Allows a player to pick a deck during the assembling phase to view its contents.
     * Only available in non-first flight games.
     *
     * @param nickname the nickname of the player picking the deck
     * @param deckNumber the number of the deck to pick (must be valid and not already picked)
     * @throws PickedDeckException if the deck is already picked by another player
     * @throws InvalidActionException if the game is not in ship building phase, if it's a first flight game,
     *                               if the player has already finished assembling, or if the deck number is invalid
     */
    public void pickDeck(String nickname, int deckNumber) throws PickedDeckException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Wait for assembling phase");
        }
        if(firstFlight){
            throw new InvalidActionException("Invalid action for first flight game");
        }
        if(isPositioned(nickname)){
            throw new InvalidActionException("You have already finished assembling");
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

    /**
     * Releases a previously picked deck, making it available for other players to pick.
     * Only available in non-first flight games.
     *
     * @param nickname the nickname of the player releasing the deck
     * @throws InvalidActionException if the game is not in ship building phase, if it's a first flight game,
     *                               or if the player has already finished assembling
     * @throws PickedDeckException if the player has no deck to release
     */
    public void releaseDeck(String nickname) throws InvalidActionException, PickedDeckException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Wait for assembling phase");
        }
        if(firstFlight){
            throw new InvalidActionException("Invalid action for first flight game");
        }
        if(isPositioned(nickname)){
            throw new InvalidActionException("You have already finished assembling");
        }
        int releasedDeckNumber = playersPlay.get(nickname).releaseDeck();
        decks.get(releasedDeckNumber).setNotPicked();

        try{clients.get(nickname).updateReleasedDeck();}
        catch(Exception e){System.out.println("Error during remote method invocation on client");}
    }

    /**
     * Sets the starting position of a player on the flight board after they finish assembling their ship.
     * Validates the position based on game level and availability.
     *
     * @param nickname the nickname of the player setting their position
     * @param initCell the initial cell number on the flight board
     * @throws InvalidPositionException if the starting position is invalid or already taken
     * @throws InvalidActionException if the game is not in the appropriate phase or if the player has already set their position
     */
    public void setPosition(String nickname, int initCell) throws InvalidPositionException, InvalidActionException {
        if(state != State.SHIP_BUILDING && state != State.SHIP_PLACEMENT){
            throw new InvalidActionException("Wait for assembling phase");
        }
        if(isPositioned(nickname)){
            throw new InvalidActionException("You have already finished assembling");
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
            setGameState(State.SHIP_CONTROL);
            checkShipBoards();
        }
    }

    /**
     * Starts a new cycle by turning the hourglass. Can be invoked by a player who has finished assembling
     * during the ship building phase. Only available in non-first flight games.
     *
     * @param nickname the nickname of the player starting the new cycle
     * @throws InvalidActionException if the player hasn't finished assembling, if the game is not in ship building phase,
     *                               or if it's a first flight game
     * @throws HourGlassException if there's an issue with the hourglass mechanism
     */
    public void startNewCycle(String nickname) throws InvalidActionException, HourGlassException{
        if (!isPositioned(nickname)) {
            throw new InvalidActionException("First finish assembling!!!");
        } else if (state != State.SHIP_BUILDING) {
            throw new InvalidActionException("Wait for assembling phase");
        } else if (firstFlight) {
            throw new InvalidActionException("Invalid action for first flight game");
        } else{
            hourglass.startNewCycle();

            for(VirtualView view: clients.values()){
                try{view.updateStartNewCycle();}
                catch(Exception e){System.out.println("Error during remote method invocation on client");}
            }
        }
    }

    /**
     * Starts a new cycle automatically when the assembling phase begins.
     * This method turns the hourglass and notifies all clients.
     */
    public void startNewCycle(){
        hourglass.startNewCycle();

        for(VirtualView view: clients.values()){
            try{view.updateStartNewCycle();}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
    }

    /**
     * Called when the hourglass has finished running to notify all clients.
     */
    public void finishedCycle() {
        for(VirtualView view: clients.values()){
            try{view.updateFinishedCycle();}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
    }


    //SHIP CONTROL/REPAIR PHASE


    /**
     * Destroys a component at the specified coordinates on a player's ship board.
     * Can be used during ship control phase or ship repair phase.
     *
     * @param nickname the nickname of the player whose component is being destroyed
     * @param x the x-coordinate of the component to destroy
     * @param y the y-coordinate of the component to destroy
     * @throws AssembledComponentException if there's no component at the specified coordinates
     * @throws InvalidActionException if the game is not in control or repair phase
     */
    public void destroyComponent(String nickname, int x, int y) throws AssembledComponentException, InvalidActionException {
        if(state == State.SHIP_CONTROL){
            playersPlay.get(nickname).destroyComponent(x,y);
            checkShipBoards();
        }else if(state == State.SHIP_REPAIR){
            playersPlay.get(nickname).destroyComponent(x,y);
            checkDamages();
        }else {
            throw new InvalidActionException("Wait for control or repair phase");
        }
    }

    /**
     * Initializes a cabin component with 2 human crew members.
     * Only available during the ship control phase.
     *
     * @param nickname the nickname of the player adding crew
     * @param x the x-coordinate of the cabin
     * @param y the y-coordinate of the cabin
     * @throws AssembledComponentException if there's no cabin component at the specified coordinates
     * @throws FullCabinException if the cabin is already full
     * @throws InvalidActionException if the game is not in ship control phase
     */
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

    /**
     * Initializes a battery container with batteries.
     * Only available during the ship control phase.
     *
     * @param nickname the nickname of the player adding batteries
     * @param x the x-coordinate of the battery container
     * @param y the y-coordinate of the battery container
     * @throws AssembledComponentException if there's no battery container at the specified coordinates
     * @throws NoBatteriesException if there are no batteries available to add
     * @throws InvalidActionException if the game is not in ship control phase
     */
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

    /**
     * Initializes a cabin component with an alien crew member.
     * Only available during the ship control phase in non-first flight games.
     *
     * @param nickname the nickname of the player adding the alien
     * @param isPurple true if the alien is purple, false otherwise
     * @param x the x-coordinate of the cabin
     * @param y the y-coordinate of the cabin
     * @throws AssembledComponentException if there's no cabin component at the specified coordinates
     * @throws FullCabinException if the cabin is already full
     * @throws InvalidActionException if the game is not in ship control phase or if it's a first flight game
     */
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

    /**
     * Checks the correctness of all ships in the game to determine if the flight phase can start.
     * Ships must be well assembled with all cabins full of crew and all containers full of batteries.
     * If all ships are correct, transitions to the card picking phase.
     */

    public void checkShipBoards(){
        boolean correctShips = true;
        for(String nickname : playersPos.keySet()) {
            if(!playersPlay.get(nickname).hasCorrectShipBoard() || !playersPlay.get(nickname).hasAllCabinsBatteriesFull()){
                correctShips = false;
                break;
            }
        }
        if(correctShips){
            setGameState(State.CARD_PICKING);
            if(gameDeck==null){
                createGameDeck();
            }
            updateTurns();
        }
    }

    /**
     * Creates the main game deck by unifying and shuffling the 4 decks used during the assembling phase.
     * This method is invoked after the assembling phase when all ships are ready.
     */

    public void createGameDeck() {
        List<EventCard> gameDeckCards = new ArrayList<>();
        for (Deck deck : decks) {
            gameDeckCards.addAll(deck.getCards());
        }
        gameDeck = new Deck(gameDeckCards);
        gameDeck.shuffle();
    }


    //FLIGHT PHASE



    /**
     * Changes the state of the game and notifies all clients.
     * Handles special cleanup when transitioning to the END state.
     *
     * @param state the new game state to set
     */
    public void setGameState(State state) {
        this.state = state;

        for(VirtualView view: clients.values()){
            try{
                if(state==State.SHIP_BUILDING){
                    view.updateStartAssembling();
                }
                else if(state==State.SHIP_PLACEMENT){
                    view.updateShipPlacement();
                }
                else if(state==State.SHIP_CONTROL){
                    view.updateShipControl();
                }
                else if(state==State.CARD_PICKING){
                    view.updateCardPicking();
                }
                else if(state==State.CARD_SOLVING){
                    view.updateCardSolving(currentCard.getImageID());
                }
                else if(state==State.END){
                    view.updateEndGame();
                }
                else{
                    view.notifyError("Ambiguous game state change");
                }
            }
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
        if(state==State.END){
            clients.clear();
            cancelGame();
        }
    }

    /**
     * Sets the game to ship repair phase for a specific player whose ship has been damaged.
     *
     * @param nickname the nickname of the player who needs to repair their ship
     */
    public void setShipRepair(String nickname){
        this.state = State.SHIP_REPAIR;

        for(VirtualView view: clients.values()){
            try{view.updateShipRepair(nickname);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
    }

    /**
     * Checks if any ship boards have been damaged and need repair after a card has been solved.
     * If damages are found, sets the game to repair phase; otherwise, returns to card picking phase.
     */

    public void checkDamages(){
        boolean correctShips = true;
        for(String nickname : playersPos.keySet()) {
            if(!playersPlay.get(nickname).hasCorrectShipBoard()){
                correctShips = false;
                setShipRepair(nickname);
                break;
            }
        }
        if(correctShips){
            setGameState(State.CARD_PICKING);
        }
    }

    /**
     * Orders players by their position on the flight board (descending order) and assigns
     * the leader's nickname to turnPlayer. Updates all clients with the new turn order.
     */
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

    /**
     * Updates turnPlayer to the next player in the turn order.
     * If the current player is the last in order, calls updateTurns() to restart from the leader.
     */
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

    /**
     * Sets the turn player to a specific player's nickname and notifies all clients.
     *
     * @param nickname the nickname of the player whose turn it should be
     */
    //sets turnPlayer to a specific player's nickname
    public void setTurnPlayer(String nickname){
        this.turnPlayer = nickname;

        for(VirtualView view: clients.values()){
            try{view.updateNextTurn(this.turnPlayer);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
    }

    /**
     * Handles forced disconnection of a player from the game.
     * Manages different scenarios based on the current game state.
     *
     * @param nickname the nickname of the player being forcibly disconnected
     * @throws InvalidActionException if there's an issue processing the disconnection
     */
    public void forceQuit(String nickname) throws InvalidActionException{
        clients.remove(nickname);

        if(state == State.CARD_PICKING || state == State.CARD_SOLVING){
            if(state==State.CARD_SOLVING){
                this.currentCard.manageGameQuit(this, nickname);
            }
            quitGame(nickname, false);
        }else{
            playersPos.remove(nickname);
            playersPlay.get(nickname).quitGame();

            if(state == State.SHIP_CONTROL){
                checkShipBoards();
            }

            for(VirtualView view: clients.values()){
                try{view.updatePlayerQuit(nickname);}
                catch(Exception e){System.out.println("Error during remote method invocation on client");}
            }
        }

        for(VirtualView view: clients.values()){
            try{view.notifyError("Player " + nickname + " disconnected");}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }

        if(playersPos.isEmpty()){
            setGameState(State.END);
        }
    }

    /**
     * Handles a player's request to leave the game or forced removal.
     * Updates turn order and checks for game end conditions.
     *
     * @param nickname the nickname of the player quitting
     * @param playerDecision true if the player chose to quit, false if forced to quit
     * @throws InvalidActionException if the player cannot quit in the current phase
     */
    public void quitGame(String nickname, boolean playerDecision) throws InvalidActionException {
        if(state != State.CARD_PICKING && state != State.CARD_SOLVING){
            throw new InvalidActionException("Can't leave the game in this phase");
        }
        if(state == State.CARD_SOLVING && playerDecision){
            throw new InvalidActionException("Can't leave the game in this phase");
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
                    playersPos.remove(nickname);
                    playersPlay.get(nickname).quitGame();
                    computeTotalRewards();
                    setGameState(State.END);
                }
            }
        }

        if(state!=State.END){
            playersPos.remove(nickname);
            playersPlay.get(nickname).quitGame();
        }

        for(VirtualView view: clients.values()){
            try{view.updatePlayerQuit(nickname);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
    }

    /**
     * Allows the leader to draw a new card from the deck during the card picking phase.
     * Transitions to card solving phase and applies the card's special effect.
     *
     * @param nickname the nickname of the player (must be the leader) picking the card
     * @throws InvalidActionException if the game is not in card picking phase or if the player is not the leader
     */
    public void pickNextCard(String nickname) throws InvalidActionException {
        if(state != State.CARD_PICKING){
            throw new InvalidActionException("Can't pick a new card");
        }
        if(!nickname.equals(turnPlayer)){
            throw new InvalidActionException("Only leader can pick cards");
        }
        try{
            currentCard = gameDeck.drawCard();
            setGameState(State.CARD_SOLVING);
            currentCard.specialEffect(this);
        }
        catch (EmptyDeckException e) {
            computeTotalRewards();
            setGameState(State.END);
        }
    }

    /**
     * Applies epidemic effect to a player's ship, removing crew members from cabins
     * that are directly connected to other busy cabins.
     *
     * @param nickname the nickname of the player affected by the epidemic
     * @throws InvalidActionException if no card has been picked yet
     */
    public void epidemicEffect(String nickname) throws InvalidActionException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        playersPlay.get(nickname).epidemicEffect();
    }

    /**
     * Removes specified crew members from a player's ship board.
     *
     * @param nickname the nickname of the player removing crew members
     * @param x list of x-coordinates of cabins
     * @param y list of y-coordinates of cabins
     * @param crewInEachCabin list of crew counts in each cabin
     * @param numberCrewToRemove total number of crew members to remove
     */
    public void removeCrewMembers(String nickname, List<Integer> x, List<Integer> y, List<Integer> crewInEachCabin, int numberCrewToRemove) {
        playersPlay.get(nickname).removeCrewMembers(x, y, crewInEachCabin, numberCrewToRemove);
    }

    /**
     * Updates the cosmic credits of a player and notifies all clients.
     *
     * @param nickname the nickname of the player whose credits are being updated
     * @param update the amount to add or subtract from current credits
     */
    public void updatePlayerCredits(String nickname, int update) {
        playersPlay.get(nickname).updateCredits(update);

        for(VirtualView view: clients.values()){
            try{view.updatePlayerCredits(nickname, update);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
    }

    /**
     * Changes a player's position on the flight board by the specified number of cells.
     *
     * @param nickname the nickname of the player moving
     * @param cells the number of cells to move (positive for forward, negative for backward)
     */
    public void changePlayerPosition(String nickname, int cells) {
        playersPos.get(nickname).changePosition(getBusyCells(), cells);

        Position position = playersPos.get(nickname);
        for(VirtualView view: clients.values()){
            try{view.updatePlayerPosition(nickname, position.getLap(), position.getCell());}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
    }

    /**
     * Handles a meteor attack on a player's ship. The player can choose to activate
     * shields or cannons for defense.
     *
     * @param nickname the nickname of the player being attacked
     * @param meteor the meteor object containing attack parameters
     * @param direction the direction of the meteor attack
     * @param activateShield true to activate shield defense
     * @param activateCannon true to activate cannon defense
     * @throws InvalidActionException if no card has been picked yet
     */

    public void meteorAttack(String nickname, Meteor meteor, int direction, boolean activateShield, boolean activateCannon) throws InvalidActionException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        playersPlay.get(nickname).meteorAttack(meteor, direction, activateShield, activateCannon);
    }
    /**
     * Handles a cannon fire attack on a player's ship. The player can choose to activate shields.
     *
     * @param nickname the nickname of the player being attacked
     * @param cannonFire the cannon shot object containing attack parameters
     * @param direction the direction of the cannon fire attack
     * @param activateShield true to activate shield defense
     * @throws InvalidActionException if no card has been picked yet
     */
    public void cannonFireAttack(String nickname, CannonShot cannonFire, int direction, boolean activateShield) throws InvalidActionException{
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        playersPlay.get(nickname).cannonFireAttack(cannonFire, direction, activateShield);
    }
    /**
     * Substitutes or adds a good in a specific container of a player's cargo hold.
     *
     * @param nickname the nickname of the player
     * @param cargo_row the row of the cargo container
     * @param cargo_col the column of the cargo container
     * @param good the color of the good to add/substitute
     * @param posInCargo the position within the cargo container
     * @throws FullCargoHoldException if the cargo hold is full
     * @throws UnsupportedCargoColorException if the cargo color is not supported
     */
    public void substituteGoods(String nickname, int cargo_row, int cargo_col, Color good, int posInCargo) throws FullCargoHoldException, UnsupportedCargoColorException{
        playersPlay.get(nickname).substituteGoods(cargo_row, cargo_col, good, posInCargo);
    }
    /**
     * Adds a set of goods to specific cargo holds of a player's ship board.
     *
     * @param nickname the nickname of the player
     * @param x list of x-coordinates of cargo holds
     * @param y list of y-coordinates of cargo holds
     * @param goods list of goods to load
     * @throws UnsupportedCargoColorException if any cargo color is not supported
     * @throws FullCargoHoldException if any cargo hold is full
     */
    public void loadGoods(String nickname, List<Integer> x, List<Integer> y, List<Color> goods) throws UnsupportedCargoColorException, FullCargoHoldException {
        playersPlay.get(nickname).loadGoods(x, y, goods);
    }

    /**
     * Removes the specified number of most precious goods from a player's ship board.
     *
     * @param nickname the nickname of the player losing goods
     * @param numberGoods the number of precious goods to remove
     */
    public void losePreciousGoods(String nickname, int numberGoods){
        playersPlay.get(nickname).losePreciousGoods(numberGoods);
    }


    //ACTIONS THAT A PLAYER CAN PERFORM TO SOLVE A CARD

    /**
     * Handles a player's decision to land on a planet to gain goods.
     * Only the player whose turn it is can perform this action.
     *
     * @param nickname the nickname of the player landing on the planet
     * @param numberPlanet the number/identifier of the planet to land on
     * @throws InvalidActionException if no card has been picked yet or if it's not the player's turn
     */
    public void planetLanding(String nickname, int numberPlanet) throws InvalidActionException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        if(!nickname.equals(turnPlayer)){
            throw new InvalidActionException("Wait for the turn");
        }
        currentCard.planetLanding(this, nickname, numberPlanet);
    }
    /**
     * Handles switching goods during planet effects.
     * Only the player whose turn it is can perform this action.
     *
     * @param nickname the nickname of the player switching goods
     * @param cargo_row the row of the cargo container
     * @param cargo_col the column of the cargo container
     * @param good the color of the good to switch
     * @param pos the position of the good in the cargo
     * @throws InvalidActionException if no card has been picked yet or if it's not the player's turn
     */
    public void switchGoods(String nickname,int cargo_row, int cargo_col, Color good, int pos) throws InvalidActionException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        if(!nickname.equals(turnPlayer)){
            throw new InvalidActionException("Wait for the turn");
        }
        currentCard.switchGoods(this, nickname,cargo_row,cargo_col,good,pos);
    }
    /**
     * Handles a hit on a player's ship (meteor or cannon shot). The player can decide
     * whether to activate shields or cannons for defense.
     *
     * @param nickname the nickname of the player being hit
     * @param diceResult the result of the dice roll determining hit severity
     * @param activateShield true to activate shield defense
     * @param activateCannon true to activate cannon defense
     * @throws InvalidActionException if no card has been picked yet or if it's not the player's turn
     * @throws NoBatteriesException if there are insufficient batteries to activate defenses
     */

    public void hit(String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws InvalidActionException, NoBatteriesException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        if(!nickname.equals(turnPlayer)){
            throw new InvalidActionException("Wait for the turn");
        }
        currentCard.hitShip(this, nickname, diceResult, activateShield, activateCannon);
    }
    /**
     * Handles a player's decision to land on an abandoned ship.
     * Requires crew members to perform the landing.
     *
     * @param nickname the nickname of the player landing on the abandoned ship
     * @param x list of x-coordinates for the landing action
     * @param y list of y-coordinates for the landing action
     * @param z list of additional parameters for the landing action
     * @throws InvalidActionException if no card has been picked yet or if it's not the player's turn
     * @throws NoCrewException if there are insufficient crew members for the landing
     */
    public void landing(String nickname, List<Integer> x, List<Integer> y, List<Integer> z) throws InvalidActionException, NoCrewException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        if(!nickname.equals(turnPlayer)){
            throw new InvalidActionException("Wait for the turn");
        }
        currentCard.landing(this, nickname, x, y, z);
    }
    /**
     * Handles a player's attempt to defeat an enemy. The player can choose to lose flight days
     * in exchange for credits or goods.
     *
     * @param nickname the nickname of the player defeating the enemy
     * @param usedBatteries the number of batteries used in the defeat attempt
     * @param loseDays true if the player chooses to lose flight days for additional rewards
     * @throws InvalidActionException if no card has been picked yet or if it's not the player's turn
     * @throws NoBatteriesException if there are insufficient batteries
     */

    public void defeat(String nickname, int usedBatteries, boolean loseDays) throws InvalidActionException, NoBatteriesException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        if(!nickname.equals(turnPlayer)){
            throw new InvalidActionException("Wait for the turn");
        }
        currentCard.defeat(this, nickname, usedBatteries, loseDays);
    }
    /**
     * Handles a player's decision to fly across the flight board using engine strength.
     *
     * @param nickname the nickname of the player flying
     * @param usedBatteries the number of batteries used to power the flight
     * @throws InvalidActionException if no card has been picked yet or if it's not the player's turn
     * @throws NoBatteriesException if there are insufficient batteries
     */
    public void fly(String nickname, int usedBatteries) throws InvalidActionException, NoBatteriesException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        if(!nickname.equals(turnPlayer)){
            throw new InvalidActionException("Wait for the turn");
        }
        currentCard.fly(this, nickname, usedBatteries);
    }
    /**
     * Allows a player to use batteries to gain an advantage while solving a card.
     *
     * @param nickname the nickname of the player using batteries
     * @param usedBatteries the number of batteries to use
     * @throws InvalidActionException if no card has been picked yet or if it's not the player's turn
     * @throws NoBatteriesException if there are insufficient batteries
     */
    public void useBatteries(String nickname, int usedBatteries) throws InvalidActionException, NoBatteriesException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        if(!nickname.equals(turnPlayer)){
            throw new InvalidActionException("Wait for the turn");
        }
        currentCard.useBatteries(this, nickname, usedBatteries);
    }
    /**
     * Allows a player to skip their turn without taking any action from the current card.
     *
     * @param nickname the nickname of the player skipping their turn
     * @throws InvalidActionException if no card has been picked yet or if it's not the player's turn
     */
    public void skip(String nickname) throws InvalidActionException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        if(!nickname.equals(turnPlayer)){
            throw new InvalidActionException("Wait for the turn");
        }
        currentCard.skip(this, nickname);
    }
    /**
     * Handles a player's decision to load goods into cargo hold components of their ship.
     *
     * @param nickname the nickname of the player loading goods
     * @param x list of x-coordinates of cargo holds
     * @param y list of y-coordinates of cargo holds
     * @throws InvalidActionException if no card has been picked yet or if it's not the player's turn
     * @throws UnsupportedCargoColorException if the cargo color is not supported
     * @throws FullCargoHoldException if the cargo hold is full
     * @throws NoGoodsException if there are no goods available to load
     */
    public void loadGoods(String nickname, List<Integer> x, List<Integer> y) throws InvalidActionException, UnsupportedCargoColorException, FullCargoHoldException, NoGoodsException {
        if(state != State.CARD_SOLVING){
            throw new InvalidActionException("Card must be picked first");
        }
        if(!nickname.equals(turnPlayer)){
            throw new InvalidActionException("Wait for the turn");
        }
        currentCard.loadGoods(this, nickname, x, y);
    }

    //
    // ENDING PHASE
    //


    /**
     * Computes the total rewards and penalties for all players when the game ends.
     * Includes finish order rewards, best ship rewards, goods sale rewards, and loss penalties.
     */
    public void computeTotalRewards(){
        finishOrderReward();
        bestShipReward();
        saleOfGoodsReward();
        lossPenalty();
    }
    /**
     * Assigns rewards to players based on their final position order on the flight board.
     * Earlier finishers receive higher rewards.
     */
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
    /**
     * Assigns a reward to the player with the best ship, determined by having the
     * lowest number of exposed connectors.
     */
    public void bestShipReward(){
        String bestShipPlayer = "";
        int minExposedConnectors = 1000;
        int exposedConnectorsCount;
        for(String nickname: playersPos.keySet()){
            exposedConnectorsCount = countExposedConnectors(nickname);
            if(exposedConnectorsCount < minExposedConnectors){
                bestShipPlayer = nickname;
                minExposedConnectors = exposedConnectorsCount;
            }
        }
        if(bestShipPlayer.equals("")){
            return;
        }
        if(firstFlight){
            updatePlayerCredits(bestShipPlayer, 2);
        }
        else{
            updatePlayerCredits(bestShipPlayer, 4);
        }
    }

    /**
     * Assigns penalties to each player based on the number of components they lost during the game.
     */
    public void lossPenalty(){
        for(String nickname: playersPlay.keySet()){
            updatePlayerCredits(nickname, -playersPlay.get(nickname).getLostComponents());
        }
    }
    /**
     * Assigns rewards to each player based on the number and type of goods they are carrying.
     * Players who abandoned the game receive half the normal reward.
     */

    public void saleOfGoodsReward(){
        for(String nickname: playersPlay.keySet()){
            if(hasAbandoned(nickname)){
                updatePlayerCredits(nickname, (int) Math.ceil(playersPlay.get(nickname).getGoodsPrice() / 2));
            }
            else{
                updatePlayerCredits(nickname, playersPlay.get(nickname).getGoodsPrice());
            }
        }
    }
    /**
     * Sets the callback procedure to be executed when the game ends.
     *
     * @param endGameManagement the Runnable to execute for end-game cleanup
     */

    public void setEndGameManagement(Runnable endGameManagement) {
        this.endGameManagement = endGameManagement;
    }
    /**
     * Executes the callback procedure to remove the controller from the server's list
     * and perform other end-game cleanup.
     */
    public void cancelGame(){
        this.endGameManagement.run();
    }
}

