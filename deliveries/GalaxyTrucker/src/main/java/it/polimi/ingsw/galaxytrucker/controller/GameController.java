package it.polimi.ingsw.galaxytrucker.controller;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.LevelOnePosition;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.LevelTwoPosition;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.Position;
import it.polimi.ingsw.galaxytrucker.virtualView.VirtualView;

import java.util.Collections;

public class GameController {
    private final GameState model;

    public GameController(boolean firstFlight, int numPlayers) {
        this.model = new GameState(firstFlight, numPlayers);
    }

    //STARTING PHASE

    //invoked when one of the players decides to start the assembling phase
    public int addPlayer(String nickname, Color color) {
        synchronized (model) {
            try{
                model.addPlayer(nickname, color);
                if(model.getGameState() == State.WAITING_FOR_PLAYERS){
                    return 0;       //still waiting for players
                }
                else{
                    return 1;       //start assembling phase
                }
            }
            catch(InvalidActionException e){
                return -1;           //game already started
            }
            catch(UniqueNicknameException e){
                return -2;          //already existing name
            }
            catch(UniquePlayerColorException e){
                return -3;          //already chosen color
            }
        }
    }

    //invoked when a player wants to pick a component among the one placed face down (assembling phase)
    public void pickHidden(String nickname){}
    //invoked when a player wants to pick a specific component among the one placed face up (assembling phase)
    public void pickShown(String nickname, int index){}
    //invoked when a player wants to reserve the component that it has picked for its ship board
    public void reserveComponent(String nickname){}
    //invoked when a player wants to pick one of the components that it has reserved for its ship board
    public void pickReservedComponent(String nickname, int position)  {}
    //invoked when a player wants to release (therefore, place face up) the component that it has picked
    public void putShown(String nickname)  {}
    //invoked when a player wants to assemble on the ship board the component that it has picked
    public void assembleComponent(String nickname, int x, int y)  {}
    //invoked when a player wants to change the orientation of the component that it has picked
    public void rotatePickedComponent(String nickname) {}
    //invoked when a player has finished the assembling phase and has to pick a free position on the flight board
    public void setPosition(String nickname, int initCell)  {
    }


}
