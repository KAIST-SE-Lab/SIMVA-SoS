package new_simvasos.simulation;//package new_simvasos;

import new_simvasos.log.Log;
import new_simvasos.not_decided.CS;
import new_simvasos.not_decided.FireFighter;
import new_simvasos.not_decided.SoS;
import new_simvasos.scenario.Event;
import new_simvasos.scenario.PatientOccurrence;
import new_simvasos.scenario.Scenario;
import new_simvasos.timebound.ConstantTimeBound;

import java.util.ArrayList;

/**
 * Example simulation based on simulation objects of <SIM-SoS>
 * An SoS consists of multiple firefighters to extinguish fire
 * This class extends Simulation to define a simulation execution unit.
 *
 * @author yjshin, ymbaek
 */
public class Simulation_Firefighters extends Simulation {
    public Simulation_Firefighters() {
        System.out.println("Simulation_Firefighter > Constructor 1");
    }

    public Simulation_Firefighters(int simulationTime) {
        System.out.println("Simulation_Firefighter > Constructor 2");
        setSimulationTime(simulationTime);
        System.out.println("Simulation_Firefighter > setSimulationTime: " + simulationTime);
    }

    public static void main(String []args) {
        System.out.println("Simulation_Firefighters > Main()");

        Simulation_Firefighters simFF = new Simulation_Firefighters(15);
        Log log = simFF.runSimulation();
        log.printSnapshot();

    }


    public Log runSimulation() {
        return simulator.run();
    }

    /**
     * (This function should be overridden.)
     * Three firefighters are included as an SoS model.
     * MCI map is included as the environment model.
     */
    @Override
    void initModels(){
        System.out.println("Simulation_Firefighters > initModels()");
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

        targetSoS = new SoS(CSs, MCIMap);
    }

    /**
     * (This function should be overridden.)
     * Number of patients is assigned to the map according to the scenario.
     */
    @Override
    void initScenario(){
        System.out.println("Simulation_Firefighters > initScenario()");

        ConstantTimeBound constantTimeBound;
        PatientOccurrence patientOccurrence;
        Event event;

        ArrayList MCIEvents = new ArrayList();
        int numPatients = 20;

        for(int j = 0; j < numPatients; j++) {
            constantTimeBound = new ConstantTimeBound(0);
            patientOccurrence = new PatientOccurrence("patient + 1", targetSoS.getEnvironment());
            event = new Event(patientOccurrence, constantTimeBound);
            MCIEvents.add(event);
        }
        targetScenario = new Scenario(MCIEvents);
    }
}
