package mci;

import com.opencsv.CSVWriter;
import kr.ac.kaist.se.Executor;
import kr.ac.kaist.se.Util;
import kr.ac.kaist.se.mc.BaseChecker;
import kr.ac.kaist.se.simulator.*;
import kr.ac.kaist.se.simulator.method.SPRTMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Main.java
 * Author: Junho Kim <jhkim@se.kaist.ac.kr>

 * The MIT License (MIT)

 * Copyright (c) 2016 Junho Kim

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions: TBD
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception{

        // To prepare the result_directory
        Util.create_result_directory("mci_result");

        // Globally used (no need to replicate in concurrency)
        NormalDistributor distributor = new NormalDistributor();
        distributor.setNormalDistParams(2000, 500);

        // Set #1
        NearestPTS np1 = new NearestPTS();
        NearestPTS np2 = new NearestPTS();
        SeverityPTS sp1 = new SeverityPTS();
        SeverityPTS sp2 = new SeverityPTS();
        BaseConstituent[] CSs = new BaseConstituent[]{np1, np2, sp1, sp2};

        Hospital hos = new Hospital();
        ArrayList<BaseAction> rActions = new ArrayList<>();
        for (int i = 0; i < 100; i++)
            rActions.add(new RescueAction(0, 0));
        Environment env = new Environment(CSs, rActions.toArray(new BaseAction[rActions.size()]));


        ArrayList<BaseConstituent> CSAList = new ArrayList<>();
        CSAList.add(np1);
        CSAList.add(np2);
        CSAList.add(sp1);
        CSAList.add(sp2);

        Executor executor = new Executor();
        executor.addCSs(CSAList);
        executor.addActions(rActions);

        Simulator sim = new Simulator(CSs, hos, env);
        double[] albes = {0.01, 0.001};
        int bound = 70;
        for(double alpha_beta : albes) {
            Date nowDate = new Date();
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String pre = transFormat.format(nowDate);
            for (int trial = 1; trial <= 10; trial++) {

                String outputName = "mci_result/" + pre + "_MCI_" + bound + "_" + String.format("%.3f", alpha_beta) + "t"+ String.valueOf(trial) +".csv";
                CSVWriter cw = new CSVWriter(new OutputStreamWriter(new FileOutputStream(outputName), "UTF-8"), ',', '"');
                cw.writeNext(new String[]{"prob", "num_of_samples", "execution_time", "result"});
                ArrayList<SMCResult> resList = new ArrayList<>();

                System.out.println("----------------------------------------------------");
                System.out.println("SoS-level benefit is greater than and equal to " + bound + ".");
                BaseChecker checker = new BaseChecker(10000, bound, BaseChecker.comparisonType.GREATER_THAN_AND_EQUAL_TO);
                SPRTMethod sprt = new SPRTMethod(alpha_beta, alpha_beta, 0.01); // 신뢰도 99%

//        int thetaSet[] = {70,90,95,99};

                for (int t = 1; t < 100; t++) {
                    double theta = 0.01 * t; // theta
                    long start = System.currentTimeMillis();
                    sprt.setExpression(theta);
                    logger.info("--------------------------------------------- Theta : "
                            + String.format("%.2f", theta) + " -----------------------------------------------------");
                    long local_start = System.currentTimeMillis();

                    while (!sprt.checkStopCondition()) {
                        logger.info("CheckStopCondition :" + String.format("%.4f", (System.currentTimeMillis() - local_start) / 1000.0));

                        // Initialize Patient map
                        for (int i = 0; i <= 100; i++) {
                            Hospital.GeoMap.add(new MapPoint(i));
                        }
                        // 매번 다른 distribution이 필요함
                        ArrayList<Integer> list = new ArrayList<>();
                        list.clear();
                        list = distributor.getDistributionArray(100);
                        sim.setActionPlan(list);
                        sim.setEndTick(10000);

                        logger.info("Initialize :" + String.format("%.4f", (System.currentTimeMillis() - local_start) / 1000.0));

                        sim.execute();

                        logger.info("Simulator Execution :" + String.format("%.4f", (System.currentTimeMillis() - local_start) / 1000.0));

                        SIMResult res = sim.getResult();
                        int checkResult = checker.evaluateSample(res);
                        sprt.updateResult(checkResult);

                        System.gc();

                        sim.reset();
                        sim.setActionPlan(list);

                        logger.info("SPRT Calculation :" + String.format("%.2f", (System.currentTimeMillis() - local_start) / 1000.0));

                        local_start = System.currentTimeMillis();
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



}
