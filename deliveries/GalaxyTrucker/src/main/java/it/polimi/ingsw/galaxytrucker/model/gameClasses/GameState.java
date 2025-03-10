package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.*;
import it.polimi.ingsw.galaxytrucker.model.eventCardClasses.*;
import it.polimi.ingsw.galaxytrucker.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.*;

import java.util.*;

//this class describes the entire status of the game, the controller will invoke its methods in order to modify the
//model according to specific actions performed by the players on the view
public class GameState {
    private Map<String,Position> playersPos;    //maps each player to its position on the ship board
    private Map<String,Player> playersPlay;     //maps each player to its information
    private List<Deck> decks;       //decks used during the assembling phase (only for standard game)
    private Deck gameDeck;      //main deck used during the game
    private int numPlayers;     //number of players
    private List<Component> hiddenComponents;       //components turned face down during the assembling phase
    private List<Component> shownComponents;        //components turned face up during the assembling phase
    private final boolean firstFlight;      //true if the game has been set as "learning flight", false if it is a standard game

    public GameState(boolean firstFlight) {     //constructor, creates the deck(s) of cards and instantiates the components
        this.firstFlight = firstFlight;
        playersPos = new HashMap<>();
        playersPlay = new HashMap<>();
        numPlayers = 0;
        createComponents();
        createDecks();
    }

    public Map<String,Position> getPlayersPos() {
        return playersPos;
    }
    public Map<String,Player> getPlayersPlay() {
        return playersPlay;
    }

    //adds a player to the game
    public void addPlayer(String nickname, Color color) {
        playersPlay.put(nickname, new Player(nickname, color));
        playersPos.put(nickname, null);
        numPlayers++;
    }
    //invoked when a player has finished the assembling phase and has to pick a free position on the flight board
    public void setPosition(String nickname, int initCell) {
        playersPos.put(nickname, new Position(initCell));
    }
    //updates the position of a player on the ship board
    public void changePlayerPosition(String nickname,int cells) {
        playersPos.get(nickname).changePosition(cells);
    }
    //invoked when a player wants to pick a component among the one placed face down (assembling phase)
    public void pickHidden(String nickname) {
        Collections.shuffle(hiddenComponents);
        Component c = hiddenComponents.getFirst();
        hiddenComponents.removeFirst();
        playersPlay.get(nickname).getShipBoard().pickComponent(c);
    }
    //invoked when a player wants to pick a specific component among the one placed face up (assembling phase)
    public void pickShown(String nickname, int index) {
        Component c = shownComponents.get(index);
        shownComponents.remove(index);
        playersPlay.get(nickname).getShipBoard().pickComponent(c);
    }
    //invoked when a player wants to reserve the component that it has picked for its ship board
    public void reserveComponent(String nickname) {
        playersPlay.get(nickname).getShipBoard().reserveComponent();
    }
    //invoked when a player wants to pick one of the components that it has reserved for its ship board
    public void pickReservedComponent(String nickname, int position) {
        playersPlay.get(nickname).getShipBoard().pickReservedComponent(position);
    }
    //invoked when a player wants to release (therefore, place face up) the component that it has picked
    public void putShown(String nickname) {
        shownComponents.add(playersPlay.get(nickname).getShipBoard().releaseComponent());
    }
    //invoked when a component of a player's ship board must be destroyed
    public void destroyComponent(String nickname, int x, int y) {
        playersPlay.get(nickname).getShipBoard().destroyComponent(x,y);
    }
    //creates the main deck for the game by unifying and shuffling the 4 decks used during the assembling phase;
    //this method is invoked after the assembling phase
    public void createGameDeck() {
        List<EventCard> gameDeckCards = new ArrayList<>();
        for (Deck deck : decks) {
            gameDeckCards.addAll(deck.getCards());
        }
        Collections.shuffle(gameDeckCards);
        gameDeck = new Deck(gameDeckCards);
    }
    //invoked when the leader draws a new card from the deck (during the game), which must be solved
    public void solveNextCard() {
        EventCard currentCard = gameDeck.drawCard();
        currentCard.solve(this);
    }
    //invoked when a player wants to assemble on the ship board the component that it has picked
    public void assembleComponentGS(String nickname, int x, int y){
       playersPlay.get(nickname).getShipBoard().assembleComponent(x,y);
    }
    //invoked when a player wants to change the orientation of the component that it has picked
    public void rotatePickedComponentLeft(String nickname){
        playersPlay.get(nickname).getShipBoard().getPickedComponent().rotateLeft();
    }
    //invoked when a player wants to view the content of one of the 3 available decks of the flight board (assembling phase)
    public Deck checkDeck(int deckNumber){
        Deck d = decks.get(deckNumber);
        d.setPicked();
        return d;
    }
    //this method instantiates all the components (tiles) of the game, which will be placed face down (hidden)
    public void createComponents(){
        hiddenComponents = new ArrayList<>();
        shownComponents = new ArrayList<>();
    }
    //this method instantiates all the adventure cards of the game and creates the 4 decks for the assembling phase
    public void createDecks(){
        List<EventCard> levelOneCards = new ArrayList<>();
        List<EventCard> levelTwoCards = new ArrayList<>();

        levelOneCards.add(new AbandonedShip(2, 3, 1, "abandonedshipL1_1.jpg"));
        levelOneCards.add(new AbandonedShip(3, 4, 1, "abandonedshipL1_2.jpg"));
        levelTwoCards.add(new AbandonedShip(4, 6, 1, "abandonedshipL2_1.jpg"));
        levelTwoCards.add(new AbandonedShip(5, 6, 1, "abandonedshipL2_2.jpg"));
        levelOneCards.add(new AbandonedStation(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN)), 5, 1, "abandonedstationL1_1.jpg"));
        levelOneCards.add(new AbandonedStation(new ArrayList<>(Arrays.asList(Color.RED, Color.RED)), 6, 1, "abandonedstationL1_2.jpg"));
        levelTwoCards.add(new AbandonedStation(new ArrayList<>(Arrays.asList(Color.RED, Color.YELLOW)), 7, 1, "abandonedstationL2_1.jpg"));
        levelTwoCards.add(new AbandonedStation(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.YELLOW, Color.GREEN)), 8, 2, "abandonedstationL2_2.jpg"));
        levelOneCards.add(new CombatZone(true, "combatzoneL1.jpg"));
        levelTwoCards.add(new CombatZone(false, "combatzoneL2.jpg"));
        levelTwoCards.add(new SpecialEvent(SpecialEventType.EPIDEMIC, "epidemicL2.jpg"));
        levelOneCards.add(new MeteorsSwarm(new ArrayList<>(Arrays.asList(new Meteor(true, Orientation.NORTH), new Meteor(false, Orientation.WEST), new Meteor(false, Orientation.EAST))), "meteorswarmL1_1.jpg"));
        levelOneCards.add(new MeteorsSwarm(new ArrayList<>(Arrays.asList(new Meteor(false, Orientation.NORTH), new Meteor(false, Orientation.NORTH), new Meteor(false, Orientation.WEST), new Meteor(false, Orientation.EAST), new Meteor(false, Orientation.SOUTH))), "meteorswarmL1_2.jpg"));
        levelOneCards.add(new MeteorsSwarm(new ArrayList<>(Arrays.asList(new Meteor(true, Orientation.NORTH), new Meteor(false, Orientation.NORTH), new Meteor(true, Orientation.NORTH))), "meteorswarmL1_3.jpg"));
        levelTwoCards.add(new MeteorsSwarm(new ArrayList<>(Arrays.asList(new Meteor(false, Orientation.NORTH), new Meteor(false, Orientation.NORTH), new Meteor(true, Orientation.WEST), new Meteor(false, Orientation.WEST), new Meteor(false, Orientation.WEST))), "meteorswarmL2_1.jpg"));
        levelTwoCards.add(new MeteorsSwarm(new ArrayList<>(Arrays.asList(new Meteor(true, Orientation.NORTH), new Meteor(true, Orientation.NORTH), new Meteor(false, Orientation.SOUTH), new Meteor(false, Orientation.SOUTH))), "meteorswarmL2_2.jpg"));
        levelTwoCards.add(new MeteorsSwarm(new ArrayList<>(Arrays.asList(new Meteor(false, Orientation.NORTH), new Meteor(false, Orientation.NORTH), new Meteor(true, Orientation.EAST), new Meteor(false, Orientation.EAST), new Meteor(false, Orientation.EAST))), "meteorswarmL2_3.jpg"));
        levelOneCards.add(new OpenSpace("openspaceL1_1.jpg"));
        levelOneCards.add(new OpenSpace("openspaceL1_2.jpg"));
        levelOneCards.add(new OpenSpace("openspaceL1_3.jpg"));
        levelOneCards.add(new OpenSpace("openspaceL1_4.jpg"));
        levelTwoCards.add(new OpenSpace("openspaceL2_1.jpg"));
        levelTwoCards.add(new OpenSpace("openspaceL2_2.jpg"));
        levelTwoCards.add(new OpenSpace("openspaceL2_3.jpg"));
        levelOneCards.add(new Pirates(4,5, new ArrayList<>(Arrays.asList(new CannonShot(false, Orientation.NORTH), new CannonShot(true, Orientation.NORTH), new CannonShot(false, Orientation.NORTH))),1, "piratesL1.jpg"));
        levelTwoCards.add(new Pirates(7,6, new ArrayList<>(Arrays.asList(new CannonShot(true, Orientation.NORTH), new CannonShot(false, Orientation.NORTH), new CannonShot(true, Orientation.NORTH))),2, "piratesL2.jpg"));
        levelOneCards.add(new Planets(new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(Color.RED, Color.GREEN, Color.BLUE, Color.BLUE, Color.BLUE)), new ArrayList<>(Arrays.asList(Color.RED, Color.YELLOW, Color.BLUE)), new ArrayList<>(Arrays.asList(Color.RED, Color.BLUE, Color.BLUE, Color.BLUE)), new ArrayList<>(Arrays.asList(Color.RED, Color.GREEN)))),3, "planetsL1_1.jpg"));
        levelOneCards.add(new Planets(new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(Color.RED, Color.RED)), new ArrayList<>(Arrays.asList(Color.RED, Color.BLUE, Color.BLUE)), new ArrayList<>(Arrays.asList(Color.YELLOW)))),2, "planetsL1_2.jpg"));
        levelOneCards.add(new Planets(new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.BLUE, Color.BLUE)), new ArrayList<>(Arrays.asList(Color.YELLOW, Color.YELLOW)))),3, "planetsL1_3.jpg"));
        levelOneCards.add(new Planets(new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(Color.GREEN, Color.GREEN)), new ArrayList<>(Arrays.asList(Color.YELLOW)), new ArrayList<>(Arrays.asList(Color.BLUE, Color.BLUE, Color.BLUE)))),1, "planetsL1_4.jpg"));
        levelTwoCards.add(new Planets(new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(Color.RED, Color.RED, Color.RED, Color.YELLOW)), new ArrayList<>(Arrays.asList(Color.RED, Color.RED, Color.GREEN, Color.GREEN)), new ArrayList<>(Arrays.asList(Color.RED, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE)))),4, "planetsL2_1.jpg"));
        levelTwoCards.add(new Planets(new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(Color.RED, Color.RED)), new ArrayList<>(Arrays.asList(Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN)))),3, "planetsL2_2.jpg"));
        levelTwoCards.add(new Planets(new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(Color.RED, Color.YELLOW)), new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.BLUE)), new ArrayList<>(Arrays.asList(Color.GREEN, Color.GREEN)), new ArrayList<>(Arrays.asList(Color.YELLOW)))),2, "planetsL2_3.jpg"));
        levelTwoCards.add(new Planets(new ArrayList<>(Arrays.asList(new ArrayList<>(Arrays.asList(Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN)), new ArrayList<>(Arrays.asList(Color.YELLOW, Color.YELLOW)), new ArrayList<>(Arrays.asList(Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE)))),3, "planetsL2_4.jpg"));
        levelOneCards.add(new Slavers(5, 6, 3, 1, "slaversL1.jpg"));
        levelTwoCards.add(new Slavers(8, 7, 4, 2, "slaversL2.jpg"));
        levelOneCards.add(new Smugglers(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.BLUE)),2,4,1,"smugglersL1.jpg"));
        levelTwoCards.add(new Smugglers(new ArrayList<>(Arrays.asList(Color.RED, Color.YELLOW, Color.YELLOW)),3,8,1,"smugglersL2.jpg"));
        levelOneCards.add(new SpecialEvent(SpecialEventType.STARDUST, "stardustL1.jpg"));
        levelTwoCards.add(new SpecialEvent(SpecialEventType.STARDUST, "stardustL2.jpg"));

        Collections.shuffle(levelOneCards);
        Collections.shuffle(levelTwoCards);

        List<EventCard> l1 = new ArrayList<>();
        l1.add(levelOneCards.removeLast());
        l1.add(levelTwoCards.removeLast());
        l1.add(levelTwoCards.removeLast());
        Deck d1 = new Deck(l1);

        List<EventCard> l2 = new ArrayList<>();
        l2.add(levelOneCards.removeLast());
        l2.add(levelTwoCards.removeLast());
        l2.add(levelTwoCards.removeLast());
        Deck d2 = new Deck(l2);

        List<EventCard> l3 = new ArrayList<>();
        l3.add(levelOneCards.removeLast());
        l3.add(levelTwoCards.removeLast());
        l3.add(levelTwoCards.removeLast());
        Deck d3 = new Deck(l3);

        List<EventCard> l4 = new ArrayList<>();
        l4.add(levelOneCards.removeLast());
        l4.add(levelTwoCards.removeLast());
        l4.add(levelTwoCards.removeLast());
        Deck d4 = new Deck(l4);

        decks = new ArrayList<>(Arrays.asList(d1, d2, d3, d4));
    }
 }

