module it.polimi.ingsw.galaxytrucker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.smartcardio;
    requires java.rmi;
    requires com.fasterxml.jackson.databind;


    opens it.polimi.ingsw.galaxytrucker.ui to javafx.fxml;
    exports it.polimi.ingsw.galaxytrucker;
    exports it.polimi.ingsw.galaxytrucker.ui;
    exports it.polimi.ingsw.galaxytrucker.model.enumerations;
    exports it.polimi.ingsw.galaxytrucker.network.rmi.server to java.rmi;
    opens it.polimi.ingsw.galaxytrucker.model.enumerations to javafx.fxml;
    opens it.polimi.ingsw.galaxytrucker.network.rmi.client to java.rmi;
    opens it.polimi.ingsw.galaxytrucker to javafx.fxml;
    exports it.polimi.ingsw.galaxytrucker.ui.gui;
    opens it.polimi.ingsw.galaxytrucker.ui.gui to javafx.fxml;

}