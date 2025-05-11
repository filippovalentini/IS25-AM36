package it.polimi.ingsw.galaxytrucker.model.componentClasses;

public class Hourglass {
    private final int cycleDurationSeconds; // Durata di un singolo ciclo della clessidra in secondi
    private long cycleEndTimeMillis;      // Timestamp di quando il ciclo corrente della clessidra terminerà
    private boolean isRunning;

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
        this.cycleEndTimeMillis = 0;
    }

    /**
     * Avvia o riavvia il timer della clessidra per la durata del suo ciclo.
     * Se era già in funzione, questo metodo la resetta e la fa ripartire.
     */
    public void startNewCycle() {
        this.cycleEndTimeMillis = System.currentTimeMillis() + (this.cycleDurationSeconds * 1000L);
        this.isRunning = true;
    }

    /**
     * Ferma la clessidra.
     */
    public void stop() {
        this.isRunning = false;
    }

    /**
     * Controlla se il tempo del ciclo corrente della clessidra è scaduto.
     * @return true se la clessidra è in funzione e il tempo del ciclo è scaduto, false altrimenti.
     */
    public boolean isCurrentCycleExpired() {
        if (!isRunning) {
            return false; // Se non è in funzione, il ciclo corrente non può essere scaduto.
        }
        return System.currentTimeMillis() >= this.cycleEndTimeMillis;
    }

    /**
     * Restituisce il tempo rimanente nel ciclo corrente in secondi.
     * Se la clessidra non è in funzione o il ciclo è scaduto, restituisce 0.
     * @return Tempo rimanente nel ciclo corrente in secondi, o 0 se non applicabile.
     */
    public long getRemainingSecondsInCycle() {
        if (!isRunning || isCurrentCycleExpired()) {
            return 0;
        }
        long remainingMillis = this.cycleEndTimeMillis - System.currentTimeMillis();
        return Math.max(0, remainingMillis / 1000L); // Assicura che non sia negativo
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