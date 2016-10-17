package mci;

import com.opencsv.CSVWriter;
import kr.ac.kaist.se.mc.BaseChecker;
import kr.ac.kaist.se.simulator.*;
import kr.ac.kaist.se.simulator.method.SPRTMethod;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

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

    public static void main(String[] args) throws Exception{

        NearestPTS np1 = new NearestPTS();
        NearestPTS np2 = new NearestPTS();
        SeverityPTS sp1 = new SeverityPTS();
        SeverityPTS sp2 = new SeverityPTS();
        BaseConstituent[] CSs = new BaseConstituent[]{np1, np2, sp1, sp2};
        Hospital hos = new Hospital();

        NormalDistributor distributor = new NormalDistributor();
        distributor.setNormalDistParams(2000, 600);

        ArrayList<RescueAction> rActions = new ArrayList<>();
        for (int i = 0; i < 100; i++)
            rActions.add(new RescueAction(0, 0));

        Environment env = new Environment(CSs, rActions.toArray(new BaseAction[rActions.size()]));

        Simulator sim = new Simulator(CSs, hos, env);


//        int[] boundArr = {55, 60, 65, 70, 75};
        int[] boundArr = {75};
        for(int bound : boundArr) {

            String outputName = "mci_result/MCI_" + bound + ".csv";
            CSVWriter cw = new CSVWriter(new OutputStreamWriter(new FileOutputStream(outputName), "UTF-8"), ',', '"');
            cw.writeNext(new String[] {"prob", "num_of_samples", "execution_time", "min_tick", "max_tick", "result"});
            ArrayList<SMCResult> resList = new ArrayList<>();

            System.out.println("----------------------------------------------------");
            System.out.println("SoS-level benefit is greater than and equal to "+bound + ".");
            BaseChecker checker = new BaseChecker(10000, bound, BaseChecker.comparisonType.GREATER_THAN_AND_EQUAL_TO);
            SPRTMethod sprt = new SPRTMethod(0.01, 0.01, 0.01); // 신뢰도 99%

            for(int t=1; t<100; t++) {
                double theta = 0.01 * t; // theta
                long start = System.currentTimeMillis();
                sprt.setExpression(theta);

                while(!sprt.checkStopCondition()) {

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

                    sim.execute();

                    SIMResult res = sim.getResult();
                    int checkResult = checker.evaluateSample(res);
//                    if(checkResult == 0)
//                        System.out.print("What is this?");
//                    System.out.print(" " + checkResult);
                    sprt.updateResult(checkResult);

//                    System.gc();

                    sim.reset();
                    sim.setActionPlan(list);
//                    env.setActionList(rActions);
                }

                boolean h0 = sprt.getResult(); // Result
                int numSamples = sprt.getNumSamples();

                long exec_time = System.currentTimeMillis() - start; //exec time
                int minTick = checker.getMinTick();
                int maxTick = checker.getMaxTick();
                sprt.reset();
                resList.add(new SMCResult(theta, numSamples, exec_time, minTick, maxTick, h0));
                System.out.print("Theta: " + theta);
                if(h0) {
                    System.out.print(" Result: T");
                }
                else {
                    System.out.print(" Result: F");
                }
                System.out.print(" with n of samples: " + numSamples);
                System.out.println(" in " + String.format("%.2f", exec_time/1000.0) + " secs");

            }
            System.out.println();
            for(SMCResult r : resList){
                System.out.print(".");
                cw.writeNext(r.getArr());
            }
            cw.close();
            resList.clear();
            System.out.println();
        }

        System.out.println("Finished");

    }



}
