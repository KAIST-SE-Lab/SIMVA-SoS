package mci;

import com.opencsv.CSVWriter;
import kr.ac.kaist.se.Executor;
import kr.ac.kaist.se.Util;
import kr.ac.kaist.se.mc.BaseChecker;
import kr.ac.kaist.se.simulator.*;
import kr.ac.kaist.se.simulator.method.SPRTMethod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        NormalDistributor distributor = new NormalDistributor();
        distributor.setNormalDistParams(2500, 700);

        LargeMCIScenario lMCI = new LargeMCIScenario();

        Simulator sim = new Simulator(lMCI);
        sim.setDEBUG();

        Executor.Perform_Experiment(distributor, sim, "large_MCI", 270);
        
    }
}
