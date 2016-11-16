package mci;

import kr.ac.kaist.se.Executor;
import kr.ac.kaist.se.Util;
import kr.ac.kaist.se.simulator.NormalDistributor;
import kr.ac.kaist.se.simulator.Simulator;
import mci.scenario.MCIScenario;

import java.io.IOException;

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

        // To prepare the result_directory
        Util.create_result_directory("mci_result");

        // Globally used (no need to replicate in concurrency)
        debugMain();

    }

    public static void experimentMain() throws IOException{
        NormalDistributor distributor = new NormalDistributor();
        distributor.setNormalDistParams(2000, 500);

        MCIScenario mci = new MCIScenario();

        Simulator sim = new Simulator(mci);

        Executor.Perform_Experiment(distributor, sim, "mci", 70);
    }

    public static void debugMain() throws IOException {
        NormalDistributor distributor = new NormalDistributor();
        distributor.setNormalDistParams(2000, 500);

        MCIScenario lMCI = new MCIScenario();

        Simulator sim = new Simulator(lMCI);
        sim.setDEBUG();

        Executor.Perform_Debug_Experiment(distributor, sim, "mci");

    }

}
