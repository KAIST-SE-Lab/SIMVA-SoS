package new_simvasos.localization;

import javafx.util.Pair;
import new_simvasos.localization.RescueSimulation;
import new_simvasos.not_decided.SoS;
import new_simvasos.property.MCIProperty;
import new_simvasos.property.MCIPropertyChecker;
import new_simvasos.scenario.Event;
import new_simvasos.scenario.PatientOccurrence;
import new_simvasos.scenario.Scenario;
import new_simvasos.simulator.Simulator;
import new_simvasos.timebound.ConstantTimeBound;
import new_simvasos.localization.SPRT;

import java.util.ArrayList;

public class main {
    public static void main (String[] args) {
        MCIProperty rescuedProperty;
        MCIPropertyChecker rescuedChecker;
        SPRT verifier;
        rescuedProperty = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIPropertyType", 0.5);
        rescuedChecker = new MCIPropertyChecker();
        verifier = new SPRT(rescuedChecker);
        int repeatSim = 2000;
        Pair<Pair<Integer, Boolean>, String> verificationResult;

        RescueSimulation sim1 = new RescueSimulation(100);
        sim1.runSimulation().printSnapshot();

        double satisfactionProb = 0;
        Boolean satisfaction = true;
        for (int i = 1; i < 100; i++) {
            double theta = i * 0.01;
            //Existence, Absence, Universality
            verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, repeatSim, theta);
            System.out.println(verificationResult.getValue());
            if (satisfaction == true && !verificationResult.getKey().getValue()){
                satisfactionProb = theta;
                satisfaction = false;
            }
        }
        System.out.println("Verification property satisfaction probability: " + satisfactionProb);
    }
}
