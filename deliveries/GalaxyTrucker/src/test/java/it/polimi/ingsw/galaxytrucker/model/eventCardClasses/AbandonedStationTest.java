package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedStationTest {
    private AbandonedStation abandonedStation;

    @BeforeEach
    void init() {
        List<Color> stationGoods = new ArrayList<Color>();
        stationGoods.add(Color.YELLOW);
        stationGoods.add(Color.GREEN);
        abandonedStation = new AbandonedStation(stationGoods, 5, 1, 0);
    }

    @Test
    void testUseStation() {

    }

    @Test
    void testShouldNotUseStationAlreadyUsed() {

    }

    @Test
    void testLandingWithRequiredCrew() {

    }

    @Test
    void testShouldNotLandingInvalidRequiredCrew() {
    }

}