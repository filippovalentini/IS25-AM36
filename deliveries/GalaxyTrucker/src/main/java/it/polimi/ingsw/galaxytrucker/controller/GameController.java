package it.polimi.ingsw.galaxytrucker.controller;

import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.virtualView.VirtualView;

public class GameController {
    private GameState model;

    public GameController(boolean firstFlight, int numPlayers) {
        this.model = new GameState(firstFlight, numPlayers);
    }


}
