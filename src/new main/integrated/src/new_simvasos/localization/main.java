package new_simvasos.localization;

import javafx.util.Pair;
import new_simvasos.localization.RescueSimulation;
import new_simvasos.log.Log;
import new_simvasos.log.Snapshot;
import new_simvasos.not_decided.SoS;
import new_simvasos.property.MCIProperty;
import new_simvasos.property.MCIPropertyChecker;
import new_simvasos.scenario.Event;
import new_simvasos.scenario.PatientOccurrence;
import new_simvasos.scenario.Scenario;
import new_simvasos.simulator.Simulator;
import new_simvasos.timebound.ConstantTimeBound;
import new_simvasos.localization.SPRT;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.StringTokenizer;

public class main {
    public static void main (String[] args) {
        // model pool generation
        double rescuePrmin = 0.85; // todo: test parameter: robot capability
        int numRescueRobot = 10; // todo: test parameter: the number of robots
        double  perceptionPrmin = 0.85; // todo: test parameter: drone capability
        int numPatrolDrone = 10; // todo: test parameter: the number of drones
        int numConnections = numRescueRobot; // todo: test parameter: number of interaction connections for each drone
        
        ArrayList<Double> rescuePrs = new ArrayList<>();
        ArrayList<Double> perceptionPrs = new ArrayList<>();
        Random r = new Random();
        for(int i = 0; i < numRescueRobot; i++) {
            rescuePrs.add(Math.round((rescuePrmin + r.nextDouble() * (1.0 - rescuePrmin)) * 100) / 100.0);
        }
        for(int i = 0; i < numPatrolDrone; i++) {
            perceptionPrs.add(Math.round((perceptionPrmin + r.nextDouble()* (1.0 - perceptionPrmin))*100)/100.0);
        }
        
        ArrayList<CS> RescueRobotCSs = new ArrayList();

        // Random number generation for Fault seeding
        ArrayList<Integer> randomValue = new ArrayList<>();
        randomGeneration(2, randomValue, numRescueRobot); //TODO test paprameter: FAULT SEEDING NUM for Robot
    
        //System.out.println(randomValue);
        for (int i = 0; i < numRescueRobot; i++) {
            RescueRobot rr;
            /*if(randomValue.contains(i)){  // fault seeding
                rr = new RescueRobot("robot" + Integer.toString(i), Math.round((rescuePrs.get(i)/2)*100)/100.0); // // todo: test parameter: FAULT SEEDING robot rescue ratio
            }
            else{
                rr = new RescueRobot("robot" + Integer.toString(i), rescuePrs.get(i));
            }*/
            rr = new RescueRobot("robot" + Integer.toString(i), rescuePrs.get(i));
            RescueRobotCSs.add(rr);
        }

        ArrayList<CS> PatrolDroneCSs = new ArrayList();
    
        randomGeneration(2, randomValue, numRescueRobot); //TODO test paprameter: FAULT SEEDING NUM
        //System.out.println(randomValue);
        int randCount = 0;
        for (int i = 0; i < numPatrolDrone; i++) {
            ArrayList<RescueRobot> connection = new ArrayList<>();
            ArrayList<Integer> delays = new ArrayList<>();
            for (int c = 0; c < numConnections; c++){
                connection.add((RescueRobot)RescueRobotCSs.get(c));
                if(randCount < 2 && randomValue.get(randCount) == i && c == i){ //&& randCount % 2 == 0){   // fault seeding
                    delays.add(50);
                    randCount++;
                }
                else{
                    delays.add(1);  // todo: test parameter: FAULT SEEDING interaction delay
                }
               // delays.add(1);
            }
            PatrolDrone pd;
            /*if(randCount < 2 && randomValue.get(randCount) == i ){//&& randCount % 2 == 1){  // fault seeding
                pd = new PatrolDrone("drone" + Integer.toString(i), Math.round((perceptionPrs.get(i)/2)*100)/100.0, connection, delays); // todo: test parameter: FAULT SEEDING drone speed
                randCount++;
            }
            else{
                pd = new PatrolDrone("drone" + Integer.toString(i), perceptionPrs.get(i), connection, delays);
            }*/
            pd = new PatrolDrone("drone" + Integer.toString(i), perceptionPrs.get(i), connection, delays);
            PatrolDroneCSs.add(pd);
        }
        
        configurationFileGeneration(RescueRobotCSs, PatrolDroneCSs);
        
        for (int i = 0; i <1; i++) { //TODO test parameter: Test number for a selected combination of drones and robots
            runTest(i, numRescueRobot, RescueRobotCSs, PatrolDroneCSs);
        }
    }

    private static void randomGeneration(int numFaultSeeding, ArrayList<Integer> randomValue, int numRescueRobot) {
        int temp;
        Random r = new Random();
        randomValue.clear();
        // Generate random index list without same numbers
        for(int i = 0; i < numFaultSeeding; i++) {
            temp = r.nextInt(numRescueRobot);
            while(randomValue.contains(temp)) temp = r.nextInt(numRescueRobot);
            randomValue.add(temp);
        }
        Collections.sort(randomValue);
    }
    
    private static void runTest(int testid, int numRescueRobot, ArrayList<CS> RescueRobotCSs, ArrayList<CS>PatrolDroneCSs) {
        //MCIProperty rescuedProperty;
        //MCIPropertyChecker rescuedChecker;
        //SPRT verifier;
        int simulationTime = 130; // todo: test parameter: simulation time
        //double goalRescueRatio = 0.8; // todo: test parameter: goal
        //rescuedProperty = new MCIProperty("RescuePatientProperty", "RescuedPatientRatioUpperThanValue", "MCIPropertyType", goalRescueRatio);
        //rescuedChecker = new MCIPropertyChecker();
        //verifier = new SPRT(rescuedChecker);
        //int repeatSim = 2000;
        //Pair<Pair<Integer, Boolean>, String> verificationResult;
        
        int numTest = 100;  //todo: test parameter: the number of the logs with same configuration
        for(int t = 0; t < numTest; t++) {
            // test model choice
            ArrayList<CS> robots = new ArrayList();
            ArrayList<CS> drones = new ArrayList();
        
            int numChosenRobots = 3; // todo: test parameter: the number of chosen robots
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
                System.out.print("robot"+i+", ");
            }
            for (int i : droneNumbers) {
                drones.add(PatrolDroneCSs.get(i));
                System.out.print("drone"+i+", ");
            }
        
            // Swap Robot numbers for increasing the probability of same numbers matching
            //System.out.println(robotNumbers);
            //System.out.println(droneNumbers);
            // todo: one to one connection: Yong-Jun Shin: a drone can send a message to only a robot.
            for (int i = 0; i<drones.size(); i++) {
                int posIndex = robotNumbers.indexOf(droneNumbers.get(i));
                if (posIndex != -1 && posIndex != i) {
                    Collections.swap(robotNumbers, posIndex, i);
                    Collections.swap(robots, posIndex, i);
                    //System.out.println(robotNumbers);
                }
                
            }
            for(int i = 0; i < drones.size(); i++) {
                drones.get(i).setSomething(robotNumbers.get(i));
            }
        
            // simulation initialization
            RescueSimulation sim1 = new RescueSimulation(simulationTime);
            sim1.initModels(robots, drones);
            sim1.initScenario();
        
            File file = new File("./localizationLog/LCS/Log" + testid + "_" + t + ".txt");
            try {
                PrintStream printStream = new PrintStream(new FileOutputStream(file));
                System.setErr(printStream);
            } catch(FileNotFoundException e) {
                System.out.println(e);
            }
            
            // single simulation
            //sim1.runSimulation().printSnapshot();
        
            // statistical verification
            /*double satisfactionProb = 0;
            Boolean satisfaction = true;
            for (int i = 1; i < 100; i++) {
                memoryManaging(RescueRobotCSs);
                memoryManaging(PatrolDroneCSs);
            
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
            logFileGeneration(testid, t, robots, drones, satisfactionProb * 100);*/
            
            // Single Simulation Result Record
            Log log = sim1.runSimulation();
            log.printSnapshot();
            String lastSnapshot = log.getSnapshotMap().get(simulationTime-1).getSnapshotString();
    
            StringTokenizer st = new StringTokenizer(lastSnapshot, " ");
            while(st.hasMoreTokens()) {
                if(st.nextToken().equals("RescuedRate:"))
                    break;
            }
    
            double rescueRate = Double.parseDouble(st.nextToken());
            logFileGeneration(testid, t, robots, drones, rescueRate * 100);
        }
    }
  
    private static void configurationFileGeneration(ArrayList<CS> RescueRobotCSs, ArrayList<CS> PatrolDroneCSs) {
        File file = new File("./localizationLog/LCS/configuration.csv");
        try {
            FileWriter fw = new FileWriter(file);
            for(int i = 0; i < RescueRobotCSs.size(); i++){
                fw.write(RescueRobotCSs.get(i).getName());
                fw.write(",");
                fw.write(Double.toString(RescueRobotCSs.get(i).getCapability()));
                fw.write("\n");
            }
            for(int i = 0; i < PatrolDroneCSs.size(); i++){
                fw.write(PatrolDroneCSs.get(i).getName());
                fw.write(",");
                fw.write(Double.toString(PatrolDroneCSs.get(i).getCapability()));
                fw.write("\n,");
                ArrayList<Integer> delays = ((PatrolDrone)PatrolDroneCSs.get(i)).getDelays();
                for(int j = 0; j < 10; j++) {//numRescueRobots == 10
                    fw.write("drone" + Integer.toString(i) + "/ robot" +Integer.toString(j) + " = " + delays.get(j));
                    fw.write(",");
                }
                fw.write("\n");
            }
            fw.write("\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void logFileGeneration(int testid, int id, ArrayList<CS> RescueRobotCSs, ArrayList<CS> PatrolDroneCSs, double result){
        File file = new File("./localizationLog/LCS/" + testid +"_" + id + ".csv");

        try {
            FileWriter fw = new FileWriter(file);
            for(int i = 0; i < RescueRobotCSs.size(); i++){
                fw.write(RescueRobotCSs.get(i).getName());
                fw.write(",");
            }
            for(int i = 0; i < PatrolDroneCSs.size(); i++){
                fw.write(PatrolDroneCSs.get(i).getName());
                if(i != PatrolDroneCSs.size()-1){
                    fw.write(",");
                }
            }
            fw.write("\n");
            for(int i = 0; i < RescueRobotCSs.size(); i++){
                /*for(int j = 0; j < PatrolDroneCSs.size(); j++){
                    fw.write("(" + RescueRobotCSs.get(i).getName() + "," + PatrolDroneCSs.get(j).getName() + ")");
                    if(i == RescueRobotCSs.size()-1 && j == PatrolDroneCSs.size()-1){
                        fw.write("\n");
                    }
                    else{
                        fw.write(",");
                    }
                }*/
                fw.write("(" + RescueRobotCSs.get(i).getName() + "," + PatrolDroneCSs.get(i).getName() + ")");
                //fw.write("("+ RescueRobotCSs.get(PatrolDroneCSs.get(i).getSomething()) + "," + PatrolDroneCSs.get(i).getName() + ")");
                if(i == RescueRobotCSs.size()-1){
                    fw.write("\n");
                }
                else{
                    fw.write(",");
                }
            }
            fw.write(Double.toString(result));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void memoryManaging(ArrayList<CS> CSs){
        for(CS cs: CSs){
            cs.reset();
        }
    }
}

