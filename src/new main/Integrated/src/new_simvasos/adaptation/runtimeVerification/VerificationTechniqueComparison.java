package new_simvasos.adaptation.runtimeVerification;

//import JSci.maths.statistics.BinomialDistribution;
import org.apache.commons.math3.distribution.BinomialDistribution;


import javafx.util.Pair;
import new_simvasos.adaptation.FileManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;



public class VerificationTechniqueComparison {
    private static double tempCentroid = 24.75;
    private static double humiCentroid = 40.85;
    private static double accuracy = 0.99;

    private static int controlDegree;
    private static Double maxTemperatureControl;
    private static Double maxHumidityControl;
    private static Double windowOpenness;
    private static Double energyEfficiencyOfTemperatureControl;
    private static Double energyEfficiencyOfHumidityControl;

    private static double[][][] solutions;

    private static int maxNumSamples;

    public VerificationTechniqueComparison() {
        controlDegree = 20;
        maxTemperatureControl = 10.;
        maxHumidityControl = 10.;
        windowOpenness = 0.2;
        energyEfficiencyOfTemperatureControl = 1.;
        energyEfficiencyOfHumidityControl = 1.;

        maxNumSamples = 10000;

        solutions = new double[controlDegree*2+1][controlDegree*2+1][2];
        for(int i = 0; i < solutions.length; i++){
            for(int j = 0; j < solutions[i].length; j++){
                solutions[i][j][0] = (maxTemperatureControl/controlDegree) * i - maxTemperatureControl;
                solutions[i][j][1] = (maxHumidityControl/controlDegree) * j - maxHumidityControl;
            }
        }

    }

    public static void main (String[] args){
        VerificationTechniqueComparison verificationComparison = new VerificationTechniqueComparison();

        String environmentalDataDirName = "./src./new main./Integrated./src./new_simvasos./adaptation./runtimeVerification./environmentalDataset./";
        String environmentalDataFileName = "2018" + " " + "MON" + ".txt";
        String configFile = environmentalDataDirName + environmentalDataFileName;

        ArrayList<Pair<String, String>> config = FileManager.readConfiguration(configFile);

        ArrayList<Double> temperature = stringToList(FileManager.getValueFromConfigDictionary(config, "temperature"));
        ArrayList<Double> humidity = stringToList(FileManager.getValueFromConfigDictionary(config, "humidity"));

        int numComparison = 168;

        Random r = new Random();

        int[] numSamples = {10, 100, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000};
        File outputFileZ3 = new File("./src./new main./Integrated./src./new_simvasos./adaptation./runtimeVerification./rq2 report Z3.csv");
        File outputFileSPRT = new File("./src./new main./Integrated./src./new_simvasos./adaptation./runtimeVerification./rq2 report SPRT.csv");
        File outputFileSMCS = new File("./src./new main./Integrated./src./new_simvasos./adaptation./runtimeVerification./rq2 report SMCS.csv");
        File outputFileSSP = new File("./src./new main./Integrated./src./new_simvasos./adaptation./runtimeVerification./rq2 report SSP.csv");
        try {
            FileWriter fwZ3 = new FileWriter(outputFileZ3);
            FileWriter fwSPRT = new FileWriter(outputFileSPRT);
            FileWriter fwSMCS = new FileWriter(outputFileSMCS);
            FileWriter fwSSP = new FileWriter(outputFileSSP);

            fwZ3.write("out temp, out humi, in temp, in humi, temp boundary, humi boundary, Z3 temp, Z3 humi, Z3 time\n");
            for(int n = 0; n < numSamples.length; n++) {
                int sample = numSamples[n];
                fwSPRT.write("SPRT" + sample + " temp, humi, time,");
                fwSMCS.write("SMCS" + sample + " temp, humi, time,");
                fwSSP.write("SSP" + sample + " temp, humi, time,");
            }
            fwSPRT.write("\n");
            fwSMCS.write("\n");
            fwSSP.write("\n");

            for(int i = 0; i < numComparison; i++){
                System.out.println(i + "th comparison");

                int randIdx = r.nextInt(temperature.size());
                Double curOutTemp = temperature.get(randIdx);
                Double curOutHumi = humidity.get(randIdx);

                double curInTemp = 22.5 + (27.0 - 22.5) * r.nextDouble();
                double curInHumi = 19.8 + (79.5 - 19.8) * r.nextDouble();

                double tempForcastingBoundary = 7.0 * r.nextDouble();
                double humiForcastingBoundary = 10 + 15.0 * r.nextDouble();

                long planningStart;
                long planningEnd;
                System.out.println("out temp:" + curOutTemp + " out humi:" + curOutHumi + " in temp:" + curInTemp + " in humi:" + curInHumi + " " + " temp boundary:" + tempForcastingBoundary + " humi boundary:" + humiForcastingBoundary + "\n");
                fwZ3.write(curOutTemp + "," + curOutHumi + "," + curInTemp + "," + curInHumi + "," + tempForcastingBoundary + "," + humiForcastingBoundary + ",");

                //Z3
                planningStart = System.currentTimeMillis();
                double[] Z3solutions = greedyProactiveSearchWithZ3(curInTemp, curOutTemp - tempForcastingBoundary, curOutTemp + tempForcastingBoundary, curInHumi, curOutHumi - humiForcastingBoundary, curOutHumi + humiForcastingBoundary);
                planningEnd = System.currentTimeMillis();
                System.out.println("Z3 solution temp:" + Z3solutions[0] + " humi:" + Z3solutions[1] + " time:" + (planningEnd-planningStart)/1000F + "\n");
                fwZ3.write(Z3solutions[0] + "," + Z3solutions[1] + "," + (planningEnd-planningStart)/1000F + "\n");

                //SMC
                for(int n = 0; n < numSamples.length; n++){
                    maxNumSamples = numSamples[n];

                    //SPRT
                    planningStart = System.currentTimeMillis();
                    double[] SPRTsolutions = greedyProactiveSearchSPRT(curInTemp, curOutTemp - tempForcastingBoundary, curOutTemp + tempForcastingBoundary, curInHumi, curOutHumi - humiForcastingBoundary, curOutHumi + humiForcastingBoundary);
                    planningEnd = System.currentTimeMillis();
                    System.out.println("SPRT" + maxNumSamples + "solution temp:" + SPRTsolutions[0] + " humi:" + SPRTsolutions[1] + " time:" + (planningEnd-planningStart)/1000F);
                    fwSPRT.write(SPRTsolutions[0] + "," + SPRTsolutions[1] + "," + (planningEnd-planningStart)/1000F + ",");

                    //Simple Monte Carlo Simulation
                    planningStart = System.currentTimeMillis();
                    double[] SMCSsolutions = greedyProactiveSearchSMCS(curInTemp, curOutTemp - tempForcastingBoundary, curOutTemp + tempForcastingBoundary, curInHumi, curOutHumi - humiForcastingBoundary, curOutHumi + humiForcastingBoundary);
                    planningEnd = System.currentTimeMillis();
                    System.out.println("SMCS" + maxNumSamples + " solution temp:" + SMCSsolutions[0] + " humi:" + SMCSsolutions[1] + " time:" + (planningEnd-planningStart)/1000F);
                    fwSMCS.write(SMCSsolutions[0] + "," + SMCSsolutions[1] + "," + (planningEnd-planningStart)/1000F + ",");

                    //Single Sampling Planing (SSP)
                    planningStart = System.currentTimeMillis();
                    double[] SSPsolutions = greedyProactiveSearchSSP(curInTemp, curOutTemp - tempForcastingBoundary, curOutTemp + tempForcastingBoundary, curInHumi, curOutHumi - humiForcastingBoundary, curOutHumi + humiForcastingBoundary);
                    planningEnd = System.currentTimeMillis();
                    System.out.println("SSP" + maxNumSamples + " solution temp:" + SSPsolutions[0] + " humi:" + SSPsolutions[1] + " time:" + (planningEnd-planningStart)/1000F);
                    fwSSP.write(SSPsolutions[0] + "," + SSPsolutions[1] + "," + (planningEnd-planningStart)/1000F + ",");

                    System.out.println();
                }
                fwSPRT.write("\n");
                fwSMCS.write("\n");
                fwSSP.write("\n");
            }
            fwZ3.close();
            fwSPRT.close();
            fwSMCS.close();
            fwSSP.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double[] greedyProactiveSearchWithZ3(Double curInTemp, double forcastedOutdoorTemperatureMin, double forcastedOutdoorTemperatureMax, Double curInHumi, double forcastedOutdoorHumidityMin, double forcastedOutdoorHumidityMax) {
        boolean[][] validityEvaluation = new boolean[controlDegree*2+1][controlDegree*2+1];
        double[][] costEvaluation = new double[controlDegree*2+1][controlDegree*2+1];

        //centroid와 너무 먼 경우는 계산을 안하도록 구현
        double forcastedTempMin = curInTemp + windowOpenness * (forcastedOutdoorTemperatureMin - curInTemp);
        double forcastedTempMax = curInTemp + windowOpenness * (forcastedOutdoorTemperatureMax - curInTemp);
        boolean notSearch = false;
        if((forcastedTempMin - maxTemperatureControl < tempCentroid - 1.25 && forcastedTempMax + maxTemperatureControl < tempCentroid - 1.25) || (forcastedTempMin - maxTemperatureControl > tempCentroid + 2.25 && forcastedTempMax + maxTemperatureControl < tempCentroid + 2.25)){
            notSearch = true;
        }
        if(!notSearch) {
            for (int i = 0; i < solutions.length; i++) {
                for (int j = 0; j < solutions[i].length; j++) {
                    validityEvaluation[i][j] = evaluateNondeterministicValidityWithZ3(curInTemp, forcastedOutdoorTemperatureMin, forcastedOutdoorTemperatureMax, curInHumi, forcastedOutdoorHumidityMin, forcastedOutdoorHumidityMax, solutions[i][j][0], solutions[i][j][1]);
                    costEvaluation[i][j] = evaluateCost(i, j);
                }
            }
        }

        int bestTempIdx = -1;
        int bestHumiIdx = -1;
        boolean bestValidity = false;
        double bestCost = -1;

        for(int i = 0; i < solutions.length; i++){
            for(int j = 0; j < solutions[i].length; j++){
                if(bestTempIdx < 0 && bestHumiIdx < 0){
                    bestTempIdx = i;
                    bestHumiIdx = j;
                    bestCost = costEvaluation[i][j];
                    bestValidity = validityEvaluation[i][j];
                    continue;
                }
                if(!bestValidity){
                    if(validityEvaluation[i][j]){
                        bestTempIdx = i;
                        bestHumiIdx = j;
                        bestCost = costEvaluation[i][j];
                        bestValidity = true;
                        continue;
                    }
                    else{
                        if(costEvaluation[i][j] < bestCost){
                            bestTempIdx = i;
                            bestHumiIdx = j;
                            bestCost = costEvaluation[i][j];
                            continue;
                        }
                    }
                }
                else{
                    if(validityEvaluation[i][j]){
                        if(costEvaluation[i][j] < bestCost){
                            bestTempIdx = i;
                            bestHumiIdx = j;
                            bestCost = costEvaluation[i][j];
                            continue;
                        }
                    }
                    else{
                        continue;
                    }
                }
            }
        }

        if(bestValidity){
            Random r = new Random();
            double[] plan = {solutions[bestTempIdx][bestHumiIdx][0], solutions[bestTempIdx][bestHumiIdx][1], bestCost/controlDegree};
            return plan;
        }
        else{
            //another solution
            double[] plan = new double[3];
            double cost = 0;

            if(tempCentroid > curInTemp + maxTemperatureControl){
                plan[0] = maxTemperatureControl;
                cost = cost + controlDegree * energyEfficiencyOfTemperatureControl;
            }
            else if(tempCentroid < curInTemp - maxTemperatureControl){
                plan[0] = -maxTemperatureControl;
                cost = cost + controlDegree * energyEfficiencyOfTemperatureControl;
            }
            else{
                plan[0] = 0;
            }
            if(humiCentroid > curInHumi + maxHumidityControl){
                plan[1] = maxHumidityControl;
                cost = cost + controlDegree * energyEfficiencyOfHumidityControl;
            }
            else if(humiCentroid < curInHumi - maxHumidityControl){
                plan[1] = -maxHumidityControl;
                cost = cost + controlDegree * energyEfficiencyOfHumidityControl;
            }
            else{
                plan[1] = 0;
            }
            plan[2] = cost/controlDegree;

            return plan;
        }
    }

    private static boolean evaluateNondeterministicValidityWithZ3(Double curInTemp, double forcastedOutdoorTemperatureMin, double forcastedOutdoorTemperatureMax, Double curInHumi, double forcastedOutdoorHumidityMin, double forcastedOutdoorHumidityMax, double tempControl, double humiControl) {
        double forcastedIndoorTempMin = curInTemp + windowOpenness * (forcastedOutdoorTemperatureMin - curInTemp);
        double forcastedIndoorTempMax = curInTemp + windowOpenness * (forcastedOutdoorTemperatureMax - curInTemp);
        double forcastedIndoorHumiMin = curInHumi + windowOpenness * (forcastedOutdoorHumidityMin - curInHumi);
        double forcastedIndoorHumiMax = curInHumi + windowOpenness * (forcastedOutdoorHumidityMax - curInHumi);

        //todo
        FileWriter fw = null;
        try {
            fw = new FileWriter("D:\\z3-4.8.4.d6df51951f4c-x64-win\\bin\\script\\z3test.txt");

            fw.write("(declare-const t Real)\n");
            fw.write("(declare-const h Real)\n");
            fw.write("(declare-const tp Real)\n");
            fw.write("(declare-const hp Real)\n");
            fw.write("(declare-const h1 Real)\n");
            fw.write("(declare-const h2 Real)\n");
            fw.write("(declare-const h3 Real)\n");
            fw.write("(declare-const h4 Real)\n");
            fw.write("(assert (= tp (+ t " + tempControl + ")))\n");
            fw.write("(assert (= hp (+ h " + humiControl + ")))\n");
            fw.write("(assert (> t " + forcastedIndoorTempMin + "))\n");
            fw.write("(assert (< t " + forcastedIndoorTempMax + "))\n");
            fw.write("(assert (> h " + forcastedIndoorHumiMin + "))\n");
            fw.write("(assert (< h " + forcastedIndoorHumiMax + "))\n");
            fw.write("(assert (= h1 (+ (* -55.1 tp) 1319.25)))\n");
            fw.write("(assert (= h2 (+ (* -1.314 tp) 55.2857)))\n");
            fw.write("(assert (= h3 (+ (* -6.3427 tp) 222.214)))\n");
            fw.write("(assert (= h4 (+ (* -37.5 tp) 1032.3)))\n");
            fw.write("(define-fun comfort () Bool\n");
            fw.write("\t(and (and (> hp h1) (> hp h2)) (and (< hp h3) (< hp h4)))\n");
            fw.write(")\n");
            fw.write("(assert (not comfort))\n");
            fw.write("(check-sat)");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String line = "sat";
        boolean sat = true;
        try {
            String cmd = "D:\\z3-4.8.4.d6df51951f4c-x64-win\\bin\\z3 -smt2 D:\\z3-4.8.4.d6df51951f4c-x64-win\\bin\\script\\z3test.txt";
            //System.out.println("Executing command: " + cmd);
            Process p = Runtime.getRuntime().exec(cmd);
            int result = p.waitFor();

//            System.out.println("Process exit code: " + result);
//            System.out.println();
//            System.out.println("Result:");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                if(line.contains("unsat")){
                    sat = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!sat){
            return true;
        }
        else{
            return false;
        }
    }

    private static double[] greedyProactiveSearchSPRT(Double curInTemp, Double forcastedOutdoorTemperatureMin, Double forcastedOutdoorTemperatureMax, Double curInHumi, Double forcastedOutdoorHumidityMin, Double forcastedOutdoorHumidityMax){
        double[][] validityEvaluation = new double[controlDegree*2+1][controlDegree*2+1];
        double[][] costEvaluation = new double[controlDegree*2+1][controlDegree*2+1];

        //centroid와 너무 먼 경우는 계산을 안하도록 구현
        double forcastedTempMin = curInTemp + windowOpenness * (forcastedOutdoorTemperatureMin - curInTemp);
        double forcastedTempMax = curInTemp + windowOpenness * (forcastedOutdoorTemperatureMax - curInTemp);
        boolean notSearch = false;
        if((forcastedTempMin - maxTemperatureControl < tempCentroid - 1.25 && forcastedTempMax + maxTemperatureControl < tempCentroid - 1.25) || (forcastedTempMin - maxTemperatureControl > tempCentroid + 2.25 && forcastedTempMax + maxTemperatureControl < tempCentroid + 2.25)){
            notSearch = true;
        }
        if(!notSearch) {
            for (int i = 0; i < solutions.length; i++) {
                for (int j = 0; j < solutions[i].length; j++) {
                    validityEvaluation[i][j] = evaluateNondeterministicValiditySPRT(curInTemp, forcastedOutdoorTemperatureMin, forcastedOutdoorTemperatureMax, curInHumi, forcastedOutdoorHumidityMin, forcastedOutdoorHumidityMax, solutions[i][j][0], solutions[i][j][1]);
                    costEvaluation[i][j] = evaluateCost(i, j);
                }
            }
        }

        int bestTempIdx = -1;
        int bestHumiIdx = -1;
        double bestValidity = -1;
        double bestCost = -1;

        for(int i = 0; i < solutions.length; i++){
            for(int j = 0; j < solutions[i].length; j++){
                if(validityEvaluation[i][j] > bestValidity){
                    bestTempIdx = i;
                    bestHumiIdx = j;
                    bestValidity = validityEvaluation[i][j];
                    bestCost = costEvaluation[i][j];
                }
                else if(validityEvaluation[i][j] == bestValidity){
                    if(costEvaluation[i][j] < bestCost){
                        bestTempIdx = i;
                        bestHumiIdx = j;
                        bestValidity = validityEvaluation[i][j];
                        bestCost = costEvaluation[i][j];
                    }
                }
                else{
                }
            }
        }

        if(!notSearch && bestValidity > accuracy){
            Random r = new Random();
            double[] plan = {solutions[bestTempIdx][bestHumiIdx][0], solutions[bestTempIdx][bestHumiIdx][1], bestCost/controlDegree};
            return plan;
        }
        else{
            //another solution
            double[] plan = new double[3];
            double cost = 0;

            if(tempCentroid > curInTemp + maxTemperatureControl){
                plan[0] = maxTemperatureControl;
                cost = cost + controlDegree * energyEfficiencyOfTemperatureControl;
            }
            else if(tempCentroid < curInTemp - maxTemperatureControl){
                plan[0] = -maxTemperatureControl;
                cost = cost + controlDegree * energyEfficiencyOfTemperatureControl;
            }
            else{
                plan[0] = 0;
            }
            if(humiCentroid > curInHumi + maxHumidityControl){
                plan[1] = maxHumidityControl;
                cost = cost + controlDegree * energyEfficiencyOfHumidityControl;
            }
            else if(humiCentroid < curInHumi - maxHumidityControl){
                plan[1] = -maxHumidityControl;
                cost = cost + controlDegree * energyEfficiencyOfHumidityControl;
            }
            else{
                plan[1] = 0;
            }
            plan[2] = cost/controlDegree;

            return plan;
        }
    }

    private static boolean isInComfortZone(Double indoorTemperature, Double indoorHumidity){
        // https://www.mathsisfun.com/straight-line-graph-calculate.html 사용
        if(indoorHumidity > equation(-55.1, 1319.25, indoorTemperature) &&
                indoorHumidity > equation(-1.314, 55.2857, indoorTemperature) &&
                indoorHumidity < equation(-6.3429, 222.214, indoorTemperature) &&
                indoorHumidity < equation(-37.5, 1032.3, indoorTemperature)){
            return true;
        }
        else{
            return false;
        }
    }

    private static double evaluateNondeterministicValiditySPRT(Double curInTemp, Double forcastedOutdoorTemperatureMin, Double forcastedOutdoorTemperatureMax, Double curInHumi, Double forcastedOutdoorHumidityMin, Double forcastedOutdoorHumidityMax, double tempControl, double humiControl){
        int numSamples = maxNumSamples;
        ArrayList<Boolean> logs = new ArrayList<>();
        Random r = new Random();

        for(int i = 0; i < numSamples; i++){
            double forcastedOutdoorTemp = forcastedOutdoorTemperatureMin + (forcastedOutdoorTemperatureMax - forcastedOutdoorTemperatureMin) * r.nextDouble();
            double forcastedOutdoorHumi = forcastedOutdoorHumidityMin + (forcastedOutdoorHumidityMax - forcastedOutdoorHumidityMin) * r.nextDouble();
            double forcastedIndoorTemp = curInTemp + windowOpenness * (forcastedOutdoorTemp - curInTemp);
            double forcastedIndoorHumi = curInHumi + windowOpenness * (forcastedOutdoorHumi - curInHumi);
            logs.add(evaluateValidity(forcastedIndoorTemp, forcastedIndoorHumi, tempControl, humiControl));
        }

        //SPRT
        double satisfactionProb = 0;
        Boolean satisfaction = true;
        for (int i = 1; i < 100; i++) {
            double theta = i * 0.01;
            boolean verificationResult = SPRT(logs, numSamples, theta);

            if (satisfaction == true && !verificationResult) {
                satisfactionProb = theta;
                satisfaction = false;
            }
        }
        if (satisfaction == true) {
            satisfactionProb = 1;
        }
        return satisfactionProb;
    }

    private static boolean SPRT(ArrayList<Boolean> logs, int maxRepeat, double theta) {
        boolean ret;
        int numSamples;
        int numTrue;

        numSamples = 0;
        numTrue = 0;

        while (isSampleNeeded(numSamples, numTrue, theta)) {
            if (!(numSamples < maxRepeat)) {
                //System.out.println("Over maximum repeat: " + maxRepeat);
                break;
            }
            Random r = new Random();
            if (logs.get(r.nextInt(logs.size()))) {
                numTrue += 1;
            }
            numSamples += 1;
        }

        ret = isSatisfied(numSamples, numTrue, theta);

        // TODO Theta가 1일때 true 나오는 이유 확인
        if(theta == 1.00) ret = false;

        String verificationResult = "theta: " + Double.parseDouble(String.format("%.2f", theta)) +
                " numSamples: " + numSamples + " numTrue: " + numTrue + " result: " + ret;


        return ret;
    }

    private static boolean isSampleNeeded(int numSample, int numTrue, double theta) {
        double alpha = 0.05;
        double beta = 0.05;
        int minimumSample = 2;

        if (numSample < minimumSample) return true;

        double h0Threshold = beta / (1-alpha);
        double h1Threshold = (1-beta) / alpha;

        double v = getV(numSample, numTrue, theta);

        if (v <= h0Threshold) {
            return false;
        }
        else if (v >= h1Threshold) {
            return false;
        }
        else {
            return true;
        }
    }

    private static boolean isSatisfied(int numSamples, int numTrue, double theta) {
        double alpha = 0.05;
        double beta = 0.05;

        double h0Threshold = beta/(1-alpha);

        double v = getV(numSamples, numTrue, theta);

        if (v <= h0Threshold) {
            return true;
        } else  {
            return false;
        }
    }

    private static double getV(int numSample, int numTrue, double theta) {
        double delta = 0.01;
        double p0 = theta + delta;
        double p1 = theta - delta;

        int numFalse = numSample - numTrue;

        double p1m = Math.pow(p1, numTrue) * Math.pow((1-p1), numFalse);
        double p0m = Math.pow(p0, numTrue) * Math.pow((1-p0), numFalse);

        if (p0m == 0) {
            p1m = p1m + Double.MIN_VALUE;
            p0m = p0m + Double.MIN_VALUE;
        }

        return p1m / p0m;
    }

    private static double[] greedyProactiveSearchSMCS(Double curInTemp, Double forcastedOutdoorTemperatureMin, Double forcastedOutdoorTemperatureMax, Double curInHumi, Double forcastedOutdoorHumidityMin, Double forcastedOutdoorHumidityMax){
        double[][] validityEvaluation = new double[controlDegree*2+1][controlDegree*2+1];
        double[][] costEvaluation = new double[controlDegree*2+1][controlDegree*2+1];

        //centroid와 너무 먼 경우는 계산을 안하도록 구현
        double forcastedTempMin = curInTemp + windowOpenness * (forcastedOutdoorTemperatureMin - curInTemp);
        double forcastedTempMax = curInTemp + windowOpenness * (forcastedOutdoorTemperatureMax - curInTemp);
        boolean notSearch = false;
        if((forcastedTempMin - maxTemperatureControl < tempCentroid - 1.25 && forcastedTempMax + maxTemperatureControl < tempCentroid - 1.25) || (forcastedTempMin - maxTemperatureControl > tempCentroid + 2.25 && forcastedTempMax + maxTemperatureControl < tempCentroid + 2.25)){
            notSearch = true;
        }
        if(!notSearch) {
            for (int i = 0; i < solutions.length; i++) {
                for (int j = 0; j < solutions[i].length; j++) {
                    validityEvaluation[i][j] = evaluateNondeterministicValiditySMCS(curInTemp, forcastedOutdoorTemperatureMin, forcastedOutdoorTemperatureMax, curInHumi, forcastedOutdoorHumidityMin, forcastedOutdoorHumidityMax, solutions[i][j][0], solutions[i][j][1]);
                    costEvaluation[i][j] = evaluateCost(i, j);
                }
            }
        }

        int bestTempIdx = -1;
        int bestHumiIdx = -1;
        double bestValidity = -1;
        double bestCost = -1;

        for(int i = 0; i < solutions.length; i++){
            for(int j = 0; j < solutions[i].length; j++){
                if(validityEvaluation[i][j] > bestValidity){
                    bestTempIdx = i;
                    bestHumiIdx = j;
                    bestValidity = validityEvaluation[i][j];
                    bestCost = costEvaluation[i][j];
                }
                else if(validityEvaluation[i][j] == bestValidity){
                    if(costEvaluation[i][j] < bestCost){
                        bestTempIdx = i;
                        bestHumiIdx = j;
                        bestValidity = validityEvaluation[i][j];
                        bestCost = costEvaluation[i][j];
                    }
                }
                else{
                }
            }
        }

        if(!notSearch && bestValidity > accuracy){
            Random r = new Random();
            double[] plan = {solutions[bestTempIdx][bestHumiIdx][0], solutions[bestTempIdx][bestHumiIdx][1], bestCost/controlDegree};
            return plan;
        }
        else{
            //another solution
            double[] plan = new double[3];
            double cost = 0;

            if(tempCentroid > curInTemp + maxTemperatureControl){
                plan[0] = maxTemperatureControl;
                cost = cost + controlDegree * energyEfficiencyOfTemperatureControl;
            }
            else if(tempCentroid < curInTemp - maxTemperatureControl){
                plan[0] = -maxTemperatureControl;
                cost = cost + controlDegree * energyEfficiencyOfTemperatureControl;
            }
            else{
                plan[0] = 0;
            }
            if(humiCentroid > curInHumi + maxHumidityControl){
                plan[1] = maxHumidityControl;
                cost = cost + controlDegree * energyEfficiencyOfHumidityControl;
            }
            else if(humiCentroid < curInHumi - maxHumidityControl){
                plan[1] = -maxHumidityControl;
                cost = cost + controlDegree * energyEfficiencyOfHumidityControl;
            }
            else{
                plan[1] = 0;
            }
            plan[2] = cost/controlDegree;

            return plan;
        }
    }

    private static double evaluateNondeterministicValiditySMCS(Double curInTemp, Double forcastedOutdoorTemperatureMin, Double forcastedOutdoorTemperatureMax, Double curInHumi, Double forcastedOutdoorHumidityMin, Double forcastedOutdoorHumidityMax, double tempControl, double humiControl){
        int numSamples = maxNumSamples;
        ArrayList<Boolean> logs = new ArrayList<>();
        Random r = new Random();

        for(int i = 0; i < numSamples; i++){
            double forcastedOutdoorTemp = forcastedOutdoorTemperatureMin + (forcastedOutdoorTemperatureMax - forcastedOutdoorTemperatureMin) * r.nextDouble();
            double forcastedOutdoorHumi = forcastedOutdoorHumidityMin + (forcastedOutdoorHumidityMax - forcastedOutdoorHumidityMin) * r.nextDouble();
            double forcastedIndoorTemp = curInTemp + windowOpenness * (forcastedOutdoorTemp - curInTemp);
            double forcastedIndoorHumi = curInHumi + windowOpenness * (forcastedOutdoorHumi - curInHumi);
            logs.add(evaluateValidity(forcastedIndoorTemp, forcastedIndoorHumi, tempControl, humiControl));
        }

        //SMCS
        int numTrue = 0;
        for(int i = 0; i < logs.size(); i++) {
            if(logs.get(i)){
                numTrue++;
            }
        }
        return (double)numTrue/logs.size();
    }

    private static double[] greedyProactiveSearchSSP(Double curInTemp, Double forcastedOutdoorTemperatureMin, Double forcastedOutdoorTemperatureMax, Double curInHumi, Double forcastedOutdoorHumidityMin, Double forcastedOutdoorHumidityMax){
        double[][] validityEvaluation = new double[controlDegree*2+1][controlDegree*2+1];
        double[][] costEvaluation = new double[controlDegree*2+1][controlDegree*2+1];

        //centroid와 너무 먼 경우는 계산을 안하도록 구현
        double forcastedTempMin = curInTemp + windowOpenness * (forcastedOutdoorTemperatureMin - curInTemp);
        double forcastedTempMax = curInTemp + windowOpenness * (forcastedOutdoorTemperatureMax - curInTemp);
        boolean notSearch = false;
        if((forcastedTempMin - maxTemperatureControl < tempCentroid - 1.25 && forcastedTempMax + maxTemperatureControl < tempCentroid - 1.25) || (forcastedTempMin - maxTemperatureControl > tempCentroid + 2.25 && forcastedTempMax + maxTemperatureControl < tempCentroid + 2.25)){
            notSearch = true;
        }
        if(!notSearch) {
            for (int i = 0; i < solutions.length; i++) {
                for (int j = 0; j < solutions[i].length; j++) {
                    validityEvaluation[i][j] = evaluateNondeterministicValiditySSP(curInTemp, forcastedOutdoorTemperatureMin, forcastedOutdoorTemperatureMax, curInHumi, forcastedOutdoorHumidityMin, forcastedOutdoorHumidityMax, solutions[i][j][0], solutions[i][j][1]);
                    costEvaluation[i][j] = evaluateCost(i, j);
                }
            }
        }

        int bestTempIdx = -1;
        int bestHumiIdx = -1;
        double bestValidity = -1;
        double bestCost = -1;

        for(int i = 0; i < solutions.length; i++){
            for(int j = 0; j < solutions[i].length; j++){
                if(validityEvaluation[i][j] > bestValidity){
                    bestTempIdx = i;
                    bestHumiIdx = j;
                    bestValidity = validityEvaluation[i][j];
                    bestCost = costEvaluation[i][j];
                }
                else if(validityEvaluation[i][j] == bestValidity){
                    if(costEvaluation[i][j] < bestCost){
                        bestTempIdx = i;
                        bestHumiIdx = j;
                        bestValidity = validityEvaluation[i][j];
                        bestCost = costEvaluation[i][j];
                    }
                }
                else{
                }
            }
        }

        if(!notSearch && bestValidity > accuracy){
            Random r = new Random();
            double[] plan = {solutions[bestTempIdx][bestHumiIdx][0], solutions[bestTempIdx][bestHumiIdx][1], bestCost/controlDegree};
            return plan;
        }
        else{
            //another solution
            double[] plan = new double[3];
            double cost = 0;

            if(tempCentroid > curInTemp + maxTemperatureControl){
                plan[0] = maxTemperatureControl;
                cost = cost + controlDegree * energyEfficiencyOfTemperatureControl;
            }
            else if(tempCentroid < curInTemp - maxTemperatureControl){
                plan[0] = -maxTemperatureControl;
                cost = cost + controlDegree * energyEfficiencyOfTemperatureControl;
            }
            else{
                plan[0] = 0;
            }
            if(humiCentroid > curInHumi + maxHumidityControl){
                plan[1] = maxHumidityControl;
                cost = cost + controlDegree * energyEfficiencyOfHumidityControl;
            }
            else if(humiCentroid < curInHumi - maxHumidityControl){
                plan[1] = -maxHumidityControl;
                cost = cost + controlDegree * energyEfficiencyOfHumidityControl;
            }
            else{
                plan[1] = 0;
            }
            plan[2] = cost/controlDegree;

            return plan;
        }
    }

    private static double evaluateNondeterministicValiditySSP(Double curInTemp, Double forcastedOutdoorTemperatureMin, Double forcastedOutdoorTemperatureMax, Double curInHumi, Double forcastedOutdoorHumidityMin, Double forcastedOutdoorHumidityMax, double tempControl, double humiControl){
        int numSamples = maxNumSamples;
        ArrayList<Boolean> logs = new ArrayList<>();
        Random r = new Random();

        for(int i = 0; i < numSamples; i++){
            double forcastedOutdoorTemp = forcastedOutdoorTemperatureMin + (forcastedOutdoorTemperatureMax - forcastedOutdoorTemperatureMin) * r.nextDouble();
            double forcastedOutdoorHumi = forcastedOutdoorHumidityMin + (forcastedOutdoorHumidityMax - forcastedOutdoorHumidityMin) * r.nextDouble();
            double forcastedIndoorTemp = curInTemp + windowOpenness * (forcastedOutdoorTemp - curInTemp);
            double forcastedIndoorHumi = curInHumi + windowOpenness * (forcastedOutdoorHumi - curInHumi);
            logs.add(evaluateValidity(forcastedIndoorTemp, forcastedIndoorHumi, tempControl, humiControl));
        }

        int numTrue = 0;
        for(int i = 0; i < logs.size(); i++) {
            if(logs.get(i)){
                numTrue++;
            }
        }
        double currentSampleProb = (double)numTrue/logs.size();
        //System.out.println(currentSampleProb);
        double alpha = 0.05;
        double beta = 0.05;

        double satisfactionProb = 0;
        Boolean satisfaction = true;
        for (int i = 1; i < 100; i++) {
            double theta = i * 0.01;
            boolean verificationResult;

            BinomialDistribution bd = new BinomialDistribution(numSamples, theta);
            int p0 = bd.inverseCumulativeProbability(1.0-alpha);
            int p1 = bd.inverseCumulativeProbability(beta);
            if(numTrue > p0){
                verificationResult = true;
            }
            else if(numTrue < p1){
                verificationResult = false;
            }
            else{
                if(r.nextDouble() < currentSampleProb){
                    verificationResult = true;
                }
                else{
                    verificationResult = false;
                }
            }

            if (satisfaction == true && !verificationResult) {
                satisfactionProb = theta;
                satisfaction = false;
            }
            //System.out.println(i + " " + verificationResult);
        }
        if (satisfaction == true) {
            satisfactionProb = 1;
        }
        return satisfactionProb;
    }

    private static double equation(double c1, double c0, double x){
        return (c1 * x) + c0;
    }


    private static boolean evaluateValidity(Double curInTemp, Double curInHumi, double tempControl, double humiControl){
        return isInComfortZone(curInTemp + tempControl, curInHumi + humiControl);
    }

    private static double evaluateCost(int tempIdx, int humiIdx){
        int temperatureControlCost = abs(tempIdx - controlDegree);
        int humidityControlCost = abs(humiIdx - controlDegree);

        return (double)temperatureControlCost * energyEfficiencyOfTemperatureControl + (double)humidityControlCost * energyEfficiencyOfHumidityControl;
    }

    private static ArrayList<Double> stringToList(String dataListStr){
        ArrayList<Double> dataList = new ArrayList<Double>();
        dataListStr = dataListStr.replace("[", "");
        dataListStr = dataListStr.replace("]", "");
        String[] trend_array = dataListStr.split(",");
        for(String trend_value_string: trend_array){
            dataList.add(Double.parseDouble(trend_value_string));
        }
        return dataList;
    }
}
