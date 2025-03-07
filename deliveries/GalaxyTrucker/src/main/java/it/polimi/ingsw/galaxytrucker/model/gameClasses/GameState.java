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
    }
    //method to add a player to the game
    public void addPlayer(String nickname, Color color) {
        playersPlay.put( nickname, new Player(nickname, color));
    }
    public void getPosition(String nickname,int initCell) {


            }
    }

