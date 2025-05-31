package it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces;

import it.polimi.ingsw.galaxytrucker.network.VirtualServer;

public interface FlightBoardController extends GuiController {
    @Override
    void setServer(VirtualServer server);
}
