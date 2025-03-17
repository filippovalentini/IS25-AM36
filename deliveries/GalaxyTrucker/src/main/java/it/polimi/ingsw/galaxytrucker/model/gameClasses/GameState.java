package it.polimi.ingsw.galaxytrucker.model.gameClasses;

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
    private EventCard currentCard;
    private List<Component> hiddenComponents;       //components turned face down during the assembling phase
    private List<Component> shownComponents;        //components turned face up during the assembling phase
    private State state;


    public GameState(boolean firstFlight, int numPlayers) {     //constructor, creates the deck(s) of cards and instantiates the components
        this.firstFlight = firstFlight;
        this.playersPos = new LinkedHashMap<>();
        this.playersPlay = new HashMap<>();
        this.numPlayers = numPlayers;
        this.state = State.WAITING_FOR_PLAYERS;
    }
    //
    // SET GAME STATE
    //
    public void setGameState(State state) {
        this.state = state;
    }
    //
    // GET GAME State
    //
    public State getGameState() {
        return this.state;
    }
    //
    //GETTERS
    //
    public Map<String,Position> getPlayersPos() { //return a copy of the player postion map
        if(this.playersPos == null) { return null;}
        Map<String,Position> retPlayerPos = new HashMap<>(playersPos);
        return retPlayerPos;
    }
    public Map<String,Player> getPlayersPlay() { // return a copy of the player nickanme map
        if(this.playersPlay == null) { return null;}
        Map<String,Player> retPlayersPlay = new HashMap<>(playersPlay);
        return retPlayersPlay;
    }
    public int getCurrentPlayers(){return playersPlay.size();}

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
    public void addPlayer(String nickname, Color color) throws UniqueNicknameException, UniquePlayerColorException, InvalidActionException {
        if(state != State.WAITING_FOR_PLAYERS){
            throw new InvalidActionException("Game has already been started");
        }
        for(String n : playersPlay.keySet()) {
            if(n.equals(nickname)) {
                throw new UniqueNicknameException("Nickname already taken");
            }
        }
        for(Player p : playersPlay.values()){
            if(color == p.getShipBoard().getColor()){
                throw new UniquePlayerColorException("Color already taken");
            }
        }
        playersPlay.put(nickname, new Player(nickname, color, firstFlight));
        playersPos.put(nickname, null);
        if(numPlayers == getCurrentPlayers()){
            state = State.SHIP_BUILDING;
            startAssembling();
        }
    }

    //invoked when one of the players decides to start the assembling phase
    public void startAssembling() {
        createComponents(firstFlight);
        createDecks(firstFlight);
    }

    //
     //ASSEMBLING PHASE
    //

    //invoked when a player wants to pick a component among the one placed face down (assembling phase)
    public void pickHidden(String nickname) throws PickedComponentException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Assembling phase is finished");
        }
        Collections.shuffle(hiddenComponents);
        Component c = hiddenComponents.removeFirst();
        playersPlay.get(nickname).getShipBoard().pickComponent(c);
    }
    //invoked when a player wants to pick a specific component among the one placed face up (assembling phase)
    public void pickShown(String nickname, int index) throws PickedComponentException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Assembling phase is finished");
        }
        Component c = shownComponents.remove(index);
        playersPlay.get(nickname).getShipBoard().pickComponent(c);
    }
    //invoked when a player wants to reserve the component that it has picked for its ship board
    public void reserveComponent(String nickname) throws PickedComponentException, ReservedComponentException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Assembling phase is finished");
        }
        playersPlay.get(nickname).getShipBoard().reserveComponent();
    }
    //invoked when a player wants to pick one of the components that it has reserved for its ship board
    public void pickReservedComponent(String nickname, int position) throws ReservedComponentException, PickedComponentException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Assembling phase is finished");
        }
        playersPlay.get(nickname).getShipBoard().pickReservedComponent(position);
    }
    //invoked when a player wants to release (therefore, place face up) the component that it has picked
    public void putShown(String nickname) throws PickedComponentException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Assembling phase is finished");
        }
        shownComponents.add(playersPlay.get(nickname).getShipBoard().releaseComponent());
    }
    //invoked when a player wants to assemble on the ship board the component that it has picked
    public void assembleComponentGS(String nickname, int x, int y) throws AssembledComponentException, PickedComponentException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Assembling phase is finished");
        }
        playersPlay.get(nickname).getShipBoard().assembleComponent(x,y);
    }
    //invoked when a player wants to change the orientation of the component that it has picked
    public void rotatePickedComponentLeft(String nickname) throws InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Assembling phase is finished");
        }
        playersPlay.get(nickname).getShipBoard().getPickedComponent().rotateLeft();
    }
    //invoked when a component of a player's ship board must be destroyed
    public void destroyComponent(String nickname, int x, int y) throws AssembledComponentException, InvalidActionException {
        if(state != State.SHIP_CONTROL){
            throw new InvalidActionException("Assembling phase is finished");
        }
        playersPlay.get(nickname).getShipBoard().destroyComponent(x,y);
        checkShipBoards();
    }
    //invoked when a player has finished the assembling phase and has to pick a free position on the flight board
    public void setPosition(String nickname, int initCell) throws InvalidPositionException, InvalidActionException {
        if(state != State.SHIP_BUILDING){
            throw new InvalidActionException("Assembling phase is finished");
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
        }
        if(!playersPos.containsValue(null)){
            state = State.SHIP_CONTROL;
            checkShipBoards();
        }
    }
    //checks the correctness of all the ships in the game
    public void checkShipBoards(){
        boolean correctShips = true;
        for(Player p : playersPlay.values()) {
            if(!p.getShipBoard().isCorrect()){
                correctShips = false;
                break;
            }
        }
        if(correctShips){
            state = State.CARD_PICKING;
            createGameDeck();
        }
    }
    //updates the position of a player on the ship board
    public void changePlayerPosition(String nickname, int cells) {
        playersPos.get(nickname).changePosition(cells);
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
    //invoked when the leader draws a new card from the deck (during the game), which must be solved
    public void pickNextCard(String nickname) throws InvalidActionException {
        if(state != State.CARD_PICKING){
            throw new InvalidActionException("Can't pick a new card");
        }
        if(!nickname.equals(playersPos.keySet().iterator().next())){
            throw new InvalidActionException("Only leader can pick cards");
        }
        try{
            currentCard = gameDeck.drawCard();
            currentCard.specialEffect(this);
            state = State.CARD_SOLVING;
        } catch (EmptyDeckException e) {
            state = State.END;
        }
    }


 }

