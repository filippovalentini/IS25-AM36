package it.polimi.ingsw.galaxytrucker.network.rmi.server;

import it.polimi.ingsw.galaxytrucker.network.VirtualView;

public interface VirtualViewRMI extends VirtualView {
    public void updateWaitingForPlayers();
    public void updateStartAssembling();
    public void notifyError(String errorMessage);
}
