package it.polimi.ingsw.galaxytrucker.ServerRMI;

public interface VirtualViewRMI {
    public void updateWaitingForPlayers();
    public void updateStartAssembling();
    public void notifyError(String errorMessage);
}
