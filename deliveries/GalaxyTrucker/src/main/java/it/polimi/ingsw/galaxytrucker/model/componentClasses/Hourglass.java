package it.polimi.ingsw.galaxytrucker.model.componentClasses;

public class Hourglass {
    private final int cycleDurationSeconds; // Durata di un singolo ciclo della clessidra in secondi
    private boolean isRunning;
    private int numberFlips; // Numero di ribaltamenti della clessidra
    private long startTime; // Tempo di inizio del ciclo corrente


    /**
     * Costruttore per la clessidra.
     * @param cycleDurationSeconds Durata di un singolo ciclo della clessidra in secondi.
     */
    public Hourglass(int cycleDurationSeconds) {
        if (cycleDurationSeconds <= 0) {
            throw new IllegalArgumentException("La durata del ciclo deve essere positiva.");
        }
        this.cycleDurationSeconds = cycleDurationSeconds;
        this.isRunning = false;
        this.numberFlips = 0;
        this.startTime=0;
    }

    /**
     * Avvia o riavvia il timer della clessidra per la durata del suo ciclo.
     * Se era già in funzione, questo metodo la resetta e la fa ripartire.
     */
    public void startNewCycle() {
        this.startTime = System.currentTimeMillis();
        this.isRunning = true;
        this.numberFlips++;
    }
    /**
     * Ferma il timer della clessidra.
     * Se la clessidra non è in funzione, questo metodo non fa nulla.
     */
    public void stop() {
        this.isRunning = false;
    }

    /**
     * Verifica se la clessidra è attualmente in funzione (cioè, il tempo sta scorrendo).
     * @return true se la clessidra è in funzione, false altrimenti.
     */
    public boolean isRunning() {
        return isRunning;
    }
    /**
     * Restituisce la durata totale di un singolo ciclo della clessidra in secondi.
     * @return la durata del ciclo in secondi.
     */
    public int getCycleDurationSeconds() {
        return cycleDurationSeconds;
    }
}