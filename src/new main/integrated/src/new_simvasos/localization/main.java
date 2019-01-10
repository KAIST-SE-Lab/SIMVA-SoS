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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class main {
    public static void main (String[] args) {
        MCIProperty rescuedProperty;
        MCIPropertyChecker rescuedChecker;
        SPRT verifier;
        int simulationTime = 100; // todo: test parameter: simulation time
        double goalRescueRatio = 0.8; // todo: test parameter: goal
        rescuedProperty = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIPropertyType", goalRescueRatio);
        rescuedChecker = new MCIPropertyChecker();
        verifier = new SPRT(rescuedChecker);
        int repeatSim = 2000;
        Pair<Pair<Integer, Boolean>, String> verificationResult;

        // model pool generation
        double rescuePr = 1; // todo: test parameter: robot capability
        int numRescueRobot = 10; // todo: test parameter: the number of robots
        int speed = 5; // todo: test parameter: drone capability
        int numPatrolDrone = 10; // todo: test parameter: the number of drones
        int numConnections = numRescueRobot; // todo: test parameter: number of interaction connections for each drone

        ArrayList<CS> RescueRobotCSs = new ArrayList();

        for (int i = 0; i < numRescueRobot; i++) {
            RescueRobot rr;
            if(i < 2){  // fault seeding
                rr = new RescueRobot("robot" + Integer.toString(i), rescuePr/2);
            }
            else{
                rr = new RescueRobot("robot" + Integer.toString(i), rescuePr);
            }
            RescueRobotCSs.add(rr);
        }

        ArrayList<CS> PatrolDroneCSs = new ArrayList();

        for (int i = 0; i < numPatrolDrone; i++) {
            ArrayList<RescueRobot> connection = new ArrayList<>();
            ArrayList<Integer> delays = new ArrayList<>();
            for (int c = 0; c < numConnections; c++){
                connection.add((RescueRobot)RescueRobotCSs.get((i+c) % RescueRobotCSs.size()));
                if((i == 5 || i == 6) && c == 0){   // fault seeding
                    delays.add(10);
                }
                else{
                    delays.add(1);  // todo: test parameter: interaction delay
                }
            }
            PatrolDrone pd;
            if(i < 2){  // fault seeding
                pd = new PatrolDrone("drone" + Integer.toString(i), 1, connection, delays);
            }
            else{
                pd = new PatrolDrone("drone" + Integer.toString(i), speed, connection, delays);
            }
            PatrolDroneCSs.add(pd);
        }

        oracleFileGeneration(RescueRobotCSs, PatrolDroneCSs);

        // test loop start
        int numTest = 100;  //todo: test parameter: the number of the logs
        for(int t = 0; t < numTest; t++) {
            // test model choice
            ArrayList<CS> robots = new ArrayList();
            ArrayList<CS> drones = new ArrayList();

            int numChosenRobots = 4; // todo: test parameter: the number of chosen robots
            int numChosenDrones = 3; // todo: test parameter: the number of chosen drones

            ArrayList<Integer> robotNumbers = new ArrayList<>();
            Random randomGenerator = new Random();
            while (robotNumbers.size() < numChosenRobots) {
                int random = randomGenerator.nextInt(numRescueRobot);
                if (!robotNumbers.contains(random)) {
                    robotNumbers.add(random);
                }
            }
            ArrayList<Integer> droneNumbers = new ArrayList<>();
            while (droneNumbers.size() < numChosenDrones) {
                int random = randomGenerator.nextInt(numRescueRobot);
                if (!droneNumbers.contains(random)) {
                    droneNumbers.add(random);
                }
            }

            for (int i : robotNumbers) {
                robots.add(RescueRobotCSs.get(i));
            }
            for (int i : droneNumbers) {
                drones.add(PatrolDroneCSs.get(i));
            }

            // simulation initialization
            RescueSimulation sim1 = new RescueSimulation(simulationTime);
            sim1.initModels(robots, drones);
            sim1.initScenario();

            // single simulation
            sim1.runSimulation().printSnapshot();

            // statistical verification
            double satisfactionProb = 0;
            Boolean satisfaction = true;
            for (int i = 1; i < 100; i++) {
                double theta = i * 0.01;
                //Existence, Absence, Universality
                verificationResult = verifier.verifyWithSimulationGUI(sim1, rescuedProperty, repeatSim, theta);
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
            logFileGeneration(t, robots, drones, satisfactionProb * 100);
        }
        // test loop end
    }

    private static void oracleFileGeneration(ArrayList<CS> RescueRobotCSs, ArrayList<CS> PatrolDroneCSs){
        // todo: oracle file generation
        for(CS cs: RescueRobotCSs){
            cs.getCapability();
            cs.getName();
        }
        for(CS cs: PatrolDroneCSs){
            cs.getCapability();
            cs.getName();
        }
    }

    private static void logFileGeneration(int id, ArrayList<CS> RescueRobotCSs, ArrayList<CS> PatrolDroneCSs, double result){
        File file = new File("D:\\연구\\SIMVA-SoS\\SIMVA-SoS Code\\SIMVA-SoS\\src\\new main\\integrated\\src\\new_simvasos\\localization\\logs\\log" + id + ".csv");
        ArrayList<String> CSNames = new ArrayList<>();

        try {
            FileWriter fw = new FileWriter(file);
            for(int i = 0; i < RescueRobotCSs.size(); i++){
                fw.write(RescueRobotCSs.get(i).getName());
                if(i != RescueRobotCSs.size()-1){
                    fw.write(",");
                }
            }
            for(int i = 0; i < PatrolDroneCSs.size(); i++){
                fw.write(PatrolDroneCSs.get(i).getName());
                if(i != PatrolDroneCSs.size()-1){
                    fw.write(",");
                }
            }
            fw.write("\n");
            for(int i = 0; i < RescueRobotCSs.size(); i++){
                for(int j = 0; j < PatrolDroneCSs.size(); j++){
                    fw.write("(" + RescueRobotCSs.get(i).getName() + "," + PatrolDroneCSs.get(j).getName() + ")");
                    if(i == RescueRobotCSs.size()-1 && j == PatrolDroneCSs.size()-1){
                        fw.write("\n");
                    }
                    else{
                        fw.write(",");
                    }
                }
            }
            fw.write(Double.toString(result));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

