package it.polimi.ingsw.galaxytrucker.model.componentClasses;

public class Hourglass {
    private final int durataTotale;
    private long tempoRimanente;
    private boolean inFunzione;
    private long ultimoAggiornamento;

    public Hourglass(int durataTotale) {
        this.durataTotale = durataTotale;
        this.tempoRimanente=durataTotale;
        this.inFunzione=false;
        this.ultimoAggiornamento=0;
    }
    public void startHourglass(){
        if(!inFunzione){
            inFunzione=true;
            ultimoAggiornamento=System.currentTimeMillis();
        }
    }
    public void stopHourglass(){
        if (inFunzione){
            updateRemainingTime();
            inFunzione=false;
        }
    }
    public void turnHourglass(){ //when a player turns the hourglass
        if (inFunzione) {
            tempoRimanente= System.currentTimeMillis()- ultimoAggiornamento;
        }
    }
    private void updateRemainingTime() {
        if (inFunzione) {
            long tempoAttuale = System.currentTimeMillis();
            long tempoTrascorsoMs = tempoAttuale - ultimoAggiornamento;
            long tempoTrascorsoSec = (tempoTrascorsoMs / 1000);

            tempoRimanente = Math.max(0, tempoRimanente - tempoTrascorsoSec);
            ultimoAggiornamento = tempoAttuale;
        }
    }
    public boolean isExpired() {
        updateRemainingTime();
        return tempoRimanente <= 0;
    }
    public long getRemainingTime() {
        updateRemainingTime();
        return tempoRimanente;
    }
    public boolean isRunning() {
        return inFunzione;
    }
}
