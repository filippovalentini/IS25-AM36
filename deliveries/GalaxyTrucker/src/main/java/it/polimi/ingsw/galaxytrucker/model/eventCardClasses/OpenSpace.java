package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.ConfigurableComponent;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Engine;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.Player;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.ShipBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//OPEN SPACE
public class OpenSpace extends EventCard {

    public OpenSpace(String imagePath) {
        super(imagePath);
    }

    @Override
    public void solve(GameState gameState) {
        List<Integer> stepsAhead = new ArrayList<>();
        List<Player> playerList = new ArrayList<>(gameState.getPlayersPlay().values());
        for (Player player : playerList) {
            {
                stepsAhead.add(player.getShipBoard().getAssembledComponents().stream()
                        .flatMap(List::stream)
                        .filter(x -> x instanceof Engine)
                        .map(x -> (ConfigurableComponent) x)
                        .filter(x -> x.getIsDouble() == false)
                        .collect(Collectors.toList())
                        .size());
            }


        }       //implements the effect of the card
    }
}