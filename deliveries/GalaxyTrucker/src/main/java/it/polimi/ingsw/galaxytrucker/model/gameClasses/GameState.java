package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.enumerations.SpecialEventType;
import it.polimi.ingsw.galaxytrucker.model.eventCardClasses.*;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.CannonShot;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.Meteor;

import java.util.*;


public class GameState {
    private Map<String,Position> playersPos;
    private Map<String,Player> playersPlay;
    private List<Deck> decks;
    private int numPlayers;
    private List<Component> hiddenComponents;
    private List<Component> shownComponents;
    private final boolean firstFlight;

    public GameState(boolean firstFlight) {
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
    //method to add a player to the game
    public void addPlayer(String nickname, Color color) {
        playersPlay.put(nickname, new Player(nickname, color));
        playersPos.put(nickname, null);
        numPlayers++;
    }
    public void setPosition(String nickname, int initCell) {
        playersPos.put(nickname, new Position(initCell));
    }
    public void changePlayerPosition(String nickname,int cells) {
        playersPos.get(nickname).changePosition(cells);
    }
    public void pickHidden(String nickname) {
        Collections.shuffle(hiddenComponents);
        Component c = hiddenComponents.getFirst();
        hiddenComponents.removeFirst();
        playersPlay.get(nickname).getShipBoard().pickComponent(c);
    }
    public void pickShown(String nickname, int index) {
        Component c = shownComponents.get(index);
        shownComponents.remove(index);
        playersPlay.get(nickname).getShipBoard().pickComponent(c);
    }
    /**
     * method to drop a component back in the shownComponents list
     * @param nickname the owner of the component to drop
     */
    public void putShown(String nickname) {
        shownComponents.add(playersPlay.get(nickname).getShipBoard().releaseComponent());
    }
    //combine all decks into one (0),shuffle it and extract a card from it
    public void solveNextCard(){
        for(int i=1;i<4;i++){
            decks.get(0).getCards().addAll(decks.get(i).getCards()); ;
            decks.get(i).getCards().clear();
        }
        for(int i=1;i<4;i++){
            decks.remove(i);
        }
        decks.get(0).shuffle();
        decks.get(0).drawCard().solve();
    }
    public void assembleComponentGS(String nickname, int x, int y){
       playersPlay.get(nickname).getShipBoard().assembleComponent(x,y);
    }
    public void rotatePickedComponentLeft(String nickname){
        playersPlay.get(nickname).getShipBoard().getPickedComponent().rotateLeft();
    }
    public Deck checkDeck(int deckNumber){
        Deck d = decks.get(deckNumber);
        d.setPicked();
        return d;
    }
    public void createComponents(){
        hiddenComponents = new ArrayList<>();
        shownComponents = new ArrayList<>();
    }
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

