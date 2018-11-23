package new_simvasos.verifier;

import javafx.util.Pair;
import new_simvasos.property.MCIProperty;
import new_simvasos.property.MCIPropertyChecker;
import new_simvasos.property.PropertyChecker;
import new_simvasos.simulation.Simulation_Firefighters;
import new_simvasos.verifier.SPRT;
import new_simvasos.verifier.Verifier;
import org.junit.Test;

import static org.junit.Assert.*;

public class SPRTTest {

    int simulationTime;
    int repeatSim;
    Simulation_Firefighters sim1;
    MCIProperty rescuedProperty;
    MCIPropertyChecker rescuedChecker;
    SPRT verifier;
    
    @Test
    public void verifyWithSimulationGUI() {
        this.sim1 = new Simulation_Firefighters(15);
    
        this.rescuedProperty = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIPropertyType", 0.8);
        this.rescuedChecker = new MCIPropertyChecker();
        this.verifier = new SPRT(rescuedChecker);
        
        Pair<Pair<Integer, Boolean>, String> verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.01);
        assertEquals(true, verificationResult.getKey().getValue());
        assertNotEquals(100,verificationResult.getKey().getValue());
        
        verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.50);
        assertEquals(false, verificationResult.getKey().getValue());
        assertNotEquals(100,verificationResult.getKey().getValue());
        
        verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.99);
        assertEquals(false, verificationResult.getKey().getValue());
        assertNotEquals(100,verificationResult.getKey().getValue());
    }
}