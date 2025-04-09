module it.polimi.ingsw.galaxytrucker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.smartcardio;
    requires java.rmi;


    opens it.polimi.ingsw.galaxytrucker to javafx.fxml;
    exports it.polimi.ingsw.galaxytrucker;
    exports it.polimi.ingsw.galaxytrucker.model.enumerations;
    exports it.polimi.ingsw.galaxytrucker.network.rmi.server to java.rmi;
    opens it.polimi.ingsw.galaxytrucker.model.enumerations to javafx.fxml;
}