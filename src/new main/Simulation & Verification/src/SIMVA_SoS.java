import java.util.ArrayList;

public class SIMVA_SoS {
    public static void main (String[] args) {
        // Input generation
        //SoS sos;
        ConstantTimeBound constantTimeBound;
        PatientOccurrence patientOccurrence;
        Event event;
        Scenario MCIScenario;
        Simulator MCISim;
        MCIProperty rescuedProperty;
        MCIPropertyChecker rescuedChecker;
        SPRT verifier;
        double fireFighterPr = 0.8;
        int numFireFighter = 3;
        ArrayList<CS> CSs = new ArrayList();

        for (int i = 0; i < numFireFighter; i++) {      // start from zero or one?
            FireFighter fireFighter = new FireFighter(Integer.toString(i), fireFighterPr);
            CSs.add(fireFighter);
        }

        int mapSize = 20;
        ArrayList<Integer> MCIMap = new ArrayList<>();

        for (int i = 0; i < mapSize; i++) {
          MCIMap.add(0);
        }

        SoS MCISoS = new SoS(CSs, MCIMap);
        ArrayList MCIEvents = new ArrayList();
        int numPatients = 20;

        for(int j = 0; j < numPatients; j++) {
            constantTimeBound = new ConstantTimeBound(0);
            patientOccurrence = new PatientOccurrence("patient + 1", MCIMap);
            event = new Event(patientOccurrence, constantTimeBound);
            MCIEvents.add(event);
        }
        MCIScenario = new Scenario(MCIEvents);

        // Simulation
        int repeatSim = 2000;
        int simulationTime = 15;
        MCISim = new Simulator(simulationTime, MCISoS, MCIScenario);
        
        /*
        ArrayList<SimulationLog> MCILogs = new ArrayList<>();

        for(int i = 0; i < repeatSim; i++) {
            MCILogs.add(MCISim.run());
        }
        */
        // Verification
        rescuedProperty = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIPropertyType", 0.8);
        rescuedChecker = new MCIPropertyChecker();
        verifier = new SPRT(rescuedChecker);

        //System.out.println("Verify existed Logs");
        //verifier.verifyExistedLogs(MCILogs, rescuedProperty);
        //System.out.println();
        System.out.println("Verify with simulator");
        verifier.verifyWithSimulator(MCISim, rescuedProperty, repeatSim);
    }
}