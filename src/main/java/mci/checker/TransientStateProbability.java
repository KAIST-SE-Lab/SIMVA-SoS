package mci.checker;

import kr.ac.kaist.se.mc.CheckerInterface;
import kr.ac.kaist.se.simulator.DebugProperty;
import kr.ac.kaist.se.simulator.DebugTick;
import kr.ac.kaist.se.simulator.SIMResult;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * TransientStateProbability.java

 * Author: Junho Kim <jhim@se.kaist.ac.kr>

 * The MIT License (MIT)

 * Copyright (c) 2016 Junho Kim

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions: TBD
 */
public class TransientStateProbability implements CheckerInterface {

    private int t_u; // untill time tick
    private int target_benefit;

    public void init(int t_u, int target_benefit) {
        this.t_u = t_u;
        this.target_benefit = target_benefit;
    }

    @Override
    public void init(String[] params) {
        // params[0]: checker name
        // params[1]: probability
        this.t_u = Integer.parseInt(params[5]);
        this.target_benefit = Integer.parseInt(params[6]);
    }

    @Override
    public String getName() {
        return "Transient State Probability Checker";
    }

    @Override
    public String getDescription() {
        return "After Q, P holds after t_u ticks with a probability () than p.";
    }

    /**
     * evaluateSample Method
     * Evaluate a given property satisfies Transient State Probability property
     * Check whether after some ticks, P holds after t_u ticks with a probability () than p.
     * @param res Simulation result class which contains debugTick Map
     * @return 1, Transient State Probability of property is guaranteed, otherwise 0
     */
    @Override
    public int evaluateSample(SIMResult res) {
        HashMap<Integer, DebugTick> traceMap = res.getDebugTraces();
        int satisfied_transient = 0;

        for(Map.Entry <Integer,DebugTick> t: traceMap.entrySet()){
            for(Map.Entry<String, DebugProperty> debugTick: t.getValue().getDebugInfoMap().entrySet()){
                String name = debugTick.getKey();
                if(name.contains("SoS_level_benefit")){
                    int benefit = (Integer) debugTick.getValue().getProperty("SoS_level_benefit");

                    if (satisfied_transient == 0) {
                        if (t.getKey() <= t_u && benefit >= target_benefit) {
                            satisfied_transient = 1;
                        } else if (t.getKey() > t_u) {
                            return 0;
                        }
                    } else if (benefit < target_benefit)
                        return 0;
                }
            }
        }

        if (satisfied_transient == 1)
            return 1;
        else
            return 0;
    }

    @Override
    public int getMinTick() {
        return 0;
    }

    @Override
    public int getMaxTick() {
        return 0;
    }
}
