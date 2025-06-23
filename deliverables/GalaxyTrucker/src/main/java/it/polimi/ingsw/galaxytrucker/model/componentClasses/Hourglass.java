package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.HourGlassException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

/**
 * Represents the hourglass used in the game.
 */

public class Hourglass {
    private final int cycleDurationSeconds; // Duration of a single cycle of the hourglass in seconds.
    private boolean isRunning;  //  Specify if the hourglass has finished or not.
    private GameState gameState;    // Gamestate
    private int numberFlips; // Nummber of times the hourglass has been flipped.

    /**
     * Constructor for the Hourglass class.
     * @param cycleDurationSeconds Duration of a single cycle of the hourglass in seconds.
     * @param gameState The current game state.
     * @throws HourGlassException if the cycle duration is not positive.
     *
     */
    public Hourglass(int cycleDurationSeconds, GameState gameState) throws HourGlassException {
        if (cycleDurationSeconds <= 0) {
            throw new HourGlassException("Duration must be positive.");
        }
        this.cycleDurationSeconds = cycleDurationSeconds; // Set the duration of a single cycle.
        this.isRunning = false; // Initially, the hourglass is not running.
        this.numberFlips = 0; // Initially, the hourglass has not been flipped.
        this.gameState = gameState; // Set the game state.
    }

    /**
     * Starts a new cycle of the hourglass.
     * @throws HourGlassException
     */
    public void startNewCycle() throws HourGlassException {
        if (isRunning) {
            throw new HourGlassException("Hourglass is already running."); // Check if the hourglass is already running.
        }
        if (numberFlips >= 2) {
            throw new HourGlassException("Can't start a new cycle"); // Check if the hourglass has already been flipped twice.
        }

        isRunning = true; // Set the hourglass to running state.
        numberFlips++; // Increment the number of flips.

        Thread thread = new Thread(() -> { // Create a new thread to handle the hourglass cycle.
            try {
                Thread.sleep(cycleDurationSeconds * 1000L); // convert seconds to milliseconds.
            } catch (InterruptedException e) { // Handle interruption of the thread.
                throw new HourGlassException("Hourglass interrupted.");
            }
            if(!gameState.getGameState().equals(State.SHIP_BUILDING)){ // Check if the game state is still in ship building.
                return;
            }
            isRunning = false; // Set the hourglass to not running state after the cycle is finished.
            gameState.finishedCycle(); // Notify the game state that the cycle is finished.
            if (numberFlips == 2) { // If the hourglass has been flipped twice, change the game state to ship placement.
                gameState.setGameState(State.SHIP_PLACEMENT);
            }
        });

        thread.start(); // Start the thread to handle the hourglass cycle.
    }

    /**
     * Returns the duration of a single cycle of the hourglass.
     * @return Duration of a single cycle in seconds.
     */
    public int getNumberFlips() {
        return numberFlips;
    }

}