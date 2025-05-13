package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.HourGlassException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

public class Hourglass {
    private final int cycleDurationSeconds; // Durata di un singolo ciclo della clessidra in secondi
    private boolean isRunning;  // Specifica se la clessidra ha finito oppure no
    private GameState gameState;    // GameState di appartenenza
    private int numberFlips; // Numero di ribaltamenti della clessidra

    /**
     * Costruttore per la clessidra.
     * @param cycleDurationSeconds Durata di un singolo ciclo della clessidra in secondi.
     */
    public Hourglass(int cycleDurationSeconds, GameState gameState) throws HourGlassException {
        if (cycleDurationSeconds <= 0) {
            throw new HourGlassException("La durata del ciclo deve essere positiva.");
        }
        this.cycleDurationSeconds = cycleDurationSeconds;
        this.isRunning = false;
        this.numberFlips = 0;
        this.gameState = gameState;
    }

    /**
     * Avvia o riavvia il timer della clessidra per la durata del suo ciclo.
     * Se era già in funzione, questo metodo la resetta e la fa ripartire.
     */
    public void startNewCycle() throws HourGlassException {
        if (isRunning) {
            throw new HourGlassException("Hourglass is already running.");
        }
        if (numberFlips >= 2) {
            throw new HourGlassException("Can't start a new cycle");
        }

        isRunning = true;
        numberFlips++;

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(cycleDurationSeconds * 1000L); // conversione in millisecondi
            } catch (InterruptedException e) {
                throw new HourGlassException("Hourglass interrupted.");
            }
            isRunning = false;
            gameState.finishedCycle();
            if (numberFlips == 2) {
                gameState.setGameState(State.SHIP_PLACEMENT);
            }
        });

        thread.start();
    }

    public int getNumberFlips() {
        return numberFlips;
    }

}