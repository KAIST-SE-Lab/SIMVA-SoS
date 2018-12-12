package new_simvasos.verifier;

import javafx.util.Pair;
import new_simvasos.log.Log;
import new_simvasos.property.*;
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
    
    /**
     * This method tests the Existence Checker by checking the result at 0.01, 0.70, 0.99
     */
    @Test
    public void verifyWExistenceChecker() {
        this.sim1 = new Simulation_Firefighters(15);
    
        this.rescuedProperty = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIPropertyType", 0.9);
        this.rescuedChecker = new MCIPropertyChecker();
        this.verifier = new SPRT(rescuedChecker);
        
        Pair<Pair<Integer, Boolean>, String> verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.01);
        assertEquals(true, verificationResult.getKey().getValue());
        assertNotEquals(100,(int)verificationResult.getKey().getKey());
        
        verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.70);
        assertEquals(false, verificationResult.getKey().getValue());
        
        verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.99);
        assertEquals(false, verificationResult.getKey().getValue());
        assertNotEquals(100,(int)verificationResult.getKey().getKey());
    }
    
    /**
     * This method tests the Absence Checker by checking the result at 0.01, 0.70, 0.99
     */
    @Test
    public void verifyWAbsenceChecker() {
        this.sim1 = new Simulation_Firefighters(15);
        
        this.rescuedProperty = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIPropertyType", 0.9);
        MCIAbsenceChecker absence = new MCIAbsenceChecker();
        this.verifier = new SPRT(absence);
        
        Pair<Pair<Integer, Boolean>, String> verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.01);
        assertEquals(true, verificationResult.getKey().getValue());
        assertNotEquals(100,(int)verificationResult.getKey().getKey());
        
        verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.70);
        assertEquals(false, verificationResult.getKey().getValue());
        assertNotEquals(100,(int)verificationResult.getKey().getKey());
        
        verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.99);
        assertEquals(false, verificationResult.getKey().getValue());
        assertNotEquals(100,(int)verificationResult.getKey().getKey());
    }
    
    @Test
    public void verifyUniversalityChecker() {
        this.sim1 = new Simulation_Firefighters(15);
        
        this.rescuedProperty = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIPropertyType", 0.9);
        MCIUniversalityChecker checker = new MCIUniversalityChecker();
        this.verifier = new SPRT(checker);
        rescuedProperty.setThresholdPatient(1.0);
        
        Pair<Pair<Integer, Boolean>, String> verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.01);
        assertEquals(true, verificationResult.getKey().getValue());
        assertNotEquals(100,(int)verificationResult.getKey().getKey());
        
        verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.80);
        assertEquals(false, verificationResult.getKey().getValue());
        assertEquals(100,(int)verificationResult.getKey().getKey());
        
        verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.99);
        assertEquals(false, verificationResult.getKey().getValue());
        assertEquals(100,(int)verificationResult.getKey().getKey());
    }
    
    @Test
    public void verifyWMinimumDurationChecker() {
        this.sim1 = new Simulation_Firefighters(15);
        
        this.rescuedProperty = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIPropertyType", 0.9);
        MCIMinimumDurationChecker checker = new MCIMinimumDurationChecker();
        this.verifier = new SPRT(checker);
        rescuedProperty.setThresholdPatient(1.0);
        
        Pair<Pair<Integer, Boolean>, String> verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.01, 5, 15);
        assertEquals(true, verificationResult.getKey().getValue());
        assertNotEquals(100,(int)verificationResult.getKey().getKey());
        
        verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.99, 5, 15);
        assertEquals(false, verificationResult.getKey().getValue());
        assertNotEquals(100,(int)verificationResult.getKey().getKey());
        
        Log log = new Log();
        String log1 = "RescuedRate: " + 0.5;
        log.addSnapshot(1, log1);
        
        String log2 = "RescuedRate: " + 0.7;
        log.addSnapshot(2, log2);
        
        String log3 = "RescuedRate: " + 0.9;
        log.addSnapshot(3, log3);
        
        boolean ret = checker.check(log, rescuedProperty);
        assertEquals(false, ret);
        
        ret = checker.check(log, rescuedProperty, 10);
        assertEquals(false, ret);
        
        ret = checker.check(log, rescuedProperty, 0.5, 10);
        assertEquals(false, ret);
        
        ret = checker.check(log, rescuedProperty, 0.5, 5, 10);
        assertEquals(false, ret);
        
        ret = checker.check(log, rescuedProperty, 1, 3);
        assertEquals(true, ret);
    }
    
    @Test
    public void verifyWSteadyStateChecker() {
        this.sim1 = new Simulation_Firefighters(15);
        
        this.rescuedProperty = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIPropertyType", 0.9);
        MCISteadyStateProbabilityChecker checker = new MCISteadyStateProbabilityChecker();
        this.verifier = new SPRT(checker);
        rescuedProperty.setThresholdPatient(0.5);
        
        Pair<Pair<Integer, Boolean>, String> verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.01, 0.5, 15);
        assertEquals(true, verificationResult.getKey().getValue());
        assertNotEquals(100,(int)verificationResult.getKey().getKey());
        
        verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.80, 0.5, 15);
        assertEquals(false, verificationResult.getKey().getValue());
        
        verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.99, 0.5, 15);
        assertEquals(false, verificationResult.getKey().getValue());
        assertNotEquals(100,(int)verificationResult.getKey().getKey());
    }
    
    @Test
    public void verifyWTransientStateChecker() {
        this.sim1 = new Simulation_Firefighters(15);
        
        this.rescuedProperty = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIPropertyType", 0.8);
        MCITransientStateProbabilityChecker checker = new MCITransientStateProbabilityChecker();
        this.verifier = new SPRT(checker);
        rescuedProperty.setThresholdPatient(0.9);
        
        Pair<Pair<Integer, Boolean>, String> verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.01, 0.5, 10, 15);
        assertEquals(true, verificationResult.getKey().getValue());
        assertNotEquals(100,(int)verificationResult.getKey().getKey());
        
        verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.80, 0.5, 10, 15);
        assertEquals(false, verificationResult.getKey().getValue());
        assertEquals(100,(int)verificationResult.getKey().getKey());
        
        verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, 100, 0.99, 0.5, 10, 15);
        assertEquals(false, verificationResult.getKey().getValue());
        assertNotEquals(100,(int)verificationResult.getKey().getKey());
    }
}