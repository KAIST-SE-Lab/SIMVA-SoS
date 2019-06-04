package new_simvasos.adaptation;

import javafx.util.Pair;
import new_simvasos.log.Log;

public class main {
    public static void main (String[] args) {
        String path = "./src./new main./Integrated./src./new_simvasos./adaptation./configuration./";
        SmartHomeSimulation smartHomeSimulation = new SmartHomeSimulation(path + "simulationConfig.txt");
        Log log = smartHomeSimulation.runSimulation();
        log.printSnapshot();

        String outputPath = "./src./new main./Integrated./src./new_simvasos./adaptation./output./";
        FileManager.saveLog(log, outputPath + "log.txt");
        FileManager.saveLog(log, outputPath + "outputTemperature.csv","indoorTemperature");
        FileManager.saveLog(log, outputPath + "outputHumidity.csv","indoorHumidity");



        //verification
        SPRT verifier;
        //ComfortZoneChecker comfortZoneChecker = new ComfortZoneChecker();
        ComfortZoneMaximumDurationChecker comfortZoneMaximumDurationChecker = new ComfortZoneMaximumDurationChecker();
        //verifier = new SPRT(comfortZoneChecker);
        verifier = new SPRT(comfortZoneMaximumDurationChecker);
        Pair<Pair<Integer, Boolean>, String> verificationResult;

        double satisfactionProb = 0;
        Boolean satisfaction = true;
        for (int i = 1; i < 100; i++) {
            double theta = i * 0.01;
            //Existence, Absence, Universality
            //verificationResult = verifier.verifyWithSimulationGUI(smartHomeSimulation, null, 2000, theta);
            verificationResult = verifier.verifyWithSimulationGUI(smartHomeSimulation, null, 2000, theta, 0, 4);
            System.out.println(verificationResult.getValue());
            if (satisfaction == true && !verificationResult.getKey().getValue()) {
                satisfactionProb = theta;
                satisfaction = false;
            }
        }
        if (satisfaction == true) {
            satisfactionProb = 1;
        }
        System.out.println("Verification property satisfaction probability: " + satisfactionProb);
    }
}
