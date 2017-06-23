package christian;

import com.opencsv.CSVWriter;
import kr.ac.kaist.se.mc.BaseChecker;
import kr.ac.kaist.se.simulator.Environment;
import kr.ac.kaist.se.simulator.Simulator;
import kr.ac.kaist.se.simulator.method.SPRTMethod;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * RobotScenario.java
 * This class example is came from "Simulation and Statistical Model Checking of Logic-Based Multi-Agent System Models", Christian Kroib

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

public class RobotMain {
    public static void main(String[] args) throws IOException{
        RobotScenario robot = new RobotScenario(0);

        Simulator sim = new Simulator(robot);

        BaseChecker checker = new BaseChecker();
        checker.init(sim.getScenario().getEndTick(), 3, BaseChecker.comparisonType.EQUAL_TO);
        SPRTMethod sprt = new SPRTMethod(0.05, 0.05, 0.01);
        sprt.setLessCheck();
        for(int j=0; j < 10; j++) {
            String outputName = "robot_result/SIM_robot_"+(j+1)+".csv";
            CSVWriter cw = new CSVWriter(new OutputStreamWriter(new FileOutputStream(outputName), "UTF-8"), ',', '"');
            cw.writeNext(new String[]{"prob", "num_of_samples", "result"});
            ArrayList<RobotResult> resList = new ArrayList<>();
            for (int trial = 1; trial <= 200; trial++) {
                for (int i = 1; i < 100; i++) {
                    double theta = 0.01 * i;
                    sprt.setExpression(theta);
                    while (!sprt.checkStopCondition()) {
                        sim.execute();
                        System.out.println(checker.evaluateSample(sim.getResult()));
                        int result = checker.evaluateSample(sim.getResult());
                        if (result == 1) result = 0;
                        else result = 1;
                        sprt.updateResult(result);
                        sim.reset();
                    }
                    boolean h0 = sprt.getResult(); // Result
                    int numSamples = sprt.getNumSamples();
                    sprt.reset();

                    if (h0) System.out.print("T");
                    else System.out.print("F");

                    if (resList.size() < 99)
                        resList.add(new RobotResult(h0, numSamples, theta));
                    else {
                        RobotResult res = resList.get(i - 1);
                        res.updateResult(numSamples);
                        resList.set(i - 1, res);
                    }
                }
                System.out.println();
            }
            for (RobotResult r : resList) {
                System.out.print(".");
                cw.writeNext(r.getArr());
            }
            System.out.println();
            cw.close();
        }
    }


}
