package new_simvasos.localization;

import new_simvasos.log.Log;
import new_simvasos.localization.CS;
import new_simvasos.not_decided.FireFighter;
import new_simvasos.localization.SoS;
import new_simvasos.property.MCIProperty;
import new_simvasos.property.MCIPropertyChecker;
import new_simvasos.scenario.Event;
import new_simvasos.scenario.PatientOccurrence;
import new_simvasos.scenario.Scenario;
import new_simvasos.localization.Simulation;
import new_simvasos.simulation.Simulation_Firefighters;
import new_simvasos.simulator.Simulator;
import new_simvasos.timebound.ConstantTimeBound;
import new_simvasos.verifier.SPRT;

import java.util.ArrayList;

public class RescueSimulation extends Simulation {

    public RescueSimulation() {
        System.out.println("Rescue Simulation > Constructor 1");
    }

    public RescueSimulation(int simulationTime) {
        System.out.println("Rescue Simulation > Constructor 2");
        setSimulationTime(simulationTime);
        System.out.println("Rescue Simulation > setSimulationTime: " + simulationTime);
    }

    public static void main(String []args) {
        System.out.println("Rescue Simulation > Main()");

        RescueSimulation sim = new RescueSimulation(100);
        Log log = sim.runSimulation();
        log.printSnapshot();
    }

    public Log runSimulation() {
        this.targetSoS.reset();
        return simulator.run(this.targetSoS, this.targetScenario);
    }

    /**
     * (This function should be overridden.)
     * Three firefighters are included as an SoS model.
     * MCI map is included as the environment model.
     */
    @Override
    public void initModels(){   //todo
        System.out.println("Rescue Simulation > initModels()");

        double rescuePr = 0.4;
        int numRescueRobot = 10;
        int speed = 4;
        int numPatrolDrone = 10;

        //double fireFighterPr = 0.5;
        // for Mutation Testing
        //double fireFighterPr = 0.8;
        //int numFireFighter = 2;

        ArrayList<CS> RescueRobotCSs = new ArrayList();

        for (int i = 0; i < numRescueRobot; i++) {
            RescueRobot rr = new RescueRobot("robot" + Integer.toString(i), rescuePr);
            RescueRobotCSs.add(rr);
        }

        ArrayList<CS> PatrolDroneCSs = new ArrayList();

        for (int i = 0; i < numPatrolDrone; i++) {
            ArrayList<RescueRobot> connection = new ArrayList<>();
            connection.add((RescueRobot)RescueRobotCSs.get(i));
            PatrolDrone pd = new PatrolDrone("drone" + Integer.toString(i), speed, connection);
            PatrolDroneCSs.add(pd);
        }


        //int mapSize = 300;
        // for Mutation Testing
        int mapSize = 20;
        ArrayList<ArrayList<Integer>> MCIMap = new ArrayList<>();

        for (int i = 0; i < mapSize; i++) {
            ArrayList<Integer> arr = new ArrayList<>();
            MCIMap.add(arr);
            for (int j = 0; j < mapSize; j++) {
                MCIMap.get(i).add(0);
            }
        }

        //targetSoS = new SoS(CSs, MCIMap);
        ArrayList<CS> CSs = (ArrayList<CS>)RescueRobotCSs.clone();
        CSs.addAll(PatrolDroneCSs);
        targetSoS = new SoS(CSs, MCIMap);
    }

    /**
     * (This function should be overridden.)
     * Number of patients is assigned to the map according to the scenario.
     */
    @Override
    void initScenario(){
        ConstantTimeBound constantTimeBound;
        PatientOccurrence2D patientOccurrence;
        Event event;

        ArrayList MCIEvents = new ArrayList();
        int numPatients = 100;

        for(int j = 0; j < numPatients; j++) {
            constantTimeBound = new ConstantTimeBound(0);
            patientOccurrence = new PatientOccurrence2D("patient + 1", targetSoS.getEnvironment());
            event = new Event(patientOccurrence, constantTimeBound);
            MCIEvents.add(event);
        }
        targetScenario = new Scenario(MCIEvents);
    }
}
