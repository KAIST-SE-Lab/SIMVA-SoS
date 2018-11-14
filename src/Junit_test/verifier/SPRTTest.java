package verifier;

import new_simvasos.property.MCIProperty;
import new_simvasos.property.MCIPropertyChecker;
import new_simvasos.property.PropertyChecker;
import new_simvasos.simulation.Simulation_Firefighters;
import new_simvasos.verifier.SPRT;
import new_simvasos.verifier.Verifier;
import org.junit.Test;

import static org.junit.Assert.*;

public class SPRTTest extends Verifier {

    int simulationTime;
    int repeatSim;
    Simulation_Firefighters sim1;
    MCIProperty rescuedProperty;
    MCIPropertyChecker rescuedChecker;
    SPRT verifier;
    
    public SPRTTest(PropertyChecker checker) {
        super(checker);
    
        this.repeatSim = 2000;
        this.simulationTime = 15;
    }

    @Test
    public void verifyWithSimulator() {
    }
    
    @Test
    public void verifyWithSimulationGUI() {
        this.sim1 = new Simulation_Firefighters(this.simulationTime);
    
        this.rescuedProperty = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIPropertyType", 0.8);
        this.rescuedChecker = new MCIPropertyChecker();
        this.verifier = new SPRT(rescuedChecker);
    }
}