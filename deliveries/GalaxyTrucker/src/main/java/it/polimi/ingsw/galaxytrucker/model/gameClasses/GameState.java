package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;

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
        decks = new ArrayList<>();
    }

    public Map<String,Position> getPlayersPos() {
        return playersPos;
    }
    public Map<String,Player> getPlayersPlay() {
        return playersPlay;
    }
    //method to add a player to the game
    public void addPlayer(String nickname, Color color) {
        playersPlay.put( nickname, new Player(nickname, color));
    }
    public void setPosition(String nickname,int initCell) {playersPos.put(nickname,new Position(initCell));}
    public void changePlayerPosition(String nickname,int cells) {playersPos.get(nickname).changePosition(cells);}
    public Component pickHidden(String nickname) {
        Collections.shuffle(hiddenComponents);
        Component c = hiddenComponents.getFirst();
        hiddenComponents.removeFirst();
        return c;
    }
    public Component pickShown(String nickname, int index) {
        Component c = shownComponents.get(index);
        shownComponents.remove(index);
        return c;
    }

    /**
     * method to drop a component back in the shownComponents list
     * @param nickname the owner of the component to drop
     */
    public void putShown(String nickname) {shownComponents.add(playersPlay.get(nickname).getShipBoard().getHandComponent());}
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

    public void rotateHandComponentLeft(String nickname){
        playersPlay.get(nickname).getShipBoard().getHandComponent().rotateLeft();
    }


 }

