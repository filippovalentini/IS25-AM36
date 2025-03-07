package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;

import java.util.*;

public class GameState {
    private Map<Player,Position> players;
    private List<Deck> decks;
    private int numPlayers;
    private List<Component> hiddenComponents;
    private List<Component> shownComponents;
    private final boolean firstFlight;

    public GameState(boolean firstFlight) {
        this.firstFlight = firstFlight;
        players = new HashMap<>();
        numPlayers = 0;
    }
    public void addPlayer(String nickname, Color color) {
        players.put(new Player(nickname, color), null);
    }
}
