package kr.ac.kaist.se;

import christian.RobotScenario;
import com.opencsv.CSVWriter;
import kr.ac.kaist.se.mc.CheckerInterface;
import kr.ac.kaist.se.simulator.*;
import kr.ac.kaist.se.simulator.method.SPRTMethod;
import mci.SMCResult;
import mci.checker.*;
import mci.scenario.MCIScenario;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Executor.java
 * Author: Junho Kim <jhkim@se.kaist.ac.kr>
 * <p>
 * Simulation Executor class
 * <p>
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 Junho Kim
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions: TBD
 */

public class Executor {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static double[] ARR_ALPHA_BETA = {0.05}; // 0.001 for MCI, 0.05 for Robot

    public static void Perform_Experiment(NormalDistributor distributor, String params) throws IOException {
        //Simulator sim, String caseName
        String args[] = params.split(",");

        int scenarioType = 2; // MCI

        BaseScenario bs = null;
        Simulator sim = null;
        if (args[0].equalsIgnoreCase("Robot")) {
            bs = new RobotScenario(0);
        } else if (args[0].equalsIgnoreCase("Robot_b1")) {
            bs = new RobotScenario(1);
        } else if (args[0].equalsIgnoreCase("Robot_b2")) {
            bs = new RobotScenario(2);
        } else if (args[0].equalsIgnoreCase("Robot_b3")) {
            bs = new RobotScenario(3);
        } else if (args[0].equalsIgnoreCase("Robot_b4")) {
            bs = new RobotScenario(4);
        } else if (args[0].equalsIgnoreCase("Robot_b5")) {
            bs = new RobotScenario(5);
        } else if (args[0].equalsIgnoreCase("MCI")) {
            bs = new MCIScenario(Integer.parseInt(args[3]), Integer.parseInt(args[4]));
        } else {
            // undefined scenario
        }
        sim = new Simulator(bs);
        sim.setDEBUG();

        CheckerInterface checker = null;
        if (args[0].startsWith("Robot")) {
            checker = new RobotChecker();
            scenarioType = 1; // Robot
        } else if (args[1].equalsIgnoreCase("Existence")) {
            checker = new Existence();
        } else if (args[1].equalsIgnoreCase("Absence")) {
            checker = new Absence();
        } else if (args[1].equalsIgnoreCase("Universality")) {
            checker = new Universality();
        } else if (args[1].equalsIgnoreCase("TransientStateProbability")) {
            checker = new TransientStateProbability();
        } else {
            // Undefined Checker
        }
        checker.init(args);

        System.out.println("==========================================\n" +
                "[ Simulation Description ]\n" +
                "Parameters: " + params + "\n" +
                "Scenario: " + sim.getScenario().getDescription() + "\n" +
                "Checker: " + checker.getName() + "\n" +
                "Statement: " + ANSI_RED + checker.getDescription() + ANSI_RESET);

        System.out.println("==========================================\n" +
                "[ Simulation Log ]");

        long totalstart = System.currentTimeMillis();
        long totaltime = 0;
        int totalsamples = 0;
        String finalres = "True";

        for (double alpha_beta : ARR_ALPHA_BETA) {
            Date nowDate = new Date();
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String pre = transFormat.format(nowDate);

            checker.init(args);
            SPRTMethod sprt = new SPRTMethod(alpha_beta, alpha_beta, 0.01); // 신뢰도 99%
//            ArrayList<SMCResult> resList = new ArrayList<>();

            for (int t = 1; t <= 95; ) {
                double theta = 0.01 * t; // theta

                int h0 = 0;
                int h1 = 0;
                int numThetaSamples = 0;
                long exec_ThetaTime = 0;

                for (int i = 0; i < 100; ) {
                    long start = System.currentTimeMillis();

                    sprt.setExpression(theta);
                    sprt.reset();

                    while (!sprt.checkStopCondition()) {

                        // Initialize Patient map
                        sim.getScenario().init();

                        // 매번 다른 distribution 이 필요함
                        ArrayList<Integer> list = new ArrayList<>();
                        list.clear();
                        list = distributor.getDistributionArray(sim.getScenario().getActionList().size());
                        sim.setActionPlan(list);

                        //sim.setEndTick(endTick);

                        sim.execute();

                        SIMResult res = sim.getResult();
                        int checkResult = checker.evaluateSample(res);
                        sprt.updateResult(checkResult);

//                        System.gc();

                        sim.reset();
                        sim.setActionPlan(list);
                    }

                    boolean res = sprt.getResult();

                    if (res) {
                        h0++;
//                        System.out.print("T");
                    } else {
                        h1++;
//                        System.out.print("F");
                    }

                    int numSamples = sprt.getNumSamples();
                    long exec_time = System.currentTimeMillis() - start; //exec time
                    int minTick = checker.getMinTick();
                    int maxTick = checker.getMaxTick();

                    numThetaSamples += numSamples;
                    exec_ThetaTime += exec_time;

                    totalsamples += numSamples;
                    totaltime += exec_time;

//                    resList.add(new SMCResult(theta, numSamples, exec_time, minTick, maxTick, res));
                    if (scenarioType == 1)
                        i++;
                    else
                        i += 34;
                }

                System.out.print("The statement is");

                if (h0 >= h1) {
                    System.out.print(" TRUE\t");
                    if (theta > Double.parseDouble(args[2]))
                        finalres = "False";
                } else {
                    System.out.print(" FALSE\t");
                    if (theta <= Double.parseDouble(args[2]))
                        finalres = "False";
                }

                System.out.print("for theta: " + String.format("%.2f", theta));
                if (scenarioType == 1)
                    System.out.print(" by examining " + (numThetaSamples/100) + " samples\t");
                else
                    System.out.print(" by examining " + (numThetaSamples/3) + " samples\t");

                //if (scenarioType == 1)
                System.out.print("(T: " + String.format("%03d", h0) + ", F: " + String.format("%03d", h1) + ")\t");

                System.out.println("[Time to Decide: " + String.format("%.2f", exec_ThetaTime / 1000.0) + " secs]");

                if (scenarioType == 1)
                    t++;
                else
                    t++;
            }

//            String outputName = caseName + "_result/" + pre + caseName + bound + "_" + String.format("%.3f", alpha_beta) + "t" + String.valueOf(trial) + ".csv";
//            CSVWriter cw = new CSVWriter(new OutputStreamWriter(new FileOutputStream(outputName), "UTF-8"), ',', '"');
//            cw.writeNext(new String[]{"prob", "num_of_samples", "execution_time", "result"});
//
//            for (SMCResult r : resList) {
//                System.out.print(".");
//                cw.writeNext(r.getArr());
//            }
//            cw.close();
//            resList.clear();
            System.out.println();
        }

        System.out.println("==========================================\n" +
                "[ Simulation Result ]\n" +
                "Result: The statement <" + checker.getDescription() + "> is " + ANSI_RED + finalres + ANSI_RESET + " with a probability <= " + ANSI_RED + args[2] + ANSI_RESET + ".\n" +
                "Total Examined Samples: " + totalsamples + " samples\n" +
                "Total Time to Decide: " + String.format("%.2f", totaltime / 1000.0) + " secs\n" +
                "Total Elapsed Time: " + String.format("%.2f", (System.currentTimeMillis() - totalstart) / 1000.0) + " secs\n" +
                "==========================================\n" +
                "Finished.");
    }

    public static void Perform_Experiment(NormalDistributor distributor, Simulator sim, String caseName, int bound) throws IOException {
        int endTick = sim.getScenario().getEndTick();

        Existence checker = new Existence();
        checker.init(endTick, bound, Existence.comparisonType.GREATER_THAN_AND_EQUAL_TO);

        System.out.println("==========================================\n" +
                            "[ Simulation Description ]\n" +
                            "Scenario: " + sim.getScenario().getDescription() + "\n" +
                            "Checker: " + checker.getName() + "\n" +
                            "Statement: " + checker.getDescription());

        System.out.println("==========================================\n" +
                            "[ Simulation Log ]");

        long totalstart = System.currentTimeMillis();
        long totaltime = 0;
        int totalsamples = 0;

        for (double alpha_beta : ARR_ALPHA_BETA) {
            Date nowDate = new Date();
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String pre = transFormat.format(nowDate);

            for (int trial = 1; trial <= 3; trial++) {
                System.out.println("Trial " + trial + " is Started");
                checker.init(endTick, bound, Existence.comparisonType.GREATER_THAN_AND_EQUAL_TO);
                SPRTMethod sprt = new SPRTMethod(alpha_beta, alpha_beta, 0.01); // 신뢰도 99%
                ArrayList<SMCResult> resList = new ArrayList<>();
//        int thetaSet[] = {70,90,95,99};

                for (int t = 80; t < 81; t++) {
                    double theta = 0.01 * t; // theta
                    long start = System.currentTimeMillis();
                    sprt.setExpression(theta);

                    while (!sprt.checkStopCondition()) {

                        // Initialize Patient map
                        sim.getScenario().init();

                        // 매번 다른 distribution 이 필요함
                        ArrayList<Integer> list = new ArrayList<>();
                        list.clear();
                        list = distributor.getDistributionArray(sim.getScenario().getActionList().size());
                        sim.setActionPlan(list);

                        //sim.setEndTick(endTick);

                        sim.execute();

                        SIMResult res = sim.getResult();
                        int checkResult = checker.evaluateSample(res);
                        sprt.updateResult(checkResult);

                        System.gc();

                        sim.reset();
                        sim.setActionPlan(list);

                    }

                    boolean h0 = sprt.getResult(); // Result
                    int numSamples = sprt.getNumSamples();

                    long exec_time = System.currentTimeMillis() - start; //exec time
                    int minTick = checker.getMinTick();
                    int maxTick = checker.getMaxTick();

                    totaltime += exec_time;
                    totalsamples += numSamples;

                    sprt.reset();
                    resList.add(new SMCResult(theta, numSamples, exec_time, minTick, maxTick, h0));

                    System.out.print("The statement is");

                    if (h0) {
                        System.out.print(" TRUE");
                    } else {
                        System.out.print(" FALSE");
                    }

                    System.out.print(" for theta: " + String.format("%.2f", theta));
                    System.out.print(" by examining " + numSamples + " samples");
                    System.out.println(" [Time to Decide: " + String.format("%.2f", exec_time / 1000.0) + " secs]");
                }

                String outputName = caseName
                        + "_result/"
                        + pre
                        + caseName
                        + bound
                        + "_"
                        + String.format("%.3f", alpha_beta)
                        + "t"
                        + String.valueOf(trial)
                        + ".csv";
                CSVWriter cw = new CSVWriter(new OutputStreamWriter(new FileOutputStream(outputName), "UTF-8"), ',', '"');
                cw.writeNext(new String[]{"prob", "num_of_samples", "execution_time", "result"});

                for (SMCResult r : resList) {
                    System.out.print(".");
                    cw.writeNext(r.getArr());
                }
                cw.close();
                resList.clear();
                System.out.println();

                System.out.println("Trial " + trial + " is Finished");
            }
        }

        System.out.println("==========================================\n" +
                            "[ Simulation Result ]\n" +
                            "Result: The statement is --- with a probability --- than --.\n" +
                            "Total Examined Samples: " + totalsamples + " samples\n" +
                            "Total Time to Decide: " + String.format("%.2f", totaltime / 1000.0) + " secs\n" +
                            "Total Elapsed Time: " + String.format("%.2f", (System.currentTimeMillis() - totalstart) / 1000.0) + " secs\n" +
                            "==========================================\n" +
                            "Finished.");
    }

    public static void Perform_Debug_Experiment(NormalDistributor distributor, Simulator sim, String caseName) throws IOException {
        int endTick = 6000;

        for (double alpha_beta : ARR_ALPHA_BETA) {
            Date nowDate = new Date();
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String pre = transFormat.format(nowDate);

            String outputName = caseName + "_result/" + pre + caseName + String.format("%.3f", alpha_beta) + "_debug.csv";
            CSVWriter cw = new CSVWriter(new OutputStreamWriter(new FileOutputStream(outputName), "UTF-8"), ',', '"');

            // Initialize Patient map
            sim.getScenario().init();

            // 매번 다른 distribution 이 필요함
            ArrayList<Integer> list = new ArrayList<>();
            list.clear();
            list = distributor.getDistributionArray(500);
            sim.setActionPlan(list);

            sim.execute(); // Simulation!
            SIMResult res = sim.getResult();
            HashMap<Integer, DebugTick> debugTraces = sim.getDebugTraces();

            sim.reset();
            sim.setActionPlan(list);

            cw.close();
        }
    }

}
