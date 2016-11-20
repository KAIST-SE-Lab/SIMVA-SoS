package kr.ac.kaist.se;

import com.opencsv.CSVWriter;
import kr.ac.kaist.se.mc.BaseChecker;
import kr.ac.kaist.se.simulator.NormalDistributor;
import kr.ac.kaist.se.simulator.SIMResult;
import kr.ac.kaist.se.simulator.Simulator;
import kr.ac.kaist.se.simulator.method.SPRTMethod;
import mci.SMCResult;

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

    public static double[] ARR_ALPHA_BETA = {0.001};

    public static void Perform_Experiment(NormalDistributor distributor, Simulator sim, String caseName, int bound) throws IOException {

        int endTick = 6000;
        for (double alpha_beta : ARR_ALPHA_BETA) {
            Date nowDate = new Date();
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String pre = transFormat.format(nowDate);

            for (int trial = 1; trial <= 3; trial++) {

                System.out.println("----------------------------------------------------");
                System.out.println("SoS-level benefit is greater than and equal to " + bound + ".");

                BaseChecker checker = new BaseChecker(endTick, bound, BaseChecker.comparisonType.GREATER_THAN_AND_EQUAL_TO);
                SPRTMethod sprt = new SPRTMethod(alpha_beta, alpha_beta, 0.01); // 신뢰도 99%
                ArrayList<SMCResult> resList = new ArrayList<>();
//        int thetaSet[] = {70,90,95,99};

                for (int t = 1; t < 100; t++) {
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

                        sim.setEndTick(endTick);

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
                    sprt.reset();
                    resList.add(new SMCResult(theta, numSamples, exec_time, minTick, maxTick, h0));

                    System.out.print("Theta: " + theta);
                    if (h0) {
                        System.out.print(" Result: T");
                    } else {
                        System.out.print(" Result: F");
                    }
                    System.out.print(" with n of samples: " + numSamples);
                    System.out.println(" in " + String.format("%.2f", exec_time / 1000.0) + " secs");

                }

                String outputName = caseName + "_result/" + pre + caseName + bound + "_" + String.format("%.3f", alpha_beta) + "t" + String.valueOf(trial) + ".csv";
                CSVWriter cw = new CSVWriter(new OutputStreamWriter(new FileOutputStream(outputName), "UTF-8"), ',', '"');
                cw.writeNext(new String[]{"prob", "num_of_samples", "execution_time", "result"});

                System.out.println();
                for (SMCResult r : resList) {
                    System.out.print(".");
                    cw.writeNext(r.getArr());
                }
                cw.close();
                resList.clear();
                System.out.println();

                System.out.println("Finished");

            }
        }
    }

    public static void Perform_Debug_Experiment(NormalDistributor distributor, Simulator sim, String caseName) throws IOException {
        int endTick = 10000;
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
            list = distributor.getDistributionArray(sim.getScenario().getActionList().size());
            sim.setActionPlan(list);

            sim.setEndTick(endTick);

            sim.execute(); // Simulation!
            SIMResult res = sim.getResult();
            HashMap<Integer, List<String>> debugTraces = sim.getDebugTraces();
            for(Map.Entry<Integer, List<String>> entry: debugTraces.entrySet()){
                List<String> each_tick_trace_list = entry.getValue();
                if(each_tick_trace_list.size() > 0){
                    String output = String.valueOf(entry.getKey()) + " ";
                    for(String each : each_tick_trace_list){
                        output += each;
                        output += " ";
//                        String[] en = {String.valueOf(entry.getKey()), each};
//                        cw.writeNext(en);
//                        System.out.println(en[0]+ " " +en[1]);
                    }
                    cw.writeNext(new String[]{output});
                }
            }
            sim.reset();
            sim.setActionPlan(list);

            cw.close();
        }

    }


}
