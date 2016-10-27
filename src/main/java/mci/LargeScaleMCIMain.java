package mci;

import com.opencsv.CSVWriter;
import kr.ac.kaist.se.mc.BaseChecker;
import kr.ac.kaist.se.simulator.*;
import kr.ac.kaist.se.simulator.method.SPRTMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * LargeScaleMCIMain.java
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
public class LargeScaleMCIMain {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception{
        NormalDistributor distributor = new NormalDistributor();
        distributor.setNormalDistParams(2000, 600);

        NearestPTS np1 = new NearestPTS();
        NearestPTS np2 = new NearestPTS();
        NearestPTS np3 = new NearestPTS();
        NearestPTS np4 = new NearestPTS();
        NearestPTS np5 = new NearestPTS();
        NearestPTS np6 = new NearestPTS();
        NearestPTS np7 = new NearestPTS();
        NearestPTS np8 = new NearestPTS();
        NearestPTS np9 = new NearestPTS();
        NearestPTS np10 = new NearestPTS();

        SeverityPTS sp1 = new SeverityPTS();
        SeverityPTS sp2 = new SeverityPTS();
        SeverityPTS sp3 = new SeverityPTS();
        SeverityPTS sp4 = new SeverityPTS();
        SeverityPTS sp5 = new SeverityPTS();
        SeverityPTS sp6 = new SeverityPTS();
        SeverityPTS sp7 = new SeverityPTS();
        SeverityPTS sp8 = new SeverityPTS();
        SeverityPTS sp9 = new SeverityPTS();
        SeverityPTS sp10 = new SeverityPTS();

        BaseConstituent[] CSs = new BaseConstituent[]{np1, np2, np3, np4, np5, np6, np7, np8, np9, np10,
                sp1, sp2, sp3, sp4, sp5, sp6, sp7, sp8, sp9, sp10};

        Hospital hos = new Hospital();
        ArrayList<RescueAction> rActions = new ArrayList<>();
        for (int i = 0; i < 500; i++)
            rActions.add(new RescueAction(0, 0));
        Environment env = new Environment(CSs, rActions.toArray(new BaseAction[rActions.size()]));
        Simulator sim = new Simulator(CSs, hos, env);

        double alpha_beta = 0.01;
        ArrayList<Integer> bounds = new ArrayList<>();
        bounds.add(250);
        bounds.add(275);
        bounds.add(300);
        bounds.add(325);
        
        for(int bound: bounds) {

            Date nowDate = new Date();
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String pre = transFormat.format(nowDate);
            String outputName = "mci_result/" + pre + "_Large_MCI_" + bound + "_" + String.format("%.3f", alpha_beta) + ".csv";
            CSVWriter cw = new CSVWriter(new OutputStreamWriter(new FileOutputStream(outputName), "UTF-8"), ',', '"');
            cw.writeNext(new String[]{"prob", "num_of_samples", "execution_time", "result"});
            ArrayList<SMCResult> resList = new ArrayList<>();

            System.out.println("----------------------------------------------------");
            System.out.println("SoS-level benefit is greater than and equal to " + bound + ".");
            BaseChecker checker = new BaseChecker(10000, bound, BaseChecker.comparisonType.GREATER_THAN_AND_EQUAL_TO);
            SPRTMethod sprt = new SPRTMethod(0.01, 0.01, 0.01); // 신뢰도 99%

            for (int t = 1; t < 100; t++) {
                double theta = 0.01 * t; // theta

                long start = System.currentTimeMillis();
                sprt.setExpression(theta);

                logger.info("------------------------------------------- Theta : "
                        + String.format("%.2f", theta) + " ---------------------------------------------------");
                long local_start = System.currentTimeMillis();

                while (!sprt.checkStopCondition()) {
                    logger.info("[Large] CheckStopCondition :" + String.format("%.4f", (System.currentTimeMillis() - local_start) / 1000.0));

                    // Initialize Patient map
                    for (int i = 0; i <= 100; i++) {
                        Hospital.GeoMap.add(new MapPoint(i));
                    }

                    ArrayList<Integer> list = new ArrayList<>();
                    list.clear();
                    list = distributor.getDistributionArray(500); // Action 수 만큼 증가

                    sim.setActionPlan(list);
                    sim.setEndTick(7000);

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
