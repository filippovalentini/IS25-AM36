module it.polimi.ingsw.galaxytrucker {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.polimi.ingsw.galaxytrucker to javafx.fxml;
    exports it.polimi.ingsw.galaxytrucker;
}