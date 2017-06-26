package mci.checker;

import kr.ac.kaist.se.mc.CheckerInterface;
import kr.ac.kaist.se.simulator.DebugProperty;
import kr.ac.kaist.se.simulator.DebugTick;
import kr.ac.kaist.se.simulator.SIMResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Universality.java

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

public class Universality implements CheckerInterface{
    private int minRange;
    private int maxRange;

    @Override
    public String getName() {
        return "Universality Checker";
    }

    @Override
    public String getDescription() {
        return "Globally, it is always the case that \"Every PTS exists within the range between " + this.minRange + " and " + this.maxRange + "\" holds [time(P)] with a probability () than p.";
    }

    @Override
    public void init(String[] params) {
        // params[0]: checker name
        // params[1]: probability
        this.minRange = Integer.parseInt(params[5]);
        this.maxRange = Integer.parseInt(params[6]);
    }

    /**
     * evaluateSample Method
     * Evaluate a given property satisfies universality property
     * Check all time ticks whether all PTSs are in the operation area, which is 0-100.
     * @param res Simulation result class which contains debugTick Map
     * @return 1, Universality is guaranteed, otherwise 0
     */
    @Override
    public int evaluateSample(SIMResult res) {
        HashMap<Integer, DebugTick> traceMap = res.getDebugTraces();

        for(Map.Entry <Integer,DebugTick> t: traceMap.entrySet()){
            for(Map.Entry<String, DebugProperty> debugTick: t.getValue().getDebugInfoMap().entrySet()){
                String name = debugTick.getKey();
                if(name.contains("PTS")){
                    int pos = (Integer) debugTick.getValue().getProperty("position");
                    if(pos < this.minRange || pos > maxRange)
                        return 0;
                }
            }
        }
        return 1;
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
