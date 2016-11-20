package mci;

import kr.ac.kaist.se.Executor;
import kr.ac.kaist.se.Util;
import kr.ac.kaist.se.simulator.NormalDistributor;
import kr.ac.kaist.se.simulator.Simulator;
import mci.scenario.LargeMCIScenario;

import java.io.IOException;
import java.util.ArrayList;

/**
 * LargeScaleMCIMain.java
 * Author: Junho Kim <jhkim@se.kaist.ac.kr>
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
public class LargeScaleMCIMain {

    public static void main(String[] args) throws Exception {

        Util.create_result_directory("mci_result");

//        debugMain();
        experimentMain();

    }

    public static void debugMain() throws IOException{
        NormalDistributor distributor = new NormalDistributor();
        distributor.setNormalDistParams(2500, 700);

        LargeMCIScenario lMCI = new LargeMCIScenario(20, 500);

        Simulator sim = new Simulator(lMCI);
        sim.setDEBUG();

        Executor.Perform_Debug_Experiment(distributor, sim, "mci");

    }

    public static void experimentMain() throws IOException{
        NormalDistributor distributor = new NormalDistributor();
        distributor.setNormalDistParams(1500, 400);

        // Experiment # 1  Total # of CSs 10
//        LargeMCIScenario lMCI = new LargeMCIScenario(5, 250);
//        Simulator sim = new Simulator(lMCI);
//        Executor.Perform_Experiment(distributor, sim, "mci", 235);
//
//        // Experiment # 2  Total # of CSs 20
//        LargeMCIScenario lMCI1 = new LargeMCIScenario(10, 400);
//        sim = new Simulator(lMCI1);
//        Executor.Perform_Experiment(distributor, sim, "mci", 380);
//
//        // Experiment # 3  Total # of CSs 30
        LargeMCIScenario lMCI2 = new LargeMCIScenario(15, 1500);
        Simulator sim = new Simulator(lMCI2);
        Executor.Perform_Experiment(distributor, sim, "mci", 1230);

        // Experiment # 4  Total # of CSs 40
//        LargeMCIScenario lMCI3 = new LargeMCIScenario(20, 2000);
//        Simulator sim = new Simulator(lMCI3);
//        Executor.Perform_Experiment(distributor, sim, "mci", 1750);

    }
}
