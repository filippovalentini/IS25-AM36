module it.polimi.ingsw.galaxytrucker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.smartcardio;
    requires java.rmi;


    opens it.polimi.ingsw.galaxytrucker to javafx.fxml;
    exports it.polimi.ingsw.galaxytrucker;
    exports it.polimi.ingsw.galaxytrucker.model.enumerations;
    opens it.polimi.ingsw.galaxytrucker.model.enumerations to javafx.fxml;
}