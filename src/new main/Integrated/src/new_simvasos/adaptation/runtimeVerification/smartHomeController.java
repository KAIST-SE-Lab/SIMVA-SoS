package new_simvasos.adaptation.runtimeVerification;

import javafx.util.Pair;
import new_simvasos.adaptation.FileManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;

public class smartHomeController {
    //environment
    private static Double currentOutdoorTemperature;
    private static Double currentOutdoorHumidity;
    private static Double currentIndoorTemperature;
    private static Double currentIndoorHumidity;
    private static ArrayList<Double> temperature;
    private static ArrayList<Double> humidity;

    //knowledge
    //environment
    private static ArrayList<Double> indoorTemperatureList;
    private static ArrayList<Double> outdoorTemperatureList;
    private static ArrayList<Double> indoorHumidityList;
    private static ArrayList<Double> outdoorHumidityList;
    private static double tempCentroid = 24.75;
    private static double humiCentroid = 40.85;

    //plan
    private static Double commandTemperature;
    private static Double commandHumidity;
    private static ArrayList<Double> commandTemperatureList;
    private static ArrayList<Double> commandHumidityList;
    private static ArrayList<Double> totalCost;

    //configuration
    private static Double windowOpenness;
    private static Double energyEfficiencyOfTemperatureControl;
    private static Double energyEfficiencyOfHumidityControl;

    private static Double maxTemperatureControl;
    private static Double maxHumidityControl;
    private static int controlDegree;
    private static double[][][] solutions;

    private static int knowledgeLength;
    private static int maxNumSamples;

    //PID controller
    private static float integral;
    private static float prevErr;
    private static float settingPoint;


    public smartHomeController(){
        currentOutdoorTemperature = 0.;
        currentOutdoorHumidity = 0.;
        currentIndoorTemperature = 0.;
        currentIndoorHumidity = 0.;

        indoorTemperatureList = new ArrayList<>();
        outdoorTemperatureList = new ArrayList<>();
        indoorHumidityList = new ArrayList<>();
        outdoorHumidityList = new ArrayList<>();

        commandTemperature = 0.;
        commandHumidity = 0.;
        commandTemperatureList = new ArrayList<>();
        commandHumidityList = new ArrayList<>();
        totalCost = new ArrayList<>();

        windowOpenness = 0.2;
        energyEfficiencyOfTemperatureControl = 1.;
        energyEfficiencyOfHumidityControl = 1.;

        controlDegree = 10;
        maxTemperatureControl = 10.;
        maxHumidityControl = 10.;
        knowledgeLength = 24 * 4;
        maxNumSamples = 10000;

        solutions = new double[controlDegree*2+1][controlDegree*2+1][2];
        for(int i = 0; i < solutions.length; i++){
            for(int j = 0; j < solutions[i].length; j++){
                solutions[i][j][0] = (maxTemperatureControl/controlDegree) * i - maxTemperatureControl;
                solutions[i][j][1] = (maxHumidityControl/controlDegree) * j - maxHumidityControl;
            }
        }

        integral = 0;
        prevErr =0;
        settingPoint = (float)1.5;
    }

    public static void main (String[] args){
        String[] years = {"2014"};//, "2015", "2016", "2017", "2018"};
        String[] days = {"SUN"};//, "MON", "TUE", "WED", "THU", "FRI", "SAT"};

        ArrayList<Double> costReportList = new ArrayList<>();
        ArrayList<Double> comfortReportList = new ArrayList<>();

        for(int x = 0; x < years.length; x++){
            for(int y = 0; y < days.length; y++){
                smartHomeController controller = new smartHomeController();
                int maxSimulationTime = 1248;

                String environmentalDataDirName = "./src./new main./Integrated./src./new_simvasos./adaptation./runtimeVerification./environmentalDataset./";
                String environmentalDataFileName = years[x] + " " + days[y] + ".txt";
                System.out.println(environmentalDataFileName);

                ArrayList<Integer> comfortList = new ArrayList<Integer>();
                ArrayList<Float> APTimeList = new ArrayList<>();
                ArrayList<Float> ATimeList = new ArrayList<>();
                ArrayList<Float> PTimeList = new ArrayList<>();
                ArrayList<Integer> numSampleList = new ArrayList<>();
                for(int t = 0; t < maxSimulationTime; t++){
//                    //scenario2
//                    if (t > maxSimulationTime/2){
//                        energyEfficiencyOfTemperatureControl = 3.;
//                    }
//                    //scenario3
//                    if (t > maxSimulationTime/2){
//                        energyEfficiencyOfHumidityControl = 3.;
//                    }

                    System.out.println(t);
                    environmentalChange(t, environmentalDataDirName + environmentalDataFileName);
                    monitor(t);
                    int option = 2;
                    long start = System.currentTimeMillis();
                    long analysisStart;
                    long planningStart;
                    long planningEnd;
                    if(option == 0){    //reactive
                        //analyze(t);
                        analysisStart = System.currentTimeMillis();
                        planningStart = System.currentTimeMillis();
                        reactivePlan(t);
                        planningEnd = System.currentTimeMillis();
                    }
                    else if(option == 1){    //deterministic forcasting
                        if(t <= knowledgeLength){
                            analysisStart = System.currentTimeMillis();
                            planningStart = System.currentTimeMillis();
                            reactivePlan(t);
                            planningEnd = System.currentTimeMillis();
                        }
                        else{
                            analysisStart = System.currentTimeMillis();
                            double[] forcastingInfo = analyze(t);
                            double forcastedOutdoorTemperature = forcastingInfo[0];
                            double forcastedOutdoorHumidity = forcastingInfo[3];
                            planningStart = System.currentTimeMillis();
                            deterministicProactivePlan(t, forcastedOutdoorTemperature, forcastedOutdoorHumidity);
                            planningEnd = System.currentTimeMillis();
                        }
                    }
                    else if(option == 2){   //nondeterministic forcasting
                        if(t <= knowledgeLength){
                            analysisStart = System.currentTimeMillis();
                            planningStart = System.currentTimeMillis();
                            reactivePlan(t);
                            planningEnd = System.currentTimeMillis();
                        }
                        else{
                            analysisStart = System.currentTimeMillis();
                            double[] forcastingInfo = analyze(t);
                            double forcastedOutdoorTemperatureMin = forcastingInfo[1];
                            double forcastedOutdoorTemperatureMax = forcastingInfo[2];
                            double forcastedOutdoorHumidityMin = forcastingInfo[4];
                            double forcastedOutdoorHumidityMax = forcastingInfo[5];
                            planningStart = System.currentTimeMillis();
                            nondeterministicProactivePlan(t, forcastedOutdoorTemperatureMin, forcastedOutdoorTemperatureMax, forcastedOutdoorHumidityMin, forcastedOutdoorHumidityMax);
                            planningEnd = System.currentTimeMillis();

                            numSampleList.add(maxNumSamples);
                            System.out.println(maxNumSamples + " " + (planningEnd-analysisStart)/1000F);
                            PIDController((planningEnd-analysisStart)/1000F);   //todo: 검토
                        }
                    }
                    else if(option == 3){   //nondeterministic forcasting with Z3
                        if(t <= knowledgeLength){
                            analysisStart = System.currentTimeMillis();
                            planningStart = System.currentTimeMillis();
                            reactivePlan(t);
                            planningEnd = System.currentTimeMillis();
                        }
                        else{
                            analysisStart = System.currentTimeMillis();
                            double[] forcastingInfo = analyze(t);
                            double forcastedOutdoorTemperatureMin = forcastingInfo[1];
                            double forcastedOutdoorTemperatureMax = forcastingInfo[2];
                            double forcastedOutdoorHumidityMin = forcastingInfo[4];
                            double forcastedOutdoorHumidityMax = forcastingInfo[5];
                            planningStart = System.currentTimeMillis();
                            nondeterministicProactivePlanWithZ3(t, forcastedOutdoorTemperatureMin, forcastedOutdoorTemperatureMax, forcastedOutdoorHumidityMin, forcastedOutdoorHumidityMax);
                            planningEnd = System.currentTimeMillis();
                        }
                    }
                    else{
                        analysisStart = System.currentTimeMillis();
                        planningStart = System.currentTimeMillis();
                        planningEnd = System.currentTimeMillis();
                        System.out.println("wrong option");
                    }
                    long end = System.currentTimeMillis();
                    APTimeList.add((end-start)/1000F);
                    ATimeList.add((planningStart-analysisStart)/1000F);
                    PTimeList.add((planningEnd-planningStart)/1000F);
                    effect(t);





                    if(isInComfortZone(currentIndoorTemperature, currentIndoorHumidity)){
                        comfortList.add(1);
                        System.out.println(1);
                    }
                    else{
                        comfortList.add(0);
                        System.out.println(0);
                    }

                }
                temperature = null;
                humidity = null;

                System.out.println(outdoorTemperatureList);
                System.out.println(commandTemperatureList);
                System.out.println(indoorTemperatureList);
                System.out.println();

                System.out.println(outdoorHumidityList);
                System.out.println(commandHumidityList);
                System.out.println(indoorHumidityList);
                System.out.println();

                System.out.println(APTimeList);
                System.out.println();

                System.out.println(totalCost);
                Double costSum = 0.;
                for(int i = 0; i < totalCost.size(); i++)
                {
                    costSum = costSum + totalCost.get(i);
                }
                System.out.println("total cost:" + costSum);
                System.out.println();

                System.out.println(comfortList);
                int comfortSum = 0;
                for(int i = 0; i < comfortList.size(); i++)
                {
                    comfortSum = comfortSum + comfortList.get(i);
                }
                System.out.println("comfort ratio:" + Double.toString((double)comfortSum/comfortList.size()));

                File outputFile = new File("./src./new main./Integrated./src./new_simvasos./adaptation./runtimeVerification./output "+ years[x] + " " + days[y] +".csv");
                try{
                    FileWriter fw = new FileWriter(outputFile);

                    fw.write("outTemp" + ',');
                    fw.write("outHumi" + ',');
                    fw.write("comTemp" + ',');
                    fw.write("comHumi" + ',');
                    fw.write("inTemp" + ',');
                    fw.write("inHumi" + ',');
                    fw.write("cost" + ',');
                    fw.write("comfort" + ',');
                    fw.write("AP time" + ',');
                    fw.write("A time" + ',');
                    fw.write("P time" + ',');
                    fw.write("samples" + '\n');

                    for(int i=0; i<outdoorTemperatureList.size(); i++){
                        fw.write(Double.toString(outdoorTemperatureList.get(i)) + ',');
                        fw.write(Double.toString(outdoorHumidityList.get(i)) + ',');
                        fw.write(Double.toString(commandTemperatureList.get(i)) + ',');
                        fw.write(Double.toString(commandHumidityList.get(i)) + ',');
                        fw.write(Double.toString(indoorTemperatureList.get(i)) + ',');
                        fw.write(Double.toString(indoorHumidityList.get(i)) + ',');
                        fw.write(Double.toString(totalCost.get(i)) + ',');
                        fw.write(Double.toString(comfortList.get(i)) + ',');
                        fw.write(Float.toString(APTimeList.get(i)) + ',');
                        fw.write(Float.toString(ATimeList.get(i)) + ',');
                        fw.write(Float.toString(PTimeList.get(i)) + ',');
                        fw.write(Integer.toString(numSampleList.get(i)) + '\n');
                    }
                    costReportList.add(costSum);
                    comfortReportList.add((double)comfortSum/comfortList.size());
                    fw.write("cost," + Double.toString(costSum)+",comfort,"+Double.toString((double)comfortSum/comfortList.size()));
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                controller = null;
            }
        }

        File reportFile = new File("./src./new main./Integrated./src./new_simvasos./adaptation./runtimeVerification./output report.csv");
        try {
            FileWriter rfw = new FileWriter(reportFile);
            for(int i=0; i <costReportList.size(); i++) {
                rfw.write(Double.toString(costReportList.get(i)) + ',');
                rfw.write(Double.toString(comfortReportList.get(i)) + '\n');
            }
            rfw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void environmentalChange(int time, String configFile){
        ArrayList<Pair<String, String>> config = FileManager.readConfiguration(configFile);

        if(temperature == null){
            temperature = stringToList(FileManager.getValueFromConfigDictionary(config, "temperature"));
        }
        if(humidity == null){
            humidity = stringToList(FileManager.getValueFromConfigDictionary(config, "humidity"));
        }

        currentOutdoorTemperature = temperature.get(time);
        currentOutdoorHumidity = humidity.get(time);

        if(time == 0){
            currentIndoorTemperature = tempCentroid;
            currentIndoorHumidity = humiCentroid;
        }
        else{
            currentIndoorTemperature = currentIndoorTemperature + windowOpenness * (currentOutdoorTemperature - currentIndoorTemperature);
            currentIndoorHumidity = currentIndoorHumidity + windowOpenness * (currentOutdoorHumidity - currentIndoorHumidity);
        }


        if (time!=0){
            currentIndoorTemperature = currentIndoorTemperature + commandTemperatureList.get(time-1);
            currentIndoorHumidity = currentIndoorHumidity + commandHumidityList.get(time-1);
        }
    }

    private static void monitor(int time){
        indoorTemperatureList.add(currentIndoorTemperature);
        indoorHumidityList.add(currentIndoorHumidity);
        outdoorTemperatureList.add(currentOutdoorTemperature);
        outdoorHumidityList.add(currentOutdoorHumidity);
    }

    private static double[] analyze(int time){
        double[] forcastedTemperature = forcasting(time, 0);
        double[] forcastedHumidity = forcasting(time, 1);

        double[] forcastingInfo = {forcastedTemperature[0], forcastedTemperature[1], forcastedTemperature[2], forcastedHumidity[0], forcastedHumidity[1], forcastedHumidity[2]}; //meanT, minT, maxT, meanH, minH, maxH
        return forcastingInfo;
    }

    private static double[] forcasting(int time, int factorOption){
        int seasonalDurationConstant = 24;

        ArrayList<Double> observedData;
        if(factorOption == 0){
            observedData = outdoorTemperatureList;
        }
        else{
            observedData = outdoorHumidityList;
        }

        ArrayList<Double> seasonalDiff = new ArrayList<>();
        for(int j = 0; j < knowledgeLength/seasonalDurationConstant; j++){
            int k1 = time - (j+1) * seasonalDurationConstant;
            double k1Value = observedData.get(k1);

            int k2 = k1 - 1;
            double k2Value = observedData.get(k2);

            seasonalDiff.add(k1Value - k2Value);
        }
        seasonalDiff.add(0.);

        Double minDiff = null;
        Double maxDiff = null;
        Double sumDiff = 0.;
        for(int j = 0; j < seasonalDiff.size(); j++){
            if(minDiff == null){
                minDiff = seasonalDiff.get(j);
            }
            else{
                if(minDiff > seasonalDiff.get(j)){
                    minDiff = seasonalDiff.get(j);
                }
            }

            if(maxDiff == null){
                maxDiff = seasonalDiff.get(j);
            }
            else{
                if(maxDiff < seasonalDiff.get(j)){
                    maxDiff = seasonalDiff.get(j);
                }
            }

            sumDiff += seasonalDiff.get(j);
        }
        Double meanDiff = sumDiff/seasonalDiff.size();

        double[] forcastedData = {observedData.get(time-1) + meanDiff, observedData.get(time-1) + minDiff, observedData.get(time-1) + maxDiff};
        return forcastedData;
    }

    private static void plan(int time){
        Double goal = 0.;
        Double curInTemp = indoorTemperatureList.get(time);
        Double curOutTemp = outdoorTemperatureList.get(time);
        Double curInHumi = indoorHumidityList.get(time);
        Double curOutHumi = outdoorHumidityList.get(time);

        commandTemperature = goal - (curInTemp + windowOpenness * (curOutTemp - curInTemp));
        commandHumidity = goal - (curInHumi + windowOpenness * (curOutHumi - curInHumi));
    }

    private static void reactivePlan(int time){
        Double curInTemp = indoorTemperatureList.get(time);
        Double curOutTemp = outdoorTemperatureList.get(time);
        Double curInHumi = indoorHumidityList.get(time);
        Double curOutHumi = outdoorHumidityList.get(time);

        Double expectedFutureIndoorTemperature = curInTemp + windowOpenness * (curOutTemp - curInTemp);
        Double expectedFutureIndoorHumidity = curInHumi + windowOpenness * (curOutHumi - curInHumi);

        double[] command = greedyReactiveSearch(expectedFutureIndoorTemperature, expectedFutureIndoorHumidity);
        commandTemperature = command[0];
        commandHumidity = command[1];
        totalCost.add(command[2]);
    }

    private static void deterministicProactivePlan(int time, double futureOutTemp, double futureOutHumi){
        Double curInTemp = indoorTemperatureList.get(time);
        Double curOutTemp = futureOutTemp;
        Double curInHumi = indoorHumidityList.get(time);
        Double curOutHumi = futureOutHumi;

        Double expectedFutureIndoorTemperature = curInTemp + windowOpenness * (curOutTemp - curInTemp);
        Double expectedFutureIndoorHumidity = curInHumi + windowOpenness * (curOutHumi - curInHumi);

        double[] command = greedyReactiveSearch(expectedFutureIndoorTemperature, expectedFutureIndoorHumidity);
        commandTemperature = command[0];
        commandHumidity = command[1];
        totalCost.add(command[2]);
    }

    private static void nondeterministicProactivePlan(int time, double forcastedOutdoorTemperatureMin, double forcastedOutdoorTemperatureMax, double forcastedOutdoorHumidityMin, double forcastedOutdoorHumidityMax){
        Double curInTemp = indoorTemperatureList.get(time);
        Double curInHumi = indoorHumidityList.get(time);

        double[] command = greedyProactiveSearch(curInTemp, forcastedOutdoorTemperatureMin, forcastedOutdoorTemperatureMax, curInHumi, forcastedOutdoorHumidityMin, forcastedOutdoorHumidityMax);
        commandTemperature = command[0];
        commandHumidity = command[1];
        totalCost.add(command[2]);
    }

    private static void nondeterministicProactivePlanWithZ3(int time, double forcastedOutdoorTemperatureMin, double forcastedOutdoorTemperatureMax, double forcastedOutdoorHumidityMin, double forcastedOutdoorHumidityMax) {
        Double curInTemp = indoorTemperatureList.get(time);
        Double curInHumi = indoorHumidityList.get(time);

        double[] command = greedyProactiveSearchWithZ3(curInTemp, forcastedOutdoorTemperatureMin, forcastedOutdoorTemperatureMax, curInHumi, forcastedOutdoorHumidityMin, forcastedOutdoorHumidityMax);
        commandTemperature = command[0];
        commandHumidity = command[1];
        totalCost.add(command[2]);
    }

    private static void effect(int time){
        if (abs(commandTemperature) > maxTemperatureControl){
            commandTemperature = maxTemperatureControl * (abs(commandTemperature)/commandTemperature);
        }
        if (abs(commandHumidity) > maxHumidityControl){
            commandHumidity = maxHumidityControl * (abs(commandHumidity)/commandHumidity);
        }

        commandTemperatureList.add(commandTemperature);
        commandHumidityList.add(commandHumidity);
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

    private static double[] greedyReactiveSearch(Double curInTemp, Double curInHumi){
        boolean[][] validityEvaluation = new boolean[controlDegree*2+1][controlDegree*2+1];
        double[][] costEvaluation = new double[controlDegree*2+1][controlDegree*2+1];

        for(int i = 0; i < solutions.length; i++){
            for(int j = 0; j < solutions[i].length; j++){
                validityEvaluation[i][j] = evaluateValidity(curInTemp, curInHumi, solutions[i][j][0], solutions[i][j][1]);
                costEvaluation[i][j] = evaluateCost(i, j);
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
            double[] plan = {solutions[bestTempIdx][bestHumiIdx][0] + ((double)r.nextInt(controlDegree)-(double)controlDegree/2)/controlDegree, solutions[bestTempIdx][bestHumiIdx][1] + ((double)r.nextInt(controlDegree)-(double)controlDegree/2)/controlDegree, bestCost/controlDegree};
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



    private static double[] greedyProactiveSearch(Double curInTemp, Double forcastedOutdoorTemperatureMin, Double forcastedOutdoorTemperatureMax, Double curInHumi, Double forcastedOutdoorHumidityMin, Double forcastedOutdoorHumidityMax){
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
                    validityEvaluation[i][j] = evaluateNondeterministicValidity(curInTemp, forcastedOutdoorTemperatureMin, forcastedOutdoorTemperatureMax, curInHumi, forcastedOutdoorHumidityMin, forcastedOutdoorHumidityMax, solutions[i][j][0], solutions[i][j][1]);
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

        if(!notSearch && bestValidity > 0.9){
            Random r = new Random();
            double[] plan = {solutions[bestTempIdx][bestHumiIdx][0] + ((double)r.nextInt(controlDegree)-(double)controlDegree/2)/controlDegree, solutions[bestTempIdx][bestHumiIdx][1] + ((double)r.nextInt(controlDegree)-(double)controlDegree/2)/controlDegree, bestCost/controlDegree};
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

    private static boolean evaluateValidity(Double curInTemp, Double curInHumi, double tempControl, double humiControl){
        return isInComfortZone(curInTemp + tempControl, curInHumi + humiControl);
    }

    private static double evaluateNondeterministicValidity(Double curInTemp, Double forcastedOutdoorTemperatureMin, Double forcastedOutdoorTemperatureMax, Double curInHumi, Double forcastedOutdoorHumidityMin, Double forcastedOutdoorHumidityMax, double tempControl, double humiControl){
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

    private static double equation(double c1, double c0, double x){
        return (c1 * x) + c0;
    }

    private static double evaluateCost(int tempIdx, int humiIdx){
        int temperatureControlCost = abs(tempIdx - controlDegree);
        int humidityControlCost = abs(humiIdx - controlDegree);

        return (double)temperatureControlCost * energyEfficiencyOfTemperatureControl + (double)humidityControlCost * energyEfficiencyOfHumidityControl;
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

    private static void PIDController(float currentTime) {  //todo: 검토, 학습
        double kp = 1000;
        double ki = 1;
        double kd = 1;
        int minNumSample = 100;

        float error = currentTime - settingPoint;
        integral += error;
        float derivative = error - prevErr;

        maxNumSamples = maxNumSamples - (int)(error * kp );//+ integral * ki + derivative * kd);

        if(maxNumSamples < minNumSample){
            maxNumSamples = minNumSample;
        }

        prevErr = error;
    }
}


