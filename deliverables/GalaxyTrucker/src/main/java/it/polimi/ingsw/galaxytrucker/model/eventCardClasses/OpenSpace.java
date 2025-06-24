package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.*;


/**
 * Represents an Open Space event card in the Galaxy Trucker game
 */
public class OpenSpace extends EventCard {
    /**
     * Constructs an OpenSpace event card with the specified image ID.
     *
     * @param imageID the ID of the image associated with this event card
     */
    public OpenSpace(int imageID) {
        super(imageID);
    }

    /**
     * Handles the flying action for a player in the game.
     * @param gameState
     * @param nickname
     * @param usedBatteries
     * @throws InvalidActionException
     * @throws NoBatteriesException
     */
    @Override
    public void fly(GameState gameState, String nickname, int usedBatteries) throws InvalidActionException, NoBatteriesException {
        if (gameState.getNumberBatteries(nickname) < usedBatteries) { // Check if the player has enough batteries
            throw new NoBatteriesException("Too few batteries");
        }

        float engineStrength = gameState.getEngineStrength(nickname, usedBatteries); // Calculate the engine strength based on the batteries used

        if (gameState.isLastInTurn(nickname)) { // If the player is the last in turn
            gameState.setGameState(State.CARD_PICKING); // Set the game state to CARD_PICKING
        }
        if (engineStrength == 0) { // If the engine strength is zero, the player cannot fly
            gameState.quitGame(nickname, false);
            throw new NoStrengthException("Insufficient engine strength: quitting game...");
        } else {
            gameState.changePlayerPosition(nickname, (int) Math.abs(engineStrength));   // Change the player's position based on the engine strength
            gameState.nextTurn();
        }
    }
}